# COMP 202 A2 Part 5
# Author: Philippe Bergeron
# 260928589

from text_to_braille import *

ENG_CAPITAL = '..\n..\n.o'

# You may want to define more global variables here

####################################################
# Here are two helper functions to help you get started

def two_letter_contractions(text):
    '''(str) -> str
    Process English text so that the two-letter contractions are changed
    to the appropriate French accented letter, so that when this is run
    through the French Braille translator we get English Braille.
    Provided to students. You should not edit it.

    >>> two_letter_contractions('chat')
    'âat'
    >>> two_letter_contractions('shed')
    'îë'
    >>> two_letter_contractions('shied')
    'îië'
    >>> two_letter_contractions('showed the neighbourhood where')
    'îœë ôe neiêbürhood ûïe'
    >>> two_letter_contractions('SHED')
    'ÎË'
    >>> two_letter_contractions('ShOwEd tHE NEIGHBOURHOOD Where') 
    'ÎŒË tHE NEIÊBÜRHOOD Ûïe'
    '''
    combos = ['ch', 'gh', 'sh', 'th', 'wh', 'ed', 'er', 'ou', 'ow']
    for i, c in enumerate(combos):
        text = text.replace(c, LETTERS[-1][i])
    for i, c in enumerate(combos):
        text = text.replace(c.upper(), LETTERS[-1][i].upper())
    for i, c in enumerate(combos):
        text = text.replace(c.capitalize(), LETTERS[-1][i].upper())

    return text


def whole_word_contractions(text):
    '''(str) -> str
    Process English text so that the full-word contractions are changed
    to the appropriate French accented letter, so that when this is run
    through the French Braille translator we get English Braille.

    If the full-word contraction appears within a word, 
    contract it. (e.g. 'and' in 'sand')

    Provided to students. You should not edit this function.

    >>> whole_word_contractions('with')
    'ù'
    >>> whole_word_contractions('for the cat with the purr and the meow')
    'é à cat ù à purr ç à meow'
    >>> whole_word_contractions('With')
    'Ù'
    >>> whole_word_contractions('WITH')
    'Ù'
    >>> whole_word_contractions('wiTH')
    'wiTH'
    >>> whole_word_contractions('FOR thE Cat WITh THE purr And The meow')
    'É thE Cat WITh À purr Ç À meow'
    >>> whole_word_contractions('aforewith parenthetical sand')
    'aéeù parenàtical sç'
    >>> whole_word_contractions('wither')
    'ùer'
    '''
    # putting 'with' first so wither becomes with-er not wi-the-r
    words = ['with', 'and', 'for', 'the']
    fr_equivs = ['ù', 'ç', 'é', 'à', ]
    # lower case
    for i, w in enumerate(words):
        text = text.replace(w, fr_equivs[i])
    for i, w in enumerate(words):
        text = text.replace(w.upper(), fr_equivs[i].upper())
    for i, w in enumerate(words):
        text = text.replace(w.capitalize(), fr_equivs[i].upper())
    return text



####################################################
# These two incomplete helper functions are to help you get started

def convert_contractions(text):
    '''(str) -> str
    Convert English text so that both whole-word contractions
    and two-letter contractions are changed to the appropriate
    French accented letter, so that when this is run
    through the French Braille translator we get English Braille.

    Refer to the docstrings for whole_word_contractions and 
    two_letter_contractions for more info.

    >>> convert_contractions('with')
    'ù'
    >>> convert_contractions('for the cat with the purr and the meow')
    'é à cat ù à purr ç à meœ'
    >>> convert_contractions('chat')
    'âat'
    >>> convert_contractions('wither')
    'ùï'
    >>> convert_contractions('aforewith parenthetical sand')
    'aéeù parenàtical sç'
    >>> convert_contractions('Showed The Neighbourhood Where')
    'Îœë À Neiêbürhood Ûïe'
    '''
    original_text = text
    lower_text = text.lower()
    # Checking if 'and' 'with' 'for' or 'the'
    # are in text (capitalized or not)
    # returning contractions
    # Or else returning two letter contractions
    if (('with' in lower_text) or ('and' in lower_text) or ('for' in lower_text) or ('the' in lower_text)):
        contracted_words = whole_word_contractions(original_text)
        return two_letter_contractions(contracted_words)
    else:
        return two_letter_contractions(original_text)

    



def convert_quotes(text):
    '''(str) -> str
    Convert the straight quotation mark into open/close quotations.
    >>> convert_quotes('"Hello"')
    '“Hello”'
    >>> convert_quotes('"Hi" and "Hello"')
    '“Hi” and “Hello”'
    >>> convert_quotes('"')
    '“'
    >>> convert_quotes('"""')
    '“”“'
    >>> convert_quotes('" "o" "i" "')
    '“ ”o“ ”i“ ”'
    '''
    
    quote = ''
    m = 0
    for i in range(len(text)):
        if (text[i] == '"'):
            if (m % 2 == 0):
                quote += '“'
                
            else:
                quote += '”'
            m += 1
        else:
            quote += text[i]
        i += 1
            

    return quote


####################################################
# Put your own helper functions here!

