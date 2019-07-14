import java.util.ArrayList;
import java.util.List;

public class Move {
	
	public static final boolean POZIOM = true;
	public static final boolean PION = false;
	
	public boolean direction;
	public Point point1;
	public Point point2;
	
	//Points which could be blocked
	public final List<Point> connectedPoints = new ArrayList<>();
	public final List<Point> connectedToPoint1 = new ArrayList<>();
	public final List<Point> connectedToPoint2 = new ArrayList<>();
	
	private final int hashCode;
	
	public Move(int row, int col, boolean direction, int rowsize, int colsize ) {

		this.direction = direction;
		point1 = new Point(row,col);
		
		if (direction==POZIOM){
			point2 = new Point(row,col+1);
			
			if (row>0){
				connectedToPoint1.add( new Point(row-1,col) );
				connectedToPoint2.add( new Point(row-1,col+1) );
			}
			if (row<rowsize-1){
				connectedToPoint1.add( new Point(row+1,col) );
				connectedToPoint2.add( new Point(row+1,col+1) );
			}
			if (col>0){
				connectedToPoint1.add( new Point(row,col-1) );
			}
			if (col<colsize-2){
				connectedToPoint2.add( new Point(row,col+2) );
			}
		}else{
			point2 = new Point(row+1,col);
			
			if (col>0){
				connectedToPoint1.add( new Point(row,col-1) );
				connectedToPoint2.add( new Point(row+1,col-1) );
			}
			if (col<colsize-1){
				connectedToPoint1.add( new Point(row,col+1) );
				connectedToPoint2.add( new Point(row+1,col+1) );
			}
			if (row>0){
				connectedToPoint1.add( new Point(row-1,col) );
			}
			if (row<rowsize-2){
				connectedToPoint2.add( new Point(row+2,col) );
			}
		}
		
		connectedPoints.addAll( connectedToPoint1 );
		connectedPoints.addAll( connectedToPoint2 );
		
		hashCode = calcHashCode();
	}
	
	private int calcHashCode(){
		return ( direction==POZIOM ? 1 : 0 ) + 2 * point1.hashCode();
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if( obj==null ){
			return false;
		}
		return hashCode == obj.hashCode();
	}

	@Override
	public String toString() {
		return "Move: " + (direction==POZIOM?"poziom":"pion") + ", " + point1 + "  " + hashCode();
	}
	
	

}
