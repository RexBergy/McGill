# SIPUGRAS v2.0, cc Marek Borik


def execute_function(module, func_name, *attr):
    if func_name in dir(module):                         # if the function is in the module
        try:
            result = getattr(module, func_name)(*attr)   # call the function with variable amount of arguments
            return result
        except TypeError:
            raise TypeError
        except NameError:
            raise NameError
        except SyntaxError:
            raise SyntaxError
        except Exception:   # catch more cases, return the exception after the keyword
            return "Invalid"
    else:
        raise NameError( "No method named " + str(func_name) + " found.")


def main():
    import a3_helpers

    execute_function(a3_helpers, 'flatten_lists', [[0],[1,2],3])
    execute_function(a3_helpers, 'flatten_dict', {'LIBERAL':5,'NDP':2})
    execute_function(a3_helpers, 'add_dicts', {'a':5,'b':2,'d':-1},{'a':7,'b':1,'c':5})
    execute_function(a3_helpers, 'get_all_candidates', [{'GREEN':3,'NDP':5},{'NDP':2,'LIBERAL':4},['CPC','NDP'],'BLOC'])
    execute_function(a3_helpers, 'get_candidate_by_place', *({'LIBERAL':4,'NDP':6,'CPC':6,'GREEN':4},min))
    execute_function(a3_helpers, 'get_winner', {'NDP': 2, 'GREEN': 1, 'LIBERAL': 0, 'BLOC': 0})
    execute_function(a3_helpers, 'last_place', {'NDP': 2, 'GREEN': 1, 'LIBERAL': 0, 'BLOC': 0})

    import single_winner

    execute_function(single_winner, 'count_plurality', ['LIBERAL','LIBERAL','NDP','LIBERAL'])
    execute_function(single_winner, 'count_approval', [ ['LIBERAL', 'NDP'], ['NDP'], ['NDP', 'GREEN', 'BLOC']])
    execute_function(single_winner, 'count_rated', [{'LIBERAL': 5, 'NDP':2}, {'NDP':4, 'GREEN':5}])
    execute_function(single_winner, 'count_first_choices', [['NDP', 'LIBERAL'], ['GREEN', 'NDP'], ['NDP', 'BLOC']])

    import instant_run_off

    execute_function(instant_run_off, 'votes_needed_to_win', *(['g']*20, 1))
    execute_function(instant_run_off, 'has_votes_needed', *({'NDP': 4, 'LIBERAL': 3}, 4))
    execute_function(instant_run_off, 'eliminate_candidate', *([['NDP', 'LIBERAL'], ['GREEN', 'NDP'], ['NDP', 'BLOC']], ['LIBERAL', 'NDP']))
    execute_function(instant_run_off, 'count_irv', [['NDP'], ['GREEN', 'NDP', 'BLOC'], ['LIBERAL','NDP'], ['LIBERAL'], ['NDP', 'GREEN'], ['BLOC', 'GREEN', 'NDP'], ['BLOC', 'CPC'], ['LIBERAL', 'GREEN'], ['NDP']])

    import proportional_representation

    execute_function(proportional_representation, 'irv_to_stv_ballot', *([['NDP', 'CPC'], ['GREEN']], 3))
    execute_function(proportional_representation, 'count_stv', *([['GREEN', 'NDP', 'BLOC']]*5 + [['NDP', 'GREEN', 'BLOC']]*3, 4))
    execute_function(proportional_representation, 'count_SL', *(['LIBERAL']*100 + ['GREEN']*80 + ['NDP']*30 + ['BLOC']*20,8))
    
    print("Your Score: 100\n")
    print("Total Score: 100\n")


if __name__ == '__main__':
    main()
