# Philippe Bergeron
# 260928589
# This program represents a simple banking application
# The program should allow to make the following operations
# a) opening an account b) finding out the balance, c) making a deposit
# d) making a withdrawal, e) transferring between accounts f) taking a loan 
# closing an account and h) displaying query history.
# The program should terminate when QUIT is entered by the user. 


.data

bank_array: .word 0, 0, 0, 0, 0			# this array holds banking details
type:	.word 0,0	# this array hold the type of command to execute
arguments:	.word 0,0,0	# holds the integers
arg_form:	.word 0,0	# gives the number of expected arguments and number of 5-digit accts 
quHist:		.word 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0	# history of commands
#command:    .space 100000


# error message for invalid transactions
error_mess:	.asciiz "That is an invalid banking transaction. Please enter a valid one. \n" 
# you can add more directives here: including additional arrays and variables.
command_pr:	.asciiz "Enter your command: \n"
statement:	.asciiz "The current bank information stored in the system is: "
rBrack:		.asciiz "]\n"
lBrack:		.asciiz "["
comma:		.asciiz ", "
dollar:		.asciiz "$"
newline:	.asciiz "\n"
error:		.asciiz "That is an invalid banking transaction. Please enter a valid one. \n"

	.text
	.globl main

# you can create a subroutine that converts ASCII to integers.
# you can call this subroutine anytime you need to make a conversion.
############################# Main ############################
main:
	# main code comes here
	
	
	li $v0,4		# print command prompt
	la $a0,command_pr
	syscall
	
	#li $v0,4
	#la $a0,error
	#syscall
	
	la $a0,type		# write the type of command in type (i.e. 2 letters in front)
	jal read_type
	jal check_type		# read the command and perform the correct task or throw an error
	move $s0,$v0		# save return value in $s0
	beq $s0,0,op		# open
	beq $s0,1,cl		# close
	beq $s0,2,de		# depo
	beq $s0,3,wt		# with
	beq $s0,4,ln		# loan
	beq $s0,5,tr		# trans
	beq $s0,6,bl		# balance
	beq $s0,7,hs		# history
	beq $s0,8,qt		# quit
	beq $s0,9,main		# error
op:	la $a0,arguments
	la $a1,type
	la $a2,arg_form
	jal open_account
	la $a0,bank_array
	jal print
	la $a0,bank_array
	jal fill_hist
	j main
cl:	la $a0,arguments
	la $a1,arg_form
	la $a2,bank_array
	jal close_account
	la $a0,bank_array
	jal print
	la $a0,bank_array
	jal fill_hist
	j main
de:	la $a0,arguments
	la $a1,arg_form
	jal deposit
	la $a0,bank_array
	jal print
	la $a0,bank_array
	jal fill_hist
	j main
wt:	la $a0,arguments
	la $a1,arg_form
	jal withdraw
	la $a0,bank_array
	jal print
	la $a0,bank_array
	jal fill_hist
	j main
ln:	la $a0,arguments
	la $a1,arg_form
	la $a2,bank_array
	jal get_loan
	la $a0,bank_array
	jal print
	la $a0,bank_array
	jal fill_hist
	j main
tr:	la $a0,arguments
	la $a1,arg_form
	la $a2,bank_array
	jal transfer
	la $a0,bank_array
	jal print
	la $a0,bank_array
	jal fill_hist
	j main
bl:	la $a0,arguments
	la $a1,arg_form
	la $a2,bank_array
	jal get_balance
	j main
hs:	la $a0,arguments
	la $a1,arg_form
	la $a2,bank_array
	jal history
	j main
qt:	j quit
	
		
#	la $t5, command
#Print:	li $v0, 1
#	lw $a0, ($t5)
#	syscall
#	addi $t5,$t5,4
#	addi $t4,$t4,1
#	bne $t4,5,Print
###############################################################

