using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PickUpGarbage :GoapAction
{
    private bool pickedUp = false;
    private GarbageComponent targetGarbage; // Target garbage, will be the closest


    public PickUpGarbage()
    {
        addPrecondition("CanCarryNut", true); 
        addEffect("hasFood1", true);
        
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
        
        GarbageComponent[] garbages = (GarbageComponent[])UnityEngine.GameObject.FindObjectsOfType(typeof(GarbageComponent));
        GarbageComponent closest = null;
        float closestDist = 0;

        foreach (GarbageComponent garbage in garbages)
        {
            if (closest == null)
            {
                
                closest = garbage;
                closestDist = (garbage.gameObject.transform.position - agent.transform.position).magnitude;
            }
            else
            {
                
                float dist = (garbage.gameObject.transform.position - agent.transform.position).magnitude;
                if (dist < closestDist)
                {
                    
                    closest = garbage;
                    closestDist = dist;
                }
            }
        }
        if (closest == null)
            return false;

        targetGarbage = closest;
        target = targetGarbage.gameObject;
        

        return closest != null;
    }

    public override bool perform(GameObject agent)
    {
       

        Food food = (Food)agent.GetComponent(typeof(Food));
        food.pickUp(target);
        pickedUp = true;
        

        return true;
    }
}
