
package uwertura;

import arenstorf.SimData;
import java.awt.Color;
import math.matrices.Vector;
import numerics.ODE;
import plot.PlotObject;

/**
 *
 * @author Grzegorz Los
 */
public class Uwertura
{
    
    public Uwertura(double c, double t, double dt)
    {
        y0 = new Vector( new double[]{ t, dt } );
        rk = new DormandPrinceForUwertura().getMethod();
        this.c = c;
    }
    
    public void calculate(SimData sd)
    {
        makeODE(sd.time);
        rk.solve(ode, sd.tolerance);
    }
     
    private void makeODE(double time)
    {
        ode = new ODE(0, time, y0, 1) {
            @Override public Vector f(double x, Vector y) {
                return Uwertura.this.f(x, y);
            }
        };
    }
     
    private Vector f(double x, Vector y)
    {
        double dy1 = y.get(2);
        double dy2 = c * y.get(2);
        return new Vector( new double[] {dy1, dy2} );
    } 
    
    public PlotObject getPlotObject(Color c)
    {
        PlotObject po = new PlotObject("", c, PlotObject.Type.Lines);
        for (int i = 0; i < rk.getSteps(); ++i)
        {
            double x = rk.getXAt(i);
            double y = rk.getYAt(i).get(1);
            po.addPoint(x, y);
        }
        return po;
    }
        
    public PlotObject getPOForStep()
    {
        PlotObject po = new PlotObject("step size", Color.BLUE, PlotObject.Type.Lines);
        for (int i = 0; i < rk.getSteps(); ++i)
            po.addPoint(rk.getXAt(i), rk.getHAt(i));
        return po;
    }
    
    public double getLastT()
    {
        return rk.getYAt(rk.getSteps()-1).get(1);
    }
    
    private final Vector y0;
    private final RungeKuttaForUwertura rk;
    private ODE ode;
    private final double c;
}