########################## error ###############################	
printE: # if none of the above commands, print error message
	#li $v0,4
	#la $a0,error_mess
	#syscall	
	
	#li $v0,4
	#la $a0,error
	#syscall
	j errorr
errorr:
	li $v0,4
	la $a0,error
	syscall
	j main
	#li $v0,9		# return value 9 for error
	#jr $ra
####################################################################

############################ read_type ############################
# writes the the first two inputs in MMIO and puts them in array type
read_type:
	addi $t2,$zero,0 	# initializes counter $t2 with value 0
	lui $t0,0xffff		# $t0 = 0xffff0000
	add $t3,$a0,$zero	# puts the address of type in $t3
next:	beq $t2,2,end_r		# checks if counter reached 2
	lw $t1,($t0)		# value at $t1 is 1 if keyboard was pressed
	andi $t1,$t1,0x0001
	beq $t1,$zero,next	# goes back to 'next' if nothing was entered
	lw $t4,4($t0)		# value at $t4 is the input
	sw $t4,0($t3)		# stores the value at $t4 in array type
	add $t3,$t3,4		# go to next address in array
	add $t2,$t2,1		# increment counter by 1
	j next 	
end_r:	jr $ra
#####################################################################

############################## check_type ###########################
# reads and checks the array type
check_type:
	lw $t0,0($a0)		# $t0 is first character
	lw $t1,4($a0)		# $t1 is second character
	add $t2,$t1,$t0		# sum of the two characters
	
	beq $t0,67,check_HL	# first letter is C, go to check H or L
	beq $t0,83,check_V	# first letter is S, go to check V
	beq $t0,68,check_E	# first letter is D, go to check E
	beq $t0,87,check_T	# first letter is W, go to check T
	beq $t0,76,check_N	# first letter is L, go to check N
	beq $t0,84,check_R	# first letter is T, go to check R
	beq $t0,66,check_L	# first letter is B, go to check L
	beq $t0,81,check_HT	# first letter is Q, go to check H or T
	j printE		# first letter not a valid input
check_HL:
	beq $t1,72,open		# CH
	beq $t1,76,close	# CL
	j printE		# C with a wrong letter	

check_V:
	beq $t1,86,open		# SV
	j printE
check_E:
	beq $t1,69,depo		# DE
	j printE
check_T:
	beq $t1,84,withd	# WT
	j printE
check_N:
	beq $t1,78,get_l	# LN
	j printE
check_R:
	beq $t1,82,trans	# TR
	j printE
check_L:
	beq $t1,76,get_bal	# BL
	j printE
check_HT:
	beq $t1,72,hist		# QH
	beq $t1,84,qut		# QT
	j printE
open:	li $v0,0		# return value 0 for opening	
	jr $ra
close:	li $v0,1		# return value 1 for closing
	jr $ra
depo:	li $v0,2		# return value 2 for deposit	
	jr $ra
withd:	li $v0,3		# return value 3 for withdraw
	jr $ra
get_l:	li $v0,4		# return value 4 for loan	
	jr $ra
trans:	li $v0,5		# return value 5 for transfer
	jr $ra
get_bal:li $v0,6		# return value 6 for balance	
	jr $ra
hist:	li $v0,7		# return value 7 for history
	jr $ra
qut:	li $v0,8		# retun value 8 for quit
	jr $ra
####################################################################
	

#################################################################
############################## print ####################################
print:
	move $t0,$a0		# print statement
	li $v0,4
	la $a0,statement
	syscall
	
	li $v0,4		# print front bracket
	la $a0,lBrack
	syscall
	li $t2,1		# counter
	
int:	lw $t1,0($t0)
	beq $t1,0,printInt
	beq $t2,1,print0
	beq $t2,2,print0
printInt:li $v0,1		# prints number which doesn't have zeroes
	move $a0,$t1
	syscall
	beq $t2,5,done
	li $v0,4
	la $a0,comma
	syscall
	addi $t0,$t0,4
	addi $t2,$t2,1
	j int
	
