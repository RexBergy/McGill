# This program calculates the slope and midpoint between two points in a plane
#Philippe Bergeron
#260928589 Computer Science

	.data
str_x1:
	.asciiz "Enter x1: "
str_y1:
	.asciiz "Enter y1: "
str_x2:
	.asciiz "Enter x2: "
str_y2:
	.asciiz "Enter y2: "
out_midpoint:
	.asciiz "The midpoint is: "
out_slope:
	.asciiz "The slope is: "
comma:
	.asciiz ","


	.text	
main:

############################# Assigning values and inputs to register #################
	li $v0, 4	#prints str_x1
	la $a0, str_x1
	syscall
	
	li $v0, 5	#loads the input of str_x1
	syscall
	
	add $t0, $zero, $v0	#assigns this input to temporary register $t0
	
	li $v0, 4	#prints str_y1
	la $a0, str_y1
	syscall
	
	li $v0, 5	#loads the input of str_y1
	syscall
	
	add $t1, $zero, $v0	#assigns this input to temporary register $t1	
	
	li $v0, 4	#prints str_x2
	la $a0, str_x2
	syscall
	
	li $v0, 5	#loads the input of str_x2
	syscall
	
	add $t2, $zero, $v0	#assigns this input to temporary register $t2
	
	li $v0, 4	#prints str_y2
	la $a0, str_y2
	syscall
	
	li $v0, 5	#loads the input of str_y2
	syscall
	
############################ Arithmetic operations ###############################
	
	add $t3, $zero, $v0	#assigns this input to temporary register $t3
	
	add $s0, $t0, $t2 	#adds str_x1 and str_x2 
				#assigns to register $s0
	
	sub $s1, $t2, $t0 	#subtracts str_x1 to str_x2
				#assigns to register $s1
				
	add $s2, $t1, $t3	#adds str_y1 and str_y2
				#assigns to register $s2
	
	sub $s3, $t3, $t1	#subtracts str_y1 to str_y2
				#assigns to register $s3 
				
	div $s0, $s0, 2	#stores the x midpoint in $s0
	
	div $s2, $s2, 2	#store the y midpoint in $s2
	
	div $s3, $s3, $s1	#store the slope in $s3
	
	
######################## Outputing the results ##########################	
	li $v0, 4	#prints string out_midpoint
	la $a0, out_midpoint
	syscall
	
	li $v0, 1	#prints a of out_midpoint
	add $a0, $zero, $s0
	syscall
	
	li $v0, 4	#prints a comma
	la $a0, comma
	syscall
	
	li $v0, 1	#prints b 0f out_midpoint
	add $a0, $zero, $s2
	syscall
	
	li $v0, 11	#prints a new line character
	li $a0, '\n'
	syscall
	
	li $v0, 4	#prints string out_slope
	la $a0, out_slope
	syscall
	
	li $v0, 1	#prints slope integer
	add $a0, $zero, $s3
	syscall
	



# EXIT PROGRAM
li $v0,10		#system call code for exit = 10
syscall			#call operating sys : EXIT
