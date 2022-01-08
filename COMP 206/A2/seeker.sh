#!/bin/bash
##Philippe Bergeron
#### This code often repeats itself! For simplicity comments will only be added to the first appearance of the code #####

# number of arguments on command line
no_args=$#

# Gives an error if no arguments are given
if [[ $no_args = 0 ]]
then
	echo -e "Error missing the pattern argument.\nUsage ./seeker.sh [-c] [-a] pattern [path]"
	exit 0
fi

# Checks if command starts with -ac or -ca
if [[ $1 = "-c" && $2 = "-a" ]] || [[ $1 = "-a" && $2 = "-c" ]]
then 	
	if [[ $no_args = 4 ]] #checks if command has 4 arguments
	then
		pattern=$3
		directory=$4
		if [[ -d $directory ]] # checks if the directory exists
		then
			cd $directory
			list=`ls  | grep $pattern`

			if [[ -n $list ]] # checks if the list has elements in it
			then

				for file in $list # shows the content of every file with a for loop
				do
					echo -e "==== Contents of: $directory/$file ===="
					cat $file
				done
			# if the list is empty return an error
			else 	
				echo -e "Unable to locate any files that has pattern $pattern in its name in $directory"
			fi
		# returns an error if directory doesn't exist
		else 
			echo -e "Error $directory is not a valid directory"
		fi
	elif [[ $no_args = 3 ]] #checks if the argument has 3 elements
	then
		pattern=$3
		list=`ls | grep $pattern`
		directory=`pwd` #gives directory the current one beccause no 4th element

		if [[ -n $list ]]
		then
			for file in $list # check up for explanation
			do 
				echo -e "==== Contents of: $directory/$file ===="
				cat $file
			done
		else
			echo -e "Unable to locate any files that has pattern $pattern in its name in $directory"
		fi

	else # if the argument has 2 or 5 or more elements gives error 
		echo -e "Error missing the pattern argument.\nUsage ./seeker.sh [-c] [-a] pattern [path]"
	fi


# checks if the argument starts with -c only
elif [[ $1 = "-c" ]]
then
	
	if [[ $no_args = 3 ]]
	then
		directory=$3
		pattern=$2
		if [[ -d $directory ]]
		then
			cd $directory
			list=`ls | grep $pattern`
			
			if [[ -n $list ]]
			then
				arr_of_list=($list) # creates an array of files from the list
				first_file=${arr_of_list[0]} # gives the first file of the array 

				echo -e "==== Content of: $directory/$first_file ====" #this makes the file appear on the shell
				cat $first_file
			else
				echo -e "Unable to locate any files that has pattern $pattern in its name in $directory"

			fi
		else
			echo -e "Error $directory is not a valid directory"

		fi

	elif [[ $no_args = 2 ]] 
	then
		pattern=$2
		directory=`pwd`
		list=`ls | grep $pattern`
		if [[ -n $list ]]
		then
			arr_of_list=($list)
			first_file=${arr_of_list[0]}

			echo -e "==== Contents of : $first_file/$directory ===="
			cat $first_file
		else
			echo -e "Unable to locate any files that has pattern $pattern in its name in $directory"
		fi
	else
		echo -e "Error missing the pattern argument.\nUsage ./seeker.sh [-c] [-a] pattern [path]"
	fi

					



				
		
elif [[ $1 = "-a" ]]
then
	if [[ $no_args = 3 ]]
	then
		directory=$3
		pattern=$2
		if [[ -d $directory ]]
		then
			cd $directory
			list=`ls | grep $pattern`
			if [[ -n $list ]]
			then
				for file in $list
				do
					echo -e "$directory/$file"
				done
			else
				echo -e "Unable to locate any files that has pattern $pattern in its name in $directory"
			fi



		else
			echo -e "Error $directory is not a valid directory"

		fi
	elif [[ $no_args = 2 ]]
	then
		pattern=$2
		directory=`pwd`
		list=`ls | grep $pattern`
		if [[ -n $list ]]
		then
			for file in $list
			do
				echo -e "$directory/$file"
			done
		else
			echo -e "Unable to locate any files that has pattern $pattern in its name in $directory"
		fi
	else
		echo -e "Error missing the pattern argument.\nUsage ./seeker.sh [-c] [-a] pattern [path]"
	fi

else
	pattern=$1
	if [[ $no_args = 2 ]]
	then

		directory=$2
		
		if [[ -d $directory ]]
		then
			cd $directory
			list=`ls | grep $pattern`
			if [[ -n $list ]]
			then
				arr_of_list=($list)
				first_file=${arr_of_list[0]}
				echo -e "$directory/$first_file"
			else
				echo -e "Unable to locate any files that has pattern $pattern in its name in $directory"
			fi
		else
			echo -e "Error $directory is not a valid directory"
		fi
	else
		directory=`pwd`
		list=`ls | grep $pattern`
		if [[ -n $list ]]
		then
			arr_of_list=($list)
			first_file=${arr_of_list[0]}
			echo -e "$directory/$first_file"
		else
			echo -e "Unable to locate any files that has pattern $pattern in its name in $directory"
		fi

	fi

	
fi


