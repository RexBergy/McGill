# fileio.asm
# Philippe Bergeron 260928589

.data

#You must use accurate an file path.
#These file paths are EXAMPLES,
#These will not work for you!
	
str1:	.asciiz ".\\Assignment 3\\test2.txt"
str2:	.asciiz "test2.txt"
str3:	.asciiz "test2.pbm"	#used as output
err1:	.asciiz "There was an error with opening the file.\n"
err2:	.asciiz "There was an error with reading/writing the file.\n"
err3:	.asciiz "There was an error with closing the file.\n"
sp_ch:	.asciiz "P1\n50 50\n"

buffer:		.space 4096		# buffer for upto 4096 bytes (increase size if necessary)
array:		.space 4096
newbuff:	.space 4096

	.text
	.globl main

main:	la $a0,str1		#readfile takes $a0 as input
	jal readfile

	la $a0, str3		#writefile will take $a0 as file location
	la $a1,buffer		#$a1 takes location of what we wish to write.
	jal writefile

exit:	li $v0,10		# exit
	syscall
	
readfile:	addi, $sp, $sp, -4	# $ra in stack pointer -4
		sw $ra, 0($sp)
		
		
		
		li $v0,13		# system call to open file in reading mode
		li $a1,0
		li $a2,0
		syscall
	
		slt $t0, $0, $v0	# error if less than zero
		beq $t0, $0, err_o	

		
		move $s0,$v0		# file descriptor in $s0

		
		li $v0, 14		# system call to read from file
		move $a0,$s0		# 4096 bytes
		la $a1, buffer
		li $a2, 4096
		syscall
	
		
		slt $t0, $0, $v0	# error if less than zero
		beq $t0, $0, err_r
		
		li $v0, 4		# print string in buffer
		la $a0, buffer
		syscall
		
		jal convert		# convert
		
		# close the file (make sure to check for errors)
		li $v0, 16
		move $a0,$s0
		syscall
		
		slt $t0, $0, $v0
		beq $t0, $0, err_c

		lw $ra, 0($sp)
		addi, $sp, $sp, 4
		
		jr $ra
		
	
err_o:		li $v0, 4
		la $a0, err1
		syscall

		li $v0, 10	# exit
		syscall
	
err_r: 		li $v0, 4
		la $a0, err2
		syscall
		
		li $v0, 10	# exit
		syscall
		
err_c:		li $v0, 4
		la $a0, err3
		syscall
		
		li $v0, 10	# exit
		syscall

		

convert:	# convert buffer into a 2D array
		la $s1, buffer		# $s1 memory address pointer of buffer
		la $s2, array		# $s2 memory address pointer of 2D array 
		
		li $s3, 48	# $s3 = 48 = 0
		li $s4, 49	# $s4 = 49 = 1
		li $s5, 10	# $s5 = 10 = \n
		
convLp:		lb $t0, 0($s1)		# $t0 byte at memory address pointer
		
		# check byte
		beq $t0, $s3, zero
		beq $t0, $s4, one
		beq $t0, $s5, newline
		
		# reached end of buffer		
		
		jr $ra
		
zero:		li $t1, 0	#store 0 in 2d Array at $s2 memory address
		sw $t1, 0($s2)		
		addi $s1, $s1, 1	
		addi $s2, $s2, 4		
		j convLp
		
one:		li $t1, 1	#store 1 in 2d Array at $s2 memory address
		sw $t1, 0($s2)		
		addi $s1, $s1, 1	
		addi $s2, $s2, 4
		j convLp
		
newline:	addi $s1, $s1, 1	# we ignore a newline, incrment address $s1
		j convLp


	



#Open the file to be read,using $a0
#Conduct error check, to see if file exists

# You will want to keep track of the file descriptor*

# read from file
# use correct file descriptor, and point to buffer
# hardcode maximum number of chars to read
# read from file

# address of the ascii string you just read is returned in $v1.
# the text of the string is in buffer
# close the file (make sure to check for errors)


	
writefile:	addi $sp, $sp, -4
		sw $ra 0($sp)

		jal cpyArr	#copy array to newbuff

		add $s1, $a1, $0	# save a1 to s1

		#open file to be written to, using $a0.
		li $v0, 13
		li $a1, 9
		add $a2, $0, $0	
		syscall
	
		#error opening
		slt $t0, $0, $v0
		beq $t0, $0, err1	

		# save file descriptor
		add $s0, $0, $v0
		
		#write the specified characters 
		li $v0, 15
		move $a0,$s0
		la $a1, sp_ch
		li $a2,10
		syscall
		
		#write the contents stored in newbuff
		li $v0, 15
		add $a0, $0, $s0
		la $a1, newbuff
		addi $a2, $0, 2500
		syscall
		
		# error writing
		slt $t0, $0, $v0
		beq $t0, $zero, err2
		
		# close the file (make sure to check for errors)
		li $v0, 16
		add $a0, $0, $s0
		syscall
		
		slt $t0, $0, $v0
		beq $t0, $zero, err3
		
		lw $ra 0($sp)
		addi $sp, $sp, 4
		
		jr $ra


cpyArr:		la $s1, array		# $s1 memory addres pointer for array
		la $s2, newbuff		# $s2 memory address pointer for newbuff
		
		addi $s3, $0, 0		# $s3 = 0
		addi $s4, $0, 1		# $s4 = 1
		
		li $s0,10000		# 2500 elements = 10000 bytes
		
		add $t2, $0, $0		# $t0 counter starting at 0
		
cpyLp:		slt $t3, $t2, $s0	# loop until end of array	
		beq $t3, $0, cpy_end

		lw $t0, 0($s1)		# load contents of $s1 in $t0 
		
		addi $t2, $t2, 1	# increment counter
		
		beq $t0, $s3, put0	# check contnets
		beq $t0, $s4, put1	
		
		jr $ra
		
put0:		addi $t1, $0, 48	# $t1 has ascii code for 0
		sb $t1, 0($s2)		# store contents of $t1 to memory address at $s2
		addi $s1, $s1, 4	# next memory address
		addi $s2, $s2, 1	# go to next byte in newbuff
		j cpyLp
		
put1:		addi $t1, $0, 49	# $t1 has ascii code for 1
		sb $t1, 0($s2)		
		addi $s1, $s1, 4	# nest memory address
		addi $s2, $s2, 1	# go to next  byte in newbuff
		j cpyLp
		
cpy_end:	jr $ra
#open file to be written to, using $a0.
#write the specified characters as seen on assignment PDF:
#P1
#50 50
#write the content stored at the address in $a1.
#close the file (make sure to check for errors)
