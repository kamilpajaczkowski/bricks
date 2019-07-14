import java.util.ArrayList;
import java.util.List;

public class AllPosPlayer extends BlockingMovePlayer{
	
	private final long SECOND = 1000L;
	
	private List<Move> remainingMoves;
	private long timerStart = 0l;
	private long maxCalculationDuration = (long)(0.47*SECOND); 
	private long breakCalculationTime;

	@Override
	public Move getCleverMove() {
		
		//startTimer();
		breakCalculationTime = now() + maxCalculationDuration;
		
		int fields = boardMovesTab.getPossibleFields();
		//debug( "fields = " + fields );
		
		//Jeœli duzo wolnych pól, to gram losowo, 
		//bo algorytmy moga nie zd¹zyæ z obliczeniami w ciagu pol sekundy
		if (fields>1000){
			return getRandomMove();
		}
		
		//Poniewa¿ algorytm getBestFromAllMove gra bardzo dobrze dla siatki 6x6, to staram
		//sie konczyæ odosobnione ruchy , zeby pozostal jeden spójny zbiór pól.
		if (fields>100){
			return getSmallPossibilitiesMove();
		}

		//Ten algorytm zlicza iloœæ pól gry na których mo¿emy zrobiæ ruch
		//i sprwadza przewiduje czy iloœc tych pól, powoduje ¿e to to my czy przeciwnik wykonamy ostatni ruch.
		//Niestety zliczamy wszystkie pola które maja mozliwoœc ruch, ale nie analizujemy tego 
		//dog³êbnie, bo trzy samotne pola daja mozliwoœc jednego ruch. Ogólnie jest to dobry pomysl,
		// gdy mamy jeden nieroz³aczny poligon pól. S³uzy temy poprzedni algorytm, ale te¿ nie jest idealny.
		if (fields>40 ){
			return getBestBlockingMove();
		}
		
		//Ten algorytm dzia³aæ mo¿e jak pozostanie juz malo ruchów (liczba pól < 40 )
		//przeszukuje on wszystkie mozliwoœci. Jednak jest on bardzo czasochlonny.
		//³atwo w nim przekroczyæ czas, dlatego, czas jest ci¹gle sprawdzany i jesli zblizy sie do 
		//ustalonej granicy (0.46 sekundy) obliczenia s¹ przerywane.
		Move bestMove = getBestFromAllMove(); 

		if (bestMove==null){
			//To jest roche niebezpieczne, bo jak tu jestesmy to niewiele czasu juz pozostalo, ale mysle ze powinno zdazyc sie wykonac
			bestMove = getBestBlockingMove();
			if (bestMove==null){
				bestMove = getRandomMove();
				//debug( "move (random) = " + bestMove );
			}else{
				//debug( "move (clever) = " + bestMove );
			}
		}else{
			//debug( "move (all check) = " + bestMove );
		}

		//endTimer();

		return bestMove;
	}
	
	//Przeszukuje wszystkie mozliwoœci
	private Move getBestFromAllMove(){
		
		Move bestMove = null;
		double bestProb = -1;
		remainingMoves = new ArrayList<Move>( this.moves() );
		
		boolean debug = PRINT_DEBUG_INFO;
		PRINT_DEBUG_INFO = false;
		
		int calc = 0;
		for (Move move: remainingMoves){
			
			double prob = getWinProbability( move, true, 1 );
			if (prob>bestProb){
				bestProb = prob;
				bestMove = move;
			}
			
			if ( breakCalculations() ){
				PRINT_DEBUG_INFO = debug;
				//debug( "    break after " + calc + " moves calculations (all= " + remainingMoves.size() +")" );
				if ( calc<5 ){
					bestMove = null;
				}
				break;
			}
			calc++;
		}
		
		PRINT_DEBUG_INFO = debug;
		return bestMove;
	}
	
