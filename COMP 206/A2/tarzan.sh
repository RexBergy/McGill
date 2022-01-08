 #!/bin/bash
 
 # Checks if the command has 2 arguments
 if [[ $# != 2 ]]
 then
	 echo "Usage ./tarzan.sh filename tarfile"
	 

# Ckecks if the tar file exists
 elif  ! [[ -r $2 ]]
 then
	 echo "Error cannot find tar file $2"
	 

 else 
	 # Gives the files found in tar file
	 files=`tar -t -f $2| grep $1`

	 # Checks if the file searched for is in the tar file
	 if [[ -n $files ]] # Checks if the $files variable is non-empty
	 then 
		 echo "$1 exist in $2"
		 
         else 
		 echo "$1 does not exist in $2"
		 
	 fi


 fi
 
