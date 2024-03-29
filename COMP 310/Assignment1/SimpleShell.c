#include <stdio.h>
#include <string.h>
#include <stdlib.h>





int getcmd(char *prompt, char *args[], int *background)
{
    int length, i = 0;
    char *token, *loc;
    char *line = NULL;
    size_t linecap = 0;

    printf("%s", prompt);
    length = getline(&line, &linecap, stdin);

    if (length < 1)
    {
        exit(-1);
    }

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
        for (int j = 0; j<strlen(token); j++)
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
    return i;

}

int main(void)
{
    int i = 0;
    char *args[20];
    int bg;
    while (1)
    {
        bg = 0;
        int cnt = getcmd("\n>>  ", args, &bg);
        i++;

    }
}