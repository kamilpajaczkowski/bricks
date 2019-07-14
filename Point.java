
public class Point {
	
	public int row;
	public int col;
	public Point(int row, int col) {
		this.row = row;
		this.col = col;
	}
	@Override
	public int hashCode() {
		return row + 1000*col; 
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		if (row != other.row)
			return false;
		if (col != other.col)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Point: [row=" + row + ", col=" + col + "]";
	}
	
	

}