print0:
	bge $t1,10000,printInt
	li $v0,1
	li $a0,0
	syscall
	bge $t1,1000,printInt
	li $v0,1
	li $a0,0
	syscall
	bge $t1,100,printInt
	li $v0,1
	li $a0,0
	syscall
	bge $t1,10,printInt
	li $v0,1
	li $a0,0
	syscall
	j printInt
done:
	li $v0,4
	la $a0,rBrack
	syscall
	jr $ra	
	


###########################################################################

######################### read_args ###################################
read_args:
	lui $t0,0xffff		# $t0 = 0xffff0000
	li $t2,1		# last char is space --> true
	li $t3,1		# argument counter
	li $t4,0		# value in base 10
	li $t5,0		# number of front zeroes
	lw $t6,0($a2)		# expected number of arguments
	lw $t7,4($a2)		# expected number of bank accounts
	li $t8,0		# bank account has zero ---> false
	li $t9,0		# $t9 = number of non-zero integers or non-leading zeroes in each argument
next1:	lw $t1,0($t0)
	andi $t1,$t1,0x0001
	beq $t1,0,next1
	lw $s0,4($t0)
	beq $s0,10,enter	# value at $s0 is equal to <newline>
	beq $s0,13,enter	# value at $s0 is equal to <CR>
	beq $s0,32,space	# value at $s0 is equal to <space>
	beq $s0,48,zero		# $s0 is equal to 0
	blt $s0,48,printE	# $s0 less than 0 ----> not an integer
	bgt $s0,58,printE	# $s0 greater than 9 ---> not an integer
	li $t8,1
	addi $t9,$t9,1
count:	addi $s0,$s0,-48
	mul $t4,$t4,10
	add $t4,$t4,$s0
	li $t2,0
	j next1
space:	bne $t2,0,next1
	li $t2,1
	ble $t3,$t7,bank_num
store:	sw $t4,0($a0)
	beq $t3,$t6,exit
	addi $t3,$t3,1
	addi $a0,$a0,4
	li $t4,0
	li $t5,0
	li $t8,0
	li $t9,0
	j next1
zero:	bgt $t3,$t6,count
	beq $t8,1,non_le
	addi $t5,$t5,1
	j count
non_le:	addi $t9,$t9,1
	j count	
bank_num:add $s1,$t5,$t9
	bne $s1,5,printE
	j store
enter:
	bne $t3,$t6,printE
	j store
add_0:
	add $t4,$t4,$zero
	addi $t3,$t3,1
	j store
exit:	jr $ra

#######################################################################

######################### read_argsT ###################################
### modified from read_args to read when transfer
read_argsT:
	lui $t0,0xffff		# $t0 = 0xffff0000
	li $t2,1		# last char is space --> true
	li $t3,1		# argument counter
	li $t4,0		# value in base 10
	li $t5,0		# number of front zeroes
	lw $t6,0($a2)		# expected number of arguments
	lw $t7,4($a2)		# expected number of bank accounts
	li $t8,0		# bank account has zero ---> false
	li $t9,0		# $t9 = number of non-zero integers or non-leading zeroes in each argument
next2:	lw $t1,0($t0)
	andi $t1,$t1,0x0001
	beq $t1,0,next2
	lw $s0,4($t0)
	beq $s0,10,enter1	# value at $s0 is equal to <newline>
	beq $s0,13,enter1	# value at $s0 is equal to <CR>
	beq $s0,32,space1	# value at $s0 is equal to <space>
	beq $s0,48,zero1	# $s0 is equal to 0
	blt $s0,48,printE	# $s0 less than 0 ----> not an integer
	bgt $s0,58,printE	# $s0 greater than 9 ---> not an integer
	li $t8,1
	addi $t9,$t9,1
count1:	addi $s0,$s0,-48
	mul $t4,$t4,10
	add $t4,$t4,$s0
	li $t2,0
	j next2
