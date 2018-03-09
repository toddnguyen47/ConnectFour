package cs420_p3;

import java.util.Scanner;

import algorithms.MiniMax_MaxDepth;

public class MainClass {

	Board board;
	int move[];
	Scanner sc;
	MiniMax_MaxDepth mm_maxDepth;
	
	public MainClass() {
		sc = new Scanner(System.in);
		move = new int[2];
		move[0] = 3;
		move[1] = 4;
		board = Board.getInstance();
		mm_maxDepth = new MiniMax_MaxDepth(getSeconds());
		
		//board.setSpecificBoard();
		
		play(whoGoesFirst());
		sc.close();
	}
	
	private void play(boolean theirAIFirst) {
		board.setTurn(theirAIFirst);
		boolean correctMove = false;
		
		while(!board.isTerminalBoard()) {
			System.out.println("Number of tiles placed: " + board.getNumOfPiecesPlayed());
			System.out.println("");
			board.printBoard();
			// Min player's turn
			if (!board.maxTurn()) {
				do {
					move = getMove();
					correctMove = board.placeOnBoard(move[0], move[1]);
				} while (!correctMove);
				board.addToMoveList(move[0], move[1]);
			} 
			// Max player (our AI)'s turn
			else {
				System.out.println("AI's turn");
				move = mm_maxDepth.execute(move[0], move[1]);
				board.placeOnBoard(move[0], move[1]);
				board.addToMoveList(move[0], move[1]);
			}
		}
		System.out.println("");
		board.printBoard();
		System.out.println("Game ends!");
	}
	
	private int[] getMove() {
		int[] move = new int[2];
		String input;
		do {
			System.out.print("Enter move (Row/Column): ");
			
			input = sc.nextLine();
			if (input.length() == 2) {
				move[0] = Integer.parseInt(input.substring(0, 1));
				move[1] = Integer.parseInt(input.substring(1, 2));
			}
		} while (input.length() != 2);
		
		return move;
	}
	
	private int getSeconds() {
		System.out.print("Enter time to think: ");
		int input = Integer.parseInt(sc.nextLine());
		return input;
	}
	
	/**
	 * Gets who goes first.
	 * @return True if our AI goes first, false otherwise
	 */
	private boolean whoGoesFirst() {
		boolean incorrectInput = true;
		String input;
		
		do {
			System.out.print("Does this AI goes first? Y/N: ");
			input = sc.nextLine();
			
			if (input.length() == 1 && (input.compareToIgnoreCase("n") == 0 ||
					input.compareToIgnoreCase("y") == 0)) incorrectInput = false;
		} while (incorrectInput);
		
		if (input.compareToIgnoreCase("n") == 0) {return false;}
		else {return true;}
	}
	
	public static void main(String[] args) {
		new MainClass();
	}
}
