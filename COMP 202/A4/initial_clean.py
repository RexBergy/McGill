# Philippe Bergeron
# 260928589
# part 1

def which_delimiter(string):
    ''' (str) -> str
    >>> which_delimiter('0 1 2,3')
    ' '
    >>> which_delimiter('0,1,2,3,4,5,6')
    ','
    >>> which_delimiter('0\\t1\\t3\\t6')
    '\t'
    >>> which_delimiter('8586945')
    AssertionError('No space/comma/tab found')
    '''
    tab_count = 0
    comma_count = 0
    space_count = 0

    # checks if there is a tab and counts it
    if '\t' in string:
        tab_count = string.count('\t')
        

    # checks if there is a comma and counts it
    if ',' in string:
        comma_count = string.count(',')
        
    # checks if there is a space and counts it
    if ' ' in string:
        space_count = string.count(' ')
        
    # returns error if no tab/comma/tab and counts it
    if ('\t' not in string) and (',' not in string) and (' ' not in string):
        raise AssertionError('No space/comma/tab found')

    # Which delimiter has the mosts occurences
    max_count = max(tab_count, comma_count, space_count)
    
    # return tab if tab has the most occurences
    if max_count == tab_count:
        return '   '

    # returns comma if comma has the most occurences
    if max_count == comma_count:
        return ','

    # returns space if space has the most occurneces
    if max_count == space_count:
        return ' '

def stage_one(input_filename, output_filename):
    '''(filename, filename) -> filename
    >>> stage_one('260928589.txt', 'stage1.tsv')
    3000
    '''

    # opens the input file for reading
    # and the output file for writing
    input_file = open(input_filename, 'r', encoding = 'utf-8')
    output_file = open(output_filename, 'w', encoding = 'utf-8')

    # creates a list of lines
    lines = input_file.readlines()

    # iterates through the list of lines
    for line in lines:
        
        # changes character in line to all caps
        # replaces '/' and '.' by '-'
        # for the first 30 characters
        line = line.upper()
        line_date_only = line[:30]
        line_date_only = line_date_only.replace('/', '-')
        line_date_only = line_date_only.replace('.', '-')
        line_excluding_date = line[30:]

        # merges the modified first characters with the rest of the characters
        line = line_date_only + line_excluding_date

        # replace the most common delimiter by tab
        # writes lines to output file
        delimiter_com = which_delimiter(line)
        line = line.replace(delimiter_com, '\t')
        output_file.write(line)

    return len(lines)

def stage_two(input_filename, output_filename):
    '''(filename, filename) -> filename
    >>> stage_two('stage1.tsv', 'stage2.tsv')
    3000
    '''
    # a list of numbers 
    number = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9']

    # opens the input file for reading
    # and the output file for writing
    input_file = open(input_filename, 'r', encoding = 'utf-8')
    output_file = open(output_filename, 'w', encoding = 'utf-8')

    # creates a list of lines
    lines = input_file.readlines()

    # iterates through the list of lines
    for line in lines:

        # creates a list of elements in the line
        # separated by tabs
        line_list = line.split('\t')

        # checks if 9 colums
        if len(line_list) == 9:
            output_file.write(line)
            
        else:
            # Checks if the postal code is separated by a tab (6th and 7th
            # element in list)
            # Checks if it is a postal code
            # merges
            if len(line_list[5]) == 3 and (len(line_list[6]) == 3 and \
            line_list[6][0] in number):
                line_list[5:7] = [line_list[5] + line_list[6]]
                
            # checks there isn't a postal code
            # merges NOTAPPLICABLE or NONAPPLICABLE
            if 'NO' in line_list[5] and 'APP' in line_list[6]:
                line_list[5:7] = [line_list[5] + line_list[6]]

            
            # Merges the C or F to temperature (8th and 9th element in list)
            if len(line_list) != 9:
                line_list[7:9] = [line_list[7] + line_list[8]]


            # converts line_list into string
            line = ''
            for column in line_list:
                if column != line_list[-1]:
                    line += column + '\t'
                else:
                    line += column

            # writes each modified line into output file
            output_file.write(line)

    

    return len(lines)
                
            
                
                
                


    

if __name__ == "__main__":
    import doctest
    doctest.testmod()
