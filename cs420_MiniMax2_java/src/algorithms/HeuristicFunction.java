package algorithms;

import cs420_p3.Board;

public class HeuristicFunction {
	Board mainBoard = Board.getInstance();
	private static HeuristicFunction instance = null;
	
	private HeuristicFunction() {} // empty instance creation
	
	public static HeuristicFunction getInstance() {
		if (instance == null) {
			instance = new HeuristicFunction();
		}
		
		return instance;
	}
	
	
	public int getScore(int[][] board, int depth) {
		int maxWinner = 0, maxPCounter = 0, maxTotal = 0;
		int minWinner = 0, minPCounter = 0, minTotal = 0;
		maxWinner += winnerOrLoser(board, depth);
		maxPCounter += killerMoves(board, depth);
		maxPCounter += oneAway(board, depth);
		
		board = flipBoard(board);
		
		minWinner -= winnerOrLoser(board, depth);
		minPCounter -= killerMoves(board, depth);
		minPCounter -= oneAway(board, depth);
		
		board = flipBoard(board); // reset board
		
		// Give priority to current turn
		if (mainBoard.maxTurn()) {
			maxPCounter *= 2;
		} else {
			minPCounter *= 2;
		}
		
		maxTotal = maxWinner + maxPCounter;
		minTotal = minWinner + minPCounter;
		
		return maxTotal + minTotal;
	}
	
