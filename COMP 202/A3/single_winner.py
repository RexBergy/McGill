# COMP 202 A3
# Name: Philippe Bergeron
# Student ID: 260928589

from a3_helpers import *


def count_plurality(ballots):
    ''' (list) -> dict
    >>> count_plurality(['LIBERAL', 'LIBERAL', 'NDP', 'LIBERAL'])
    {'LIBERAL': 3, 'NDP': 1}
    '''
    # creating new dictionary
    new_dict = {}

    # creating a key from every unique element
    # its value = number of times element in list
    for element in ballots:
        if element in new_dict:
            new_dict[element] +=1
        else:
            new_dict[element] = 1

    return new_dict
    
    
    

def count_approval(ballots):
    ''' (list of list) -> dict
    >>> count_approval([ ['LIBERAL', 'NDP'], ['NDP'], ['NDP', 'GREEN', 'BLOC']] )
    {'LIBERAL': 1, 'NDP': 3, 'GREEN': 1, 'BLOC': 1}
    '''
    # modifying list of list in a simple list
    # counting plurality
    new_ballot = flatten_lists(ballots)

    return count_plurality(new_ballot)


def count_rated(ballots):
    ''' (list of dicts) -> dict
    >>> count_rated([{'LIBERAL': 5, 'NDP':2}, {'NDP':4, 'GREEN':5}])
    {'LIBERAL': 5, 'NDP': 6, 'GREEN': 5}
    '''
    # creating a new list which contains
    # flatten dicts
    # modify it to dictionary with count plurality
    new_list = []
    for element in ballots:
        new_list.append(flatten_dict(element))
    new_list = flatten_lists(new_list)

    return count_plurality(new_list)
        

    
        

def count_first_choices(ballots):
    '''(list of lists) -> dict
    >>> count_first_choices([['NDP', 'LIBERAL'], ['GREEN', 'NDP'], ['NDP', 'BLOC']])
    {'NDP': 2, 'GREEN': 1, 'LIBERAL': 0, 'BLOC': 0}
    '''
    # creating a new dictionary
    new_dict = {}
 
    # for every 1st element inside list
    # add value 1 to new dictionary
    for element in ballots:
        for nest_elem in element:
            # executes if 1st element of nested list
            if nest_elem == element[0]:
                if element[0] in new_dict:
                    new_dict[element[0]] += 1
                else:
                    new_dict[element[0]] = 1
            # for every element except 1st element add 0
            # or give value 0 to its key
            else:
                if nest_elem in new_dict:
                    new_dict[nest_elem] += 0
                else:
                    new_dict[nest_elem] = 0
        


        

    return new_dict


if __name__ == '__main__':
    doctest.testmod()
