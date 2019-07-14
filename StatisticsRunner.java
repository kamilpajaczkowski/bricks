
public class StatisticsRunner {
	
    public static void main(String args[]) {
    	
    	BasicMovePlayer.PRINT_BOARD = false;
    	BasicMovePlayer.VERIFY_MOVES = false;
    	BasicMovePlayer.PRINT_DEBUG_INFO = true;
    	BasicMovePlayer.PRINT_FILE_DEBUG_INFO = false;

    	//BasicMovePlayer blockLastMovePlayer = new BlockingMovePlayer();
    	AllPosPlayer player = new AllPosPlayer();    	
    	//BlockingMovePlayer player = new BlockingMovePlayer();
    	
    	player.init( 10 );
    	player.gameAgainstRandom( 100 );

    }

}
