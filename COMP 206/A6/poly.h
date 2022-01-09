/*
 *A program to compile poly.c
 ***********************************************************************
 *Name                    Dept.                Date           Notes
 ***********************************************************************
 Philippe Bergeron        Computer Science     April 12 2020  Initial
 */

#ifndef POLY
#define POLY

struct PolyTerm{
	int coeff;
	int expo;
	struct PolyTerm *next;
};

extern struct PolyTerm *head;

int addPolyTerm(int c, int e);
void displayPolynomial();
int evaluatePolynomial(int x);

#endif
