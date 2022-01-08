#!/bin/bash

#Philippe Bergeron
#260928589

if ! [[ -f a3config ]] #checks if this file doesn't exist
then
	echo "Error cannot find a3config" #gives an error message if file doesn't exist
	exit 1 
else
	source a3config #loads the functions from a3config
	if [[ -z $DIRNAME ]] || [[ -z $EXTENSION ]] #Checks if DIRNAME or EXTENSION doesn't exist in a3config
	then
		echo "Error DIRNAME and EXTENSION must be set" #gives an error if true
		exit 2
	elif ! [[ -d $DIRNAME ]] #checks if directory DIRNAME doesn't exist 
	then 
		echo "Error directory $DIRNAME does not exist" #gives an error if true
		exit 3
	fi
	cd $DIRNAME #changes the current directory to DIRNAME because it exists
	
	list=`ls *.$EXTENSION 2>/dev/null` #creates a list with every file with EXTENSION, 
	#doesn't give an error if nothing is found
	if [[ -z $list ]] #checks if list is empty 
	then
		echo "Unable to locate any files with extension $EXTENSION in $DIRNAME" #gives error if true
		exit 0
	fi
	#if [[ $SHOW = "false" ]] || [[ -z $SHOW ]]
	if ! [[ $SHOW = "true" ]] #checks if SHOW isn't true 
	then 
		for file in $list # if condition true, shows the file with the directory
		do
			echo $DIRNAME/$file
			
		done
	else
		# shows the file with the directory and shows its contents
		for file in $list
		do
			echo $DIRNAME/$file
			cat $file
			
		done
	
	fi

fi
