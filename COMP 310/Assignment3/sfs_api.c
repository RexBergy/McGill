#include "sfs_api.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fuse.h>
#include <strings.h>
#include "disk_emu.h"


/**------------------------------
 * DISK COMPOSITION
 * ------------------------------
 * The disk has 1024 data blocks and each block is 1024 bytes.
 * 1 superblock
 * 64 inode blocks
 * 1024 data blocks 
 * 1 bitmap block
 * 1090 blocks total
 * 
 **/


#define Philippe_Bergeron_260928589 "sfs_disk" //Represents the disk 
#define NUM_DATABLOCKS      1024 // maximum number of data blocks on the disk.
#define NUM_INODE_BLOCKS            64   // num blocks of inodes
#define TOTAL_NUM_DATABLOCKS 1090

// set _which_bit to 1 in _data
#define FREE_BIT(data, which_bit) data = data | (1 << which_bit)
// set _which_bit to 0 in _data
#define USE_BIT(data, which_bit) data = data & ~(1 << which_bit)

/**--------------------------------
 * SIZE
 * --------------------------------*/


#define MAXFILELENGTH           16 // 
#define INODE_SIZE                  64   // num bytes per inode
#define BLOCK_SIZE 1024 // num bytes per block

#define BITMAP_ROW_SIZE (NUM_DATABLOCKS/8) // number of bytes needed to have 1 bit for each block

/**---------------------------------
 * INDEXES
 * ---------------------------------*/
#define SUPERBLOCK_BLOCK_INDEX      0
#define FIRST_INODE_BLOCK_INDEX     1
#define FIRST_DATA_BLOCK_INDEX      65
#define BITMAP_BLOCK_INDEX          1089

/*--------------------------------------------------
 *  BITMAP
 *------------------------------------------------- */
//first bit is used for root directory 0xFE 011111111 
static uint8_t free_bitmap[BITMAP_ROW_SIZE] = { 0xFE, [1 ... BITMAP_ROW_SIZE - 1] = UINT8_MAX };


/*--------------------------------------------------
 *  INODE
 *------------------------------------------------- */
typedef struct _inode
{
    uint32_t mode;
    uint32_t link_cnt;
    uint8_t  uid;
    uint8_t  gid;
    uint16_t size; // number of bytes
    uint32_t blk_pntrs[12]; // actual block numbers on disk
    uint32_t ind_pntr; // inode idx in inode_table
} inode;

static inode default_root_dir_inode = {
    .mode      = 0x000001FD, 
    .link_cnt  = 1,
    .uid       = 1,
    .gid       = 1,
    .size      = 0,
    .blk_pntrs = {FIRST_DATA_BLOCK_INDEX, 0,0,0,0,0,0,0,0,0,0,0},
    .ind_pntr  = 0 // an inode number
};

// first inode is used for root dir
static uint8_t inode_bitmap[BITMAP_ROW_SIZE] = { 0xFE, [1 ... BITMAP_ROW_SIZE - 1] = UINT8_MAX };

// in-memory cache of inode table
static inode inode_table[NUM_DATABLOCKS] = {0};

/*--------------------------------------------------
 *  SUPERBLOCK
 *------------------------------------------------- */
typedef struct _superblock
{
    uint32_t magic;             // type of filesystem, a magic number
    uint32_t block_size;        // bytes per block, 1024
    uint32_t file_system_size;  // number of blocks in filesystem, 1538
    uint32_t inode_table_length;// number of blocks in inode table, 512
    uint32_t root_dir_inode_num;// inode number of block containing root directory, 0
} superblock;

static superblock default_superblock = {
    .magic              = 0xACBD1005,
    .block_size         = BLOCK_SIZE,
    .file_system_size   = TOTAL_NUM_DATABLOCKS,
    .inode_table_length = INODE_SIZE,
    .root_dir_inode_num = 0
};

/*--------------------------------------------------
 *  DIRECTORY_ENTRY
 *------------------------------------------------- */
typedef struct _dir_entry
{
    uint32_t inode_number;
    uint8_t filename[MAXFILELENGTH];
} dir_entry;

static dir_entry dir_cache[NUM_DATABLOCKS] = {0};

/*--------------------------------------------------
 *  OPEN FILE DESCRIPTORS
 *------------------------------------------------- */
