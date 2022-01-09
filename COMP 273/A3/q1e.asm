# This program manipulates an array by inserting and deleting at specified index and sorting the contents of the array.
# The program should also be able to print the current content of the array.
# The program should not terminate unless the 'quit' subroutine is called
# You can add more subroutines and variables as you wish.
# Remember to use the stack when calling subroutines.
# You can change the values and length of the beginarray as you wish for testing.
# You will submit 5 .asm files for this quesion, Q1a.asm, Q1b.asm, Q1c.asm, Q1d.asm and Q1e.asm.
# Each file will be implementing the functionalities specified in the assignment.
# Use this file to build the helper functions that you will need for the rest of the question.

# Philippe Bergeron
# 260928589


.data

beginarray: .word 1, 3, 77, 5, 23, -999			#’beginarray' with some contents	 DO NOT CHANGE THE NAME "beginarray"
array: .space 4000					#allocated space for ‘array'
str_command:	.asciiz "Enter a command (i, d, s or q): " # command to execute
space: .asciiz " "
i: .asciiz "i"
d: .asciiz "d"
s: .asciiz "s"
q: .asciiz "q"
error: .asciiz "An invalid invalid input was entered. Valid inputs are -i for insert, -d for delete, -s for sort and -q to quit program.\n"
newline: .asciiz "\n"
str_index: .asciiz "Enter index: "
str_value: .asciiz "Enter value: "
error_in: .asciiz "Invalid index."
curr_arr:.asciiz "The current array is: "


	.text
	.globl main

main:
	# main code comes here
	#a $a0, beginarray
	#jal length
	
	#addi $a2,$v0,1			# copy length as argument 3
	la $a0, beginarray		# load address of begin array as argu 1
	la $a1, array			# load address of array as argu 2
	jal copyarray

prompt:	
	li $v0,4			# print current array
	la,$a0,curr_arr
	syscall
	la $a0, array
	jal printarray
	
	li $v0,4			# print newline
	la,$a0,newline
	syscall


	li $v0,4			# print str_command
	la,$a0,str_command
	syscall
	
	li $v0,12			# read character
	syscall

	
	la $t0,i			# checks if equal to insert
	lb $t0,($t0)
	beq $t0,$v0,insert
	
	la $t0,d			#checks if equal to delete
	lb $t0,($t0)
	beq $t0,$v0,delete
	
	la $t0,s			#checks if equal to sort
	lb $t0,($t0)
	beq $t0,$v0,sort
	
	la $t0,q			#checks if equal to sort
	lb $t0,($t0)
	beq $t0,$v0,quit
		
	li $v0,4			# print newline
	la,$a0,newline
	syscall

	
	li $v0,4			# prints error because of invalid input
	la,$a0,error
	syscall
	
	j prompt			
	

	# main code ends here
quit:	li $v0,10
	syscall

########## length subroutine #########################
length: 	add $v0,$zero,$zero		# We use $v0 as the counter and initialize it
		addi $t1,$zero, -999		# Set $t1 to -999

loop1:		lw $t0,0($a0)			# load integer (4 bytes = 1 word) into $t0
		beq $t0,$t1,done_length 	# reached the special integer -999
		addi $v0,$v0,1			# increment count 1
		addi $a0,$a0,4			# advance pointer
		j loop1				# loop again

done_length: 	jr $ra				# goes back to main

################# copyarray ######################

copyarray:		add $t2,$a0,$zero
			sw $ra, ($sp)
			jal length
			addi $t1,$v0,1
loop2:			beq $t1,$zero,done_copyarray
			lw $t0,0($t2)			# load the content of pointer 1 to $t0
			sw $t0,0($a1)			# store the content of of $t0 in address of pointer 2
			addi $t2,$t2,4			# increment pointer 1 by 1
			addi $a1,$a1,4			# increment pointer 2 by 1
			addi $t1,$t1,-1
			j loop2				# copy next integers

done_copyarray:		lw $ra, ($sp)
			jr $ra				# goes back to main

###################### printarray ###########################
printarray:		addi $t1,$zero, -999		# Set $t1 to -999
			add $t2,$zero,$a0		# copy arg0 to $t1
loop3:			lw $t0,0($t2)			# load the content of pointer 1 to $t0
			beq $t0,$t1,done_printarray	# reached the special integer -999
			
			li $v0,1			# system call for print_int
			add $a0,$zero,$t0
			syscall
			
			li $v0,4			# system call for print_str
			la $a0, space
			syscall
			
			addi $t2,$t2,4			# incrment pointer by 1
			j loop3				# loop again


done_printarray:	jr $ra





# INSERT subroutine expects index of and value to insert
# You will repeat the steps below for each of the .asm files. Q1b.asm is shown below

# For Q1b.asm, you will need to implement the insert operation

