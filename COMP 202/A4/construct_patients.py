# Philippe Bergeron
# 260928589
# part 3

import doctest
import datetime
import pandas
import numpy
import matplotlib


# a list of numbers and letters
numbers = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9']
letters = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', \
           'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', \
           'Y', 'Z']

# creates a class Patient
class Patient:


    def __init__(self, num, day_diagnosed, age, sex_gender, postal, state, \
                 temps, days_symptomatic):
        
        # changes all sex_gender strings into a single letter F, M or X
        if 'WO' in sex_gender or 'FE' in sex_gender or 'GI' in sex_gender \
           or 'FI' in sex_gender or sex_gender == 'F':
            sex_gender = 'F'

        elif 'MA' in sex_gender or 'HOM' in sex_gender \
             or sex_gender == 'H' or 'BOY' in sex_gender or sex_gender == 'M':
            sex_gender = 'M'

        else:
            sex_gender = 'X'

        # checks if it is a valid Montreal postal code
        # and gives only first 3 characters
        if postal[0] != 'H' or postal[1] not in numbers or postal[2] \
           not in letters:
            postal = '000'

        elif len(postal) > 3:
            postal = postal[:3]

        # creates a list of temperatures
        list_of_temps = []

        # checks if temperature is available
        # changes to 0 if not
        if 'N' in temps:
            temps = 0
            list_of_temps.append(temps)

        # changes temperature to remove any comma
        # remove any letter
        # and converts F to C if temperature above 45C
        # changes to float 2 decimals
        else:
            temps = str(temps)
            temps = temps.replace(',', '.')
            for charac in temps:
                 if charac in letters:
                    temps = temps.replace(charac, '')

            temps = float(temps)

            if temps > 45.0:
                temps = round((temps - 32.0)/(9/5), 2)
            list_of_temps.append(temps)

        # initializes each variable
        self.num = int(num) #integer
        self.day_diagnosed = int(day_diagnosed) #integer
        self.age = int(age) #integer
        self.sex_gender = sex_gender
        self.postal = postal
        self.state = state
        self.temps = list_of_temps
        self.days_symptomatic = int(days_symptomatic) #integer

    def __str__(self):

        
        # creates a new string separated by tabs of several
        #initialized variables above
        new_string = str(self.num) + '\t' + str(self.age) + '\t' + \
                         self.sex_gender + '\t' + self.postal + '\t' + \
                         str(self.day_diagnosed) + '\t' + self.state + '\t' \
                         + str(self.days_symptomatic) + '\t'

        # adds temperature to string with ';'
        i = 0
        for temp in self.temps:
            temp = str(temp)
            if i == 0:
                new_string += temp
                i += 1

            else:
                new_string += ';' + temp
                i += 1

            
        return new_string

    def update(self, new_entry):

        # checks if patient number, gender, and postal code are the same
        # in both Patient objects
        if self.num != new_entry.num or self.sex_gender != new_entry.sex_gender or \
           self.postal != new_entry.postal:
            raise AssertionError('Number, gender, or postal is/are different')

        # updates old entries to new entries
        # adds temp with ';'
        else:
            self.days_symptomatic = new_entry.days_symptomatic
            self.state = new_entry.state

            for temp in new_entry.temps:
                self.temps.append(temp)


def stage_four(input_filename, output_filename):
    '''(filename, filename) -> dict
    >>> pat = stage_four('long-abr9.tsv', 'almost_finis.tsv')
    >>> print(pat[3])
    3\t0\tM\tH2L\t2\tI\t11\t41.32;39.88;39.81;39.4;39.59;38.52;38.0;37.3
    >>> print(len(pat))
    1741
    
    '''
    # opens the input file for reading
    # and the output file for writing
    input_file = open(input_filename, 'r', encoding = 'utf-8')
    output_file = open(output_filename, 'w', encoding = 'utf-8')

    # creates a patient dictionary
    pat_dict = {}

    # creates a list of lines
    lines = input_file.readlines()

    # iteraes through every list of lines
    i = 0
    for line in lines:
        # creates list of elements in line separated by tab
        line_list = line.split('\t')

        # initializes every element in line into a variable
        # in Patient object
        patient = Patient(line_list[1], line_list[2], line_list[3], line_list[4], \
                          line_list[5], line_list[6], line_list[7], line_list[8])
        # patient number
        pat_number = int(line_list[1])

        # adds every patient number as a key in a dictionary
        # gives value Patient object
        # updates if multiple entries for one patient
        if pat_number in pat_dict:
            pat_dict.get(pat_number).update(patient)

        else:
            pat_dict[pat_number] = patient


    # writes every patient object in a new line
    # in a sorted order
    for patient in sorted(pat_dict):
        pat_object = pat_dict.get(patient)
        line = str(pat_object) + '\n'
        output_file.write(line)
        
        

    return pat_dict

def fatality_by_age(dictionary_of_patients):
    '''
    '''
    for patient in patient_dictionary:
        return None
        
        
        
        
        
    
        



if __name__ == "__main__":
    doctest.testmod()
    '''

    p = Patient('0', '4', '54', 'GENDERQUEER', 'H5B', 'I', '104', '4')
    p8 = Patient('0', '4', '54', 'GENDERQUEER', 'H5B', 'I', '110F', '7')
    p.update(p8)
    
    #print(str(p))
    #print(str(p8))
    #p.update(p8)
    print(str(p))
    p10 = Patient('0', '4', '54', 'GENDERQUEER', 'H5B', 'R', '35C', '10')
    p.update(p10)
    print(str(p))
    '''
        
