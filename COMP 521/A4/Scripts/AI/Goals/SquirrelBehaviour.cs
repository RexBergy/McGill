using UnityEngine;
using System.Collections;
using System.Collections.Generic;



public class SquirrelBehaviour : MonoBehaviour, IGoap
{
    public Food foodComponent;
    UnityEngine.AI.NavMeshAgent navMeshAgent;
    float timer = 0;


    void Start()
    {
        navMeshAgent = this.GetComponent<UnityEngine.AI.NavMeshAgent>();
        if (foodComponent == null)
        {
            foodComponent = this.GetComponent<Food>();
        }
        
    }


    void Update()
    {
        timer -= Time.deltaTime;
    }

    
    public HashSet<KeyValuePair<string, object>> getWorldState()
    {
        HashSet<KeyValuePair<string, object>> worldData = new HashSet<KeyValuePair<string, object>>();
        
        worldData.Add(new KeyValuePair<string, object>("CanCarryNut", (foodComponent.getCarry() < 3)));
        worldData.Add(new KeyValuePair<string, object>("hasFood", (foodComponent.getCarry() > 0)));
        
        return worldData;
    }

    
    public HashSet<KeyValuePair<string, object>> createGoalState()
    {
        HashSet<KeyValuePair<string, object>> goal = new HashSet<KeyValuePair<string, object>>();
        goal.Add(new KeyValuePair<string, object>("FoodDeposited", true));
        return goal;
    }


    public void planFailed(HashSet<KeyValuePair<string, object>> failedGoal)
    {
        roam();
    }

    public void planFound(HashSet<KeyValuePair<string, object>> goal, Queue<GoapAction> actions)
    {
        
        Debug.Log("<color=green>Plan found</color> " + GoapAgent.Print(actions));
    }

    public void actionsFinished()
    {
        
        Debug.Log("<color=yellow>Actions completed</color>");
    }

    

    public bool moveAgent(GoapAction nextAction)
    {
        

        navMeshAgent.SetDestination(nextAction.target.transform.position);

        return true;


    }

    public void roam()
    {
        var x = UnityEngine.Random.Range(-35, 35);
        var z = UnityEngine.Random.Range(-35, 35);
        var position = new Vector3(x, 1, z);
        if (timer < 0)
        {
            navMeshAgent.SetDestination(position);
            timer = 4;
        }
        
    }
}