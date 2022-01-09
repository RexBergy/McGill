using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class TreeBehaviour : MonoBehaviour
{

    public GameObject nut;
    float timer = 8;
    float x;
    float y;
    float z;
    int NbNuts = 0;
    
    void Start()
    {
        //nuts = new List<GameObject>();
        x = transform.position.x;
        y = transform.position.y;
        z = transform.position.z;
    }

    // Update is called once per frame
    void Update()
    {
        timer -= Time.deltaTime;
        if (timer <= 0f && NbNuts < 5)
        {
            nut = Instantiate(nut, new Vector3(x+ UnityEngine.Random.Range(-5f, 5f), y+0.5f, z+ UnityEngine.Random.Range(-5f, 5f)), transform.rotation);
            //nuts.Add(nut);
            nut.GetComponent<NutComponent>().ParentTree = this.gameObject;
            timer = 2;
            NbNuts += 1;
        }
        
    }

    public void nutEaten()
    {
        NbNuts--;
        timer = 2;
        //nuts.Remove();

    }
}
