#include <stdio.h>
#include <stdlib.h>
#include "utils.h"
#include "poly.h"

/************************************************************************
 * Name                Dept.                Date                  Notes
 * **********************************************************************
 * Philippe Bergeron   Computer Science     April 12 2020         Initial
 * Philippe Bergeron   Computer Science     April 14 2020         Fixed bugs
 * */

int main(int argc, char *argv[]) {
	char line[500];

	FILE* findfile = fopen(argv[1], "r");
	if (findfile == NULL) { //the data file was not found
		fprintf(stderr, "Error, unable to locate the data file %s\n", argv[1]);
		return 100;
	}
	while (fgets(line, sizeof(line), findfile)){ // creates the linked list from the data file
		int c;
		int e;
		parse(line, &c, &e);
		int spa = addPolyTerm(c, e);
		if (spa == -1){
			fprintf(stderr, "Not sufficient memory");
			return 100;
		}
		
	} 
	displayPolynomial(); //displays the polynomial
	for (int i = -2; i<=2; i++){ // evaluates the polynomial for a given integer
		int p = evaluatePolynomial(i);
		printf("for x=%d, y=%d\n", i, p);
	}

	return 0;


}


