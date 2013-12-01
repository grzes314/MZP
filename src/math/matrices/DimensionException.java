package math.matrices;

public class DimensionException extends IllegalArgumentException
{
    public DimensionException() {
    }

    public DimensionException(String mssg) {
        super(mssg);
    }
}
