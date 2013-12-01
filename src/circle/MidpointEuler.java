
package circle;

/**
 *
 * @author Grzegorz Los
 */
public class MidpointEuler implements Method
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
        double midX, midY;
        for (int i = 0; i < n; ++i)
        {
            midY = y + 0.5 * p.h * x;
            midX = x - 0.5 * p.h * y;
            y += p.h * midX;
            x -= p.h * midY;
            points.addPoint(x, y);
        }
    }

    private double x = 1;
    private double y = 0;
    private Points points;
    private Parameters p;
}
