
package numerics;

import java.util.ArrayList;
import math.matrices.Matrix;
import math.matrices.Vector;

/**
 *
 * @author Grzegorz Los
 */
public class RungeKutta
{
    public RungeKutta(Matrix A, Vector b4, Vector b5, Vector c, boolean controlStep)
    {
        checkArgs(A, b4, b5, c);
        this.A = A;
        this.b4 = b4;
        this.b5 = b5;
        this.c = c;
        this.controlStep = controlStep;
        s = A.getRows();
        k = new Vector[s+1];
    }
    
    public void solve(ODE ode, double tol)
    {
        this.ode = ode;
        this.tol = tol;
        prepare();
        solve();
    }
    
    public int getSteps()
    {
        return next;
    }
    
    public double getXAt(int step)
    {
        return xs.get(step);
    }
    
    public Vector getYAt(int step)
    {
        return ys.get(step);
    }
    
    private void solve()
    {
        for (int i = 0; i < 10000000; ++i)
        {
            updateCoefs();
            x = x + h;
            y = calcNewY5();
            xs.add(x);
            ys.add(y);
            next++;
            if (x > ode.xn)
                return;
        }
    }
    
    private void prepare()
    {
        ys = new ArrayList<>();
        xs = new ArrayList<>();
        y = ode.y0;
        x = ode.x0;
        ys.add(y);
        xs.add(x);
        next = 1;
        //h = (ode.xn - ode.x0) / 1000;
        h = 1.0/64;
    }
    
    private void checkArgs(Matrix A, Vector b4, Vector b5, Vector c)
    {
        if (    !A.isSquare()
                || A.getRows() != c.getRows()
                || A.getCols() != b4.getRows()
                || A.getCols() != b5.getRows()
           ) throw new RuntimeException("Invalid arguments");
    }
    
    private void updateKs()
    {
        for (int i = 1; i <= s; ++i)
            updateK(i);
    }

    private void updateK(int i)
    {
        double x_tmp = x + c.get(i)*h;
        Vector y_tmp = new Vector(y);
        for (int j = 1; j < i; ++j)
            y_tmp = y_tmp.add(k[j].times(h*A.get(i, j)));
        k[i] = ode.f(x_tmp, y_tmp);
    }
    

    private Vector calcNewY4()
    {
        Vector newY = new Vector(y);
        for (int j = 1; j <= s; ++j)
            newY = newY.add(k[j].times(h*b4.get(j)));
        return newY;
    }
    
    private Vector calcNewY5()
    {
        Vector newY = new Vector(y);
        for (int j = 1; j <= s; ++j)
            newY = newY.add(k[j].times(h*b5.get(j)));
        return newY;
    }

    private void updateCoefs()
    {
        updateKs();
        if (controlStep)
            updateH();
    }

    private void updateH()
    {
        for (int i = 0; i < 10; ++i)
        {
            Vector y4 = calcNewY4();
            Vector y5 = calcNewY5();
            double norm = y4.add(y5.times(-1)).norm();
            if (norm < tol && norm > tol/4) //step is reasonable
                return;                     //no need to update h
            double alfa = 0.8 * Math.pow(tol/norm, 0.2);
            h *= alfa;
            if (h < hmin)
            {
                h = hmin;
                updateKs();
                return;
            }
            updateKs();
        }
    }
    
    private Matrix A;
    private Vector b4;
    private Vector b5;
    private Vector c;
    private ODE ode;
    private double tol;
    private ArrayList<Vector> ys;
    private ArrayList<Double> xs;
    private Vector y;
    private double x;
    private int next;
    private int s;
    private Vector[] k;
    private double h;
    private double hmin = 1.0/8192;
    private final boolean controlStep;
}
