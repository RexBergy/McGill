using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class RandomTreePlacer : MonoBehaviour
{

    public GameObject tree;
    public GameObject garbageCan;
    public GameObject squirrel;
    private List<Vector3> treePositions;
    private List<GameObject> trees;
    private List<Vector3> garbagePositions;
    

    // Start is called before the first frame update
    void Start()
    {
        treePositions = new List<Vector3>();
        trees = new List<GameObject>();
        garbagePositions = new List<Vector3>();
        int i = 0;
        while (i < 10)
        {
            float x = UnityEngine.Random.Range(-35, 35);
            float z = UnityEngine.Random.Range(-35, 35);
            treePositions.Add(new Vector3(x, 0.5f, z));
            tree = Instantiate(tree, new Vector3(x, 0.5f, z), Quaternion.identity);
            trees.Add(tree);
            if (i%2 == 0)
            {
                garbagePositions.Add(new Vector3(x + x / 4, 1, z - x / 4));
                Instantiate(garbageCan, new Vector3(x+x/4, 1, z-x/4), Quaternion.identity);
            } else
            {
                ;
                var sqr1 = Instantiate(squirrel, new Vector3(x + x / 2, 1, z + x / 4), Quaternion.identity);
                Food food = (Food)sqr1.GetComponent(typeof(Food));
                food.hometree = tree;
            }
            i++;
        }
        /*var sqr1 = Instantiate(squirrel, new Vector3(25, 1, 15), Quaternion.identity);
        Food food = (Food)sqr1.GetComponent(typeof(Food));
        food.hometree = tree;*/


    }

    void Update()
    {


    }

    public List<Vector3> getTreePositions()
    {
        return treePositions;
    }

    public List<Vector3> getGarbagePositions()
    {
        return garbagePositions;
    }

    public List<GameObject> getTrees()
    {
        return trees;
    }
}
