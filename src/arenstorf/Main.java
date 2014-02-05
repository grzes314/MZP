
package arenstorf;

import java.awt.BorderLayout;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JFrame;
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
        setTitle("Arenstorf");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUpInterface();
    }

    private void setUpInterface()
    {
        setLayout(new BorderLayout());
        paneResults = new JTabbedPane();
        paneResults.add("orbit", makeOrbitPanel());
        paneResults.add("step", makeStepPanel());
        paneResults.add("results", makePeriodEndPanel());
        add(paneResults, BorderLayout.CENTER);
        add(makeSouth(), BorderLayout.SOUTH);
    }
    
    private JPanel makeOrbitPanel()
    {
        plotOrbit = new PlotPanel();
        return plotOrbit;
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
        Arenstorf a = new Arenstorf();
        a.calculate(controlPanel.getSimData());
        plotOrbit.clear();
        plotOrbit.addPlotObjects(a.getPlotData());
        plotH.clear();
        plotH.addPlotObject(a.getPOForStep());
        writeResultInfo(a.getResultInfo());
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

    private void writeResultInfo(ArrayList<PeriodEndInfo> infos)
    {
        resultInfo.setText("");
        for (PeriodEndInfo pei: infos)
            appendResultInfo(pei);
    }

    private void appendResultInfo(PeriodEndInfo pei)
    {
        resultInfo.append("time = " + pei.x);
        resultInfo.append("\nx = " + pei.y.get(1));
        resultInfo.append("\ny = " + pei.y.get(2));
        resultInfo.append("\ndx = " + pei.y.get(3));
        resultInfo.append("\ndy = " + pei.y.get(4));
        resultInfo.append("\n\n");
    }
    
    private ControlPanel controlPanel;
    private JTabbedPane paneResults;
    private PlotPanel plotOrbit;
    private PlotPanel plotH;
    private JTextArea resultInfo;
}
