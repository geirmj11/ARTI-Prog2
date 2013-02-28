import java.util.Collection;
import java.util.ArrayList; 

public class Point implements Comparable<Point>
{
	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public int x;
	public int y;

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (obj.getClass() != getClass())
            return false;
		Point other = (Point)obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
			
		return true;
    }
	
	@Override
    public int hashCode(){
        return 31 * x + 19 * y;
    }
	
	@Override
    public int compareTo(Point other){
		if (x < other.x) return -1;
		if (x > other.x) return +1;
		if (y < other.y) return -1;
		if (y > other.y) return +1;
		return 0;
	}
}