# a function that converts English numbers by replacing by letters 
def english_num(text):
    #'''(str) -> str
    #>>> english_num('2078')
    #'2jgh\n..\n.o\n.o'
    #>>> english_num('8065')
    #'8jfe\n..\n.o\n.o'
    #'''
    
    text =  text.replace('1','a')
    text =  text.replace('2','b')
    text =  text.replace('3','c')
    text =  text.replace('4','d')
    text =  text.replace('5','e')
    text =  text.replace('6','f')
    text =  text.replace('7','g')
    text =  text.replace('8','h')
    text =  text.replace('9','i')
    text =  text.replace('0','j')


    
    return text
# a function to interchange quotes and paratheses
def inter_quo_para(text):
    '''(str) -> str
    >>> inter_quo_para('"while"')
    '(while)'
    >>> inter_quo_para('(before)')
    '"before"'
    '''
    text = convert_quotes(text)
    text = text.replace('(','"')
    text = text.replace(')','"')
    text = text.replace('“','(')
    text = text.replace('”',')')
    
    
    return text
    



####################################################

def english_text_to_braille(text):
    '''(str) -> str
    Convert text to English Braille. Text could contain new lines.

    This is a big problem, so think through how you will break it up
    into smaller parts and helper functions.
    Hints:
        - you'll want to call text_to_braille
        - you can alter the text that goes into text_to_braille
        - you can alter the text that comes out of text_to_braille
        - you shouldn't have to manually enter the Braille for 'and', 'ch', etc

    You are expected to write helper functions for this, and provide
    docstrings for them with comprehensive tests.

    >>> english_text_to_braille('202') # numbers
    '⠼⠃⠚⠃⠰'
    >>> english_text_to_braille('2') # single digit
    '⠼⠃⠰'
    >>> english_text_to_braille('COMP') # all caps
    '⠠⠠⠉⠕⠍⠏'
    >>> english_text_to_braille('COMP 202') # combining number + all caps
    '⠠⠠⠉⠕⠍⠏ ⠼⠃⠚⠃⠰'
    >>> english_text_to_braille('and')
    '⠯'
    >>> english_text_to_braille('and And AND aNd')
    '⠯ ⠠⠯ ⠠⠯ ⠁⠠⠝⠙'
    >>> english_text_to_braille('chat that the with')
    '⠡⠁⠞ ⠹⠁⠞ ⠷ ⠾'
    >>> english_text_to_braille('hi?')
    '⠓⠊⠦'
    >>> english_text_to_braille('(hi)')
    '⠶⠓⠊⠶'
    >>> english_text_to_braille('"hi"')
    '⠦⠓⠊⠴'
    >>> english_text_to_braille('COMP 202 AND COMP 250')
    '⠠⠠⠉⠕⠍⠏ ⠼⠃⠚⠃⠰ ⠠⠯ ⠠⠠⠉⠕⠍⠏ ⠼⠃⠑⠚⠰'
    >>> english_text_to_braille('For shapes with colour?')
    '⠠⠿ ⠩⠁⠏⠑⠎ ⠾ ⠉⠕⠇⠳⠗⠦'
    >>> english_text_to_braille('(Parenthetical)\\n\\n"Quotation"')
    '⠶⠠⠏⠁⠗⠑⠝⠷⠞⠊⠉⠁⠇⠶\\n\\n⠦⠠⠟⠥⠕⠞⠁⠞⠊⠕⠝⠴'
    '''
    # You may want to put code after this comment. You can also delete this comment.
    # convert to english numbers
    text = english_num(text)

    # Here's a line we're giving you to get started: change text so the
    # contractions become the French accented letter that they correspond to
    text = convert_contractions(text)
    text = inter_quo_para(text)


    # You may want to put code after this comment. You can also delete this comment.

    # Run the text through the French Braille translator
    text = text_to_braille(text)

    # You may want to put code after this comment. You can also delete this comment.

    # Replace the French capital with the English capital
    # Replace the French question with the English question
    text = text.replace(ostring_to_unicode('..\no.\n.o'),ostring_to_unicode('..\no.\noo'))
    text = text.replace(ostring_to_unicode(CAPITAL), ostring_to_unicode('..\n..\n.o'))


    # Replace empty ostring with space

    text = text.replace('..\n..\n..','')
    return text


def english_file_to_braille(fname):
    '''(str) -> NoneType
    Given English text in a file with name fname in folder tests/,
    convert it into English Braille in Unicode.
    Save the result to fname + "_eng_braille".
    Provided to students. You shouldn't edit this function.

    >>> english_file_to_braille('test4.txt')
    >>> file_diff('tests/test4_eng_braille.txt', 'tests/expected4.txt')
    True
    >>> english_file_to_braille('test5.txt')
    >>> file_diff('tests/test5_eng_braille.txt', 'tests/expected5.txt')
    True
    >>> english_file_to_braille('test6.txt')
    >>> file_diff('tests/test6_eng_braille.txt', 'tests/expected6.txt')
    True
    '''  
    file_to_braille(fname, english_text_to_braille, "eng_braille")


if __name__ == '__main__':
    doctest.testmod()    # you may want to comment/uncomment along the way
    # and add tests down here
