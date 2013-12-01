
package circle;

import math.matrices.Vector;

/**
 *
 * @author Grzegorz Los
 */
public class RungeKutta4 implements Method
{

    @Override
    public Points run(Parameters params)
    {
        if (params == null)
            throw new NullPointerException();
        p = params;
        initAuxVals();
        calculate();
        return points;
    }

    private void initAuxVals()
    {
        y.set(1, 0);
        y.set(2, 1);
        points = new Points();
        points.addPoint(1, 0);
    }

    private Vector f(Vector v)
    {
        double up = v.get(2);
        double down = -v.get(1);
        return new Vector(new double[]{up, down});
    }
    
    private void calculate()
    {
        int n = p.getNumberOfSteps();
        Vector k1, k2, k3, k4, dy;
        double h = p.h;
        for (int i = 0; i < n; ++i)
        {
            k1 = f(y).times(h);
            k2 = f( y.add(k1.times(0.5)) ).times(h);
            k3 = f( y.add(k2.times(0.5)) ).times(h);
            k4 = f( y.add(k3) ).times(h);
            dy = k1.times(1.0/6).add(k2.times(1.0/3)).add(k3.times(1.0/3)).add(k4.times(1.0/6));
            y = y.add(dy);
            points.addPoint(y.get(2), y.get(1));
        }
    }

    Vector y = new Vector(2); // coordinates, first y-coord, second x-coord
    private Points points;
    private Parameters p;
}
