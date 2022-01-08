# COMP 202 A3 Part 1
# Name: Philippe Bergeron
# Student ID: 260928589

import doctest
import random

def flatten_lists(nested):
    ''' (list of lists) -> list
    >>> flatten_lists([[0], [1, 2], 3])
    [0, 1, 2, 3]
    >>> flatten_lists([2, 4, [3, 7, 8],[0]])
    [2, 4, 3, 7, 8, 0]
    '''
    new_list = [] # creating a new list

    # Checking for every element in nested
    for element in nested:
        if type(element) == list: # A nested loop when element is a sublist
            for nested_element in element:
                # adding every nested element inside list in a new list
                new_list.append(nested_element) 
        else:
            # adding element in a new list
            new_list.append(element)

    return new_list


def flatten_dict(d):
    ''' (dict) -> list
    >>> flatten_dict({'LIBERAL': 5, 'NDP': 2, 'BLOC': 4})
    ['LIBERAL', 'LIBERAL', 'LIBERAL', 'LIBERAL', 'LIBERAL', 'NDP', 'NDP', 'BLOC', 'BLOC', 'BLOC', 'BLOC']
    '''

    # creating a new list
    new_list = []
    
    # getting the value for each key
    for key in d:
        value_of_key = d.get(key)
        # adding key to new_list "value_of_key" times
        for indice in range(value_of_key):
            new_list.append(key)
        
        

    return new_list


def add_dicts(d1, d2):
    ''' (dict, dict) -> (dict)
    >>> add_dicts({'a':5, 'b':2, 'd':-1}, {'a':7, 'b':1, 'c':5})
    {'a': 12, 'b': 3, 'd': -1, 'c': 5}
    '''
    # creating a new dictionary
    new_dict = {}

    # Putting every key in d1 with its value in new dictionary
    for element_key1 in d1:
        d1_value = d1.get(element_key1)
        new_dict[element_key1] = d1_value

    # Putting every key in d2 with its value in the same new dictionary   
    for element_key2 in d2:
        d2_value = d2.get(element_key2)
        # if the key already exists, add both values
        if element_key2 in new_dict:
            associated_val = new_dict.get(element_key2)
            new_dict[element_key2] = associated_val + d2_value
        # add new key to dictionary 
        else:
            new_dict[element_key2] = d2_value
            

    return new_dict


def get_all_candidates(ballots):
    '''(list of dicts, lists, elements) -> (list)
    >>> get_all_candidates([{'GREEN':3, 'NDP':5},{'NDP':2, 'LIBERAL':4}, ['CPC', 'NDP'], 'BLOC'])
    ['GREEN', 'NDP', 'LIBERAL', 'CPC', 'BLOC']
    '''
    
    # creating a new list that will contain all candidates
    # creating a list of dictionaries
    # creating a list of lists
    # creating a simplified ballot that contains only 1 occurence of each element
    new_list = []
    list_of_dicts = []
    list_of_lists = []
    simplified_ballot = []

    # putting each element in either list of dicts or list of lists
    for element in ballots:
        if type(element) == dict:
            list_of_dicts.append(element)
        else:
            list_of_lists.append(element)
            
    # adding dictionaries in list of dicts 
    if len(list_of_dicts) == 2:
        new_dict = add_dicts(list_of_dicts[0], list_of_dicts[1])
        
    # executes if list of dictionaries more than 2
    else:
        new_dict1 = add_dicts(list_of_dicts[0], list_of_dicts[1])
        new_dict = add_dicts(new_dict1, list_of_dicts[3])

    # a set of functions to have a list of every vote
    new_list = flatten_dict(new_dict)
    new_list.append(list_of_lists)
    new_list = flatten_lists(new_list)
    new_list = flatten_lists(new_list)
    
    # 1 occurance of list of votes (list of parties)
    for element in new_list:
        if element not in simplified_ballot:
            simplified_ballot.append(element)



    return simplified_ballot


###################################################### winner/loser

def get_candidate_by_place(result, func):
    ''' (dict, func) -> str
    >>> result = {'LIBERAL':4, 'NDP':6, 'CPC':6, 'GREEN':4}
    >>> random.seed(0)
    >>> get_candidate_by_place(result, min)
    'GREEN'
    >>> random.seed(1)
    >>> get_candidate_by_place(result, min)
    'LIBERAL'
    '''
    # if there is no result return empty string
    if result == {}:
        return ''
    
    win_or_loss_list = []
    list_of_results = []
    
    # adding every result for each party in a list
    for element in result:
        one_result = result.get(element)
        list_of_results.append(one_result)
        
    # getting the min/max value of list of of results
    value_func = func(list_of_results)

    # creating a list of winner/losers
    for element in result:
        if result.get(element) == value_func:
            win_or_loss_list.append(element)
            
    # randomly selectin winner/loser
    win_or_loss = win_or_loss_list[random.randint(0, (len(win_or_loss_list)-1))]
    

    return win_or_loss

        


def get_winner(result):
    ''' (dict) -> str
    >>> get_winner({'NDP': 2, 'GREEN': 1, 'LIBERAL': 0, 'BLOC': 0})
    'NDP'
    '''

    # using previous function but with max
    win = get_candidate_by_place(result, max)

    return win


def last_place(result, seed = None):
    ''' (dict) -> str
    >>> last_place({'NDP': 2, 'GREEN': 1, 'LIBERAL': 0, 'BLOC': 0})
    'LIBERAL'
    '''

    #using previous function but with min
    loss = get_candidate_by_place(result, min)
    

    return loss
    


###################################################### testing help

def pr_dict(d):
    '''(dict) -> None
    Print d in a consistent fashion (sorted by key).
    Provided to students. Do not edit.
    >>> pr_dict({'a':1, 'b':2, 'c':3})
    {'a': 1, 'b': 2, 'c': 3}
    '''
    l = []
    for k in sorted(d):
        l.append( "'" + k + "'" + ": " + str(d[k]) )
    print('{' + ", ".join(l) + '}')


if __name__ == '__main__':
    doctest.testmod()
