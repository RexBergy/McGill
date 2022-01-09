using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Food : MonoBehaviour
{
    public GameObject nut;
    public GameObject closestTree;
    public GameObject hometree;
    int carrying;
    // Start is called before the first frame update
    void Start()
    {
        carrying = 0;
    }

    // Update is called once per frame
    void Update()
    {
        
    }

    public void pickUp(GameObject nut)
    {
        if (carrying < 3)
        {
            carrying++;
        }
        this.nut = nut;
        
    }

    public void dropping()
    {
        if (carrying > 0)
        {
            carrying--;
        }
        
    }

    public int getCarry()
    {
        return carrying;
    }


    public void FindclosestTree()
    {

    }
}
