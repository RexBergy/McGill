# Philippe Bergeron
# 260928589
# part 2

import numpy
import matplotlib

def date_diff(ISO_date1, ISO_date2):
    '''(string, string) -> int
    >>> date_diff('2019-03-09', '2018-09-12')
    -178
    >>> date_diff('2019-10-31', '2019-11-2')
    2
    >>> date_diff('2019-12-21', '2006-04-07')
    -5006
    >>> date_diff('2016-12-23', '2019-02-14')
    783
    '''
    import datetime
    
    # creates a list from string separated by '-'
    date1_list = ISO_date1.split('-')
    date2_list = ISO_date2.split('-')

    # creates new list to modify strings into ints
    mod_date1 = []
    mod_date2 = []

    # iterates through date list
    # appends modified elements into new list above
    for element in date1_list:
        element = int(element)
        mod_date1.append(element)
    
    for element in date2_list:
        element = int(element)
        mod_date2.append(element)

    # modifies the modified date list into a tupple
    real1 = tuple(mod_date1)
    real2 = tuple(mod_date2)

    # calculates each date
    date1 = datetime.date(real1[0], real1[1], real1[2])
    date2 = datetime.date(real2[0], real2[1], real2[2])

    # day difference between each date
    diff = date2 - date1

    return diff.days


def get_age(iso_date1, iso_date2):
    '''(string, string) -> int
    >>> get_age('2018-10-31', '2019-11-2')
    1
    >>> get_age('2018-10-31', '2000-11-2')
    -17
    '''
    # calculates day difference between two dates
    days = date_diff(iso_date1, iso_date2)

    # convert into number of years
    num_years = days/365.2425

    # since round() rounds to bottom
    # add 0.51 to accurately round to nearest integer
    # when negative
    if num_years < 0:
        num_years = round(num_years + 0.51)
        return num_years

    # rounds number of years
    return round(num_years)

def stage_three(input_filename, output_filename):
    '''(filename, filename) -> dict
    >>> p = stage_three('stage2.tsv', 'stage3.tsv')
    >>> print(p[0])
    {'I': 1, 'D': 0, 'R': 0}
    '''
    
    # opens the input file for reading
    # and the output file for writing
    input_file = open(input_filename, 'r', encoding = 'utf-8')
    output_file = open(output_filename, 'w', encoding = 'utf-8')

    # creates a list of lines
    lines = input_file.readlines()

    # Finds the index date
    first_line = lines[0]
    list_first_line = first_line.split('\t')
    index_date = list_first_line[2]

    # creates a dictionary which has each day as a key
    # and value which is a dictionary on the condition of
    # the patient
    day_dict = {}
    cond_dict = {}

    
    # iteraes through every list of lines
    for line in lines:

        # creates list of elements in line separated by tab
        line_list = line.split('\t')

        # modifies columns 3 and 4 to be days since diagnosed
        # and age respectively
        line_list[2] = str(date_diff(index_date, line_list[2]))
        line_list[3] = str(get_age(line_list[3], index_date))

        # changes days into ints
        days = line_list[2]
        days = int(days)

        # creates a new condition dictionary for every day
        if days not in day_dict:
            cond_dict = {}

        # the next 3 'if' statemants  check the condition of
        # each patient and return a letter in column 7 as the condition
        # also gives the number patients with a given condition
        # on each day in a dictionary
        if 'I' in line_list[6]:
            line_list[6] = 'I'
            if 'I' in cond_dict:
                cond_dict['I'] += 1
            else:
                cond_dict['I'] = 1

        else:
            if 'I' in cond_dict:
                cond_dict['I'] += 0
            else:
                cond_dict['I'] = 0

        if 'D' == line_list[6] or 'M' in line_list[6] or 'DEA' in line_list[6]:
            line_list[6] = 'D'
            if 'D' in cond_dict:
                cond_dict['D'] += 1
            else:
                cond_dict['D'] = 1

        else:
            if 'D' in cond_dict:
                cond_dict['D'] += 0
            else:
                cond_dict['D'] = 0

        if 'R' in line_list[6]:
            line_list[6] = 'R'
            if 'R' in cond_dict:
                cond_dict['R'] += 1
            else:
                cond_dict['R'] = 1

        else:
            if 'R' in cond_dict:
                cond_dict['R'] += 0
            else:
                cond_dict['R'] = 0
        

        day_dict[days] = cond_dict
                

        # converts line_list into string
        line = ''
        for  column in line_list:
            if column != line_list[-1]:
                line += column + '\t'
            else:
                line += column
                
        # writes each modified line into output file
        output_file.write(line)

    return day_dict


def plot_time_series(dictionary):
    '''(dict) -> list
    >>> plot_time_series({0: {'I': 1, 'D': 0, 'R': 0}, 1: {'I': 2, 'D': 1, 'R': 0}})
    [[1, 0, 0], [2, 1, 0]]
    '''
    import numpy as np
    import matplotlib.pyplot as plt

    
    # creates a new list (each element represents a day)
    list_of_days = []

    # iterates through every day in given dictionary
    for day in dictionary:

        # gets dictionary of conditions
        conditions = dictionary.get(day)

        # creates a new list of conditions
        list_of_cond = []

        # iterates through every condition in dictionary of conditions
        for condition in conditions:
            
            # appends the number of people with a given condition
            number = conditions.get(condition)
            list_of_cond.append(number)
            
        # appends every day to list of days
        list_of_days.append(list_of_cond)

    return list_of_days
            


    
        




if __name__ == "__main__":
    import doctest
    doctest.testmod()
