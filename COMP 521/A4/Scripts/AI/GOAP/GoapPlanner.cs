using System;
using System.Collections.Generic;
using UnityEngine;


public class GoapPlanner
{

    private class Node
    {
        public Node parent;
        public HashSet<KeyValuePair<string, object>> state;
        public GoapAction action;

        public Node(Node parent, HashSet<KeyValuePair<string, object>> state, GoapAction action)
        {
            this.parent = parent;
            this.state = state;
            this.action = action;
        }
    }

    public Queue<GoapAction> plan(GameObject agent,
                                  HashSet<GoapAction> availableActions,
                                  HashSet<KeyValuePair<string, object>> worldState,
                                  HashSet<KeyValuePair<string, object>> goal)
    {

       
        foreach (GoapAction a in availableActions)
        {
            a.doReset();
            
        }
        

        
        HashSet<GoapAction> usableActions = new HashSet<GoapAction>();
        foreach (GoapAction a in availableActions)
        {
            if (a.checkProceduralPrecondition(agent))
                usableActions.Add(a);
        }

       
        List<Node> leaves = new List<Node>();
        
        Node start = new Node(null, worldState, null);
        bool succes = false;
        
        foreach (GoapAction action in usableActions)
        {

            if (inState(action.Preconditions, start.state))
            {

                HashSet<KeyValuePair<string, object>> currentState = populateState(start.state, action.Effects);
                Node node = new Node(start, currentState, action);

                if (inState(goal, currentState))
                {
                    leaves.Add(node);
                    succes = true;
                }
                else
                {
                    
                    HashSet<GoapAction> subset = new HashSet<GoapAction>();
                    foreach (GoapAction a in usableActions)
                    {
                        if (!a.Equals(action))
                            subset.Add(a);
                    }
                    bool found = buildGraph(node, leaves, subset, goal);
                    if (found)
                        succes = true;
                }
            }
        }

        if (!succes)
        {
            return null;
        }
        
        List<GoapAction> result = new List<GoapAction>();
        Node n = leaves[0];
        while (n != null)
        {
            if (n.action != null)
            {
                result.Insert(0, n.action);
            }
            n = n.parent;
        }

        Queue<GoapAction> queue = new Queue<GoapAction>();
        foreach (GoapAction a in result)
        {
            queue.Enqueue(a);
        }

        return queue;
    }

    
    private bool inState(HashSet<KeyValuePair<string, object>> test, HashSet<KeyValuePair<string, object>> state)
    {
        bool allMatch = true;
        foreach (KeyValuePair<string, object> t in test)
        {
            bool match = false;
            foreach (KeyValuePair<string, object> s in state)
            {
                if (s.Equals(t))
                {
                    match = true;
                    break;
                }
            }
            if (!match)
                allMatch = false;
        }
        return allMatch;
    }

    
    private HashSet<KeyValuePair<string, object>> populateState(HashSet<KeyValuePair<string, object>> currentState, HashSet<KeyValuePair<string, object>> stateChange)
    {
        int iterrations = 0;
        HashSet<KeyValuePair<string, object>> state = new HashSet<KeyValuePair<string, object>>();
        foreach (KeyValuePair<string, object> s in currentState)
        {
            state.Add(new KeyValuePair<string, object>(s.Key, s.Value));
            iterrations++;
        }

        foreach (KeyValuePair<string, object> change in stateChange)
        {
            bool exists = false;

            foreach (KeyValuePair<string, object> s in state)
            {
                if (s.Equals(change))
                {
                    exists = true;
                    iterrations++;
                    break;
                }
            }

            if (exists)
            {
                state.RemoveWhere((KeyValuePair<string, object> kvp) => { return kvp.Key.Equals(change.Key); });
                KeyValuePair<string, object> updated = new KeyValuePair<string, object>(change.Key, change.Value);
                state.Add(updated);
                iterrations--;
            }
            
            else
            {
                state.Add(new KeyValuePair<string, object>(change.Key, change.Value));
            }
        }
        return state;
    }

    private bool buildGraph(Node parent, List<Node> leaves, HashSet<GoapAction> usableActions, HashSet<KeyValuePair<string, object>> goal)
    {
        int size = 0;
        bool found = false;
        foreach (GoapAction action in usableActions)
        {
            
            if (inState(action.Preconditions, parent.state))
            {
                
                HashSet<KeyValuePair<string, object>> currentState = populateState(parent.state, action.Effects);
                Node node = new Node(parent, currentState, action);
                if (inState(goal, currentState))
                {
                    leaves.Add(node);
                    found = true;
                }
                else
                {
                
                    HashSet<GoapAction> subset = new HashSet<GoapAction>();
                    foreach (GoapAction a in usableActions)
                    {
                        if (!a.Equals(action))
                            subset.Add(a);
                    }
                    
                    bool wasfound = buildGraph(node, leaves, subset, goal);
                    if (wasfound)
                        found = true;
                }
            }
        }
        size += 1;

        return found;
    }



}
