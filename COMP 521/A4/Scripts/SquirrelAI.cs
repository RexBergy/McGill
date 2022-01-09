using System.Collections;
using System.Collections.Generic;
using UnityEngine.AI;
using UnityEngine;

public class SquirrelAI : MonoBehaviour
{
    Vector3 destination;
    NavMeshAgent navMeshAgent;
    RandomTreePlacer treePlacer;
    GameObject terrain;
    // Start is called before the first frame update
    void Start()
    {
        terrain = GameObject.Find("Terrain");
        treePlacer = terrain.GetComponent<RandomTreePlacer>();
        List<Vector3> treePositions = treePlacer.getTreePositions();
        navMeshAgent = this.GetComponent<NavMeshAgent>();
        List<GameObject> trees = treePlacer.getTrees();
        if (navMeshAgent == null)
        {
            Debug.LogError("The nav mesh agent component attached to " + gameObject.name);
        } else
        {
            SetDestination();
            var i = UnityEngine.Random.Range(0, 9);
            navMeshAgent.SetDestination(trees[1].transform.position);
        }
    }

    private void SetDestination()
    {
        var x = UnityEngine.Random.Range(-50, 50);
        
        var z = UnityEngine.Random.Range(-50, 50);
        destination = new Vector3(x, 1, z);

    }

    // Update is called once per frame
    //
    void Update()
    {
        
    }
}
