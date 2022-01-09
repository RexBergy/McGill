/* A program that implements a simple banking application
 * which can (i) add an account number, (ii) make a deposit
 * (iii) make a withdrawal
 * ***********************************************************************
 * Author                 Dept.                Date          Notes
 * ***********************************************************************
 * Philippe Bergeron      Computer Science     March 6 2020  Initial version
 * Philippe Bergeron      Computer Science     March 7 2020  Update
 * Philippe Bergeron      Computer Science     March 12 2020 Update
 * Philippe Bergeron      Computer Science     March 13 2020 Update
 */
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#define FALSE  0
#define TRUE  1

int main( int argc, char *argv[] ){

	char line[250];
	FILE *file;
	FILE *testfile;
	char* accnum;
	char* name;
	char* date;
	char* amount;
	int exists = FALSE;
	int change = FALSE;
	float balance = 0;
	void add(char* accnum, char* name, FILE *file);
	void deposit(char* accnum, char* date, char* amount, FILE *file);
	void withdraw(char* accnum, char* date, char* amount, FILE *file);
	
	testfile = fopen("bankdata.csv", "r"); // creating testfile variable to check if file exists
	if (testfile == NULL) {
		fprintf(stderr, "Error, unable to locate the data file bankdata.csv");
		exit(100);
	}

	file = fopen("bankdata.csv", "a+"); // Opening file with apend mode
	

	if (argc < 3) { // checks if no of arguments lower than 3, gives error
		fprintf(stderr, "Error, incorrect usage!\n-a ACCTNUM NAME\n-d ACCTNUM DATE AMOUNT\n-w ACCTNUM DATE AMOUNT\n");
		exit(1);
	}
	accnum = argv[2]; // account number always 3rd argument

	while (fgets(line,sizeof(line),testfile)) { // reading existing file to get information and using it

		char* record_type = strtok(line, ",");
		char* record_accnum = strtok(NULL, ",");
		if (strcmp(record_accnum, accnum) == 0) {
			exists = TRUE;

			if (strcmp(record_type, "TX") == 0) {
				record_accnum = strtok(NULL, ",");
				record_accnum = strtok(NULL, ",");
				balance += strtof(record_accnum, NULL);
			}
		}
	}
	
	if (strcmp(argv[1], "-a") == 0) { // checks if second argument is -a and executes add operation
		if (argc != 4) {
			fprintf(stderr, "Error, incorrect usage!\n-a ACCTNUM NAME\n");
			exit(1);
		}
		else if (exists) {
			fprintf(stderr, "Error, account number %s already exists\n", accnum);
			exit(50);
		}
		else {
			name = argv[3];
			add(accnum, name, file);
		}
	}
	else if (exists == FALSE) { // gives an error if the account number doesn't exist for withdraw and deposit
		fprintf(stderr, "Error, account number %s does not exists\n", accnum);
		exit(50);
	} 
	else if (strcmp(argv[1],"-d") == 0) { // checks if second argument is -d and executes deposit operation
		if (argc != 5) {
			fprintf(stderr, "Error, incorrect usage!\n-d ACCTNUM DATE AMOUNT\n");
			exit(1);
		}
		else {
			date = argv[3];
			amount = argv[4];
			deposit(accnum, date, amount, file);
		}
	}
	else if (strcmp(argv[1], "-w") == 0) { // checks if second argument is -w and executes withdraw operation 
	
		if (argc != 5) {
			fprintf(stderr, "Error, incorrect usage!\n-w ACCTNUM DATE AMOUNT\n");
			exit(1);
		}
		else if ( strtof(argv[4], NULL) > balance ) {
			fprintf(stderr, "Error, account number %s has only %.2f\n", accnum, balance);
			exit(60);
		}
		else {
			date = argv[3];
			amount = argv[4];
			withdraw(accnum, date, amount, file);
		}
	}
	else { // gives an error if all of the above is false
		fprintf(stderr, "Error, incorrect usage!\n-a ACCTNUM NAME\n-d ACCTNUM DATE AMOUNT\n-w ACCTNUM DATE AMOUNT\n");
		exit(1);
	}	
	
	
}
// writes and appends the correct information for each operation in the bankdata.csv file
void add(char* accnum, char* name, FILE *file) {

	fprintf(file, "AC,%s,%s\n", accnum, name);
	exit(0);
}
void deposit(char* accnum, char* date, char* amount, FILE *file) {

	fprintf(file, "TX,%s,%s,%s\n", accnum, date, amount);
	exit(0);
}
void withdraw(char* accnum, char* date, char* amount, FILE *file) {
	char minus[5];
	strcpy(minus, "-");	
	strcat(minus, amount);
	
	fprintf(file, "TX,%s,%s,%s\n", accnum, date, minus);
	exit(0);
}
	
		