space1:	bne $t2,0,next2
	li $t2,1
	ble $t3,$t7,bank_num1
store1:	sw $t4,0($a0)
	beq $t3,$t6,exit1
	addi $t3,$t3,1
	addi $a0,$a0,4
	li $t4,0
	li $t5,0
	li $t8,0
	li $t9,0
	j next2
zero1:	bgt $t3,$t6,count1
	beq $t8,1,non_le1
	addi $t5,$t5,1
	j count1
non_le1:	addi $t9,$t9,1
	j count1	
bank_num1:add $s1,$t5,$t9
	bne $s1,5,printE
	j store1
enter1:
	bne $t3,$t6,add_01
	j store1
add_01:
	add $t4,$t4,$zero
	sw $t4,0($a0)
	addi $t3,$t3,1
	li $t4,0
	addi $a0,$a0,4
	j store1
exit1:	jr $ra

#######################################################################



############################ open_account #############################
open_account:
	li $t0,2		# open account has 2 arguments after the command type
	li $t1,1		# 1 bank account
	sw $t0,0($a2)
	sw $t1,4($a2)
	move $t2,$a1
	lw $t2,0($t2)
	beq $t2,83,savings	# check if opening savings or chequing account
	sw $ra,4($sp)
	jal read_args
	lw $ra,4($sp)
	la $a0,arguments
	la $t2,bank_array
	lw $t3,0($t2)		# checkings
	lw $t4,4($t2)		# savings
	bne $t3,0,printE	# checkings exists
	lw $t3,0($a0)		# checkings
	beq $t3,$t4,printE	# checkinhs & savings must be different
	lw $t5,4($a0)
	sw $t3,0($t2)
	sw $t5,8($t2)
	

	jr $ra	
	
savings:
	sw $ra,4($sp)
	jal read_args
	lw $ra,4($sp)
	la $a0,arguments
	la $t2,bank_array
	lw $t3,0($t2)		# checkings
	lw $t4,4($t2)		# savings
	bne $t4,0,printE	# savings exists
	lw $t4,0($a0)		# savings
	beq $t3,$t4,printE	# checkings & savings must be different
	lw $t5,4($a0)
	sw $t4,4($t2)
	sw $t5,12($t2)
	
	jr $ra
####################### deposit ##########################
deposit:
	move $t2,$a0		# arguments
	move $t3,$a1		# info on arguments
	li $t0,2		# deposit has 2 arguments after command type
	li $t1,1		# only 1 bank number
	sw $t0,0($t3)
	sw $t1,4($t3)
	move $a2,$a1		# read takes $a0 = arguments & $a2 = info
	sw $ra,4($sp)
	jal read_args
	lw $ra,4($sp)
	la $a0,arguments
	la $t2,bank_array
	lw $t0,0($t2)		# checkings
	lw $t1,4($t2)		# savings
	lw $t4,0($a0)		# bank account number entered
	lw $t5,4($a0)		# amount to deposit
	beq $t4,$t0,cheqD
	beq $t4,$t1,saviD
	j printE		# bank number does not exist
cheqD:	lw $t6,8($t2)
	add $t5,$t5,$t6
	sw $t5,8($t2)
	jr $ra
saviD:	lw $t6,12($t2)
	add $t5,$t5,$t6
	sw $t5,12($t2)
	jr $ra	


	
	
###########################################################
withdraw:
	move $t2,$a0		# arguments
	move $t3,$a1		# info on arguments
	li $t0,2		# deposit has 2 arguments after command type
	li $t1,1		# only 1 bank number
	sw $t0,0($t3)
	sw $t1,4($t3)
	move $a2,$a1		# read takes $a0 = arguments & $a2 = info
	sw $ra,4($sp)
	jal read_args
	lw $ra,4($sp)
	la $a0,arguments
	la $t2,bank_array
	lw $t0,0($t2)		# checkings
	lw $t1,4($t2)		# savings
	lw $t4,0($a0)		# bank account number entered
	lw $t5,4($a0)		# amount to withraw
	beq $t4,$t0,cheqW
	beq $t4,$t1,saviW
	j printE		# bank number does not exist