typedef struct _fd_table_entry
{
    uint32_t inode_number;
    uint32_t rw_pointer;
} fd_table_entry; 

static uint32_t num_open_fd = 0;
static fd_table_entry open_fd_table[NUM_DATABLOCKS] =  {0};

int nextfilepointer = 2; // 2


void mksfs(int fresh) {
    if (fresh)
    {
        init_fresh_disk(Philippe_Bergeron_260928589, BLOCK_SIZE, TOTAL_NUM_DATABLOCKS);
        // write superblock to disk
        uint8_t superblock_buffer[BLOCK_SIZE] = {0};
        memcpy(superblock_buffer, &default_superblock, sizeof(default_superblock));
        write_blocks(SUPERBLOCK_BLOCK_INDEX, 1, &superblock_buffer);
        // writeinode table to disk
        inode_table[0] = default_root_dir_inode;
        write_blocks(FIRST_INODE_BLOCK_INDEX, NUM_INODE_BLOCKS, inode_table);
        // write bitmap to disk
        uint8_t bitmap_buffer[BLOCK_SIZE] = {0};
        memcpy(bitmap_buffer, free_bitmap, sizeof(free_bitmap));
        memcpy(bitmap_buffer + sizeof(free_bitmap), inode_bitmap, sizeof(inode_bitmap));
        write_blocks(BITMAP_BLOCK_INDEX, 1, &bitmap_buffer);

        
    }
    else
    {
        init_disk(Philippe_Bergeron_260928589, BLOCK_SIZE, TOTAL_NUM_DATABLOCKS);
        
        // read in inode table
        read_blocks(FIRST_INODE_BLOCK_INDEX, NUM_INODE_BLOCKS, inode_table);
        // read in bitmaps
        uint8_t bitmap_buffer[BLOCK_SIZE] = {0};
        read_blocks(BITMAP_BLOCK_INDEX, 1, bitmap_buffer);
        memcpy(free_bitmap, bitmap_buffer, sizeof(free_bitmap));
        memcpy(inode_bitmap, bitmap_buffer + sizeof(free_bitmap), sizeof(inode_bitmap));
        // read in directory to dir_cache
        inode root_dir_inode = inode_table[0];
        uint32_t root_dir_num_blocks  = root_dir_inode.size / BLOCK_SIZE;
        if (root_dir_inode.size % BLOCK_SIZE) root_dir_num_blocks += 1;
        uint32_t *root_dir_contents_bytes = (uint8_t*)dir_cache;
        for (int i = 0; i < root_dir_num_blocks; ++i)
        {
            if (i < 12) // direct block pointers
            {
                uint32_t block_address = root_dir_inode.blk_pntrs[i];
                read_blocks(block_address, 1, root_dir_contents_bytes + i * BLOCK_SIZE);
            }
            else // indirect block pointer
            {
                uint32_t block_address = inode_table[root_dir_inode.ind_pntr].blk_pntrs[i % 12];
                read_blocks(block_address, 1, root_dir_contents_bytes + i * BLOCK_SIZE);
            }
        }
    }
}
int sfs_getnextfilename(char *fname){
    
    if (dir_cache[nextfilepointer].inode_number == 0){
        return 0;
    } else {
        memcpy(fname,dir_cache[nextfilepointer].filename,sizeof(dir_cache[nextfilepointer].filename));
        nextfilepointer++;
        return 1;
    }
    

}
int sfs_getfilesize(const char* path){
    uint32_t file_inode = 0; //default value 0
    uint32_t file_first_block = 0; // default value 0
    for (int dir_entry_index = 0; dir_entry_index < NUM_DATABLOCKS; ++dir_entry_index) {
        if (strcmp(dir_cache[dir_entry_index].filename, path) == 0) {  // if we find a filename with the corresponding name we want to open 
            file_inode = dir_cache[dir_entry_index].inode_number; // set the file inode to that one corresponding to the directory entry cache 
        }
    }

    if (file_inode) {// if file exists, check if already open
        for (int fd_table_index = 0; fd_table_index < NUM_DATABLOCKS; ++fd_table_index) {
            if (open_fd_table[fd_table_index].inode_number == file_inode) { // if the file inode equals return the index
                return inode_table[file_inode].size;
            }
        }
    } else {
        return -1;
    }

}
int sfs_fopen(char *name){
    int bitmapForOpen = 0;
    if (strlen(name) > MAXFILELENGTH){
        
        return -1;
    }
    uint32_t file_inode = 0; //default value 0
    uint32_t file_first_block = 0; // default value 0
    for (int dir_entry_index = 0; dir_entry_index < NUM_DATABLOCKS; ++dir_entry_index) {
        if (strcmp(dir_cache[dir_entry_index].filename, name) == 0) {  // if we find a filename with the corresponding name we want to open 
            file_inode = dir_cache[dir_entry_index].inode_number; // set the file inode to that one corresponding to the directory entry cache 
        }
    }

    if (file_inode) {// if file exists, check if already open
        for (int fd_table_index = 0; fd_table_index < NUM_DATABLOCKS; ++fd_table_index) {
            if (open_fd_table[fd_table_index].inode_number == file_inode) { // if the file inode equals return the index
                return fd_table_index;
            }
        }
    } else { // file doesn't exist, create inode and directory entry
        //Searche in the inode bitmap for the first free bit and set this numnber to file_inode
        int found_first_free_inode = 0;
        for (int inode_bitmap_idx = 0; inode_bitmap_idx < BITMAP_ROW_SIZE; ++inode_bitmap_idx) {
            if (found_first_free_inode) break;
            if (inode_bitmap[inode_bitmap_idx] > 0) {
                for (int bit_idx = 0; bit_idx < 8; ++bit_idx) { //
                    if (found_first_free_inode) break;
                    if (inode_bitmap[inode_bitmap_idx] & (1 << bit_idx)) {
                        USE_BIT(inode_bitmap[inode_bitmap_idx], bit_idx);
                        file_inode = inode_bitmap_idx * 8 + bit_idx;
                        found_first_free_inode = 1;
                    }
                }
            }
        }
        if (!found_first_free_inode) {
             return -1; 
             }
        //Find free data block
        int found_first_free_data_block = 0;
        for (int free_bitmap_idx = 0; free_bitmap_idx < BITMAP_ROW_SIZE; ++free_bitmap_idx) {
            if (found_first_free_data_block) break;
            if (free_bitmap[free_bitmap_idx] > 0) {
                for (int bit_idx = 0; bit_idx < 8; ++bit_idx) {
                    if (found_first_free_data_block) break;
                    if (free_bitmap[free_bitmap_idx] & (1 << bit_idx)) {
                        USE_BIT(free_bitmap[free_bitmap_idx], bit_idx);
                        file_first_block = FIRST_DATA_BLOCK_INDEX + free_bitmap_idx*8 + bit_idx;
                        found_first_free_data_block = 1;
                    }
                }
            }
        }
        if (!found_first_free_data_block) {
            return -1; 
        }

        // create inode
        inode_table[file_inode] = default_root_dir_inode;
        inode_table[file_inode].blk_pntrs[0] = file_first_block;
        uint8_t directory_buffer[BLOCK_SIZE] = {0};
        dir_entry new_dir = {
            .inode_number = file_inode,
            .filename = name
        };
        

        // create directory entry, set file_inode and filename
        for (int dir_entry_idx = 0; dir_entry_idx < NUM_DATABLOCKS; ++dir_entry_idx) {
            if (dir_cache[dir_entry_idx].inode_number == 0) {
                dir_cache[dir_entry_idx].inode_number = file_inode;
                memcpy(dir_cache[dir_entry_idx].filename, name, strlen(name));
                break;
            }
        }
    }

    // write the new inode and dir_entry to disk
    write_blocks(FIRST_INODE_BLOCK_INDEX, NUM_INODE_BLOCKS, inode_table);

    // put the new open file in the fd_table and return its index therein
    for (int fd_table_index = 0; fd_table_index < NUM_DATABLOCKS; ++fd_table_index) {
        if (open_fd_table[fd_table_index].inode_number == 0)
        {
            open_fd_table[fd_table_index].inode_number = file_inode;
            open_fd_table[fd_table_index].rw_pointer = inode_table[file_inode].size;
            num_open_fd++;
            return fd_table_index;
        }
    }
}
int sfs_fclose(int fileID) {
    if (open_fd_table[fileID].inode_number == 0) {
        return -1;
    } else {
        fd_table_entry empty_fd_entry = {0};
        memcpy(open_fd_table + fileID, &empty_fd_entry, sizeof(fd_table_entry));
        //num_open_fd--;
    }
}
int sfs_fread(int fileID, char *buf, int length) {
    //get inode of file
    int file_inode_number = 0;
    if (open_fd_table[fileID].inode_number != 0)
        file_inode_number = open_fd_table[fileID].inode_number;
    if (!file_inode_number) {  
    return -1; }
    inode file_inode = inode_table[file_inode_number];
    int num = file_inode.size;
    uint8_t file_buffer[BLOCK_SIZE*BLOCK_SIZE];
    memset(file_buffer, 0, sizeof(file_buffer));
    int file_num_blocks = file_inode.size / BLOCK_SIZE;
    if (file_inode.size % BLOCK_SIZE != 0) file_num_blocks += 1;
    for (int block_in_file_idx = 0; block_in_file_idx < file_num_blocks; ++block_in_file_idx) {
        if (block_in_file_idx < 12) { // first 12 direct block pointer
            uint32_t block_address = file_inode.blk_pntrs[block_in_file_idx];
            read_blocks(block_address, 1, file_buffer + block_in_file_idx * BLOCK_SIZE);
        } else { // indirect block pointer
            if (block_in_file_idx < 24){
                uint32_t block_address = inode_table[file_inode.ind_pntr].blk_pntrs[block_in_file_idx % 12];
                read_blocks(block_address, 1, file_buffer + block_in_file_idx * BLOCK_SIZE);
            } else {
                uint32_t block_address = inode_table[inode_table[file_inode.ind_pntr].ind_pntr].blk_pntrs[block_in_file_idx% 12];
                read_blocks(block_address, 1, file_buffer + block_in_file_idx * BLOCK_SIZE);
            }
        }
    }
    
    char out_data[1024];
    
    int i;
    for (i = 0; i < length && i < file_inode.size; ++i) {
        buf[i] = file_buffer[i+open_fd_table[fileID].rw_pointer];
        
        
    }
    
    //adavance pointer
    open_fd_table[fileID].rw_pointer = open_fd_table[fileID].rw_pointer + length;
    
    //return number of bytes read
    if (length > file_inode.size){
        return file_inode.size;
    } else {
        return length;
    }
}
void write_indirect(){
  //Write to indirect pointers
  int inode_table1 = inode_table[1].blk_pntrs;
  for (int i = 1; i<sizeof(inode_table);i++){
      if (1){
          printf("Points to something");
      }
  }

}

