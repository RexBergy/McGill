#include <stdio.h>
#include <sys/types.h>
#include <unistd.h>
#include <string.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <signal.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <errno.h>


int cd(char **args);
int exit1(char **args);
int pwd(char **args);
int fg(char **args);
int jobs(char **args);

pid_t list_jobs[100];
pid_t global_pid = (pid_t)-1;
int nbJobs = 0;

static void addJob(pid_t pid){
    list_jobs[nbJobs] = pid;
    nbJobs++;
}

static void removeJob(pid_t pid){
    for (int i=0; i<nbJobs; i++){
        if (list_jobs[i] == pid){
            
            list_jobs[i] = 0;
        }
    }
    nbJobs--;
}

static void signalHandler(int sig)
{
    if (sig == 2)
    {
        kill(global_pid,SIGKILL);
    
    } else if (sig == 20)
    {
        
    }
    
}

static void removeChild(pid_t pid){
    removeJob(pid);
}
/*
  List of builtin commands
 */
char *builtin_str[] = {
  "cd",
  "exit",
  "pwd",
  "fg",
  "jobs"
};

int (*builtin_func[]) (char **) = {
  &cd,
  &exit1,
  &pwd,
  &fg,
  &jobs
};

int num_builtins() {
  return sizeof(builtin_str) / sizeof(char *);
}

/*
  Builtin function implementations.
*/
int cd(char **args)
{
  if (args[1] == NULL) {
    char cwd[50];
    getcwd(cwd, sizeof(cwd));
    printf("%s\n",cwd);
  } else {
    if (chdir(args[1]) != 0) {
      perror("No directory was found");
    }
  }
  return 1;
}


int exit1(char **args)
{
    if (nbJobs != 0){
        while (1){
            if (nbJobs == 0){
                return 0;
            }
        }

    } else {
        return 0;
    }
    
}

int pwd(char **args){
    char cwd[50];
    getcwd(cwd, sizeof(cwd));
    printf("%s\n",cwd);
}

int fg(char **args){
    if (nbJobs == 0){
        printf("No background jobs to foreground");
    } else if (args[1] != NULL){
        waitpid(list_jobs[atoi(args[1])],0,2);
    }
}

int jobs(char **args){
    if (nbJobs == 0){
        printf("No background jobs");
    } else {
        for (int i=0; i<nbJobs; i++){
            if (list_jobs[i] != 0) {
                 printf("%d\n",list_jobs[i]);
            }
        }
    }
    
    

}

int multi_process(char **args, int background, int io, int *pipefd){
    pid_t pid, wpid;
    int status;

    pid = fork();
    global_pid = pid;
    
    addJob(pid);
    //if(setpgid(pid, pid) == 0) perror("setpid");
    setpgid(global_pid,global_pid);
    if (pid == 0){ // Child process
        sleep(5);
        if (io == 0){ // recieves
            dup2(pipefd[0], STDIN_FILENO);
            close(pipefd[0]);
            close(pipefd[1]);

        } else if (io == 1){ //send
            dup2(pipefd[1], STDOUT_FILENO);
            close(pipefd[0]);
            close(pipefd[1]);
        } 
        if (execvp(args[0], args) == -1){
            perror("failed child execute");
        }
        
        exit(3);
    } else if (pid < 0) { //There was an error forking
        perror("failed to fork");
    } else {
        
        do {
            if (background != 1){
                //list_jobs[0] = pid;
                wpid = waitpid(pid, &status, WUNTRACED);
            } else {
                //removeJob(pid);
                
            }
            

    } while (!WIFEXITED(status) && !WIFSIGNALED(status));
    return 1;
    }
}


int execute(char **args, int background){
    int i;
    int j = 0; // number of arguments
    while (args[j] != NULL){
        j++;
    }

    if (args[0] == NULL){
        return 1;
    }
    for (int i=0; i<j; i++) {
        // Pipe found!
        if (strcmp(args[i], "|") == 0) {
            //char *newargs[20];
            
            char *newargs[20];
            int f = 0;
            int pipefd[2];
            if (pipe(pipefd) == -1) {
                perror("pipe");
                exit(EXIT_FAILURE);
            }
            
            args[i] = '\0';
            multi_process(args,0, 1,pipefd);
            
            
            for (int z = i+1; z<j; z++){
                args[f++] = args[z];
            }
            args[f++] = '\0';
            
            multi_process(args,1, 0, pipefd);
            close(pipefd[0]);
            close(pipefd[1]);
            return 1;

        // Redirect found!
        } else if (strcmp(args[i], ">") == 0) {
            char *newargs[20];
            
            
            close(1);
            int fd = open(args[i+1], O_CREAT | O_WRONLY,  0777);
            if (fd == -1){
                errno;
            }
            args[i] = '\0';
            multi_process(args,0, -1, 0);
            freopen("/dev/tty", "w", stdout);
            return 1;
            
        }
    }
    for (i=0; i<num_builtins(); i++){
        if (strcmp(args[0], builtin_str[i]) == 0){
            return (*builtin_func[i])(args);
        }
    }
    return multi_process(args, background, -1, 0);
}

int getcmd(char *prompt, char *args[], int *background)
{
    int length, i = 0;
    char *token, *loc;
    char *line = NULL;
    //token[0] = '\0';
    size_t linecap = 0;
    printf("%s", prompt);
    length = getline(&line, &linecap, stdin);
    if (length <= 0) 
    {
        exit(-1);
    }
    // Check if background is specified..
    if ((loc = index(line, '&')) != NULL) 
    {
        *background = 1;
        *loc = ' ';
    } else
    {
        *background = 0;
    }
    while ((token = strsep(&line, " \t\n")) != NULL) 
    {
        for (int j = 0; j < strlen(token); j++)
        {
            if (token[j] <= 32)
            {
               token[j] = '\0';
            }
        }
        if (strlen(token) > 0)
        {
            args[i++] = token;
        }

    }
    args[i++] = '\0';   
    return i;
}

void shell_loop()
{
    char *args[20];
    int bg;
    int status;
    do {
        int cnt = getcmd("\n >> ", args, &bg);
        status = execute(args,bg);


    } while (status);
}

int main()
{
    signal(SIGTSTP, signalHandler);
    signal(SIGINT, signalHandler);
    signal(SIGCHLD,removeChild);
    shell_loop();
}