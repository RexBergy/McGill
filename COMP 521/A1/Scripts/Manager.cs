using System;
using UnityEngine;
using UnityEngine.UI;

public class Manager : MonoBehaviour
{
    public GameObject line;                             // In scene line. 
    public GameObject point;                            // In scene point. 
    public Text text;                                   // In scene text.
    double[] yValues = new double[500];
    
    


    private GameObject m_instantiated_point;            // Point we will instantiate. 


    private Color m_point_color;                        // Color and position variables. 
    private Vector3 m_point_position;
    private Color m_instantiated_point_color;
    private Vector3 m_instantiated_point_position;

    private uint counter = 0;                           // Counter variable to demonstrate text.
    System.Random random;

    // Start is called before the first frame update
    // Draws the game terrain
    void Start()
    {
       
        double j = -10.5;
        
        double curr = UnityEngine.Random.Range(0, 1000);
        for (double i = 30; i <= 150; i++)
        {
            double ran = curr + 1;
            double y = PerlinNoise(ran);
            double f = j + 0.2;
            DrawLine(new Vector3(Convert.ToSingle(j), Convert.ToSingle(PerlinNoise(Convert.ToDouble(curr))+2*Math.Cos(i/15))-2, 0), 
                new Vector3(Convert.ToSingle(f),Convert.ToSingle(y + 2 * Math.Cos((i+1)/ 15)) -2,0), Color.green, 0.2f);
            j = f;
            curr = ran;

            //Create pointers to vectors
        }
        DrawLine(new Vector3(-9.5f, -2.93f, 0), new Vector3(-9.5f, 7, 0), Color.green, 0.2f);
        


    }

    // Update is called once per frame
    void Update()
    {
        
    }

    void grid()
    {
        System.Random random = new System.Random(UnityEngine.Random.Range(1, 1000000));
        for (int i=0;i<100; i++){
            var n = random.NextDouble();
            yValues[i] = n;
        }

    }
    //Method to draw a line
    void DrawLine(Vector3 start, Vector3 end, Color color, float duration = 0.2f)
    {
        GameObject myLine = new GameObject();
        myLine.transform.position = start;
        myLine.AddComponent<LineRenderer>();
        LineRenderer lr = myLine.GetComponent<LineRenderer>();
        
        lr.SetColors(color, color);
        lr.SetWidth(0.1f, 0.1f);
        lr.SetPosition(0, start);
        lr.SetPosition(1, end);
        
    }
    // Interpolate function
    double Interpolate(double a, double b, double x)
    {
        var p = x * Math.PI;
        var f = (1 - Math.Cos(p)) * 0.6;
        return 0.5*(a * (1 - f) + b * f);
    }

    // Noise function
    double Noise(double g)
    {
        

        System.Random random = new System.Random((int)g);
        return random.NextDouble();
    }

    // Smooths out the noise
    double SmoothNoise(double f)
    {
        return Noise(f) / 2 + Noise(f - 1) / 4 + Noise(f + 1) / 4;
    }

    // Interpolates noise
    double InterpolateNoise(double x)
    {
        int i = (int)x;
        double frac = x - i;

        var f1 = SmoothNoise(i);
        var f2 = SmoothNoise(i + 1);

        return Interpolate(f1, f2, frac);
    }


    // 1D perlin noise function
    double PerlinNoise(double x)
    {
        var total = 0.0;
        var p = 0.5;
        var n = 16;

        for(int i = 0; i < n; i++)
        {
            var frequency = Math.Pow(2,i);
            var amplitude = Math.Pow(p,i);

            total = total + InterpolateNoise(x * frequency) * amplitude;
        }

        return total;
    }
}