cheqW:	lw $t6,8($t2)
	#add $t7,$t5,$t7
	bgt $t5,$t6,printE
	sub $t5,$t6,$t5
	sw $t5,8($t2)
	jr $ra
saviW:	mul $t7,$t5,5
	div $t7,$t7,100
	lw $t6,12($t2)
	add $t7,$t5,$t7
	bgt $t7,$t6,printE
	sub $t5,$t6,$t7
	sw $t5,12($t2)
	jr $ra	
	
	
	
#########################################################	
get_loan:
	move $t2,$a0		# arguments
	move $t3,$a1		# info on arguments
	move $t4,$a2		# bank_array
	li $t0,1		# loan has 1 argument after command type
	li $t1,0		# 0 bank numbers
	sw $t0,0($t3)
	sw $t1,4($t3)
	move $a2,$a1		# read takes $a0 = arguments & $a2 = info
	sw $ra,4($sp)
	jal read_args
	lw $ra,4($sp)
	la $a0,arguments
	la $t2,bank_array
	#move $t2,$t4
	#la $t2,bank_array
	lw $t0,8($t2)		# checkings amount
	lw $t1,12($t2)		# savings amount
	add $t3,$t0,$t1		# total balance
	ble $t3,10000,printE
	lw $t4,0($a0)		# loan desired
	lw $t6,16($t2)		# existing loans
	add $t4,$t6,$t4
	div $t5,$t3,2		# maximum loan possible 50% == 1/2 of totl balance
	bgt $t4,$t5,printE
	sw $t4,16($t2)		# put loan in bank array
	
	# lw $t5,4($a0)	

	
	jr $ra	
#################### transfer ############################	
transfer:
	move $t2,$a0		# arguments
	move $t3,$a1		# info on arguments
	move $t4,$a2		# bank_array
	li $t0,3		# transfer has 3 max arguments after command type
	li $t1,2		# 2 max at leat 1 bank numbers
	sw $t0,0($t3)
	sw $t1,4($t3)
	move $a2,$a1		# read takes $a0 = arguments & $a2 = info
	sw $ra,4($sp)
	jal read_argsT
	lw $ra,4($sp)
	la $a0,arguments
	la $t2,bank_array
	lw $t3,0($a0)		# from
	lw $t4,4($a0)		# to
	lw $t5,8($a0)		# amount to tranfer
	lw $t6,0($t2)		# checking in bank array
	lw $t7,4($t2)		# savings in bank array
	beq $t5,0,pay_loan
	beq $t3,$t6,cheqT
	beq $t3,$t7,saviT
	j printE		# account doesn't exist from which to transfer 
cheqT:	bne $t4,$t7,printE	# account to transfer to doesn't exist
	lw $t6,8($t2)		# amount in checkings
	bgt $t5,$t6,printE	# transferring a greater amount than cheq bal
	lw $t7,12($t2)		#amount in savings
	sub $t6,$t6,$t5		# remove transfer from origin save as $t6
	add $t7,$t7,$t5		# add transfer to destination save as $t7
	sw $t6,8($t2)
	sw $t7,12($t2)
	jr $ra
saviT:	bne $t4,$t6,printE	# account to transfer to doesn't exist
	lw $t7,12($t2)		# amount in savings
	bgt $t5,$t7,printE	# transferring a greater amount than cheq bal
	lw $t6,8($t2)		# amount in chequings
	sub $t7,$t7,$t5		# remove transfer from origin save as $t6
	add $t6,$t6,$t5		# add transfer to destination save as $t7
	sw $t7,12($t2)
	sw $t6,8($t2)
	jr $ra
pay_loan:
	beq $t3,$t6,loanC	# pay loan cheq
	beq $t3,$t7,loanS	# pay loan savi
	j printE
