using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class InsectMovement : MonoBehaviour
{
    Insect i = new Insect();
    Insect g = new Insect();
    Insect f = new Insect();
    // Start is called before the first frame update
    void Start()
    {
        i.CreateInsect();
        g.CreateInsect();
        f.CreateInsect();
    }

    // Update is called once per frame
    void Update()
    {
        i.Movement();
        g.Movement();
        f.Movement();
        i.Draw();
        g.Draw();
        f.Draw();
    }

    public Insect getFirst()
    {
        return i;
    }

    public Insect getSec()
    {
        return g;
    }

    public Insect getThird()
    {
        return f;
    }
}
