using System;
using System.Collections.Generic;
using UnityEngine;

public class CanonPhysics : MonoBehaviour
{
    public GameObject manager;
    private Vector3 force;
    private Vector3 speed;
    private Vector3 accel = new Vector3(0,-9.81f,0);
    bool stop = false;
    bool bounced = false;
    bool wallstop = false;
    bool wallBounced = false;
    float time;
    float timeout = 10000000000000f;
    bool notDetected = true;
    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        if (!stop)
        {
            this.transform.position = UpdatePosition(transform.position, new Vector3(-1, 1, 0), accel, speed, Time.deltaTime);
            speed = speed + accel * Time.deltaTime;
        }
        
        if (transform.position.y < yPosition(5f*transform.position.x+30f) )
        {

            //this.transform.position = new Vector3(this.transform.position.x, this.transform.position.y, 0);
            speed.y = this.force.y/3;
            speed.x = this.force.x / 3f;
            this.force.x = speed.x;
            this.force.y = speed.y;

            this.transform.position = UpdatePosition(transform.position, new Vector3(0, 1, 0), accel, speed, Time.deltaTime);
            stop = true;
            bounced = true;

        }

        if (transform.position.x < -9.5f)
        {
            speed.y = this.force.y / 2f;
            speed.x = this.force.x / 2f;
            this.force.x = speed.x;
            this.force.y = speed.y;
            this.transform.position = UpdatePosition(transform.position, new Vector3(-1, -1, 0), accel, speed, Time.deltaTime);
            wallstop = true;
            wallBounced = true;
        }

        if (bounced)
        {
            this.transform.position = UpdatePosition(transform.position, new Vector3(-1, 1, 0), accel, speed, Time.deltaTime);
            speed = speed + accel * Time.deltaTime;
            stop = false;
        }

        if (transform.position.y > 6f)
        {
            Destroy(gameObject);
        }

        if (wallstop)
        {

        }
        if (wallBounced)
        {

        }
        if (speed.x < 0.01f && speed.y < 0.01f && notDetected)
        {
            
            time = Time.time;
            notDetected = false;
            timeout = time + 1f;

        }

        if (Time.time > timeout)
        {
            ;
            Destroy(gameObject);
        }
        
    }

    public void addForce(Vector3 force)
    {
        this.force = force;
        this.speed = force;
    }

    Vector3 UpdatePosition(Vector3 lastPosition, Vector3 direction, Vector3 a, Vector3 s, double dt)
    {
        
        var dirX = direction.x;
        var dirY = direction.y;

        var aX = dirX * a.x;
        var aY = dirY * a.y;

        var sX = dirX * s.x;
        var sY = dirY * s.y;

        var posX = lastPosition.x;
        var posY = lastPosition.y;

        var newX = posX + sX * dt + 0.5 * aX * dt * dt;
        var newY = posY + sY * dt + 0.5 * aY * dt * dt;
        
        return new Vector3(Convert.ToSingle(newX), Convert.ToSingle(newY), 0);
    }

    float yPosition(float x)
    {
        
        return 2f * Mathf.Cos((x / 15f)-2.78f)-1.45f;
    }

    

}
