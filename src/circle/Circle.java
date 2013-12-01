
package circle;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JApplet;
import plot.PlotObject;
import plot.PlotProxy;

/**
 *
 * @author Grzegorz Los
 */
public class Circle extends JApplet
{

    /**
     * Initialization method that will be called after the applet is loaded into the browser.
     */
    @Override
    public void init()
    {
        setUpInterface();
        PlotProxy.setOptionsPO(new PlotObject("Approximation", Color.RED, PlotObject.Type.Lines));
        draw();
    }
    

    private void setUpInterface()
    {
        mainPanel = new MainPanel();
        mainPanel.addPropertyChangeListener(new PropertyChangeListener() { 
            @Override public void propertyChange(PropertyChangeEvent evt) {
                draw();
            }
        });
        add(mainPanel);
    }
    
    private void draw()
    {
        Parameters params = mainPanel.getParams();
        Method method = mainPanel.getMethod();
        Points points = method.run(params);
        PlotProxy.plot(points.getXs(), points.getYs());
        PlotProxy.addPoints(getCircle());
        PlotProxy.resetLimits();
        revalidate();
        repaint();
    }
    
    private PlotObject getCircle()
    {
        PlotObject po = new PlotObject("", Color.BLUE, PlotObject.Type.Lines);
        for (int i = 0; i < 1000; ++i)
        {
            double t = (double)i / 500 * Math.PI;
            po.addPoint(Math.cos(t), Math.sin(t));
        }
        return po;
    }
    
    MainPanel mainPanel;
}
