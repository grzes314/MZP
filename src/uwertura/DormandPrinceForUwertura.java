
package uwertura;

import math.matrices.Matrix;
import math.matrices.Vector;

/**
 *
 * @author Grzegorz Los
 */
public class DormandPrinceForUwertura
{
    public RungeKuttaForUwertura getMethod()
    {
        Matrix A = makeA();
        Vector b4 = makeB4();
        Vector b5 = makeB5();
        Vector c = makeC();
        return new RungeKuttaForUwertura(A, b4, b5, c, true);
    }

    private Matrix makeA()
    {
        double[][] mat = new double[][] {
            {0, 0, 0, 0, 0, 0, 0},
            {1.0/5, 0, 0, 0, 0, 0, 0},
            {3.0/40, 9.0/40, 0, 0, 0, 0, 0},
            {44.0/45, -56.0/15, 32.0/9, 0, 0, 0, 0},
            {19372.0/6561, -25360.0/2187, 64448.0/6561, -212.0/729, 0, 0, 0},
            {9017.0/3168, -355.0/33, 46732.0/5247, 49.0/176, -5103.0/18656, 0, 0},
            {35.0/384, 0, 500.0/1113, 125.0/192, -2187.0/6784, 11.0/84, 0},
        };
        return new Matrix(mat);
    }

    private Vector makeB4()
    {
        double[] vec = new double[] {
            5179.0/57600, 0, 7571.0/16695, 393.0/640, -92097.0/339200, 187.0/2100, 1.0/40
        };
        return new Vector(vec);
    }

    private Vector makeB5()
    {
        double[] vec = new double[] {
            35.0/384, 0, 500.0/1113, 125.0/192, -2187.0/6784, 11.0/84, 0
        };
        return new Vector(vec);
    }
    
    private Vector makeC()
    {
        double[] vec = new double[] {
            0, 0.2, 0.3, 0.8, 8.0/9, 1, 1
        };
        return new Vector(vec);
    }

}