	//Wybiera pola, ktore maja malo ruchów, to trzeba by tak zrobic, aby zasklepiac, male zgrupowania 
	//pustych pól
	private Move getSmallPossibilitiesMove(){
		
		Move bestMove = null;
		int minPossibilities = 100;
		
		for (Move move: moves()){
			
			int possibilities = 0;
			for ( Point point: move.connectedPoints ){
				possibilities += moveCount( point );
			}
			
			possibilities += moveCount( move.point1 );
			possibilities += moveCount( move.point2 );
				
			if (possibilities < minPossibilities){
				minPossibilities = possibilities;
				bestMove = move;
			}
			
			if (minPossibilities==0){
				break;
			}

		}
		return bestMove;
	
	}
	
	//przerywamy obliczenia, jak niebespiecznie zblizamy sie do maksymalnego czasu
	private boolean breakCalculations(){
		return now() >= breakCalculationTime;
	}
	
	private long now(){
		return System.currentTimeMillis();
	}
	
	private void startTimer(){
		if( !PRINT_DEBUG_INFO ){
			return;
		}
		timerStart = now();
	}
	
	private void endTimer(){
		if( !PRINT_DEBUG_INFO ){
			return;
		}
		debug("time = " + (System.currentTimeMillis()-timerStart)/(1000.0) + " [s}" );
	}
 
	private double getWinProbability( Move move, boolean myMove, int level ){
		
		if ( breakCalculations() ){
			return -2;
		}
		
		if (myMove){
			return getWinProbabilityMyMove( move, level );
		}else{
			return getWinProbabilityOpponentMove( move, level );
		}
	}
	
	//Zwraca prawdopodobienstwo ¿e ja wygram
	//Gdy ostatni ruch byl przeciwnika (oponentMove), to ja szykam takiego mojego ruchu
	//który da maksymalne prawdopodobienstwo wygrania
	private double getWinProbabilityOpponentMove( Move oponentMove, int level ){

		BoardMovesTab tmp = boardMovesTab.duplicate();
		
        makeOponentMove( oponentMove, MODE_SIMULATION );

		Double bestProbability = null;
		
		for (Move nextMove: remainingMoves){
			if (moves().contains(nextMove)){
				double prob = getWinProbability( nextMove, true, level+1 );
				if (bestProbability==null || prob > bestProbability){
					bestProbability = prob;	
				}
			}
		}
		
		//gdy ja nie mam juz ruchów (bestProbability==null), to prawdopodobienstwo
		//ze ja wygram jest równe 0.0
		if (bestProbability == null ){
			bestProbability = 0.0;
		}

		this.undoMove( oponentMove );
		
		boardMovesTab.areTheSame(tmp);

		return bestProbability;
	}
	

	//Zwraca prawdopodobienstwo ¿e ja wygram
	//Gdy ostatni ruch byl mój (myMove), to prawdopodobienstwo
	//wygrania licze jak srednia prawdopodobienstw wygrania, dla wszystkich róchów przeciwnika
	//To jest prawda, gdy ruch przeciwnika jest losowy.
	private double getWinProbabilityMyMove( Move myMove, int level ){
		
		BoardMovesTab tmp = boardMovesTab.duplicate();
		
		makeMyMove(myMove, MODE_SIMULATION);
		
		int countPossibiilities = 0;
		double probablitySum = 0;
		for (Move nextMove: remainingMoves){
			if (moves().contains(nextMove)){
				countPossibiilities ++;	
				double prob = getWinProbability( nextMove, false, level+1 );
				probablitySum += prob;
			}
		}
		
		//gdy przeciwnik nie ma juz ruchów (countPossibiilities==0), to prawdopodobienstwo
		//ze ja wygram jest równe 1.0
		double prob = countPossibiilities==0 ? 1 : probablitySum / countPossibiilities;

		this.undoMove(myMove);
		
		boardMovesTab.areTheSame(tmp);

		return prob;
	}
	
}






