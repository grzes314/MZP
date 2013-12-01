package math.matrices;

import java.util.Iterator;
import java.util.List;

public class Vector extends Matrix implements Iterable<Double>
{
    public class VecIterator implements  Iterator<Double>
    {
        @Override
        public boolean hasNext()
        {
            return row < getSize();
        }

        @Override
        public Double next()
        {
            return get(++row);
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException("Remove does not "
                    + "have sense for constant size vectors.");
        }         
        int row = 0;
    };
        
    public Vector(int rows)
    {
        super(rows, 1);
        set(1, 0);
    }
    
    public Vector(double[] vals)
    {
        super(vals);
    }
    
    public Vector(Vector original)
    {
        super(original);
    }
    
    public Vector(List<Double> vals)
    {
        super(vals.size(), 1);
        for(int row = 1; row <= vals.size(); ++row)
            set(row, vals.get(row-1));
    }
    
    public final int getSize()
    {
        return getRows();
    }
    
    public final double get(int row)
    {
        return get(row, 1);
    }
    
    public final void set(int row, double val)
    {
        set(row, 1, val);
    }

    public Vector add(Vector other) throws DimensionException
    {
        if (getRows() != other.getRows())
            throw new DimensionException("Cannot add vector of diffrent lengths");
        Vector res = new Vector(getRows());
        for (int row = 1; row <= getRows(); ++row)
            res.set(row, get(row) + other.get(row));
        return res;
    }
    
    @Override
    public Vector times(double t)
    {
        Vector res = new Vector(getRows());
        for (int row = 1; row <= getRows(); ++row)
            res.set(row, get(row)*t);
        return res;
    }

    @Override
    public VecIterator iterator()
    {
        return new VecIterator();
    }
    
    public Vector subvector(int firstRow, int lastRow)
    {
        ensureSubvectorParamsOK(firstRow, lastRow);
        Vector res = new Vector(lastRow - firstRow + 1);
        for (int i = 1; i <= res.getSize(); ++i)
            res.set(i, get(firstRow + i -1));
        return res;
    }

    private void ensureSubvectorParamsOK(int firstRow, int lastRow)
    {
        ensureIndicesOK(firstRow, 1);
        ensureIndicesOK(lastRow, 1);
        if (firstRow > lastRow)
            throw new IllegalArgumentException("First row can not be greater than "
                    + "last row, firstRow = " + firstRow + ", lastRow = " + lastRow);
    }
}