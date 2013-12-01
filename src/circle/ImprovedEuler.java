
package circle;

/**
 *
 * @author Grzegorz Los
 */
public class ImprovedEuler implements Method
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
        double predX, predY;
        for (int i = 0; i < n; ++i)
        {
            predY = y + p.h * x;
            predX = x - p.h * y;
            y += 0.5 * p.h * (x + predX);
            x -= 0.5 * p.h * (y + predY);
            points.addPoint(x, y);
        }
    }

    private double x = 1;
    private double y = 0;
    private Points points;
    private Parameters p;
}
