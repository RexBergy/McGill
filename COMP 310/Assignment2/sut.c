#include <stdio.h>
#include <signal.h>
#include <unistd.h>
#include <stdlib.h>
#include <pthread.h>
#include <ucontext.h>
#include "sut.h"
#include "YAUThreads.h"

// Change value to 2 for part b 
int num_computation_threads = 1;
int numthreads, curthread, last;
int currentLine;
pthread_t CEXEC1, CEXEC2, IEXEC;
ucontext_t parent;
threaddesc threadarr[100];
threaddesc threadarrWait[100];
pthread_mutex_t lck;
bool t = false;



void iner_yield(){
   
    
    int tempid = threadarr[0].threadid;
    sut_task_f temp_fn = threadarr[0].threadfunc;
    ucontext_t temp_ctx = threadarr[0].threadcontext;
    
    
    for (int i=0; i<=last-2;i++){
        //printf("Change order: %d \n",i);
        threadarr[i].threadid = threadarr[i+1].threadid;
        threadarr[i].threadcontext = threadarr[i+1].threadcontext;
        threadarr[i].threadfunc = threadarr[i+1].threadfunc; 
    }

    threadarr[last-1].threadid = tempid;
    threadarr[last-1].threadcontext = temp_ctx;
    threadarr[last-1].threadfunc = temp_fn;
    
    
}
void * io_loop(void * args){

}


void * exec_loop(void* args){
    
    
    while (true){
        
        
        if (threadarr[0].threadid != -1){
            
            
            pthread_mutex_lock(&lck);
            swapcontext(&parent, &(threadarr[0].threadcontext));
            pthread_mutex_unlock(&lck);
            if (t){
                
                iner_yield();
                t = false;
            }
            
            
            
        } else {
            usleep(1000);
            
        }
        
    }
}




void sut_init(){
    numthreads = 0;
    curthread = 0;
    last = 0;
    
    getcontext(&parent);
    
    if (num_computation_threads > 1){
        pthread_create(&CEXEC2, NULL, exec_loop, NULL);
    }
    
    for (int i = 0; i<100; i++){
        threaddesc *tptr;
        tptr = &(threadarr[i]);
        tptr->threadid = -1; // This means it is uninitialized
        threaddesc *tptrW;
        tptrW = &(threadarrWait[i]);
        tptrW->threadid = -1;
    }
    pthread_create(&CEXEC1, NULL, exec_loop, NULL);
    pthread_create(&IEXEC, NULL, io_loop, NULL);
    
}

bool sut_create(sut_task_f fn){
    ;
    
    
    ucontext_t thread_context;
    getcontext(&thread_context);
    threaddesc  tdescptr = { numthreads, (char *)malloc(THREAD_STACK_SIZE), fn, thread_context };
    threaddesc *tptr;
    tptr = &tdescptr;
    
    tptr->threadcontext.uc_stack.ss_sp = tptr->threadstack;
    tptr->threadcontext.uc_stack.ss_size = THREAD_STACK_SIZE;
	tptr->threadcontext.uc_link = &parent;
	tptr->threadcontext.uc_stack.ss_flags = 0;
    
    threadarr[last] = tdescptr;
    makecontext(&(threadarr[last].threadcontext),threadarr[last].threadfunc,0);
    
    numthreads++;
    last++;
    
    return 1;
}

void sut_yield(){
    t = true;
    
    swapcontext(&(threadarr[0].threadcontext),&parent);
    
    
    
    
}

void sut_exit(){
    
    
    for (int i=0; i<=last-2;i++){
        
        threadarr[i].threadid = threadarr[i+1].threadid;
        threadarr[i].threadcontext = threadarr[i+1].threadcontext;
        threadarr[i].threadfunc = threadarr[i+1].threadfunc; 
    }

    threadarr[last-1].threadid = -1;
    
    last--;
    
}

int sut_open(char *dest){
    return fileno(fopen(dest,"a+"));
}

void sut_write(int fd, char *buf, int size){
    write(fd,buf,size);
}

void sut_close(int fd){
    close(fd);
}

char *sut_read(int fd, char *buf, int size){
    
    read(fd,buf,size);
    
    return buf;
    
}

void sut_shutdown(){
    
    pthread_join(CEXEC1, NULL);
    if (num_computation_threads > 1){
        pthread_join(CEXEC2, NULL);
    }
    pthread_join(IEXEC, NULL);
    
    pthread_exit(NULL);
    printf("Shutdown called");
    exit(0);
}




