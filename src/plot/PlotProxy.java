
package plot;

import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author Grzegorz Los
 */
public class PlotProxy
{
    public static PlotPanel getPanel() {
        return panel;
    }
    
    public static void plot(PlotObject po) {
        clear();
        addPoints(po);
    }
    
    public static void plot(PlotObject[] pos) {
        clear();
        for (PlotObject po: pos)
            addPoints(po);
    }
    
    public static void plot(double[] xs, double[] ys) {
        clear();
        addPoints(xs, ys);
    }
    
    public static void plot(Collection<Double> xs, Collection<Double> ys) {
        clear();
        addPoints(xs, ys);
    }    

    private static void clear() {
        panel.clear();
    }

    public static void addPoints(PlotObject po) {
        panel.addPlotObject(po);
    }

    public static void addPoints(double[] xs, double[] ys)
    {
        int l = Math.min(xs.length, ys.length);
        PlotObject po = new PlotObject(optionsPO);
        for (int i = 0; i < l; ++i)
            po.addPoint(xs[i], ys[i]);
        panel.addPlotObject(po);
    }
        
    public static void addPoints(Collection<Double> xs, Collection<Double> ys)
    {
        PlotObject po = new PlotObject(optionsPO);
        Iterator<Double> xit = xs.iterator();
        Iterator<Double> yit = ys.iterator();
        while (xit.hasNext() && yit.hasNext())
            po.addPoint(xit.next(), yit.next());
        panel.addPlotObject(po);
    }

    public static PlotObject getOptionsPO()
    {
        return optionsPO;
    }

    public static void setOptionsPO(PlotObject optionsPO)
    {
        PlotProxy.optionsPO = optionsPO;
    }

    public static void resetLimits()
    {
        panel.resetLimits();
    }
    
    private static PlotObject optionsPO = new PlotObject();
    private static final PlotPanel panel = new PlotPanel();
}
