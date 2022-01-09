Note for the TA. 

-sut.c runs with the following command for test5 for example:

"gcc test5.c sut.c -pthread"

-sut.c uses 
#include <stdio.h>
#include <signal.h>
#include <unistd.h>
#include <stdlib.h>
#include <pthread.h>
#include <ucontext.h>
#include "sut.h"
#include "YAUThreads.h" 

To grade part A, leave 
int num_computation_threads = 1;
For part B:
int num_computation_threads = 2;

Final note, please run the tests multiple times as there is sometimes unexplained bugs
on the first try :)
Thank you