########################## insert ###########################
insert:				la $t0,array		# pointer of array in $t0
				add $a0,$t0,$zero	# argument is pointer
				jal length		# compute length
				add $t1,$v0,$zero	# length 
				
				
				li $v0,4		# print newline
				la,$a0,newline
				syscall

				li $v0,4		# Asks user for an index
				la $a0,str_index
				syscall
				
				li $v0,5		# Read index
				syscall
				
				add $t2,$v0,$zero	# stores index in register $t2
				add $t5,$zero,$t2	# save index to $t5 as well
				
				bgt $t2,$t1,index_er	# index greater than length -1
				blt $t2,$zero,index_er	# index less than zero
				
				
				j put_val

index_er:			li $v0,4		# index too large or too samll
				la $a0,error_in
				syscall		
				
				j insert		# go back to insert

put_val:	
				li $v0,4		# Asks user for value
				la,$a0,str_value
				syscall
				
				li $v0,5		# read value
				syscall
				
				add $t3,$v0,$zero	# store value in register $t3
				
				la $t0,array		# puts array address at $t0
				
				#add $t6,$zero,$t5	# save index to $t5 as well
reach_in:			beq $t5,$zero,ins	# checks if index reached zero, then we reached index
				addi $t0,$t0,4		# move pointer to next address
				addi $t5,$t5,-1		# decrease index by 1
				j reach_in

ins:				lw $t4,0($t0)		# loads the contents of $t0 in $t4
				sw $t3,0($t0)		# store the content of $t3 in address of pointer $t0
				sub $t5,$t1,$t5		# subtract length -1 minus index
				addi $t5,$t5,1		# add 1 because of special int
				
ins_lp:				beq $t5,$zero,done_ins	# finished inserting
				addi $t0,$t0,4		# moves address pointer by 1
				lw $t3,0($t0)		# loads the contents of $t0 in $t3
				sw $t4,0($t0)		# stores the contents of $t4 in address pointer $t0
				add $t4,$t3,$zero	# updates $t4
				addi $t5,$t5,-1		# decreament by 1
				j ins_lp
				
				
done_ins:			j prompt

########################## delete ########################										

delete:				la $t0,array		# pointer of array in $t0
				add $a0,$t0,$zero	# argument is pointer
				jal length		# compute length
				add $t1,$v0,$zero	# length
				
				
				li $v0,4		# print newline
				la,$a0,newline
				syscall
				
				li $v0,4		# Asks user for an index
				la $a0,str_index
				syscall
				
				li $v0,5		# Read index
				syscall
				
				add $t2,$v0,$zero	# stores index in register $t2
				add $t5,$zero,$t2	# save index to $t5 as well
				
				bgt $t2,$t1,ind_er	# index greater than length -1
				blt $t2,$zero,ind_er	# index less than zero
				
				
				j del_val

ind_er:				li $v0,4		# index too large or too samll
				la $a0,error_in
				syscall		
				
				j delete		# go back to insert

del_val:			la $t0,array		# pointer of array in $t0	
				
point_i:			beq $t5,$zero,del	# pointer is at address to delete
				addi $t0,$t0,4		# move pointer to next address
				addi $t5,$t5,-1		# decrement index by 1
				j point_i

del:				beq $t1,$zero,done_del	# loop size of length		
				lw $t4,4($t0)		# load word at next address in $t4
				sw $t4,($t0)		# store contents of $t4 in address $t0
				addi $t0,$t0,4		# move pointer to next address	
				addi $t1,$t1,-1		# decrement by 1
				j del
				
done_del:			j prompt

############################### sort #############################################
# we used bubble sort
sort: 				li $v0,4		# print a newline
				la,$a0,newline
				syscall
	
				la $a0, array	
				jal length		# $v0 is length(array)
				addi $t0, $v0, 1	# $t0 is length + 1
				addi $t1, $v0, -1	# $t1 is length - 1
				li $t2, 0		# $t2 first counter 
				li $t3, 0		# $t3 second counter 
				la $a0, array

check:				lw $t4, 0($a0)		# checks if element at current address
				lw $t5 4($a0)		# is greater or equal to element at next address
				bge $t4, $t5, swap	# swap them
	
next:				addi $a0, $a0, 4	# Move to next address in array
				addi $t2, $t2, 1	# increment first counter by 1
				bge $t2, $t1, back	# we check if we reached the end, then go back
				j check			# check next element
		
back:				la $a0, array		
				li $t2, 0
				beq $t3, $t0, done_s
				addi $t3, $t3, 1	#  increment second counter by 1
				j check
	
swap:				sw $t4, 4($a0)		# we swap contents 
				sw $t5, 0($a0)
				j next			# go to next address

done_s:				la $a0, array		
				j prompt	
