
package arenstorf;

import java.awt.Color;
import java.util.ArrayList;
import math.matrices.Vector;
import numerics.DormandPrince;
import numerics.ODE;
import numerics.RungeKutta;
import plot.PlotObject;

/**
 *
 * @author Grzegorz Los
 */
public class Arenstorf
{
    public Arenstorf(double y1, double y2, double dy1, double dy2, double mu1)
    {
        y0 = new Vector( new double[]{ y1, y2, dy1, dy2 } );
        this.mu1 = mu1;
        mu2 = 1 - mu1;
        rk = new DormandPrince().getMethod();
    }
    
    public Arenstorf()
    {
        this( 0.994,
              0,
              0,
              -2.00158510637908252240537862224,
               0.012277471
            );
    }
    
    public void calculate(SimData sd)
    {
        makeODE(sd.time);
        rk.solve(ode, sd.tolerance);
    }
    
    public PlotObject[] getPlotData()
    {
        ArrayList<PlotObject> pos = new ArrayList<>();
        int ind = 0;
        for (int rot = 0; ; ++rot)
        {
            PlotObject po = initPlotObjectForNextRotation(rot);
            ind = insertPointsFromOneRotation(ind, (rot+1)*period, po); 
            if (po.getSize() == 0) break;
            else pos.add(po);
        }
        return plotObjectsToArray(pos);
    }
    
    private PlotObject[] plotObjectsToArray(ArrayList<PlotObject> pos)
    {
        PlotObject[] arr = new PlotObject[pos.size()];
        for (int i = 0; i < pos.size(); ++i)
            arr[i] = pos.get(i);
        return arr;
    }
    
    private int insertPointsFromOneRotation(int ind, double time, PlotObject po)
    {
        for (int i = ind; i < rk.getSteps(); ++i)
        {
            double t = rk.getXAt(i);
            double x = rk.getYAt(i).get(1);
            double y = rk.getYAt(i).get(2);
            if (t > time)
                return i;
            po.addPoint(x, y);
        }
        return rk.getSteps();
    }
    
    private PlotObject initPlotObjectForNextRotation(int rot)
    {
        if (rot == 0) return new PlotObject("First rotation", Color.BLACK, PlotObject.Type.Points);
        if (rot == 1) return new PlotObject("Second rotation", Color.BLUE, PlotObject.Type.Points);
        if (rot == 2) return new PlotObject("Third rotation", Color.GREEN, PlotObject.Type.Points);
        if (rot == 3) return new PlotObject("Fourth rotation", Color.RED, PlotObject.Type.Points);
        if (rot == 4) return new PlotObject("Fifth rotation", Color.ORANGE, PlotObject.Type.Points);
        else return new PlotObject("Next rotation", Color.GRAY, PlotObject.Type.Points);
    }

    public PlotObject getPOForStep()
    {
        PlotObject po = new PlotObject("step size", Color.BLUE, PlotObject.Type.Lines);
        for (int i = 0; i < rk.getSteps(); ++i)
            po.addPoint(rk.getXAt(i), rk.getHAt(i));
        return po;
    }

    private void makeODE(double time)
    {
        ode = new ODE(0, time, y0, period) {
            @Override public Vector f(double x, Vector y) {
                return Arenstorf.this.f(x, y);
            }
        };
    }
    
    private double sqr(double x)
    {
        return x*x;
    }

    private Vector f(double x, Vector y)
    {
        double y1 = y.get(1), y2 = y.get(2), y3 = y.get(3), y4 = y.get(4);
        double D1 = Math.pow( sqr(y1 + mu1) + sqr(y2), 1.5 );
        double D2 = Math.pow( sqr(y1 - mu2) + sqr(y2), 1.5 );
        double dy1 = y3;
        double dy2 = y4;
        double dy3 = y1 + 2*y4 - mu2 * (y1 + mu1) / D1 - mu1 * (y1 - mu2) / D2;
        double dy4 = y2 - 2*y3 - mu2 * y2 / D1 - mu1 * y2 / D2;
        return new Vector( new double[] {dy1, dy2, dy3, dy4} );
    }
    
    public ArrayList<PeriodEndInfo> getResultInfo()
    {
        return rk.getPeriodEndInfos();
    }
    
    private final Vector y0;
    private double mu1, mu2;
    private RungeKutta rk;
    private ODE ode;
    private final double period = 17.06521656015;
}
