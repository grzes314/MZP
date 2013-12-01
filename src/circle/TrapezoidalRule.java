
package circle;

/**
 *
 * @author Grzegorz Los
 */
public class TrapezoidalRule implements Method
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
        x = 1;
        y = 0;
        points = new Points();
        points.addPoint(x, y);
    }

    private void calculate()
    {
        int n = p.getNumberOfSteps();
        double prevX, prevY, h = p.h, h2 = p.h*p.h;
        for (int i = 0; i < n; ++i)
        {
            prevX = x;
            prevY = y;
            x = (x - h*y - h2*x/4) / (1 + h2/4);
            y = h*(prevX + x)/2 + y;
            points.addPoint(x, y);
        }
    }

    private double x = 1;
    private double y = 0;
    private Points points;
    private Parameters p;
}