loanC:
	li $s3,1
	lw $t6,16($t2)		# loan amount
	lw $t7,8($t2)		# amount in chequing
	bgt $t4,$t6,printE	# can't pay mor than the loan
	bgt $t4,$t7,printE	# can't pay more than amount in cheq
	sub $t6,$t6,$t4		# new loan
	sub $t7,$t7,$t4		# new chequing amount
	sw $t6,16($t2)		# store
	sw $t7,8($t2)		# store
	jr $ra
loanS:
	li $s3,1
	lw $t6,16($t2)		# loan amount
	lw $t7,12($t2)		# amount in savings
	bgt $t4,$t6,printE	# can't pay mor than the loan
	bgt $t4,$t7,printE	# can't pay more than amount in savi
	sub $t6,$t6,$t4		# new loan
	sub $t7,$t7,$t4		# new savings amount
	sw $t6,16($t2)		# store
	sw $t7,12($t2)		# store
	jr $ra
########################### close account #####################	
close_account:
	move $t2,$a0		# arguments
	move $t3,$a1		# info on arguments
	move $t4,$a2		# bank_array
	li $t0,1		# 1 total 
	li $t1,1		# 1 bank number
	sw $t0,0($t3)
	sw $t1,4($t3)
	move $a2,$a1		# read takes $a0 = arguments & $a2 = info
	sw $ra,4($sp)
	jal read_args
	lw $ra,4($sp)
	la $a0,arguments
	la $t2,bank_array
	lw $t3,0($a0)		# account number
	lw $t4,0($t2)		# cheq account
	lw $t5,4($t2)		# savi account
	beq $t3,$t4,clCheq
	beq $t3,$t5,clSavi
	j printE
clCheq:
	lw $t6,8($t2)		# amount in cheques
	beq $t5,0,closeAll	# other account doesn't exist
	sw $0,0($t2)
	lw $t7,12($t2)		# amount in savings
	add $t7,$t7,$t6		# new amount in savings
	sub $t6,$t6,$t6		# new amount in cheques
	sw $t6,8($t2)
	sw $t7,12($t2)
	jr $ra
	
clSavi:
	lw $t6,12($t2)		# amount in savings
	beq $t4,0,closeAll	# other account doesn't exist
	sw $0,4($t2)
	lw $t7,8($t2)		# amount in cheques
	add $t7,$t7,$t6		# new amount in cheques
	sub $t6,$t6,$t6		# new amount in savings
	sw $t6,12($t2)
	sw $t7,8($t2)
	jr $ra
closeAll:
	lw $t8,16($t2)		# amount in loan
	bgt $t8,$t6,printE	# loan greater than remaining amount
	sw $0,0($t2)
	sw $0,4($t2)
	sw $0,8($t2)
	sw $0,12($t2)
	sw $0,16($t2)
	jr $ra
###################### get_balance ###########################
get_balance:
	move $t2,$a0		# arguments
	move $t3,$a1		# info on arguments
	move $t4,$a2		# bank_array
	li $t0,1		# 1 total 
	li $t1,1		# 1 bank number
	sw $t0,0($t3)
	sw $t1,4($t3)
	move $a2,$a1		# read takes $a0 = arguments & $a2 = info
	sw $ra,4($sp)
	jal read_args
	lw $ra,4($sp)
	la $a0,arguments
	la $t2,bank_array
	lw $t3,0($a0)		# account number
	lw $t4,0($t2)		# cheq account
	lw $t5,4($t2)		# savi account
	beq $t3,$t4,blCheq
	beq $t3,$t5,blSavi
	j printE		# no acount was found
blCheq:	lw $t6,8($t2)		# amount in cheques

	li $v0,4		# print dollar sign
	la $a0,dollar
	syscall
	
	li $v0,1		# print int
	move $a0,$t6
	syscall
	
	li $v0,4		# print newLine
	la $a0,newline
	syscall
	jr $ra
	
