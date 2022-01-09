using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Insect : MonoBehaviour
{
    GameObject myLine;
    private LineRenderer lineRenderer;
    private List<InsectPoint> insectPoints = new List<InsectPoint>();
    private float xPos;
    private float yPos;
    private Vector3 mainBody;
    private Vector3 leftWing1;
    private Vector3 leftWing2;
    private Vector3 leftWing3;
    private Vector3 leftWing4;

    private Vector3 rightWing1;
    private Vector3 rightWing2;
    private Vector3 rightWing3;
    private Vector3 rightWing4;

    private Vector3 antena1;
    private Vector3 antena2;

    private float wingBodyLength;
    private float antenaLength;
    private float wingToWingLength;

    public InsectPoint ipMain;
    private InsectPoint ipRightWing1;
    private InsectPoint ipRightWing2;
    private InsectPoint ipRightWing3;
    private InsectPoint ipRightWing4;
    private InsectPoint ipLeftWing1;
    private InsectPoint ipLeftWing2;
    private InsectPoint ipLeftWing3;
    private InsectPoint ipLeftWing4;
    private InsectPoint ipAntena1;
    private InsectPoint ipAntena2;



    // Start is called before the first frame update
    void Start()
    {
        

        xPos = Random.Range(-8.0f, 0.0f);
        yPos = Random.Range(-1.0f, 3.0f);

    }

    // Update is called once per frame
    void Update()
    {
        

    }

    // Function that simulates perlin noise for collision detection
    // Much simpler
    private float yPosition(float x)
    {

        return 2f * Mathf.Cos((x / 15f) - 2.78f) - 1.45f;
    }

    
    // Creates an insect
    public void CreateInsect()
    {

        myLine = new GameObject();
        myLine.AddComponent<LineRenderer>();
        this.lineRenderer = myLine.GetComponent<LineRenderer>();
        Vector3 mainBody = new Vector3(Random.Range(-8.0f, 0.0f), Random.Range(-1.0f, 3.0f), 0);
        

        Vector3 leftWing1 = new Vector3(mainBody.x - 1.0f / 2, mainBody.y - 0.1f / 2, mainBody.z);
        Vector3 leftWing2 = new Vector3(mainBody.x - 0.7f / 2, mainBody.y + 0.4f / 2, mainBody.z);
        Vector3 leftWing3 = new Vector3(mainBody.x - 0.4f / 2, mainBody.y + 0.7f / 2, mainBody.z);
        Vector3 leftWing4 = new Vector3(mainBody.x - 0.1f / 2, mainBody.y + 1.0f / 2, mainBody.z);

        Vector3 rightWing1 = new Vector3(mainBody.x + 0.1f / 2, mainBody.y - 1.0f / 2, mainBody.z);
        Vector3 rightWing2 = new Vector3(mainBody.x + 0.4f / 2, mainBody.y - 0.7f / 2, mainBody.z);
        Vector3 rightWing3 = new Vector3(mainBody.x + 0.7f / 2, mainBody.y - 0.4f / 2, mainBody.z);
        Vector3 rightWing4 = new Vector3(mainBody.x + 1.0f / 2, mainBody.y + 0.1f / 2, mainBody.z);

        Vector3 antena1 = new Vector3(mainBody.x + 6 * 0.05f, mainBody.y + 6 * 0.03f, mainBody.z);
        Vector3 antena2 = new Vector3(mainBody.x + 6 * 0.03f, mainBody.y + 6 * 0.05f, mainBody.z);

        wingBodyLength = (leftWing1 - mainBody).magnitude;
        wingToWingLength = (leftWing1 - leftWing2).magnitude;
        antenaLength = (antena1 - mainBody).magnitude;
        
        ipAntena1 = new InsectPoint(antena1);
        ipAntena2 = new InsectPoint(antena2);
        ipLeftWing1 = new InsectPoint(leftWing1);
        ipLeftWing2 = new InsectPoint(leftWing2);
        ipLeftWing3 = new InsectPoint(leftWing3);
        ipLeftWing4 = new InsectPoint(leftWing4);
        ipMain = new InsectPoint(mainBody);
        ipRightWing1 = new InsectPoint(rightWing1);
        ipRightWing2 = new InsectPoint(rightWing2);
        ipRightWing3 = new InsectPoint(rightWing3);
        ipRightWing4 = new InsectPoint(rightWing4);
        
    }

    // Function that dictates the movement and restrictions of mainBody then applies constraints
    public void Movement()
    {
        if (this.ipMain.currPos.y < yPosition(5f * ipMain.currPos.x + 30f) || this.ipMain.currPos.x < -9.5f
            || ipMain.currPos.y > 6f)
        {
            
            Destroy(myLine);
            CreateInsect();
            
        }
        var current = this.ipMain;
        current.currPos = new Vector3(current.currPos.x + 0.000001f*Mathf.Cos(0.7f * Time.time),
            current.currPos.y + 0.000001f *Mathf.Cos(0.7f * Time.time), 0);
        Vector3 velocity = current.currPos - current.oldPos;
        current.oldPos = current.currPos;
        current.currPos += velocity;
       
        this.ipMain = current;
        
        ApplyConstraint();
    }


    private void ApplyConstraint()
    {
        // LeftWing1 and MainBody
        var dist = (ipMain.currPos - ipLeftWing1.currPos).magnitude;
        var diffAbs = Mathf.Abs(wingBodyLength - dist);
        var diff = (wingBodyLength - dist) / dist * 0.05f;
        var offset = dist * diff * 0.5f;
        Vector3 changeDir = Vector3.zero;
        if (dist > diffAbs)
        {
            changeDir = (ipMain.currPos - ipLeftWing1.currPos).normalized;
        }
        else if (dist < diffAbs)
        {
            changeDir = (ipLeftWing1.currPos - ipMain.currPos).normalized;
        }
        ipLeftWing1.currPos -= offset * changeDir;

        // LeftWing2 and MainBody
        dist = (ipMain.currPos - ipLeftWing2.currPos).magnitude;
        diffAbs = Mathf.Abs(wingBodyLength - dist);
        diff = (wingBodyLength - dist) / dist * 0.05f;
        offset = dist * diff * 0.5f;
        
        if (dist > diffAbs)
        {
            changeDir = (ipMain.currPos - ipLeftWing2.currPos).normalized;
        }
        else if (dist < diffAbs)
        {
            changeDir = (ipLeftWing2.currPos - ipMain.currPos).normalized;
        }
        ipLeftWing2.currPos -= offset * changeDir;

        // LeftWing3 and MainBody
        dist = (ipMain.currPos - ipLeftWing3.currPos).magnitude;
        diffAbs = Mathf.Abs(wingBodyLength - dist);
        diff = (wingBodyLength - dist) / dist * 0.05f;
        offset = dist * diff * 0.5f;
        
        if (dist > diffAbs)
        {
            changeDir = (ipMain.currPos - ipLeftWing3.currPos).normalized;
        }
        else if (dist < diffAbs)
        {
            changeDir = (ipLeftWing3.currPos - ipMain.currPos).normalized;
        }
        ipLeftWing3.currPos -= offset * changeDir;

        // LeftWing4 and MainBody
        dist = (ipMain.currPos - ipLeftWing4.currPos).magnitude;
        diffAbs = Mathf.Abs(wingBodyLength - dist);
        diff = (wingBodyLength - dist) / dist * 0.05f;
        offset = dist * diff * 0.5f;
        
        if (dist > diffAbs)
        {
            changeDir = (ipMain.currPos - ipLeftWing4.currPos).normalized;
        }
        else if (dist < diffAbs)
        {
            changeDir = (ipLeftWing4.currPos - ipMain.currPos).normalized;
        }
        ipLeftWing4.currPos -= offset * changeDir;

        // RightWing1 and MainBody
        dist = (ipMain.currPos - ipRightWing1.currPos).magnitude;
        diffAbs = Mathf.Abs(wingBodyLength - dist);
        diff = (wingBodyLength - dist) / dist * 0.05f;
        offset = dist * diff * 0.5f;
        
        if (dist > diffAbs)
        {
            changeDir = (ipMain.currPos - ipRightWing1.currPos).normalized;
        }
        else if (dist < diffAbs)
        {
            changeDir = (ipRightWing1.currPos - ipMain.currPos).normalized;
        }
        ipRightWing1.currPos -= offset * changeDir;

        // RightWing2 and MainBody
        dist = (ipMain.currPos - ipRightWing2.currPos).magnitude;
        diffAbs = Mathf.Abs(wingBodyLength - dist);
        diff = (wingBodyLength - dist) / dist * 0.05f;
        offset = dist * diff * 0.5f;
        
        if (dist > diffAbs)
        {
            changeDir = (ipMain.currPos - ipRightWing2.currPos).normalized;
        }
        else if (dist < diffAbs)
        {
            changeDir = (ipRightWing2.currPos - ipMain.currPos).normalized;
        }
        ipRightWing2.currPos -= offset * changeDir;

        // RightWing3 and MainBody
        dist = (ipMain.currPos - ipRightWing3.currPos).magnitude;
        diffAbs = Mathf.Abs(wingBodyLength - dist);
        diff = (wingBodyLength - dist) / dist * 0.05f;
        offset = dist * diff * 0.5f;
        
        if (dist > diffAbs)
        {
            changeDir = (ipMain.currPos - ipRightWing3.currPos).normalized;
        }
        else if (dist < diffAbs)
        {
            changeDir = (ipRightWing3.currPos - ipMain.currPos).normalized;
        }
        ipRightWing3.currPos -= offset * changeDir;

        // RightWing4 and MainBody
        dist = (ipMain.currPos - ipRightWing4.currPos).magnitude;
        diffAbs = Mathf.Abs(wingBodyLength - dist);
        diff = (wingBodyLength - dist) / dist * 0.05f;
        offset = dist * diff * 0.5f;
        
        if (dist > diffAbs)
        {
            changeDir = (ipMain.currPos - ipRightWing4.currPos).normalized;
        }
        else if (dist < diffAbs)
        {
            changeDir = (ipRightWing4.currPos - ipMain.currPos).normalized;
        }
        ipRightWing4.currPos -= offset * changeDir;

        // Antena1 and MainBody
        dist = (ipMain.currPos - ipAntena1.currPos).magnitude;
        diffAbs = Mathf.Abs(antenaLength - dist);
        diff = (antenaLength - dist) / dist * 0.05f;
        offset = dist * diff * 0.5f;
        
        if (dist > diffAbs)
        {
            changeDir = (ipMain.currPos - ipAntena1.currPos).normalized;
        }
        else if (dist < diffAbs)
        {
            changeDir = (ipAntena1.currPos - ipMain.currPos).normalized;
        }
        ipAntena1.currPos -= offset * changeDir;

        // Antena2 and MainBody
        dist = (ipMain.currPos - ipAntena2.currPos).magnitude;
        diffAbs = Mathf.Abs(antenaLength - dist);
        diff = (antenaLength - dist) / dist * 0.05f;
        offset = dist * diff * 0.5f;
        
        if (dist > diffAbs)
        {
            changeDir = (ipMain.currPos - ipAntena2.currPos).normalized;
        }
        else if (dist < diffAbs)
        {
            changeDir = (ipAntena2.currPos - ipMain.currPos).normalized;
        }
        ipAntena2.currPos -= offset * changeDir;

        // RightWing1 and RightWing2
        dist = (ipRightWing1.currPos - ipRightWing2.currPos).magnitude;
        diffAbs = Mathf.Abs(wingToWingLength - dist);
        diff = (wingToWingLength - dist) / dist * 0.05f;
        offset = dist * diff * 0.5f;
        
        if (dist > diffAbs)
        {
            changeDir = (ipRightWing1.currPos - ipRightWing2.currPos).normalized;
        }
        else if (dist < diffAbs)
        {
            changeDir = (ipRightWing2.currPos - ipRightWing1.currPos).normalized;
        }
        ipRightWing1.currPos += offset * changeDir;
        ipRightWing2.currPos -= offset * changeDir;

        // RightWing2 and RightWing3
        dist = (ipRightWing2.currPos - ipRightWing3.currPos).magnitude;
        diffAbs = Mathf.Abs(wingToWingLength - dist);
        diff = (wingToWingLength - dist) / dist * 0.05f;
        offset = dist * diff * 0.5f;
        
        if (dist > diffAbs)
        {
            changeDir = (ipRightWing2.currPos - ipRightWing3.currPos).normalized;
        }
        else if (dist < diffAbs)
        {
            changeDir = (ipRightWing3.currPos - ipRightWing2.currPos).normalized;
        }
        ipRightWing2.currPos += offset * changeDir;
        ipRightWing3.currPos -= offset * changeDir;

        // RightWing3 and RightWing4
        dist = (ipRightWing3.currPos - ipRightWing4.currPos).magnitude;
        diffAbs = Mathf.Abs(wingToWingLength - dist);
        diff = (wingToWingLength - dist) / dist * 0.05f;
        offset = dist * diff * 0.5f;
        
        if (dist > diffAbs)
        {
            changeDir = (ipRightWing3.currPos - ipRightWing4.currPos).normalized;
        }
        else if (dist < diffAbs)
        {
            changeDir = (ipRightWing4.currPos - ipRightWing3.currPos).normalized;
        }
        ipRightWing3.currPos += offset * changeDir;
        ipRightWing4.currPos -= offset * changeDir;

        // LeftWing1 and LeftWing2
        dist = (ipLeftWing1.currPos - ipLeftWing2.currPos).magnitude;
        diffAbs = Mathf.Abs(wingToWingLength - dist);
        diff = (wingToWingLength - dist) / dist * 0.05f;
        offset = dist * diff * 0.5f;
        
        if (dist > diffAbs)
        {
            changeDir = (ipLeftWing1.currPos - ipLeftWing2.currPos).normalized;
        }
        else if (dist < diffAbs)
        {
            changeDir = (ipLeftWing2.currPos - ipLeftWing1.currPos).normalized;
        }
        ipLeftWing1.currPos += offset * changeDir;
        ipLeftWing2.currPos -= offset * changeDir;

        // LeftWing2 and LeftWing3
        dist = (ipLeftWing2.currPos - ipLeftWing3.currPos).magnitude;
        diffAbs = Mathf.Abs(wingToWingLength - dist);
        diff = (wingToWingLength - dist) / dist * 0.05f;
        offset = dist * diff * 0.5f;
        
        if (dist > diffAbs)
        {
            changeDir = (ipLeftWing2.currPos- ipLeftWing3.currPos).normalized;
        }
        else if (dist < diffAbs)
        {
            changeDir = (ipLeftWing3.currPos - ipLeftWing2.currPos).normalized;
        }
        ipLeftWing2.currPos += offset * changeDir;
        ipLeftWing3.currPos -= offset * changeDir;

        // LeftWing3 and LeftWing4
        dist = (ipLeftWing3.currPos - ipLeftWing4.currPos).magnitude;
        diffAbs = Mathf.Abs(wingToWingLength - dist);
        diff = (wingToWingLength - dist) / dist * 0.05f;
        offset = dist * diff * 0.5f;
        
        if (dist > diffAbs)
        {
            changeDir = (ipLeftWing3.currPos - ipLeftWing4.currPos).normalized;
        }
        else if (dist < diffAbs)
        {
            changeDir = (ipLeftWing4.currPos - ipLeftWing3.currPos).normalized;
        }
        ipLeftWing3.currPos += offset * changeDir;
        ipLeftWing4.currPos -= offset * changeDir;

        // LeftWing4 and RightWing4
        dist = (ipLeftWing4.currPos - ipRightWing4.currPos).magnitude;
        diffAbs = Mathf.Abs(wingToWingLength - dist);
        diff = (wingBodyLength - dist) / dist * 0.05f;
        offset = dist * diff * 0.5f;
        
        if (dist > diffAbs)
        {
            changeDir = (ipLeftWing4.currPos - ipRightWing4.currPos).normalized;
        }
        else if (dist < diffAbs)
        {
            changeDir = (ipRightWing4.currPos - ipLeftWing4.currPos).normalized;
        }
        ipLeftWing4.currPos += offset * changeDir;
        ipRightWing4.currPos -= offset * changeDir;

        // LeftWing1 and RightWing1
        dist = (ipLeftWing1.currPos - ipRightWing1.currPos).magnitude;
        diffAbs = Mathf.Abs(wingToWingLength - dist);
        diff = (wingBodyLength - dist) / dist * 0.05f;
        offset = dist * diff * 0.5f;
        
        if (dist > diffAbs)
        {
            changeDir = (ipLeftWing1.currPos - ipRightWing1.currPos).normalized;
        }
        else if (dist < diffAbs)
        {
            changeDir = (ipRightWing1.currPos - ipLeftWing1.currPos).normalized;
        }
        ipLeftWing1.currPos += offset * changeDir;
        ipRightWing1.currPos -= offset * changeDir;
        
    }

    // InsectPoint to represents points
    public struct InsectPoint
    {
        public Vector3 currPos;
        public Vector3 oldPos;

        public InsectPoint(Vector3 pos)
        {
            this.currPos = pos;
            this.oldPos = pos;
        }
    }

    public List<InsectPoint> GetInsectPoints()
    {
        return this.insectPoints;
    }

    // Prints the insect on the screen with the correct positions
    public void Draw()
    {
        
        float lineWidth = 0.05f;
        lineRenderer.startWidth = lineWidth;
        lineRenderer.endWidth = lineWidth;
        Vector3[] positions = new Vector3[30];


        positions[0] = ipMain.currPos;
        positions[1] = ipLeftWing1.currPos;
        positions[2] = ipMain.currPos;
        positions[3] = ipLeftWing2.currPos;
        positions[4] = ipMain.currPos;
        positions[5] = ipLeftWing3.currPos;
        positions[6] = ipMain.currPos;
        positions[7] = ipLeftWing4.currPos;
        positions[8] = ipMain.currPos;
        positions[9] = ipRightWing1.currPos;
        positions[10] = ipMain.currPos;
        positions[11] = ipRightWing2.currPos;
        positions[12] = ipMain.currPos;
        positions[13] = ipRightWing3.currPos;
        positions[14] = ipMain.currPos;
        positions[15] = ipRightWing4.currPos;
        positions[16] = ipMain.currPos;
        positions[17] = ipRightWing1.currPos;
        positions[18] = ipRightWing2.currPos;
        positions[19] = ipRightWing3.currPos;
        positions[20] = ipRightWing4.currPos;
        positions[21] = ipMain.currPos;
        positions[22] = ipLeftWing1.currPos;
        positions[23] = ipLeftWing2.currPos;
        positions[24] = ipLeftWing3.currPos;
        positions[25] = ipLeftWing4.currPos;
        positions[26] = ipMain.currPos;
        positions[27] = ipAntena1.currPos;
        positions[28] = ipMain.currPos;
        positions[29] = ipAntena2.currPos;
       

        lineRenderer.positionCount = positions.Length;
        lineRenderer.SetPositions(positions);


    }

    
}
