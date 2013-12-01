
package circle;

import java.util.ArrayList;

/**
 *
 * @author Grzegorz Los
 */
public class Points
{

    public ArrayList<Double> getXs()
    {
        return xs;
    }

    public ArrayList<Double> getYs()
    {
        return ys;
    }
    
    public void addPoint(double x, double y)
    {
        xs.add(x);
        ys.add(y);
    }
    
    private ArrayList<Double> xs = new ArrayList<>();
    private ArrayList<Double> ys = new ArrayList<>();
}