	private int[][] flipBoard(int[][] board) {
		int[][] newBoard = new int[board.length][board[0].length];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				newBoard[i][j] = board[i][j];
				if (newBoard[i][j] == Board.PLAYER_MAX) newBoard[i][j] = Board.PLAYER_MIN;
				else if (newBoard[i][j] == Board.PLAYER_MIN) newBoard[i][j] = Board.PLAYER_MAX;
			}
		}
		
		return newBoard;
	}
	
	/**
	 * Use this function to score the current moves
	 * @param board - current board
	 * @return
	 */
	public int getScoreSortMoves(int[][] board) {
		int maxWinner = 0, maxPCounter = 0, maxTotal = 0;
		int minWinner = 0, minPCounter = 0, minTotal = 0;
		maxWinner += winnerOrLoser(board, 0);
		maxPCounter += killerMoves(board, 0);
		
		board = flipBoard(board);
		
		minWinner -= winnerOrLoser(board, 0);
		minPCounter -= killerMoves(board, 0);
		
		board = flipBoard(board); // reset board
		
		// Give priority to current turn
		if (mainBoard.maxTurn()) {
			maxPCounter *= 2;
		} else {
			minPCounter *= 2;
		}
		
		maxTotal = maxWinner + maxPCounter;
		minTotal = minWinner + minPCounter;
		
		return maxTotal + minTotal;
	}
	
	public int winnerOrLoser(int[][] board, int depth) {
		int counter = 0;
		int winningValue = 520000;
		for (int i = 0; i < mainBoard.getBoardSize(); i++) {
			for (int j = 0; j < mainBoard.getBoardSize(); j++) {
				// Row test
				if (j < Board.BOARD_SIZE - 3 && board[i][j] != 0 && board[i][j] == board[i][j+1] &&
						board[i][j] == board[i][j+2] && board[i][j] == board[i][j+3]
						) {
					
					if (board[i][j] == Board.PLAYER_MAX) counter += winningValue;
				}
				
				// Col test
				if (i < Board.BOARD_SIZE - 3 && board[i][j] != 0 && board[i][j] == board[i+1][j] &&
						board[i][j] == board[i+2][j] && board[i][j] == board[i+3][j]
						) {
					if (board[i][j] == Board.PLAYER_MAX) counter += winningValue;
				}
			}
		}
		// Each successful depth will have a higher heuristic score
		return counter + (depth * 50);
	}
	
	private int killerMoves(int[][] board, int depth) {
		int counter = 0;
		int killerMove = 1000;
		for (int i = 0; i < mainBoard.getBoardSize(); i++) {
			for (int j = 0; j < mainBoard.getBoardSize(); j++) {
				// Row test _XXX_
				if (j < Board.BOARD_SIZE - 4 && board[i][j] == 0 && board[i][j+4] == 0 &&
						board[i][j+1] != 0 && board[i][j+1] == board[i][j+2] &&
						board[i][j+1] == board[i][j+3]
						) {
					
					if (board[i][j+1] == Board.PLAYER_MAX) counter ++;
				}
				
				// Col test _XXX_
				if (i < Board.BOARD_SIZE - 4 && board[i][j] == 0 && board[i+4][j] == 0 &&
						board[i+1][j] != 0 && board[i+1][j] == board[i+2][j] &&
						board[i+1][j] == board[i+3][j]
						) {
					
					if (board[i+1][j] == Board.PLAYER_MAX) counter ++;
				}
			}
		}
		
		counter = counter * killerMove;
		
		// Each successful depth will have a higher heuristic score
		return counter + (depth * 50);
	}
	
	private int oneAwayFromKillerMove(int[][] board, int depth) {
		int counter = 0;
		int killerMoveJr = 200;
		for (int i = 0; i < mainBoard.getBoardSize(); i++) {
			for (int j = 0; j < mainBoard.getBoardSize(); j++) {
				// Row test -X-X-
				if (j < Board.BOARD_SIZE - 4 && board[i][j] == 0 &&
						board[i][j+4] == 0 &&
						board[i][j+1] != 0 &&
						board[i][j+1] == board[i][j+3] &&
						board[i][j+2] == 0
						) {
					
					if (board[i][j+1] == Board.PLAYER_MAX) counter ++;
				}
				
				// Row test -XX--
				if (j < Board.BOARD_SIZE - 4 && board[i][j] == 0 && board[i][j+4] == 0 &&
						board[i][j+1] != 0 &&
						board[i][j+1] == board[i][j+2] &&
						board[i][j+3] == 0
						) {
					
					if (board[i][j+1] == Board.PLAYER_MAX) counter ++;
				}
				
				// Row test --XX-
				if (j < Board.BOARD_SIZE - 4 && board[i][j] == 0 && board[i][j+4] == 0 &&
						board[i][j+1] == 0 &&
						board[i][j+2] != 0 &&
						board[i][j+2] == board[i][j+3]
						) {
					
					if (board[i][j+2] == Board.PLAYER_MAX) counter ++;
				}
				
				// Col test -X-X-
				if (i < Board.BOARD_SIZE - 4 && board[i][j] == 0 &&
						board[i+4][j] == 0 &&
						board[i+1][j] != 0 &&
						board[i+1][j] == board[i+3][j] &&
						board[i+2][j] == 0
						) {
					
					if (board[i+1][j] == Board.PLAYER_MAX) counter ++;
				}
				
				// col test -XX--
				if (i < Board.BOARD_SIZE - 4 && board[i][j] == 0 && 
						board[i+4][j] == 0 &&
						board[i+1][j] != 0 &&
						board[i+1][j] == board[i+2][j] &&
						board[i+3][j] == 0
						) {
					
					if (board[i+1][j] == Board.PLAYER_MAX) counter ++;
				}
				
				// col test --XX-
				if (j < Board.BOARD_SIZE - 4 && board[i][j] == 0 &&
						board[i+4][j] == 0 &&
						board[i+1][j] == 0 &&
						board[i+2][j] != 0 &&
						board[i+2][j] == board[i][j+3]
						) {
					
					if (board[i+2][j] == Board.PLAYER_MAX) counter ++;
				}
			}
		}
		
		counter = counter * killerMoveJr;
		
		// Each successful depth will have a higher heuristic score
		return counter + (depth * 50);
	}
	
	private int oneAway(int[][] board, int depth) {
		int counter = 0;
		int score = 400;
		for (int i = 0; i < mainBoard.getBoardSize(); i++) {
			for (int j = 0; j < mainBoard.getBoardSize(); j++) {
				// Row test XXX_
				if (j < Board.BOARD_SIZE - 3 && board[i][j] != 0 &&
						board[i][j] == board[i][j+1] &&
						board[i][j] == board[i][j+2] &&
						board[i][j+3] == 0
						) {					
					if (board[i][j] == Board.PLAYER_MAX) counter += score;
				}
				
				// Row test _XXX
				if (j < Board.BOARD_SIZE - 3 && board[i][j+1] != 0 &&
						board[i][j+1] == board[i][j+2] &&
						board[i][j+1] == board[i][j+3] &&
						board[i][j] == 0
						) {					
					if (board[i][j+1] == Board.PLAYER_MAX) counter += score;
				}
				
				// Row test X_XX
				if (j < Board.BOARD_SIZE - 3 && board[i][j] != 0 &&
						board[i][j] == board[i][j+2] &&
						board[i][j] == board[i][j+3] &&
						board[i][j+1] == 0
						) {					
					if (board[i][j] == Board.PLAYER_MAX) counter += score;
				}
				
				// Row test XX_X
				if (j < Board.BOARD_SIZE - 3 && board[i][j] != 0 &&
						board[i][j] == board[i][j+1] &&
						board[i][j] == board[i][j+3] &&
						board[i][j+2] == 0
						) {					
					if (board[i][j] == Board.PLAYER_MAX) counter += score;
				}
				
				// Col test XXX_
				if (i < Board.BOARD_SIZE - 3 && board[i][j] != 0 &&
						board[i][j] == board[i+1][j] &&
						board[i][j] == board[i+2][j] &&
						board[i+3][j] == 0
						) {					
					if (board[i][j] == Board.PLAYER_MAX) counter += score;
				}
				
				// Col test _XXX
				if (i < Board.BOARD_SIZE - 3 && board[i+1][j] != 0 &&
						board[i+1][j] == board[i+2][j] &&
						board[i+1][j] == board[i+3][j] &&
						board[i][j] == 0
						) {					
					if (board[i+1][j] == Board.PLAYER_MAX) counter += score;
				}
				
				// Col test X_XX
				if (i < Board.BOARD_SIZE - 3 && board[i][j] != 0 &&
						board[i][j] == board[i+2][j] &&
						board[i][j] == board[i+3][j] &&
						board[i+1][j] == 0
						) {					
					if (board[i][j] == Board.PLAYER_MAX) counter += score;
				}
				
				// Col test XX_X
				if (i < Board.BOARD_SIZE - 3 && board[i][j] != 0 &&
						board[i][j] == board[i+1][j] &&
						board[i][j] == board[i+3][j] &&
						board[i+2][j] == 0
						) {					
					if (board[i][j] == Board.PLAYER_MAX) counter += score;
				}
			}
		}
		// Each successful depth will have a higher heuristic score
		return counter + (depth * 50);
	}
}
