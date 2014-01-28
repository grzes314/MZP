
package arenstorf;

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
        makeODE();
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
    
    public void calculate()
    {
        rk.solve(ode, 1e-7);
    }
    
    public PlotObject getPlotData()
    {
        PlotObject po = new PlotObject();
        for (int i = 0; i < rk.getSteps(); ++i)
        {
            po.addPoint(rk.getYAt(i).get(1), rk.getYAt(i).get(2));
        }
        return po;
    }

    private void makeODE()
    {
        ode = new ODE(0, 18, y0) {
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
    
    private final Vector y0;
    private double mu1, mu2;
    private RungeKutta rk;
    private ODE ode;
}
