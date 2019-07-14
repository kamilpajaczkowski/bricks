
import java.util.Set;

public class BoardMoves {
	
	private boolean isRight = true;
	private boolean isDown  = true;
	
	private final Move right;
	private final Move down;
	
	public BoardMoves neighbourRight;
	public BoardMoves neighbourDown;
	public BoardMoves neighbourLeft;
	public BoardMoves neighbourUp;
	
	private final int row;
	private final int col;
	
	private final Set<Move> moves;
	private boolean enable = true;
	
	public BoardMoves( BoardMoves org, Set<Move> moves ){
		
		this.moves = moves;
		
		this.row = org.row;
		this.col = org.col;
		
		this.isRight = org.isRight;
		this.isDown = org.isDown;
		
		this.right = org.right;
		this.down = org.down;
	}
	
	public BoardMoves( int row, int col, int rowsize, int colsize, Set<Move> moves ){
		
		this.moves = moves;
		
		this.row = row;
		this.col = col;
		
		if(col<colsize-1){
			right = new Move(row,col,Move.POZIOM, rowsize, colsize);
			moves.add(right);
		}else{
			right = null;
		}
		
		if(row<rowsize-1){
			down = new Move(row,col,Move.PION, rowsize, colsize);
			moves.add(down);
		}else{
			down = null;
		}
	}
	
	public Move getRight(){
		return isRight ? right: null;
	}	

	public Move getDown(){
		return isDown ? down: null;
	}
	
	private static boolean theSameMove( Move move1, Move move2 ){
		if (move1==null){
			return move2==null;
		}
		return move1.equals( move2 );
	}

	public boolean isTheSame( BoardMoves board ){	
		if ( !theSameMove(getDown(),board.getDown()) ){
			return false;
		}		
		if ( !theSameMove(getRight(),board.getRight()) ){
			return false;
		}
		
		return true;
	}
	
	public void setEnable(){
		
		this.enable = true;
		
		if ( neighbourLeft!=null && neighbourLeft.isEnable() ){
			neighbourLeft.isRight = true;
			moves.add( neighbourLeft.getRight() );
		}
		if ( neighbourUp!=null && neighbourUp.isEnable() ){
			neighbourUp.isDown = true;
			moves.add( neighbourUp.getDown() );
		}
		if ( neighbourRight!=null && neighbourRight.isEnable() ){
			isRight = true;
			moves.add( getRight() );
		}
		if ( neighbourDown!=null && neighbourDown.isEnable() ){
			isDown = true;
			moves.add( getDown() );
		}
	}
	
	public void setDisable(){
		
		this.enable = false;
		
		if (right!=null){
			moves.remove( right );
			isRight = false;
		}
		if (down!=null){
			moves.remove( down );
			isDown = false;
		}
		if (neighbourLeft!=null){
			moves.remove( neighbourLeft.right );
			neighbourLeft.isRight = false;
		}
		if (neighbourUp!=null){
			moves.remove( neighbourUp.down );
			neighbourUp.isDown = false;
		}

	}
	
	public boolean isEnable(){
		return enable;
	}
	
	public boolean hasMoves(){
		if (!enable){
			return false;
		}
		if ( getRight() !=null ) {
			return true;
		}
		if ( getDown() !=null ) {
			return true;
		}
		if (neighbourLeft!=null && neighbourLeft.getRight()!=null ){
			return true;
		}
		if (neighbourUp!=null && neighbourUp.getDown()!=null ){
			return true;
		}
		
		return false;
	}
	
	
	public int moveCount(){
		if (!enable){
			return 0;
		}
		
		int count = 0;
		if ( getRight() !=null ) {
			count++;
		}
		if ( getDown() !=null ) {
			count++;
		}
		if (neighbourLeft!=null && neighbourLeft.getRight()!=null ){
			count++;
		}
		if (neighbourUp!=null && neighbourUp.getDown()!=null ){
			count++;
		}
		
		return count;
	}

}
