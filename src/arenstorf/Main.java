
package arenstorf;

import java.awt.BorderLayout;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import plot.PlotProxy;

/**
 *
 * @author Grzegorz Los
 */
public class Main extends JFrame
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run()
            {
                new Main().setVisible(true);
            }
        });
    }

    Main()
    {
        setTitle("Arenstorf");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUpInterface();
    }

    private void setUpInterface()
    {
        setLayout(new BorderLayout());
        add(PlotProxy.getPanel(), BorderLayout.CENTER);
        add(makeSouth(), BorderLayout.SOUTH);
    }

    private Component makeSouth()
    {
        controlPanel = new ControlPanel();
        controlPanel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override public void propertyChange(PropertyChangeEvent evt) {
                drawClicked();
            }
        });
        return controlPanel;
    }
    
    private void calculate()
    {
        Arenstorf a = new Arenstorf();
        a.calculate(controlPanel.getSimData());
        PlotProxy.plot(a.getPlotData());
        PlotProxy.resetLimits();
        controlPanel.drawEnabled(true);
    }
    
    private void drawClicked()
    {
        controlPanel.drawEnabled(false);
        new Thread( new Runnable() {
            @Override public void run() {
                calculate();
            }
        }).start();
    }
    
    private ControlPanel controlPanel;
}
