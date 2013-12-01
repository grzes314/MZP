
package circle;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import plot.PlotObject;
import plot.PlotProxy;

/**
 *
 * @author Grzegorz Los
 */
public class CircleApp extends JFrame
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run()
            {
                new CircleApp().setVisible(true);
            }
        });
    }

    public CircleApp()
    {
        setShit();
        setUpInterface();
        setMenu();
        PlotProxy.setOptionsPO(new PlotObject("Approximation", Color.RED, PlotObject.Type.Lines));
        draw();
    }
    
    private void setShit()
    {        
        setTitle("Circle");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    private void setMenu()
    {
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem quit = new JMenuItem("Quit");
        quit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                CircleApp.this.dispose();
            }
        });
        file.add(quit);
        bar.add(file);
        this.setJMenuBar(bar);
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
        final Parameters params = mainPanel.getParams();
        final Method method = mainPanel.getMethod();
        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception
            {
                mainPanel.block(true);
                Points points = method.run(params);
                PlotProxy.plot(points.getXs(), points.getYs());
                PlotProxy.addPoints(getCircle());
                PlotProxy.resetLimits();
                //revalidate();
                //repaint();
                mainPanel.block(false);
                return null;
            }
        }.execute();
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
