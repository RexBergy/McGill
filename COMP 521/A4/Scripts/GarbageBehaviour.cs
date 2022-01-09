using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GarbageBehaviour : MonoBehaviour
{
    float timer = 10;
    bool Empty;
    Color OGcolor;
    
    void Start()
    {

        OGcolor = GetComponent<Renderer>().material.color;
        if (UnityEngine.Random.Range(0f, 10f) > 5f)
        {
            Empty = true;
            
        } else
        {
            Empty = false;
            GetComponent<Renderer>().material.color = new Color(0, 255, 0);
        }


    }

    // Update is called once per frame
    void Update()
    {
        timer -= Time.deltaTime;
        if (timer <= 0f)
        {
            
            Empty = !Empty;
            timer = 10;
            if (GetComponent<Renderer>().material.color == OGcolor)
            {
                GetComponent<Renderer>().material.color = new Color(0, 255, 0);
            } else
            {
                GetComponent<Renderer>().material.color = OGcolor;
            }
            
        }
    }
}
