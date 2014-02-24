
package uwertura;

import arenstorf.SimData;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import plot.PlotPanel;

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
        setTitle("Uwertura");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUpInterface();
    }

    private void setUpInterface()
    {
        setLayout(new BorderLayout());
        paneResults = new JTabbedPane();
        paneResults.add("plot", makePlotPanel());
        paneResults.add("step", makeStepPanel());
        paneResults.add("results", makePeriodEndPanel());
        add(paneResults, BorderLayout.CENTER);
        add(makeSouth(), BorderLayout.SOUTH);
    }
    
    private JPanel makePlotPanel()
    {
        plotSol = new PlotPanel();
        return plotSol;
    }
    
    private JPanel makeStepPanel()
    {
        plotH = new PlotPanel();
        return plotH;
    }
    
    private JComponent makePeriodEndPanel()
    {
        resultInfo = new JTextArea();
        return new JScrollPane(resultInfo);
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
        double c = controlPanel.getC();
        int tries = controlPanel.getTries();
        double guess = -1.0/c/c/c;
        double upperBound = 0;
        double lowerBound = 2*guess;
        plotSol.clear();
        resultInfo.setText("");
        for (int i = 0; i < tries; ++i)
        {
            Uwertura u = new Uwertura(c, 1, guess);
            u.calculate(new SimData(10-9, 1));
            plotSol.addPlotObject(u.getPlotObject(getColor(i)));
            plotH.clear();
            plotH.addPlotObject(u.getPOForStep());
            appendResultInfo(guess, u.getLastT());
            if (u.getLastT() > 0) // poczatkowo byla ustawiona zbyt duza (mala co do modulu) pochodna
                upperBound = guess;      
            else lowerBound = guess;
            
                guess = (lowerBound + upperBound) / 2;
        }
        plotSol.setMinY(-1);
        plotSol.setMaxX(1);
        plotSol.repaint();
        controlPanel.drawEnabled(true);
    }
    
    private static Color getColor(int i)
    {
        if (i < colors.length)
            return colors[i];
        else return Color.BLACK;
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
    
    private void appendResultInfo(double guess, double lastT)
    {
        resultInfo.append("initial guess = " + guess);
        resultInfo.append("\nT(1) = " + lastT);
        resultInfo.append("\n\n");
    }
    
    private ControlPanel controlPanel;
    private JTabbedPane paneResults;
    private PlotPanel plotSol;
    private PlotPanel plotH;
    private JTextArea resultInfo;
    private final double initialGuess = -0.001;
    private static final Color[] colors = new Color[]{
        Color.BLUE, Color.CYAN, Color.DARK_GRAY, Color.GRAY, Color.GREEN, Color.LIGHT_GRAY,
        Color.MAGENTA, Color.ORANGE, Color.PINK, Color.RED, Color.YELLOW};

}
