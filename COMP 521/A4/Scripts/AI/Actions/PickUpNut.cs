using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PickUpNut : GoapAction
{
   private bool pickedUp = false;
   private NutComponent targetNut; // Nut to go pick up


    public PickUpNut()
    {
        addPrecondition("hasFood1", true);
        addEffect("hasFood2", true);
    }


    public override void reset()
    {
        pickedUp = false;
        
    }

    public override bool isDone()
    {
        return pickedUp;
    }

    public override bool requiresInRange()
    {
        return true; 
    }

    public override bool checkProceduralPrecondition(GameObject agent)
    {
        
        NutComponent[] nuts = (NutComponent[])UnityEngine.GameObject.FindObjectsOfType(typeof(NutComponent));
        NutComponent closest = null;
        float closestDist = 0;

        foreach (NutComponent nut in nuts)
        {
            if (closest == null)
            {
                
                closest = nut;
                closestDist = (nut.gameObject.transform.position - agent.transform.position).magnitude;
            }
            else
            {
                
                float dist = (nut.gameObject.transform.position - agent.transform.position).magnitude;
                if (dist < closestDist)
                {
                    
                    closest = nut;
                    closestDist = dist;
                }
            }
        }
        if (closest == null)
            return false;

        targetNut = closest;
        target = targetNut.gameObject;

        return closest != null;
    }

    public override bool perform(GameObject agent)
    {
        

        Food food = (Food)agent.GetComponent(typeof(Food));
        food.pickUp(target);
        NutComponent nutToEat = target.GetComponent<NutComponent>();
        var tree = nutToEat.ParentTree;
        var behaviour = tree.GetComponent<TreeBehaviour>();
        behaviour.nutEaten();
        Destroy(target);
        
        pickedUp = true;
        
        
        return true;
    }

    
}
