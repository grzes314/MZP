
package plot;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Grzegorz Los
 */
public class PlotObject implements Iterable<PlotPoint>
{
    class POIterator implements Iterator<PlotPoint>
    {
        public POIterator()
        {
            it = points.iterator();            
        }
        
        @Override
        public boolean hasNext()
        {
            return it.hasNext();
        }

        @Override
        public PlotPoint next()
        {
            return it.next();
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        Iterator<PlotPoint> it;
    }

    public enum Type
    {
        Lines, Points
    }
    
    public PlotObject()
    {
        this("", Color.BLACK, Type.Points);
    }
    
    public PlotObject(String label, Color col, Type type)
    {
        this.label = label;
        this.col = col;
        this.type = type;
        points = new ArrayList<>();
    }
    
    public PlotObject(PlotObject po)
    {
        label = po.label;
        col = po.col;
        type = po.type;
        points = new ArrayList<>();
        for (PlotPoint pp: po.points)
            points.add(pp);
    }

    @Override
    public Iterator<PlotPoint> iterator()
    {
        return new POIterator();
    }

    public void addPoint(PlotPoint p)
    {
        points.add(p);
    }

    public void addPoint(double x, double y)
    {
        points.add( new PlotPoint(x,y) );
    }

    public void setLabel(String label)
    {
        this.label = label;
       
    }

    public String getLabel()
    {
        return label;
    }

    public void setColor(Color col)
    {
        this.col = col;
    }
    
    public Color getColor()
    {
        return col;
    }

    public void setType(Type type)
    {
        this.type = type;
    }

    public Type getType()
    {
        return type;
    }
    
    public int getSize()
    {
        return points.size();
    }

    public double minX()
    {
        if (points.isEmpty()) return -1;
        double min = points.get(0).x;
        for (PlotPoint pp: points)
            if (pp.x < min) min = pp.x;
        return min;
    }

    public double minY()
    {
        if (points.isEmpty()) return -1;
        double min = points.get(0).y;
        for (PlotPoint pp: points)
            if (pp.y < min) min = pp.y;
        return min;
    }

    public double maxX()
    {
        if (points.isEmpty()) return 1;
        double max = points.get(0).x;
        for (PlotPoint pp: points)
            if (pp.x > max) max = pp.x;
        return max;
    }

    public double maxY()
    {
        if (points.isEmpty()) return 1;
        double max = points.get(0).y;
        for (PlotPoint pp: points)
            if (pp.y > max) max = pp.y;
        return max;
    }

    public void clear()
    {
        points.clear();
    }
    
    private ArrayList<PlotPoint> points;
    private String label;
    private Color col;
    private Type type;
}
