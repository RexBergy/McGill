#A program to make a file named polyapp
#Philippe Bergeron
#260928589


polyapp: polyapp.o poly.o utils.o
	gcc -o polyapp polyapp.o poly.o utils.o
polyapp.o: polyapp.c poly.h utils.h
	gcc -c polyapp.c 
poly.o: poly.c
	gcc -c poly.c 
utils.o: utils.c
	gcc -c utils.c 
