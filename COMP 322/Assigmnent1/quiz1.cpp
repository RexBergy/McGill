#include <iostream>
#include <string>
#include <cstdlib>
#include <ctime>

using namespace std;
void  snowfall(float snow[12]){
    float total = 0;
    float average = 0;
    float mostSnow = 0;
    float leastsnow = 10000;
    int snowiest = 0;
    int leastSnow = 0;
    for (int i=0; i<12; i++){
        float snowf = snow[i];
        total += snowf;
    if (snowf > mostSnow){
            snowiest = i;
        }
    if (snowf < leastSnow){
        leastsnow = i;
    }
    } 
    average = total/12;
    cout << "Snowiest month is: " << snowiest << endl;
    cout << "Snowiest month is: " << snowiest << endl;
    cout << "Snowiest month is: " << snowiest << endl;
    cout << "Snowiest month is: " << snowiest << endl;
}