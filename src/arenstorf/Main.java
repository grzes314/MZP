
package arenstorf;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
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
        JPanel panel = new JPanel();
        draw = new JButton("Draw");
        draw.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                drawClicked();
            }
        });
        panel.add(draw);
        return panel;
    }
    
    private void calculate()
    {
        Arenstorf a = new Arenstorf();
        a.calculate();
        PlotProxy.plot(a.getPlotData());
        draw.setEnabled(true);
    }
    
    private void drawClicked()
    {
        draw.setEnabled(false);
        new Thread( new Runnable() {
            @Override public void run() {
                calculate();
            }
        }).start();
    }
    
    private JButton draw;
}
