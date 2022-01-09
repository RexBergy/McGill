using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class DepositFood : GoapAction
{
    private bool deposited = false;
    private GameObject targetTree; // Target Tree

    

    public DepositFood()
    {
        addPrecondition("hasFood2", true); 
        addEffect("FoodDeposited", true);
    }


    public override void reset()
    {
        deposited = false;
        
        
    }

    public override bool isDone()
    {
        return deposited;
    }

    public override bool requiresInRange()
    {
        return true; 
    }

    public override bool checkProceduralPrecondition(GameObject agent)
    {
        
        Food food = (Food)agent.GetComponent(typeof(Food));

        targetTree = food.hometree;
        target = targetTree.gameObject;

        return true;
    }

    public override bool perform(GameObject agent)
    {
        

        Food food = (Food)agent.GetComponent(typeof(Food));
        for(int i = 0; i < 3; i++)
        {
            food.dropping();
        }
        
        deposited = true;
        

        return true;
    }
}
