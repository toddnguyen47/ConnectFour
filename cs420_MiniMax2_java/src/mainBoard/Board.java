package mainBoard;

import java.util.ArrayList;

// Singleton Board class
public class Board {
	public final static int BOARD_SIZE = 8;
	public final static int PLAYER_MAX = 1;
	public final static int PLAYER_MIN = -1;
	private boolean maxTurn = true;
	private static Board instance = null;
	private int numberOfPiecesPlaced;
	private ArrayList<String> moveList;
	
	private char[] rowChar = {'a','b','c','d','e','f','g','h'};
	private int[] colChar = {1,2,3,4,5,6,7,8};
	
	private int[][] board;
	
	private Board() {
		moveList = new ArrayList<String>();
		initBoard();
		numberOfPiecesPlaced = setInitialCounter();
	}
	
	public static Board getInstance() {
		if (instance == null) {
			instance = new Board();
		}
		return instance;
	}
	
	private void initBoard() {
		board = new int[BOARD_SIZE][BOARD_SIZE];
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				board[i][j] = 0;
			}
		}
	}
	
	private int setInitialCounter() {
		int numPiecesCounter = 0;
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				if (board[i][j] != 0) numPiecesCounter++;
			}
		}
		
		return numPiecesCounter;
	}
	
	public boolean placeOnBoard(int row, int col) {
		if (row >= 0 && row <= 7 && col >= 0 && col <= 7 && 
				board[row][col] == 0) {
			if (maxTurn) board[row][col] = PLAYER_MAX;
			else board[row][col] = PLAYER_MIN;
			
			numberOfPiecesPlaced++;
			maxTurn = !maxTurn;
			return true;
		}
		else {
			System.out.println("Invalid move");
			return false;
		}
	}
	
	public void resetBoard(int row, int col) {
		if (row >= 0 && row <= 7 && col >= 0 && col <= 7) {
			board[row][col] = 0;
			numberOfPiecesPlaced--;
			maxTurn = !maxTurn;
		}
		else {
			System.out.println("Invalid reset move");
		}
	}
	
	public void printBoard() {
		System.out.println("  1 2 3 4 5 6 7 8");
		for (int i = 0; i < BOARD_SIZE; i++) {
			System.out.print(rowChar[i] + " ");
			
			for (int j = 0; j < BOARD_SIZE; j++) {
				if (board[i][j] == PLAYER_MIN) {System.out.print("X ");}
				else if (board[i][j] == PLAYER_MAX) {System.out.print("O ");}
				else {
					System.out.print("- ");
				}
			}
			System.out.println("");
		}
		System.out.println("");
		int counter = 0;
		int moveListSize = moveList.size();
		for (int i = 0; i < moveListSize; i++) {
			if (counter % 2 == 0) {
				String numFormatted = String.format("%02d", (counter / 2) + 1);
				System.out.print("[" + numFormatted + "] ");
				System.out.print(moveList.get(i));
			}
			else {
				System.out.print(" " + moveList.get(i));
				System.out.print("    ");
			}
			counter++;
			// counter % 8, bitwise operation
			if ((counter & 7) == 0) {
				System.out.println("");
			}
		}
		
		if (moveListSize > 0)
			System.out.println("\nMost recent move: " + moveList.get(moveListSize - 1));
		
		System.out.println("");
	}
	
	
	public boolean isTerminalBoard() {
		int zeroCounter = 0;
		
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				// Row test
				if (j < BOARD_SIZE - 3 && board[i][j] != 0 &&
						board[i][j] == board[i][j+1] &&
						board[i][j] == board[i][j+2] &&
						board[i][j] == board[i][j+3]
						) {
					return true;
				}
				
				// Col test
				if (i < BOARD_SIZE - 3 && board[i][j] != 0 && 
						board[i][j] == board[i+1][j] &&
						board[i][j] == board[i+2][j] &&
						board[i][j] == board[i+3][j]
						) {
					return true;
				}
				
				if (board[i][j] == 0) {zeroCounter++;}
			}
		}
		
		// No places left
		if (zeroCounter == 0) {return true;}
		
		return false;
	}
	
	public void setSpecificBoard() {
		board[6][6] = PLAYER_MIN;
		board[7][6] = PLAYER_MIN;
		board[5][6] = PLAYER_MIN;
		numberOfPiecesPlaced += 3;
	}
	
	public boolean isValidMove(int row, int col) {
		return (board[row][col] == 0);
	}
	
	public void flipBoard() {
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				if (board[i][j] == PLAYER_MAX)
					board[i][j] = PLAYER_MIN;
				else if (board[i][j] == PLAYER_MIN)
					board[i][j] = PLAYER_MAX;
			}
		}
	}
	
	public void addToMoveList(int row, int col) {
		moveList.add(rowChar[row] + String.valueOf(colChar[col]));
	}
	
	public int[] getFirstMove() {
		int firstMove[] = new int[2];
		if (moveList.size() > 0) {
			String first = moveList.get(0);
			int row = first.charAt(0) - 'a';
			int col = first.charAt(1) - '1';
			firstMove = new int[] {row, col};			
		} else {
			firstMove = null;
		}
		
		return firstMove;
	}
	
	public int getBoardSize() {return BOARD_SIZE;}
	public int[][] getCurrentBoard() {return board;}
	public int getNumOfPiecesPlayed() {return numberOfPiecesPlaced;}
	public boolean maxTurn() {return maxTurn;}
	/**
	 * Set the current turn
	 * @param curTurn - Min player is opponent, max player is our AI
	 */
	public void setTurn(boolean isMaxTurn) {maxTurn = isMaxTurn;}
}
