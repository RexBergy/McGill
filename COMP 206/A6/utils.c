#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "utils.h"

/********************************************************************
 * Name                 Dept.              Date              Notes
 * ******************************************************************
 * Philippe Bergeron    Computer Sceince   April 12 2020     Initial
 * Philippe Bergeron    Computer Cience    April 14 2020     Fixed bugs
 * ******************************************************************
 */


void parse(char *line, int *c1, int *c2) { // Stores coefficient & exponent
	char *pter1 = strtok(line, " ");
	char *pter2 = strtok(NULL, " ");
	*c1 = atoi(pter1);
	*c2 = atoi(pter2);
}

int powi(int x, int exp) { // returns integer x raised to the power of int exp
	int result = 1; // result initialised to 1

	while (exp != 0) { // multiplies result by x, exp number of times
		result *= x;
		exp -= 1;
	}
	return result;
}
