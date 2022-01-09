using UnityEngine;
using System.Collections;
using System.Collections.Generic;



public interface IGoap
{
    void planFailed(HashSet<KeyValuePair<string, object>> failedGoal);

    void planFound(HashSet<KeyValuePair<string, object>> goal, Queue<GoapAction> actions);

    void actionsFinished();

    bool moveAgent(GoapAction nextAction);

    HashSet<KeyValuePair<string, object>> getWorldState();
    
    HashSet<KeyValuePair<string, object>> createGoalState();
    
   
}
