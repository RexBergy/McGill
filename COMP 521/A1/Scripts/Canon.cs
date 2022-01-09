using System;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class Canon : MonoBehaviour
{
    public GameObject projectile;
    public Text text1;
    public Text text2;
    bool fired = false;
    Vector3 force = new Vector3(0f, 0.9f, 0f);
    private int angle = 90;
    private float velocity = 0f;
    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        text1.text = "Launch velocity " + velocity + " m/s";                         // Setting text content.
        text2.text = "Angle " + angle + " degrees";
        double dt = Time.time;
        GameObject canonball;
        
        

        if (Input.GetButtonDown("Fire1") && velocity > 2f)
        {
            fired = true;

            if (fired)
            {
                
                dt = Time.time - dt;
                canonball = Instantiate(projectile, transform.position, Quaternion.identity);
                canonball.GetComponent<CanonPhysics>().addForce(velocity*force);
           
            }

        }

        if (Input.GetKeyDown(KeyCode.DownArrow))
        {
            if (angle <=90 && angle > 0)
            {
                transform.Rotate(0, 0, 5f, Space.Self);
                force.x += 0.05f;
                force.y -= 0.05f;
                angle -= 5;
            }
            
        }

        if (Input.GetKeyDown(KeyCode.UpArrow))
        {
            if (angle < 90 && angle >= 0)
            {
                transform.Rotate(0, 0, -5f, Space.Self);
                force.x -= 0.05f;
                force.y += 0.05f;
                angle += 5;
            }
            
        }

        if (Input.GetKeyDown(KeyCode.RightArrow))
        {
            if (velocity != 0)
            {
                velocity -= 1f;
            }
            
        }
        if (Input.GetKeyDown(KeyCode.LeftArrow))
        {
            velocity += 1;
        }

        fired = false;


    }

    void UpdatePosition(Vector3 lastPosition, Vector3 direction, double a, double s, double dt, GameObject canon)
    {
        Debug.Log("Update position called");
        var dirX = direction.x;
        var dirY = direction.y;

        var aX = dirX * a;
        var aY = dirY * a;

        var sX = dirX * s;
        var sY = dirY * s;

        var posX = lastPosition.x;
        var posY = lastPosition.y;

        var newX = posX + sX * dt + 0.5 * aX * dt * dt;
        var newY = posY + sY * dt + 0.5 * aY * dt * dt;
        canon.transform.position = new Vector3(Convert.ToSingle(newX), Convert.ToSingle(newY), 0);
        if ( newY < -4)
        {

        } else
        {
            UpdatePosition(canon.transform.position, direction, a, s + a * dt, dt + Time.time, canon);
        }
        
        
    }

    
}
