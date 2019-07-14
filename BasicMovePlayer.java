import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import javax.management.RuntimeErrorException;

/**
 * Created by Kamil on 13.01.2018.
 * Project ComputerPlayer
 */
public abstract class BasicMovePlayer {
	
	protected static boolean PRINT_BOARD = false;
	protected static boolean VERIFY_MOVES = false;
	protected static boolean PRINT_DEBUG_INFO = true;
	protected static boolean PRINT_FILE_DEBUG_INFO = false;
	//private static boolean PRINT_MOVES = false;
	
	protected static final boolean MODE_SIMULATION = true;
	protected static final boolean MODE_REAL_RUN = false;
	
    protected BoardMovesTab boardMovesTab;
    //protected int[][] board;
    protected int size = 0;
    //protected Set<Move> moves = new HashSet<>();
    protected boolean printDebugInfo = true;
    public Random rnd = new Random();
    
    public abstract Move getCleverMove();
    
	
    public void run(String args[]) {
    	output("Start", false );
    	printDebugInfo = false;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
        	String a = br.readLine();
			String b = a.replace('_', ' ');
			String[] sizeandcoords = b.split(" "); 
			
            init( Integer.parseInt( sizeandcoords[0]) );
			
			if(sizeandcoords.length>1){
				int i = 1;
				while(i<sizeandcoords.length){
					String klocek = sizeandcoords[i];
					String[] coords = klocek.split("x");
					
					int Apreviousx1 = Integer.parseInt(coords[0]);
					int Apreviousy1 = Integer.parseInt(coords[1]);
					int Apreviousx2 = Integer.parseInt(coords[0]);
					int Apreviousy2 = Integer.parseInt(coords[1]);
              
					Move AopponentMove = createMove( Apreviousx1, Apreviousy1, Apreviousx2, Apreviousy2 );
					output("opponent move " + AopponentMove, true );
					AmakeOponentMove( AopponentMove, MODE_REAL_RUN );
				
					i++;
				}
			}
			
            System.out.println("OK");
            
            while(true) {
                String inputText = br.readLine();
                String zaczynaj = "START";
                if(!inputText.equals(zaczynaj)) {
                	
                	String c = inputText.replace('x', ' ');
					String d = c.replace('_', ' ');
                	
                 	final String[] coordinates = d.split(" ");               	
                    int previousx1 = Integer.parseInt(coordinates[0]);//EDYCJA (start od 0)
                    int previousy1 = Integer.parseInt(coordinates[1]);
                    int previousx2 = Integer.parseInt(coordinates[2]);
                    int previousy2 = Integer.parseInt(coordinates[3]);
                    Move opponentMove = createMove( previousx1, previousy1, previousx2, previousy2 );
                    output("opponent move " + opponentMove, true );
                    makeOponentMove( opponentMove, MODE_REAL_RUN );
                }
                  	
                Move move = getCleverMove();
                makeMyMove( move, MODE_REAL_RUN );
                //sendMoveToOutput( move ); EDYCJA
                output("my move " + move, true );
            }
        }
        catch (Exception e) {

        }
    }
    
    public void init(int size){
    	this.size = size;
    	boardMovesTab = new BoardMovesTab( size );
    }
    
    protected void debug(String txt){
    	if (PRINT_DEBUG_INFO){
    		System.out.println( txt );
    	}
    }
    
    protected void output( String txt,boolean append ){
    	if (!PRINT_FILE_DEBUG_INFO){
    		return;
    	}
    	try{
	    	BufferedWriter w = new BufferedWriter( new FileWriter("C:/workspace/SimplePlayerBricks/out/output.txt",append) );	
	    	w.write( txt );
	    	w.newLine();
	    	w.close();
    	}catch( Exception e){
    		
    	}
    }
    
    private void setSize(int size){
    	this.size = size;
    }

    /**
     * @param size
     * @return 1 when Our algorithm wins with random
     */
    protected double gameAgainstRandom(){
    	
    	init(size);

    	boolean myMove = rnd.nextBoolean();
    	Move move = null;
    	
    	printBoard();
    	
    	while(true){
    		move = myMove ? getCleverMove() : getRandomMove();
    		
    		if (move==null){
    			return myMove ? 0: 1;
    		}
    		
    		if (myMove){
    			makeMyMove(move, MODE_SIMULATION);
    		}else{
    			makeOponentMove(move, MODE_SIMULATION);
    		}

    		myMove = !myMove;
    		
    		printBoard();
    	}

    }
    
    protected void printBoard(){
    	if (!PRINT_BOARD){
    		return;
    	}
    	System.out.println( boardMovesTab.printBoard() );
    }
    
    
    protected double gameAgainstRandom(int trials){
    	printDebugInfo = true;
    	double wins = 0;
    	for( int i=1; i<=trials; i++){
    		wins += gameAgainstRandom();
    		
    		debug( "Probability of winning game by our alghorithm, after " + i + " trials is + " + (wins/i)  );
    	}
    	
    	//debug( "Probability of winning game by our alghorithm is = " + (wins/trials)  );
    	return trials;
    }
    
    public Set<Move> moves(){
    	return boardMovesTab.moves();
    }
    
    public Move getRandomMove(){
    	 if( moves().size()==0 ){
    		 return null;
    	 }
    	
    	 int randomId = rnd.nextInt( moves().size() );      
    	 return moves().toArray( new Move[0] )[randomId];
    }
    
    public int moveSize(){
    	return moves().size();
    }
    
    protected void undoMove( Move move ){
    	
    	boardMovesTab.undoMove(move);
    }
    
    protected void makeMove( Move move, boolean myMove, boolean simulation ){
    	
    	boardMovesTab.makeMove(move);
    	
    	if (myMove && !simulation){
    		sendMoveToOutput( move );
    	}
    	
    	verifyMoves();
    }
    protected void AmakeMove( Move move, boolean myMove, boolean simulation ){
    	
    	boardMovesTab.AmakeMove(move);
    	
    	if (myMove && !simulation){
    		sendMoveToOutput( move );
    	}
    	
    	verifyMoves();
    }
    
    protected void makeMyMove( Move move, boolean simulation ){
    	makeMove( move, true, simulation);
    }
    
    protected Move createMove( int row1, int col1, int row2, int col2 ){
    	int minrow = row1<row2 ? row1 : row2;
    	int mincol = col1<col2 ? col1 : col2;
    	boolean direction = (row1==row2) ? Move.POZIOM : Move.PION;
    	return new Move( minrow, mincol, direction, size, size);
    }
    
    protected void makeOponentMove11( int row1, int col1, int row2, int col2 ){
    	int minrow = row1<row2 ? row1 : row2;
    	int mincol = col1<col2 ? col1 : col2;
    	boolean direction = (row1==row2) ? Move.POZIOM : Move.PION;
    	makeOponentMove( new Move( minrow, mincol, direction, size, size), false );
    }
    
    protected void makeOponentMove( Move move, boolean simulation ){
    	makeMove( move, false, simulation );
    }
    
    protected void AmakeOponentMove( Move move, boolean simulation ){
    	AmakeMove( move, false, simulation );
    }
    
    protected void sendMoveToOutput( Move move ){ //EDYCJA (start od 0)
        int row1 = move.point1.row ;
        int col1 = move.point1.col ;
        int row2 = move.point2.row ;
        int col2 = move.point2.col ;
        System.out.println( row1 + "x" + col1+ "_" + row2+ "x"+col2);
    }
    
    
    private void verifyMoves(){
    	if (!VERIFY_MOVES){
    		return;
    	}
//    	int sum = 0;
//        for (int row = 0; row < size; row++) {
//            for (int col = 0; col < size; col++) {
//            	sum += boardMovesTab[row][col].size();
//            }
//        }
//    	
//    	if (sum != moves.size()*2 ){
//    		throw new RuntimeException("Niezgodna liczba ruchow");
//    	}
    }
    

}
