import java.util.Random;

public class BlockingMovePlayer extends BasicMovePlayer {

	@Override
	public Move getCleverMove() {
		return getBestBlockingMove();
	}
	
	public Move getBestBlockingMove() {
		if (moves().size() == 0) {
			return null;
		}
		
		Move move = getBlockingMove();
		if (move!=null){
			return move;
		}
		
		int possibleFields = boardMovesTab.getPossibleFields();
//		debug( "possibleFields " + possibleFields );
//		debug( "Optimal blocking = " + ((possibleFields-2)%4));
		
		move = findOptimalOnesBlocked( possibleFields );
		if (move!=null){
			return move;
		}
		
//		debug( "move = " + move );
		
		return getRandomMove();
	}
	
	

	public NeighboursMoveStat getNeighboursData( Move move ){
		
		NeighboursMoveStat neighbours = new NeighboursMoveStat();
		
		for ( Point point: move.connectedPoints ){
			neighbours.add( moveCount( point ) );
		}
			
		return neighbours;
	}
	
	private Move findOptimalOnesBlocked( int possibleFields ){
		
		if( (( possibleFields-2) % 4) == 0 ){//perfect situation
			//niszcz jedynki
			for (Move move: moves()){
				int moveCount1 = getBoardMoves(move.point1).moveCount();
				int moveCount2 = getBoardMoves(move.point2).moveCount();
				if (moveCount1==1 && moveCount2!=1 || moveCount1!=1 && moveCount2==1){
					return move;
				}
			}
		}

		for (Move move: moves()){
			NeighboursMoveStat neighbours = getNeighboursData( move );
			int blocked = neighbours.getNumOfFields(1);
			int remianingFields = possibleFields - blocked - 2;
			if ((remianingFields % 4) == 0 ){
				return move;
			}
		}
		for (Move move: moves()){
			NeighboursMoveStat neighbours = getNeighboursData( move );
			int blocked = neighbours.getNumOfFields(1);
			int remianingFields = possibleFields - blocked - 2;
			if ((remianingFields % 4) == 1 ){
				return move;
			}
		}
		for (Move move: moves()){
			NeighboursMoveStat neighbours = getNeighboursData( move );
			int blocked = neighbours.getNumOfFields(1);
			int remianingFields = possibleFields - blocked - 2;
			if ((remianingFields % 4) == 3 ){
				return move;
			}
		}
		return null;
	}

	protected int moveCount( Point point ){
		return moveCount( point.row, point.col );
	}

	protected int moveCount( int row, int col ){
		return boardMovesTab.getBoardMoves(row, col).moveCount();
	}
	
	private Move getBlockingMove() {

		int size = moves().size();

		if (size < 5) {
			for (Move move : moves()) {
				int blocked = getBoardMoves(move.point1).moveCount()
						+ getBoardMoves(move.point2).moveCount() - 1;
				if (blocked >= size) {
					return move;
				}
			}
		}
		return null;
	}
	
	private BoardMoves getBoardMoves( Point point ){
		return boardMovesTab.getBoardMoves(point.row, point.col);
	}
    

}
