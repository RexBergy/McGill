/*
Philippe Bergeron
*/
#include <iostream>
#include <string>
#include <cstdlib>
#include <ctime>

using namespace std;

const int rows = 5;
const int cols = 5;



//Function to check password
//Gives all errors which occur in the puted password
void checkPassword()
{
    string password;
    cout << "Please input a password : " ;
    getline(cin,password);

    cout << "Password is : " << password << endl;
    int size = password.length();
    bool hasNumber = false;
    bool hasSpec = false;
    int characters[255] = {0},j;        // an array that contains all 255 characters
    bool noRepetition = true;
    for (int i = 0; i < size; i++){     //Reads character by character the password
            if (
                password[i] =='*' ||
                password[i] =='#' ||
                password[i] =='$'
                ){
                    hasSpec = true;
                }
            if (
                password[i] =='0' ||
                password[i] =='1' ||
                password[i] =='2' ||
                password[i] =='3' ||
                password[i] =='4' ||
                password[i] =='5' ||
                password[i] =='6' ||
                password[i] =='7' ||
                password[i] =='8' ||
                password[i] =='9'
                ){
                    hasNumber = true;
                }
            j=password[i];
            ++characters[j];        // adds 1 to the given character
            if (characters[j]>2)
            {
                noRepetition = false;   // If a character appears more than twice --> false
            }
    }

    if ( size>=8 && hasSpec && hasNumber && noRepetition){
        cout << "The password was succesfully created." << endl;
    } else {                                                        // Error messages
        if (!(size>=8)){
            cout << "Error. The password is not at least 8 characters long." << endl;
        }
        if (!hasSpec){
            cout << "Error. The password does not contain at least one special character: * OR # OR $." << endl;
        }

        if (!hasNumber){
            cout << "Error. The password does not contain one number." << endl;
        }

        if (!noRepetition){
            cout << "Error. A character appears more than twice in the password." << endl;
        }
    }

}
// converts a word to phonetic alpahabet
// using a switch case
// puts the word in small caps
void convertPhonetic()
{
    string word;
    string newWord = "";

    cout << "Please write a word : " ;
    getline(cin,word);

    int size = word.length();

    for (int i = 0;i<size;i++){
        char c = word[i];
        c = tolower(c);
        switch (c){
            case 'a':
                newWord= newWord +"Alfa ";
                break;
            
            case 'b':
                newWord+="Bravo ";
                break;

            case 'c':
                newWord+="Charlie ";
                break;
            
            case 'd':
                newWord+="Delta ";
                break;
            
            case 'e':
                newWord+="Echo ";
                break;

            case 'f':
                newWord+="Foxtrot ";
                break;

            case 'g':
                newWord+="Golf ";
                break;
            
            case 'h':
                newWord+="Hotel ";
                break;

            case 'i':
                newWord+="India ";
                break;

            case 'j':
                newWord+="Juliett ";
                break;

            case 'k':
                newWord+="Kilo ";
                break;

            case 'l':
                newWord+="Lima ";
                break;

            case 'm':
                newWord+="Mike ";
                break;
            
            case 'n':
                newWord+="November ";
                break;

            case 'o':
                newWord+="Oscar ";
                break;

            case 'p':
                newWord+="Papa ";
                break;
            
            case 'q':
                newWord+="Quebec ";
                break;

            case 'r':
                newWord+="Romeo ";
                break;

            case 's':
                newWord+="Sierra ";
                break;

            case 't':
                newWord+="Tango ";
                break;

            case 'u':
                newWord+="Uniform ";
                break;

            case 'v':
                newWord+="Victor ";
                break;

            case 'w':
                newWord+="Whiskey ";
                break;

            case 'x':
                newWord+="X-ray ";
                break;

            case 'y':
                newWord+="Yankee ";
                break;

            case 'z':
                newWord+="Zulu ";
                break;

        }
    }

    cout <<  newWord << endl;
}
// fills a matrix with random numbers
// uses current time 
// and a static variable to count
// number of times the function is calles
void fillMatrix(int matrix[rows][cols])
{
    static int ra = 0;

    srand(ra+time(NULL));
    
    for (int i = 0;i<rows;i++){
        for (int j = 0;j<rows;j++){
            matrix[i][j]= rand() % 26;
        }
    }
    ra++;
}
// prints a matrix
//
void printMatrix(int matrix[rows][cols]){
    
    for (int i = 0; i<rows;i++){
        for (int j = 0;j<cols;j++){
            cout << matrix[i][j];
            if (j<cols-1){
                cout << " | ";
            } else {
                cout << endl;
            }
        }
        cout << "---------------------" << "3" << endl;
    }
    cout << endl;
}
// recursive multiplication
// updates the matrix_result at each recursion
// when the matrix is finished updating
// returns
void multiplyMatrices(  int matrix_left[rows][cols], 
                        int matrix_right[rows][cols], 
                        int matrix_result[rows][cols])
{
    static int i = 0, j = 0, k = 0;
    static double result = 0;       
    
    if (i>=rows){
        return;
    } 

    if (j < cols)
    {
        if (k < cols)
        {
            result += matrix_left[i][k] * matrix_right[k][j];   
            k++;
            multiplyMatrices(matrix_left,matrix_right,matrix_result);
        }
        matrix_result[i][j] = result;
        result = 0;
        k=0;
        j++; // Changes column
        multiplyMatrices(matrix_left,matrix_right,matrix_result);
    }
    j = 0;
    i++; // Changes row
    multiplyMatrices(matrix_left,matrix_right,matrix_result);

}
float price(int items){
    if (items > 40){
    float price = (float)(items * 50);
    price = price*0.5;
    return price;
    } else if (items ==30){
        return 0;
    } else{
        return 3;
    }
}
double* getLongitude(void){
    double longitude = -73.15;
    double* ptr = &longitude;

    return ptr; 
}
// main functon
int main()
{
    checkPassword();
    convertPhonetic();
    int matrix[rows][cols];
    int matrix2[rows][cols];
    int matrix_result[rows][cols];
    fillMatrix(matrix);
    fillMatrix(matrix2);
    printMatrix(matrix);
    printMatrix(matrix2);
    multiplyMatrices(matrix,matrix2,matrix_result);
    printMatrix(matrix_result);
    return 0;
}