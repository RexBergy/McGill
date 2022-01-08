# COMP 202 A3
# Name: Philippe Bergeron
# ID: 260928589

from single_winner import *

################################################################################

def votes_needed_to_win(ballots, num_winners):
    '''(list or list of dicts, int) -> int
    >>> votes_needed_to_win([{'CPC':3, 'NDP':5}, {'NDP':2, 'CPC':4},{'CPC':3, 'NDP':5}], 1)
    2
    >>> votes_needed_to_win(['g']*20, 2)
    7
    '''

    # removes an empty list in ballots 
    for i in range(len(ballots)):
        for element in ballots:
            if '[]' == element:
                ballots.remove('[]')
                i = i
            else:
                i += 1
    # gives number of votes
    total_votes = len(ballots)

    # gives number of to win
    votes_to_win = round((total_votes)/(num_winners + 1)-0.51)+ 1

    return votes_to_win
        
    


def has_votes_needed(result, votes_needed):
    '''(dict, int) -> Bool
    >>> has_votes_needed({'NDP': 4, 'LIBERAL': 3}, 4)
    True
    '''
    # check if key has value equal to votes needed
    for element in result:
        if result.get(element) == votes_needed:
            return True

    # returns false if condition not met 
    return False
    


################################################################################


def eliminate_candidate(ballots, to_eliminate):
    '''(
    >>> eliminate_candidate([['NDP', 'LIBERAL'], ['GREEN', 'NDP'], ['NDP', 'BLOC']], ['NDP', 'LIBERAL'])
    [[], ['GREEN'], ['BLOC']]
    '''
    # to keep intinal ballots
    new_ballots = ballots

    # checks if every nested element is in to_eliminate
    # deletes the nested element if true
    for i in range(len(flatten_lists(new_ballots))):
        for element in new_ballots:
            for nest_element in element:
                if nest_element in to_eliminate:
                    element.remove(nest_element)
                    i = i # this makes the loop restart when element deleted
                else:
                    i += 1
                

    return new_ballots
    


################################################################################


def count_irv(ballots):
    '''
    >>> count_irv([['NDP'], ['GREEN', 'NDP', 'BLOC'], ['LIBERAL','NDP'],['LIBERAL'], ['NDP', 'GREEN'], ['BLOC', 'GREEN', 'NDP'],['BLOC', 'CPC'], ['LIBERAL', 'GREEN'], ['NDP']])
    {'BLOC': 0, 'CPC': 0, 'GREEN': 0, 'LIBERAL': 3, 'NDP': 5}
    '''
    # checks the votes needed to win initially
    votes_to_win = votes_needed_to_win(ballots, 1)
    # creates a new dictionary
    new_dict = {}

    # puts the first last place candidate in dictionary with value 0
    new_dict[last_place(count_first_choices(ballots))] = 0

    # a loop to eliminate every last cadidate until
    # a majority is found
    # gives value 0 to to eliminated cadidate
    while has_votes_needed(count_first_choices(ballots), votes_to_win) == False:
        last = last_place(count_first_choices(ballots))
        ballots = eliminate_candidate(ballots, last)
        new_dict[last] = 0

    # modifies the ballots to have a list of first choice votes    
    ballots = count_first_choices(ballots)
    ballots = flatten_dict(ballots)

    # add 1 to every 1st choice vote in dictionary
    for element in ballots:
        if element in new_dict:
            new_dict[element] +=1
        else:
            new_dict[element] = 1



    

    return new_dict


################################################################################

if __name__ == '__main__':
    doctest.testmod()