int sfs_fwrite(int fileID, const char *buf, int length) {
    // get new size of file with additional text, make buffer of that size
    int file_inode_number = 0;
    if (open_fd_table[fileID].inode_number != 0)    // check if file is in openfdtable 
        file_inode_number = open_fd_table[fileID].inode_number;
    if (!file_inode_number) {
        return -1;
    }
    
    inode file_inode = inode_table[file_inode_number];
    int file_size_new = file_inode.size;
    int file_number_ptr = file_inode.ind_pntr;
    
    if (open_fd_table[fileID].rw_pointer + length > file_inode.size)
        file_size_new = open_fd_table[fileID].rw_pointer + length;
    uint8_t file_buffer[BLOCK_SIZE*BLOCK_SIZE];
    memset(file_buffer, 0, sizeof(file_buffer));
    // read entire file into the buffer
    int file_num_blocks = file_inode.size / BLOCK_SIZE;
    if (file_inode.size % BLOCK_SIZE != 0) file_num_blocks += 1; // if
    
    for (int block_in_file_idx = 0; block_in_file_idx < file_num_blocks; ++block_in_file_idx) {
        if (block_in_file_idx < 12) { // first 12 direct block pointer
            uint32_t block_address = file_inode.blk_pntrs[block_in_file_idx];
            read_blocks(block_address, 1, file_buffer + block_in_file_idx * BLOCK_SIZE);
        } else { // indirect block pointer
            if (block_in_file_idx < 24){
                uint32_t block_address = inode_table[file_inode.ind_pntr].blk_pntrs[block_in_file_idx % 12];
                read_blocks(block_address, 1, file_buffer + block_in_file_idx * BLOCK_SIZE);
            } else {
                uint32_t block_address = inode_table[inode_table[file_inode.ind_pntr].ind_pntr].blk_pntrs[block_in_file_idx % 12];
                
                read_blocks(block_address, 1, file_buffer + block_in_file_idx * BLOCK_SIZE);
            }
            
        }
    }
    
    // write new text to the buffer at rw_pointer
    
    for (int i = 0; i < length; ++i) {
        file_buffer[i+open_fd_table[fileID].rw_pointer] = buf[i];
    }
    
    // update inode file size
    file_inode.size = file_size_new;
    open_fd_table[fileID].rw_pointer = file_inode.size;
    
    // write the file to disk, creating inode block pointers as necesary
    file_num_blocks = file_inode.size / BLOCK_SIZE;
    if (file_inode.size % BLOCK_SIZE != 0) file_num_blocks += 1;
    
    for (int file_block_idx = 0; file_block_idx < file_num_blocks; ++file_block_idx) {
        if (file_block_idx < 12) { // first 12 direct block pointer
            uint32_t block_address = file_inode.blk_pntrs[file_block_idx];
            
            if (block_address != 0) {
                
                write_blocks(block_address, 1, file_buffer + file_block_idx * BLOCK_SIZE);
            } else {
                int found_first_free_data_block = 0;
                for (int free_bitmap_idx = 0; free_bitmap_idx < BITMAP_ROW_SIZE; ++free_bitmap_idx) {
                    if (found_first_free_data_block) break;
                    if (free_bitmap[free_bitmap_idx] > 0) {
                        for (int bit_idx = 0; bit_idx < 8; ++bit_idx) {
                            if (found_first_free_data_block) break;
                            if (free_bitmap[free_bitmap_idx] & (1 << bit_idx)) {
                                USE_BIT(free_bitmap[free_bitmap_idx], bit_idx);
                                found_first_free_data_block = FIRST_DATA_BLOCK_INDEX + free_bitmap_idx*8 + bit_idx;
                            }
                        }
                    }
                }
                if (!found_first_free_data_block) {
                    return -1;
                }
                file_inode.blk_pntrs[file_block_idx] = found_first_free_data_block;
                
                write_blocks(found_first_free_data_block, 1, file_buffer + file_block_idx * BLOCK_SIZE);
                
            }
        } else {
            
                if (file_block_idx > 35 ){
                    return -1;
                }
                if (file_block_idx > 23) {
                    if (!inode_table[file_inode.ind_pntr].ind_pntr ){
                    // set up inode for indirect pointer
                    int found_first_free_inode = 0;
                    for (int inode_bitmap_idx = 0; inode_bitmap_idx < BITMAP_ROW_SIZE; ++inode_bitmap_idx) {
                        if (found_first_free_inode) break;
                        if (inode_bitmap[inode_bitmap_idx] > 0) {
                            for (int bit_idx = 0; bit_idx < 8; ++bit_idx) { //
                                if (found_first_free_inode) break;
                                if (inode_bitmap[inode_bitmap_idx] & (1 << bit_idx)) {
                                    USE_BIT(inode_bitmap[inode_bitmap_idx], bit_idx);
                                    found_first_free_inode = inode_bitmap_idx * 8 + bit_idx;
                                }
                            }
                        }
                    }
                    if (!found_first_free_inode) {
                        return -1;
                    }
                    inode_table[file_inode.ind_pntr].ind_pntr = found_first_free_inode;
                    inode_table[found_first_free_inode] = default_root_dir_inode;
                    inode_table[found_first_free_inode].blk_pntrs[0] = 0;
                    
                    
                    }
                    uint32_t block_address = inode_table[inode_table[file_inode.ind_pntr].ind_pntr].blk_pntrs[file_block_idx % 12];
                    if (block_address != 0) {
                        write_blocks(block_address, 1, file_buffer + file_block_idx * BLOCK_SIZE);
                    } else {
                        int found_first_free_data_block = 0;
                        for (int free_bitmap_idx = 0; free_bitmap_idx < BITMAP_ROW_SIZE; ++free_bitmap_idx) {
                            if (found_first_free_data_block) break;
                            if (free_bitmap[free_bitmap_idx] > 0) {
                                for (int bit_idx = 0; bit_idx < 8; ++bit_idx) {
                                    if (found_first_free_data_block) break;
                                    if (free_bitmap[free_bitmap_idx] & (1 << bit_idx)) {
                                        USE_BIT(free_bitmap[free_bitmap_idx], bit_idx);
                                        found_first_free_data_block = FIRST_DATA_BLOCK_INDEX + free_bitmap_idx*8 + bit_idx;
                                    }
                                }
                            }
                        }
                        if (!found_first_free_data_block) {
                            return -1;
                        }
                        inode_table[inode_table[file_inode.ind_pntr].ind_pntr].blk_pntrs[file_block_idx % 12] = found_first_free_data_block;
                        write_blocks(found_first_free_data_block, 1, file_buffer + file_block_idx * BLOCK_SIZE);
                    }
                } else {
                    if (!file_inode.ind_pntr) {
                    // set up inode for indirect pointer
                    int found_first_free_inode = 0;
                    for (int inode_bitmap_idx = 0; inode_bitmap_idx < BITMAP_ROW_SIZE; ++inode_bitmap_idx) {
                        if (found_first_free_inode) break;
                        if (inode_bitmap[inode_bitmap_idx] > 0) {
                            for (int bit_idx = 0; bit_idx < 8; ++bit_idx) { //
                                if (found_first_free_inode) break;
                                if (inode_bitmap[inode_bitmap_idx] & (1 << bit_idx)) {
                                    USE_BIT(inode_bitmap[inode_bitmap_idx], bit_idx);
                                    found_first_free_inode = inode_bitmap_idx * 8 + bit_idx;
                                }
                            }
                        }
                    }
                    if (!found_first_free_inode) {
                        return -1;
                    }
                    file_inode.ind_pntr = found_first_free_inode;
                    inode_table[found_first_free_inode] = default_root_dir_inode;
                    inode_table[found_first_free_inode].blk_pntrs[0] = 0;
                    
                    
                    }
                    
                    uint32_t block_address = inode_table[file_inode.ind_pntr].blk_pntrs[file_block_idx % 12];
                    if (block_address != 0) {
                        write_blocks(block_address, 1, file_buffer + file_block_idx * BLOCK_SIZE);
                    } else {
                        int found_first_free_data_block = 0;
                        for (int free_bitmap_idx = 0; free_bitmap_idx < BITMAP_ROW_SIZE; ++free_bitmap_idx) {
                            if (found_first_free_data_block) break;
                            if (free_bitmap[free_bitmap_idx] > 0) {
                                for (int bit_idx = 0; bit_idx < 8; ++bit_idx) {
                                    if (found_first_free_data_block) break;
                                    if (free_bitmap[free_bitmap_idx] & (1 << bit_idx)) {
                                        USE_BIT(free_bitmap[free_bitmap_idx], bit_idx);
                                        found_first_free_data_block = FIRST_DATA_BLOCK_INDEX + free_bitmap_idx*8 + bit_idx;
                                    }
                                }
                            }
                        }
                        if (!found_first_free_data_block) {
                            return -1;
                        }
                        
                        inode_table[file_inode.ind_pntr].blk_pntrs[file_block_idx % 12] = found_first_free_data_block;
                        write_blocks(found_first_free_data_block, 1, file_buffer + file_block_idx * BLOCK_SIZE);
                    }
                }
        }
    }
    
    uint8_t bitmap_buffer[BLOCK_SIZE] = {0};
    memcpy(bitmap_buffer, free_bitmap, sizeof(free_bitmap));
    memcpy(bitmap_buffer + sizeof(free_bitmap), inode_bitmap, sizeof(inode_bitmap));
    write_blocks(BITMAP_BLOCK_INDEX, 1, &bitmap_buffer);
    write_blocks(FIRST_INODE_BLOCK_INDEX, NUM_INODE_BLOCKS, inode_table);
    inode_table[file_inode_number] = file_inode;
    return length;
}
int sfs_fseek(int fileID, int loc) {
    if (open_fd_table[fileID].inode_number == 0) {
        return -1;
    } else {
        inode inode = inode_table[open_fd_table[fileID].inode_number];
        if (loc < 0 || loc > inode.size) {
            return -1;
        } else {
            //Pointer to loc
            open_fd_table[fileID].rw_pointer = loc;
        }
    }
}
int sfs_remove(char *file) {
    //TODO


}