blSavi:	lw $t6,12($t2)		# amount in savings
	
	li $v0,4		# print dollar sign
	la $a0,dollar
	syscall
	
	li $v0,1		# print int
	move $a0,$t6
	syscall
	
	li $v0,4		# print newLine
	la $a0,newline
	syscall
	jr $ra
	
#####################################################

####################################################
history:
	move $t2,$a0		# arguments
	move $t3,$a1		# info on arguments
	move $t4,$a2		# bank_array
	li $t0,1		# 1 total 
	li $t1,0		# 0 bank number
	sw $t0,0($t3)
	sw $t1,4($t3)
	move $a2,$a1		# read takes $a0 = arguments & $a2 = info
	sw $ra,4($sp)
	jal read_args
	lw $ra,4($sp)
	la $a0,arguments
	lw $a0,0($a0)
	bgt $a0,5,printE
	bgt $a0,$s7,printE
	sw $ra,4($sp)
	jal pr_history
	lw $ra,4($sp)


	jr $ra	
###################################################

########################## fill_hist #############################
fill_hist:
	addi $s7,$s7,1		# number of queries
	li $s1,0
	la $s0,quHist		# shift every element by 20
	lw $t1,0($s0)
	lw $t2,4($s0)
	lw $t3,8($s0)
	lw $t4,12($s0)
	lw $t5,16($s0)
shift:	addi $s0,$s0,20
	lw $s2,0($s0)
	lw $s3,4($s0)
	lw $s4,8($s0)
	lw $s5,12($s0)
	lw $s6,16($s0)
	sw $t1,0($s0)
	sw $t2,4($s0)
	sw $t3,8($s0)
	sw $t4,12($s0)
	sw $t5,16($s0)
	move $t1,$s2
	move $t2,$s3
	move $t3,$s4
	move $t4,$s5
	move $t5,$s6
	addi $s1,$s1,1
	bne $s1,5,shift

	lw $t2,0($a0)
	lw $t3,4($a0)
	lw $t4,8($a0)
	lw $t5,12($a0)
	lw $t6,16($a0)
	la $s0,quHist
	sw $t2,0($s0)
	sw $t3,4($s0)
	sw $t4,8($s0)
	sw $t5,12($s0)
	sw $t6,16($s0)
	

	jr $ra
#########################################################

######################## print history #################################
pr_history:
	move $s1,$a0		# number
	la $t0,quHist		# array
	li $s2,0		# counter quer

next_q:	li $v0,4		# print front bracket
	la $a0,lBrack
	syscall
	li $t2,1		# counter
	
int1:	lw $t1,0($t0)
	beq $t1,0,printInt1
	beq $t2,1,print01
	beq $t2,2,print01
printInt1:li $v0,1		# prints number which doesn't have zeroes
	move $a0,$t1
	syscall
	beq $t2,5,done1
	li $v0,4
	la $a0,comma
	syscall
	addi $t0,$t0,4
	addi $t2,$t2,1
	j int1
	
print01:
	bge $t1,10000,printInt1
	li $v0,1
	li $a0,0
	syscall
	bge $t1,1000,printInt1
	li $v0,1
	li $a0,0
	syscall
	bge $t1,100,printInt1
	li $v0,1
	li $a0,0
	syscall
	bge $t1,10,printInt1
	li $v0,1
	li $a0,0
	syscall
	j printInt
done1:
	li $v0,4
	la $a0,rBrack
	syscall
	addi $s2,$s2,1
	addi $t0,$t0,4
	bne $s2,$s1,next_q
	jr $ra
		
	

#########################################################

quit:	li $t2,0
	lui $t0,0xffff
	lw $t1,0($t0)
	andi $t1,$t1,0x0001
	beq $t1,$zero,quit
	lw $t2,4($t0)
	bne $t2,10,printE
	
	li $v0,10
	syscall




