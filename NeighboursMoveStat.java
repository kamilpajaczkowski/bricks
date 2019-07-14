
public class NeighboursMoveStat {
	
	int[] moveCount = new int[5];
	
	public int getNumOfFields( int containsMoveCount ){
		return moveCount[containsMoveCount];
	}
	
	public void add( int count ){
		moveCount[count]++;
	}

}
