#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "poly.h"
#include "utils.h"

// A program to make a linked list to store a polynomial of any length
/**********************************************************************
 * Name                Dept.               Date              Notes
 * ********************************************************************
 * Philippe Bergeron   Computer Science    April 12 2020     Initial
 * Philippe Bergeron   Computer Science    April 14 2020     Fixed bugs
 * ********************************************************************
 * */


/*struct PolyTerm { 
	int coeff;
	int expo;
	struct PolyTerm *next; //pointer to next node in linked list
};*/

typedef struct PolyTerm term;
term *head = NULL; //Declares global variable

int addPolyTerm(int c, int e){
	
	term *tmp = (term *) malloc(sizeof(term)); // Creates a new node to be added to the list
	if (tmp == NULL) {
		return -1;
	}
	tmp->coeff = c;
	tmp->expo = e;

	if (head == NULL) { //The head is tmp if the linked list is empty
		head = tmp;
		tmp->next = NULL;
		return 0;
	} else { //The linked list is not empty 

		if (head->expo > e){ // replaces head if the exponent is larger
			tmp->next = head;
			head = tmp;
			return 0;
		} else {
			term *pointer = head; // creates a current and previous pointer
			term *previous = NULL;
			while (pointer) { // A while loop to add the new node in the correct position
				if (pointer->expo == e) {
					pointer->coeff += c;
					return 0;
				}
				else if (pointer->expo > e) {
					previous->next = tmp;
					tmp->next = pointer;
					return 0;
				}
				else if (pointer->next == NULL) {
					pointer->next = tmp;
					tmp->next = NULL;
					return 0;
				}
				previous = pointer;
				pointer = pointer->next;
			}
		}

	}
}





void displayPolynomial(){
	printf("%dx%d", head->coeff, head->expo); // Prints the first polynomial
	term *pter = head->next; //Creates a pointer to traverse a linked list

	while (pter) { 
		int c = pter->coeff;
		int e = pter->expo;
		if (c<0) {
			printf("%dx%d", c, e);
		} else {
			printf("+%dx%d", c, e); // prints the polynomial
		}
		
		pter = pter->next;
	}
	printf("\n");
}

int evaluatePolynomial(int x){ // traverses the linked list and evaluates the polynomial
	int result = 0;
	term *pter = head;
	while (pter){
		int c = pter->coeff;
		int e = pter->expo; 
		result += c*powi(x, e); // adds the power
		pter = pter->next; //changes to next term
	}
	return result;
}

