
package uwertura;

import numerics.*;
import arenstorf.PeriodEndInfo;
import java.util.ArrayList;
import math.matrices.Matrix;
import math.matrices.Vector;

/**
 *
 * @author Grzegorz Los
 */
public class RungeKuttaForUwertura
{
    public RungeKuttaForUwertura(Matrix A, Vector b4, Vector b5, Vector c, boolean controlStep)
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
    
    public double getHAt(int step)
    {
        return hs.get(step);
    }
    
    public double getXAt(int step)
    {
        return xs.get(step);
    }
    
    public Vector getYAt(int step)
    {
        return ys.get(step);
    }

    public ArrayList<PeriodEndInfo> getPeriodEndInfos()
    {
        return pei;
    }

    private void solve()
    {
        for (int i = 0; i < maxSteps; ++i)
        {
            updateCoefs();
            x = x + h;
            y = calcNewY5();
            if (y.get(1) < -10)
                break;
            maybePeriodEndCorrection();
            xs.add(x);
            ys.add(y);
            hs.add(h);
            next++;
            if (x >= ode.xn)
                return;
        }
    }
    
    private void prepare()
    {
        //h = (ode.xn - ode.x0) / 1000;
        h = 1.0/512;
        ys = new ArrayList<>();
        xs = new ArrayList<>();
        hs = new ArrayList<>();
        y = ode.y0;
        x = ode.x0;
        updateKs();
        ys.add(y);
        xs.add(x);
        hs.add(h);
        next = 1;
        periodNr = 1;
        pei = new ArrayList<>();
        addPeriodEndInfo();
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
        if (controlStep)
            updateHAndKs();
        else
            updateKs();
    }

    /*private void updateH_old()
    {
        for (int i = 0; i < maxHAdjustmentsPerStep; ++i)
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
    }*/
    
    private void updateHAndKs()
    {
        for (int i = 0; i < maxHAdjustmentsPerStep; ++i)
        {
            Vector y4 = calcNewY4();
            Vector y5 = calcNewY5();
            double alfa = min(getAlfas(y4, y5));
            h *= 0.8 * alfa;
            if (h > 1.0/32) h = 1.0/32;
            updateKs();
        }
    }
    
    private boolean accept(Vector y4, Vector y5)
    {
        for (int i = 1; i <= y4.getSize(); ++i)
        {
            double a = y4.get(i), b = y5.get(i);
            if (Math.abs(a-b) > (1 + Math.abs(b)) * tol)
                return false;
        }
        return true;
    }
    
    private Vector getAlfas(Vector y4, Vector y5)
    {
        Vector res = new Vector(y4.getSize());
        for (int i = 1; i <= y4.getSize(); ++i)
            res.set(i, getAlfa(y4.get(i), y5.get(i)));
        return res;
    }
    
    private double getAlfa(double y4, double y5)
    {
        double norm = Math.abs(y4 - y5);
        //return Math.pow((1 + Math.abs(y5))*tol/norm, 0.2);
        return Math.pow(tol/norm, 0.2);
    }
    
    private double min(Vector v)
    {
        double m = v.get(1);
        for (int i = 2; i <= v.getSize(); ++i)
            m = Math.min(v.get(i), m);
        return m;
    }
    
    private void maybePeriodEndCorrection()
    {
        if (x > periodNr * ode.period)
        {
            h = periodNr * ode.period + h - x;
            x = periodNr * ode.period;
            updateKs();
            y = calcNewY5();
            addPeriodEndInfo();
            periodNr++;
            h = 1.0 / 256;
            updateKs();
        }
    }
    
    private void addPeriodEndInfo()
    {
        pei.add(new PeriodEndInfo(x, y));
    }
    
    private final Matrix A;
    private final Vector b4;
    private final Vector b5;
    private final Vector c;
    private ODE ode;
    private double tol;
    private ArrayList<Vector> ys;
    private ArrayList<Double> xs;
    private ArrayList<Double> hs;
    private ArrayList<PeriodEndInfo> pei;
    private Vector y;
    private double x;
    private int next;
    private final int s;
    private final Vector[] k;
    private double h;
    private final boolean controlStep;
    private final int maxSteps = 10000000;
    private final int maxHAdjustmentsPerStep = 10;
    private int periodNr;
}
