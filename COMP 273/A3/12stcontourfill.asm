
.data

#You must use accurate an file path.
#These file paths are EXAMPLES,
#These will not work for you!
	
str1:	.asciiz ".\\Assignment 3\\test1.txt"
str3:	.asciiz "ttestfill.pbm"	#used as output
err1:	.asciiz "There was an error with opening the file.\n"
err2:	.asciiz "There was an error with reading/writing the file.\n"
err3:	.asciiz "There was an error with closing the file.\n"
sp_ch:	.asciiz "P1\n50 50\n"

buffer:  .space 10000		# buffers for upto 10000 bytes
newbuff: .space 10000		# (increase sizes if necessary)

	.text
	.globl main

main:	la $a0,str1		#readfile takes $a0 as input
	jal readfile
	
	la $s0,buffer
	
	
	la $a0,buffer		# subroutine to convert 1D buffer array in 
	la $a1,newbuff		# 2D newbuff array
	jal convert
	
	la $s1,newbuff

	la $a0,($s1)		#$a0 will specify the "2D array" we will be filling
	li $a1,3		# x coord or i (i.e. the column)
	li $a2,3		# y coord or j (i.e. the row)
	jal fillregion


	la $a0, str3		#writefile will take $a0 as file location
	la $a1,newbuff		#$a1 takes location of what we wish to write.
	jal writefile

exit:	li $v0,10		# exit
	syscall

readfile:

	li $v0,13		# system call to open file in reading mode
	li $a1,0
	li $a2,0
	syscall
	
	move $t0,$v0		# save file descriptor at $s0
	
	blt $v0,$zero,err_o	# checks if file exists
	j read
	
err_o:	li $v0,4		# error
	la $a0,err1
	syscall
	j exit
err_r:	li $v0,4		# error
	la $a0,err2
	syscall
	j exit
err_c:	li $v0,4		# error
	la $a0,err3
	syscall
	j exit
	
read:	li $v0,14		# read
	move $a0,$t0		# argument is file descriptor
	la $a1,buffer		# address of buffer
	li $a2,50000		# hardcode number of char to 50000
	syscall
	
	move $t1,$v0		# number of characters read in $s1
	move $t2,$v1		# address of asciiz string
	
	blt $v0,$zero,err_r	# checks if error
	
	li $v0, 4		# print string in buffer
	la $a0, buffer
	syscall
	
	li $v0, 16		# close file
	move $a0,$t0		# a0 = file descriptor
	syscall				
	blt $v0,$zero,err_c	# Print closing error
	jr $ra
	
convert:
	addi $t0,$zero,0	# counter $t0 = 0
	move $t1,$a0		# save buffer address at $t1
	move $t2,$a1		# save newbuff address at $t2
	addi $t4,$zero,2500	# 50 x 50 array = 2500
	
loop_c:	beq $t0,$t4,done_c	# check if counter = 2500
	lw $t3,($t1)		# load contents of address pointer (buffer) to $t3
	sw $t3,($t2)		# save contents of $t3 to address pointer (newbuff)
	addi $t1,$t1,4		# next buffer address (4 bytes)
	addi $t2,$t2,4 		# next newbuff address (4 bytes)
	addi $t0,$t0,1		# increment counter by 1
	j loop_c
done_c:	jr $ra	
	

fillregion:
	addi $sp,$sp,-4		# need to save $ra
	sw $ra,0($sp)		# because of nested subroutine calls here
	move $t0,$a0
	move $t1,$a1
	move $s1,$a1
	move $t2,$a2
	move $s2,$a2
	lw $t4,($t0)	# register used to fill 
	addi $s3,$s3,1	# stack counter
	
	mul $t1,$t1,50	# get to column (50 * 4)
	mul $t2,$t2,1	# has to be multiple of 4
	add $t1,$t1,$t2	# get to row
	mul $t1,$t1,4
	
	add $t3,$t1,$t0	# set pointer $t3 to coord in 2D array
	
	
	sw $t4,($t3)	# store contents of $t4 to $t3
	
	
	#addi $t3,$t3,4	# check down 
	#lw $t5,($t3)	# load content
	
	
	#bne $t5,$t4,fill_d
	
	addi $t3,$t3,200	# check down right 
	lw $t5,($t3)	# load content
	
	bne $t5,$t4,fill_dr
	
	addi $t3,$t3,-4	# check right 
	lw $t5,($t3)	# load content
	
	bne $t5,$t4,fill_r
	
	addi $t3,$t3,-4	# check up right 
	lw $t5,($t3)	# load content
	
	bne $t5,$t4,fill_ur
	
	addi $t3,$t3,-200	# check up  
	lw $t5,($t3)	# load content
	
	bne $t5,$t4,fill_u
	
	addi $t3,$t3,-200	# check up left
	lw $t5,($t3)	# load content
	
	bne $t5,$t4,fill_ul
	
	addi $t3,$t3,4	# check left
	lw $t5,($t3)	# load content
	
	bne $t5,$t4,fill_l
	
	addi $t3,$t3,4	# check down left
	lw $t5,($t3)	# load content
	
	bne $t5,$t4,fill_dl
	
stack:	beq $s3,1,load
	addi $sp,$sp,4
	addi $s3,$s3,-1
	j stack
load:	lw $ra,0($sp)
	
	jr $ra
	
fill_d:	sw $t4,($t3)	# filled with 1
	
	la $a0,($t0)
	addi $a2,$a2,1
	
	jal fillregion 
fill_dr:	
	sw $t4,($t3)	# filled with 1
	
	la $a0,($t0)
	
	addi $a1,$a1,1
	addi $a2,$a2,1
	
	jal fillregion 
fill_r:	sw $t4,($t3)	# filled with 1
	
	la $a0,($t0)
	addi $a1,$a1,1
	
	jal fillregion 
fill_ur:	
	sw $t4,($t3)	# filled with 1
	
	la $a0,($t0)
	addi $a1,$a1,1
	addi $a2,$a2,-1
	
	jal fillregion 
fill_u:	sw $t4,($t3)	# filled with 1
	
	la $a0,($t0)
	addi $a2,$a2,-1
	
	jal fillregion 
fill_ul:	
	sw $t4,($t3)	# filled with 1
	
	la $a0,($t0)
	addi $a1,$a1,-1
	addi $a2,$a2,-1
	
	jal fillregion 
fill_l:	sw $t4,($t3)	# filled with 1
	
	la $a0,($t0)
	addi $a1,$a1,-1
	
	jal fillregion 
	
fill_dl:	
	sw $t4,($t3)	# filled with 1
	
	la $a0,($t0)
	addi $a1,$a1,-1
	addi $a2,$a2,1
	
	jal fillregion 
	


writefile:
	move $t0, $a0		# t0 = filename
	move $t1, $a1		# t1 = newbuff address
	
	li $v0, 13		# system call to open file in writing mode
	la $a0,($t0)
	li $a1,1
	li $a2,0		
	syscall
				
	move $t2, $v0		# t2 = file descriptor
	blt $v0,$zero, err_o	# error
	
	
	li $v0, 15		# system call to write special characters on file
	move $a0, $t2		
	la $a1, sp_ch		
	li $a2, 9		
	syscall			
	
	
	blt $v0, $zero, err_r	#  error
	
	
	li $v0, 15		# system call to write buffer on file
	move $a0,$t2		
	la $a1, ($t1)		
	li $a2, 4096		
	syscall			
	
	
	blt $v0, $zero, err_r	# error
	
	
	move $a0, $t2		# system call to close file and check for
	li $v0, 16		# potential error
	syscall			
	
	blt $v0, $zero, err_c	#error
	
	jr $ra
