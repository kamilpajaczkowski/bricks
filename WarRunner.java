public class WarRunner {
	
    public static void main(String args[]) {
    	
    	BasicMovePlayer.PRINT_BOARD = false;
    	BasicMovePlayer.VERIFY_MOVES = false;
    	BasicMovePlayer.PRINT_DEBUG_INFO = false;
    	BasicMovePlayer.PRINT_FILE_DEBUG_INFO = false;

    	AllPosPlayer player = new AllPosPlayer();    	
    	player.run( args );
    }

}
