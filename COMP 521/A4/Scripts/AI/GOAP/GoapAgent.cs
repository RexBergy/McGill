using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System;


public sealed class GoapAgent : MonoBehaviour
{

    private FSM stateMachine;

    private FSM.FSMState idleState; // finds something to do
    private FSM.FSMState moveToState; // moves to a target
    private FSM.FSMState performActionState; // performs an action

    private HashSet<GoapAction> availableActions; //all available
    private Queue<GoapAction> currentActions; //current

    private IGoap dataProvider;

    private GoapPlanner planner;


    void Start()
    {
        // ! Instructions need to be in this order !
        stateMachine = new FSM();
        availableActions = new HashSet<GoapAction>();
        currentActions = new Queue<GoapAction>();
        planner = new GoapPlanner();
        findDataProvider();
        createIdleState();
        createMoveToState();
        createPerformActionState();
        stateMachine.pushState(idleState);
        loadActions();
    }


    void Update()
    {
        stateMachine.Update(this.gameObject);
    }


    public void addAction(GoapAction a)
    {
        availableActions.Add(a);
    }

    public GoapAction getAction(Type action)
    {
        foreach (GoapAction g in availableActions)
        {
            if (g.GetType().Equals(action))
                return g;
        }
        return null;
    }

    public void removeAction(GoapAction action)
    {
        availableActions.Remove(action);
    }

    private bool hasActionPlan()
    {
        return currentActions.Count > 0;
    }

    private void createIdleState()
    {
        idleState = (fsm, gameObj) => {
            HashSet<KeyValuePair<string, object>> worldState = dataProvider.getWorldState();
            HashSet<KeyValuePair<string, object>> goal = dataProvider.createGoalState();

            
            Queue<GoapAction> plan = planner.plan(gameObject, availableActions, worldState, goal);
            if (plan != null)
            {
                currentActions = plan;
                dataProvider.planFound(goal, plan);

                fsm.popState(); 
                fsm.pushState(performActionState);
            }
            else
            { 
                Debug.Log("<color=blue>Failed Plan:</color>" + Print(goal));
                dataProvider.planFailed(goal);
                fsm.popState(); 
                fsm.pushState(idleState);
            }

        };
    }

    private void createMoveToState()
    {
        moveToState = (fsm, gameObj) => 
        {
            GoapAction action = currentActions.Peek();
            if (action.requiresInRange() && action.target == null)
            {
                
                fsm.popState();
                fsm.popState();
                fsm.pushState(idleState);
                return;
            }
            if ((gameObj.transform.position - action.target.transform.position).magnitude < 1.8f)
            {
                action.setInRange(true);
            }

            
            if (dataProvider.moveAgent(action))
            {
                fsm.popState();
            }

            
        };
    }

    private void createPerformActionState()
    {
        performActionState = (fsm, gameObj) => {
            

            GoapAction action = currentActions.Peek();
            if (action.isDone())
            {
                currentActions.Dequeue();
                
            }
            
            if (hasActionPlan())
            {
               
                action = currentActions.Peek();
                bool inRange = action.requiresInRange() ? action.isInRange() : true;

                if (inRange)
                {
                  
                    
                    bool success = action.perform(gameObj);

                    if (!success)
                    {
                        
                        fsm.popState();
                        fsm.pushState(idleState);
                    }
                }
                else
                {
                    fsm.pushState(moveToState);
                }

            }
            else
            {
                fsm.popState();
                fsm.pushState(idleState);
                dataProvider.actionsFinished();
            }

        };
    }

    private void findDataProvider()
    {
        foreach (Component comp in gameObject.GetComponents(typeof(Component)))
        {
            if (typeof(IGoap).IsAssignableFrom(comp.GetType()))
            {
                dataProvider = (IGoap)comp;
                return;
            }
        }
    }

    private void loadActions()
    {
        GoapAction[] actions = gameObject.GetComponents<GoapAction>();
        foreach (GoapAction a in actions)
        {
            availableActions.Add(a);
        }
     
    }

    public static string Print(HashSet<KeyValuePair<string, object>> state)
    {
        String s = "";
        foreach (KeyValuePair<string, object> kvp in state)
        {
            s += kvp.Key + ":" + kvp.Value.ToString();
            s += ", ";
        }
        return s;
    }

    public static string Print(Queue<GoapAction> actions)
    {
        String s = "";
        foreach (GoapAction a in actions)
        {
            s += a.GetType().Name;
            s += "-> ";
        }
        s += "GOAL";
        return s;
    }

    public static string Print(GoapAction[] actions)
    {
        String s = "";
        foreach (GoapAction a in actions)
        {
            s += a.GetType().Name;
            s += ", ";
        }
        return s;
    }

    public static string Print(GoapAction action)
    {
        String s = "" + action.GetType().Name;
        return s;
    }
}