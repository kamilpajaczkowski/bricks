import java.util.HashSet;
import java.util.Set;

public class BoardMovesTab {
	
	protected static final int BOARD_EMPTY = 0;
	protected static final int BOARD_BRICK_MY = -1;
	protected static final int BOARD_BRICK_OPONENT = -2;
	
    private BoardMoves[][] boardMoves;
    private int[][] board;
    private final Set<Move> moves = new HashSet<>();
    private final int size;
	
	public BoardMovesTab( int size ){
		this.size = size;
		init();
	}

	//for duplication
    private BoardMovesTab( BoardMovesTab org ) {
    	this.size = org.size();
    	
    	boardMoves = new BoardMoves[size][size];
		for (int i=0; i<size; i++){
			for (int j=0; j<size; j++){
				boardMoves[i][j] = new BoardMoves( org.boardMoves[i][j], moves );
			}
		}
		
		board = new int[size][size];
		for (int i=0; i<size; i++){
			for (int j=0; j<size; j++){
				board[i][j] = org.board[i][j];
			}
		}

    	this.moves.addAll( org.moves() );
	}

    public BoardMovesTab duplicate(){
    	return new BoardMovesTab(this);
    }
    
	public void init(){ 
    	initBoardMoves();
        initBoard();
    }
	
	public int size(){
		return size;
	}
	
	public Set<Move> moves(){
		return moves;
	}
    
    protected void initBoardMoves(){
    	boardMoves = new BoardMoves[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
            	boardMoves[row][col] = new BoardMoves(row, col, size, size, moves);
            }
        }        
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size-1; col++) {
            	boardMoves[row][col].neighbourRight = boardMoves[row][col+1];
            }
        }
        for (int row = 0; row < size-1; row++) {
            for (int col = 0; col < size; col++) {
            	boardMoves[row][col].neighbourDown = boardMoves[row+1][col];
            }
        }
        for (int row = 0; row < size; row++) {
            for (int col = 1; col < size; col++) {
            	boardMoves[row][col].neighbourLeft = boardMoves[row][col-1];
            }
        }
        for (int row = 1; row < size; row++) {
            for (int col = 0; col < size; col++) {
            	boardMoves[row][col].neighbourUp = boardMoves[row-1][col];
            }
        }
    }
    
    protected void initBoard(){
    	board = new int[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                board[row][col] = 0;
            }
        }
    }
    
    public void makeMove( Move move ){
    	
    	disablePoint( move.point1 );
    	disablePoint( move.point2 );
    	
    }
    
    public void AmakeMove( Move move ){//DOPISANE
    	
    	disablePoint( move.point1 );
    	//disablePoint( move.point2 );
    	
    }
    
    public void undoMove( Move move ){
    	enablePoint( move.point1 );
    	enablePoint( move.point2 );
    }
    
	private void disablePoint(Point point) {
		BoardMoves boardMove = boardMoves[point.row][point.col];
		boardMove.setDisable();
	}

	private void enablePoint(Point point) {
		BoardMoves boardMove = boardMoves[point.row][point.col];
		boardMove.setEnable();
	}
	
	public BoardMoves getBoardMoves( int row, int col ){
		return boardMoves[row][col];
	}
	
    public int getPossibleFields(){
    	int count = 0;
    	for (int row=0; row<size; row++){
    		for( int col=0; col<size; col++){
    			if ( boardMoves[row][col].hasMoves() ){
    				count ++;
    			}
    		}
    	}
    	return count;
    }
    
    private String getView( int value, BoardMoves boardMoves ){
    	
		switch( value ){
			case BOARD_BRICK_MY: return "M";
			case BOARD_BRICK_OPONENT: return "O";
		}
		
		//return "-";
		return ""+boardMoves.moveCount();
    }
 
    protected String printBoard(){

    	String ret = "";
		for(int row=0; row<size; row++){
			for(int col=0; col<size; col++){	
				ret += getView( board[row][col], boardMoves[row][col]  );
			}
			ret += "\n";
		}
		ret += "-------------------";
		
		return ret;
    }
    
    
	public void areTheSame( BoardMovesTab tab ){
		
		for (int i=0; i<size; i++){
			for (int j=0; j<size; j++){
				if ( !tab.boardMoves[i][j].isTheSame( boardMoves[i][j] ) ){
//					printBoard( null, boardMovesTab);
//					debug("-------------------");
//					printBoard( null, tab);
					
//					tab.boardMoves[i][j].isTheSame( boardMoves[i][j] );
					
					throw new RuntimeException("Boards are not the same");
					
				}
			}
		}
	}
    


//    public void removeMove( Move move ){
//    	moves.remove( move );
//    }
//    
//    public void removeMoves( List<Move> moves ){
//    	for (Move move: moves){
//    		removeMove( move );
//    	}
//    }
//    
//    protected void removeFromMoves( Move move ){
//    	if (move!=null){
//    		moves.remove(move);
//    	}
//    }

//    protected void removeAllPointsMoves( Point point ){
//    	BoardMoves boardMoves = boardMovesTab[point.row][ point.col];
//    	moves.removeAll( boardMoves.moves );
//    	boardMoves.removeaAll();
//    }
//    
//    protected void removeAllCommonMoves( Move move ){
//
//    	removeAllPointsMoves( move.point1 );
//    	removeAllPointsMoves( move.point2 );
//    	
//    	removeAllRelatedMoves( move.point1 );
//    	removeAllRelatedMoves( move.point2 );
//    }
//    
//    private void setBoard( int row, int col, int val ){
//    	if (row>=0 && row<size && col>=0 && col<size ){
////			if (board[row][col]==BOARD_BRICK_MY || board[row][col]==BOARD_BRICK_OPONENT ){
////				throw new RuntimeException("To pole jest juz zajete, wystapil jakis blad");
////			}
//    		board[row][col] = val;
//    	}
//    }
}
