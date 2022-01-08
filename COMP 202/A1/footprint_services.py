# COMP 202 A1: Part 2
# Footprint of utilities & university
# Author: Philippe Bergeron
# 260928589

import doctest
from unit_conversion import *

INCOMPLETE = -1

######################################### Utilities


def fp_from_gas(monthly_gas):
    '''(num) -> float
    Calculate metric tonnes of CO2E produced annually
    based on monthly natural gas bill in $.

    Source: https://www.forbes.com/2008/04/15/green-carbon-living-forbeslife-cx_ls_0415carbon.html#1f3715d01852
        B.) Multiply your monthly gas bill by 105 [lbs, to get annual amount] 

    >>> fp_from_gas(0)
    0.0
    >>> round(fp_from_gas(100), 4)
    4.7627
    >>> round(fp_from_gas(25), 4)
    1.1907
    '''

    # Converting lbs into tonnes
    annual_gas_fp_lbs = float((105*monthly_gas))
    annual_gas_fp_kg = float(pound_to_kg(annual_gas_fp_lbs))
    annual_gas_fp_tonnes = float(kg_to_tonnes(annual_gas_fp_kg))

    return annual_gas_fp_tonnes



def fp_from_hydro(daily_hydro):
    '''(num) -> float
    Calculate metric tonnes of CO2E produced annually
    based on average daily hydro usage.

    To find out your average daily hydro usage in kWh:
        Go to https://www.hydroquebec.com/portail/en/group/clientele/portrait-de-consommation
        Scroll down to "Annual total" and press "kWh"

    Source: https://www.hydroquebec.com/data/developpement-durable/pdf/co2-emissions-electricity-2017.pdf
        0.6 kg CO2E / MWh

    >>> fp_from_hydro(0)
    0.0
    >>> round(fp_from_hydro(10), 4)
    0.0022
    >>> round(fp_from_hydro(48.8), 4)
    0.0107
    '''
    # Converting daily kwh into annual mwh
    annual_elec_kwh = float(daily_to_annual(daily_hydro))
    annual_elec_mwh = float((annual_elec_kwh/1000))

    # Calculating the footprint of electricity in tonnes
    annual_elec_kg = float(annual_elec_mwh*0.6)
    annual_elec_tonnes = float(kg_to_tonnes(annual_elec_kg))

    return annual_elec_tonnes



def fp_of_utilities(daily_hydro, monthly_gas):
    '''(num, num, num) -> float
    Calculate metric tonnes of CO2E produced annually from
    daily hydro (in kWh) and gas bills (in $).

    >>> fp_of_utilities(0, 0)
    0.0
    >>> round(fp_of_utilities(100, 0), 4)
    0.0219
    >>> round(fp_of_utilities(0, 100), 4)
    4.7627
    >>> round(fp_of_utilities(50, 20), 4)
    0.9635
    '''

    # Adding the 2 functions above to get the overall footprint of utilities
    hydro_gas_fp_annual = fp_from_hydro(daily_hydro) + fp_from_gas(monthly_gas)
    
    return hydro_gas_fp_annual


#################################################


def fp_of_studies(annual_uni_credits):
    '''(num, num, num) -> flt
    Return metric tonnes of CO2E from being a student, based on
    annual university credits.

    Source: https://www.mcgill.ca/facilities/files/facilities/ghg_inventory_report_2017.pdf
        1.12 tonnes per FTE (30 credit) student

    >>> round(fp_of_studies(0), 4)
    0.0
    >>> round(fp_of_studies(30), 4)
    1.12
    >>> round(fp_of_studies(18), 4)
    0.672
    '''
    # Converting credits into tonnes of CO2
    study_fp = 1.12*(annual_uni_credits/30)
    
    return study_fp


#################################################

if __name__ == '__main__':
    doctest.testmod()
