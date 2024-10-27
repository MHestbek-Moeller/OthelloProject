import java.util.ArrayList;
import java.util.Random;

/**
 * A simple OthelloAI-implementation. The method to decide the next move just
 * returns the first legal move that it finds. 
 * @author Mai Ajspur
 * @version 9.2.2018
 */
public class DumAI implements IOthelloAI{

	/**
	 * Returns first legal move
	 */
	public Position decideMove(GameState s){
		ArrayList<Position> moves = s.legalMoves();
		Random random = new Random();
		int r = 0;
		if ( !moves.isEmpty() ){
			r = random.nextInt((moves.size()-0))+0;
			return moves.get(r);
		}else{
			return new Position(-1,-1);
		}
	}
	
}
