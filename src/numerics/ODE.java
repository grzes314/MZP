
package numerics;

import math.matrices.Vector;


/**
 *
 * @author Grzegorz Los
 */
public abstract class ODE
{
    public ODE(double x0, double xn, Vector y0, double period)
    {
        this.y0 = y0;
        this.x0 = x0;
        this.xn = xn;
        this.period = period;
    }
    abstract public Vector f(double x, Vector y);
    public final Vector y0;
    public final double x0;
    public final double xn;
    public final double period;
}
