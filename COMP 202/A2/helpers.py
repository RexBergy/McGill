# COMP 202 A2 Part 1
# Author: Philippe Bergeron
# Student ID: 260928589

import doctest
INCOMPLETE = -1

# These constants are for you to use
IRREGULAR_CHARS = " -–`'’«»"
DIGITS = '1234567890'
PUNCTUATION = ',;:.?!"(*)' 

LETTERS =  ['abcdefghij',
            'klmnopqrst',
            'uvxyzçéàèù',
            'âêîôûëïüœw'] 

################ Functions


def is_in_decade(c, decade):
    '''(str, str) -> bool
    Return whether c is a single-letter character contained within decade.

    >>> is_in_decade('a', 'abcdefghij')
    True
    >>> is_in_decade('z', 'abcdefghij')
    False
    >>> is_in_decade('a', 'âêîôûëïüœw')
    False
    >>> is_in_decade('âêî', 'âêîôûëïüœw')
    False
    '''
    # Checking if c has one character
    if (len(c) != 1):
        return False
    
    # Checking if c is contained in decade
    if (decade.find(c) < 0):
        return False
        

    return True


def is_irregular(c):
    '''(str) -> bool
    Return whether character c is a single one of the irregular characters
    in French Braille. Note the global variable we made for you: IRREGULAR_CHARS.

    >>> is_irregular(' ')
    True
    >>> is_irregular('’')
    True
    >>> is_irregular(')')
    False
    >>> is_irregular('-')
    True
    >>> is_irregular('`')
    True
    >>> is_irregular("'")
    True
    >>> is_irregular('a')
    False
    >>> is_irregular('')
    False
    >>> is_irregular('1')
    False
    >>> is_irregular('«»')
    False
    >>> is_irregular('(1-1)')
    False
    '''
    # Checking if c contains an irregular charcater
    if (IRREGULAR_CHARS.find(c) < 0):
        return False

    # Checking if c is a single character
    if (len(c) != 1):
        return False
    
    return True


def is_digit(c):
    '''(str) -> bool
    Return whether character c represents a single digit.
    Note the global variable we made for you: DIGITS.

    >>> is_digit('2')
    True
    >>> is_digit('0')
    True
    >>> is_digit('12')
    False
    '''
    # Checking if c is a digit
    if (DIGITS.find(c) < 0):
         return False

    # Checking if c is a single character
    if (len(c) != 1):
        return False
    
    return True


def is_punctuation(c):
    '''(str) -> bool
    Return whether c is a single character that is one of the common, regular
    forms of punctuation in French Braille.
    Note the global variable we made for you: PUNCTUATION.

    >>> is_punctuation(',')
    True
    >>> is_punctuation(',,')
    False
    >>> is_punctuation('-')
    False
    >>> is_punctuation('"')
    True
    >>> is_punctuation('')
    False
    >>> is_punctuation('a')
    False
    '''
    # Checking if c is a form of punctuation
    if (PUNCTUATION.find(c) < 0):
        return False

    # Checking if c is single character
    if (len(c) !=1):
        return False
    
    return True


def is_letter(c):
    '''(str) -> bool
    Return whether c is a single one of one of the standard letters
    in French Braille. Provided to students.
    Do not edit this function.

    >>> is_letter('a')
    True
    >>> is_letter('z')
    True
    >>> is_letter('w')
    True
    >>> is_letter('é')
    True
    >>> is_letter('A')
    True
    >>> is_letter('Œ')
    True
    >>> is_letter('1')
    False
    >>> is_letter('ß')
    False
    >>> is_letter('aa')
    False
    >>> is_letter('Hello')
    False
    '''
    c = c.lower()
    for decade in LETTERS:
        if is_in_decade(c, decade):
            return True
    return False


def is_known_character(c):
    '''(str) -> bool
    Is c one of the characters supported by French Braille?
    (Letter, digit, punctuation or irregular.)

    >>> is_known_character('a')
    True
    >>> is_known_character('É')
    True
    >>> is_known_character('-')
    True
    >>> is_known_character('4')
    True
    >>> is_known_character('.')
    True
    >>> is_known_character('@')
    False
    >>> is_known_character('ß')
    False
    >>> is_known_character('\\n')
    False
    '''
    # Checking if c is not a letter, digit, punctuation and irregular
    if ((is_letter(c) == False)
        and (is_punctuation(c) == False)
        and (is_digit(c) == False)
        and (is_irregular(c) == False)):
        return False
    
    return True


def is_capitalized(c):
    '''(str) -> bool
    Return whether c is a single capitalized letter supported by French Braille.

    >>> is_capitalized('A') 
    True
    >>> is_capitalized('a')
    False
    >>> is_capitalized('W')
    True
    >>> is_capitalized('É')
    True
    >>> is_capitalized(' ')
    False
    >>> is_capitalized('')
    False
    >>> is_capitalized('Δ')
    False
    >>> is_capitalized('femmes')
    False
    >>> is_capitalized('FEMMES')
    False
    '''
    # Converting the variable LETTERS into capital letters
    letters = 'abcdefghijklmnopqrstuvwxyzçéàèùâêîôûëïüœw'
    cap_letters = letters.upper()

    if (cap_letters.find(c) < 0):
        return False

    if (len(c) != 1):
        return False
    
    return True


#########################################

if __name__ == '__main__':
    doctest.testmod()
