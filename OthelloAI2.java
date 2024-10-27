import java.util.ArrayList;

/**
 * An AI-implementation. The method to decide the next move uses the minimax algorithm with alpha-beta pruning 
 * @author Othello Project #2
 * @version 22.03.2023
 */
public class OthelloAI2 implements IOthelloAI {

    int maxDepth = 6;

    @Override
    public Position decideMove(GameState s) { // search minimax function
        int player = s.getPlayerInTurn();
        ArrayList<Position> moves = s.legalMoves();
        if (moves.isEmpty()) {
            s.changePlayer();
        }
        if (!moves.isEmpty()) {
          Position temp = maxValue(s, player, Integer.MIN_VALUE, Integer.MAX_VALUE,0).getMove();
        return temp;  
        } else {
            return new Position(-1, -1);
        }
        
    }

    /**
     * Calculates how many tokens the current player would have after a certain move minus the opponents coins.
     * @param s The current state of the game.
     * @param player The current player
     * @return An int being the coinParity, the difference in coins.  
     */
    public int utility(GameState s, int player) {
        int[] tokens = s.countTokens();
        int black = tokens[0];
        int white = tokens[1];
        int tokenParity = 0;
        if (player == 1) {
            tokenParity = black - white;
            return tokenParity;
        } else {
            tokenParity = white - black;
            return tokenParity;
        }

    }

    /**
     * Places a token and returns the new GameState
     * @param s The current state of the game.
     * @param p The Position where the player wants to place its token.
     * @return A new GameState after the player has placed a token.  
     */
    public GameState result(GameState s, Position p) {
        GameState newState = new GameState(s.getBoard(), s.getPlayerInTurn());
        newState.insertToken(p);
        return newState;
    }

    /**
     * Calculates the number of corners for the player. The value is increased with 1 for each corner the player.
     * It is decreased with 1 for each corner the opposing player has.
     * @param s The current state of the game.
     * @param player The current player.
     * @return An int the cornerHeuristicValue, being the difference in corners.
     */
    public int corners(GameState s, int player){
        int[][] board = s.getBoard();
        int size = board.length;
        int cornerHeuristicValue = 0;
        int[][] corners = {{0,0},{0,size-1},{size-1,0},{size-1,size-1}};
        for (int[] corner : corners) {
            if (board[corner[0]][corner[1]]!=0) {
                if (board[corner[0]][corner[1]]==player) {
                    cornerHeuristicValue++;
                } else {
                    cornerHeuristicValue--;
                }
            }
            
        }       
        return cornerHeuristicValue;
    }

    /**
     * Calculates the amount of legalmoves. If the player in turn is the given player the moves are counted positive.
     * If that is not the case the moves are counted negatively.
     * @param s The current state of the game.
     * @param player The current player.
     * @return An int, the mobilityHeuristicValue, being the difference of legalmoves.
     */
    public int mobility(GameState s, int player){
        int mobilityHeuristicValue;
        if (s.getPlayerInTurn() == player) {
            mobilityHeuristicValue = s.legalMoves().size();
        } else {
            mobilityHeuristicValue = -s.legalMoves().size();
        }
        return mobilityHeuristicValue;
    }


    /**
     * Calculates the heuristic by adding up corners, mobility and utility.
     * Weighting corners heighest, mobility second highest and utility lowest.
     * @param s The current state of the game.
     * @param player The current player.
     * @return An int consisting of corners, mobility and utility factors.
     */
    public int heuristic (GameState s, int player){
        return 1000*corners(s, player) + 100*mobility(s, player) +  utility(s, player);
    }

    /**
     * Finding the minValue Pair with alpha-beta pruning
     * @param s The current state of the game.
     * @param player The current player.
     * @param alpha 
     * @param beta
     * @param depth The search depth of the tree
     * @return A pair consisting of the value and Position of a move.
     */
    public Pair maxValue(GameState s, int player, int alpha, int beta, int depth) {
        ArrayList<Position> moves = s.legalMoves();
        if (s.isFinished()) {
            return new Pair(utility(s, player), null);
        }
        if(depth == maxDepth){
            return new Pair(heuristic(s, player), null); 
        }
        int v = Integer.MIN_VALUE;
        Position move = null;
        for (Position position : moves) {
            Pair tempPair = minValue(result(s, position), player, alpha, beta, depth + 1);
            if (tempPair.getValue() > v) {
                v = tempPair.getValue();
                move = position;
                alpha = Math.max(alpha, v);
            }
            if (v >= beta) {
                return new Pair(v, move);
            }  
        }
        return new Pair(v, move);

    }

    /**
     * Finding the minValue Pair with alpha-beta pruning
     * @param s The current state of the game.
     * @param player The current player.
     * @param alpha 
     * @param beta
     * @param depth The search depth of the tree.
     * @return A pair consisting of the value and Position of a move.
     */
    public Pair minValue(GameState s, int player, int alpha, int beta, int depth) {
        ArrayList<Position> moves = s.legalMoves();
        if (s.isFinished()) {
            return new Pair(utility(s, player), null);
        }
        if(depth == maxDepth){
            return new Pair(heuristic(s, player), null);
        }
        int v = Integer.MAX_VALUE;
        Position move = null;
        for (Position position : moves) {
            Pair tempPair = maxValue(result(s, position), player, alpha, beta, depth + 1);
            if (tempPair.getValue() < v) {
                v = tempPair.getValue();
                move = position;
                beta = Math.min(beta, v);
            }
            if (v <= alpha) {
                return new Pair(v, move);
            } 
        }
        return new Pair(v, move);
    }
}

/**
 * Class to represent a pair of an int value and a Position move.
 */
class Pair {
    int value;
    Position move;

    //************ Constructors ****************//
	/**
	 * Initializes a pair with the value and position of a move. 
	 * @param value The value of a certain move.
	 * @param move The Position of a move.
	 */        
    public Pair(int value, Position move) {
        this.value = value;
        this.move = move;
    }

    //************ Getter methods *******************//
	/**
	 * Returns the int value of a certain move.
	 */
    public int getValue() {
        return value;
    }

	/**
	 * Returns the Position of the move in the (value, move) Pair.
	 */
    public Position getMove() {
        return move;
    }

}