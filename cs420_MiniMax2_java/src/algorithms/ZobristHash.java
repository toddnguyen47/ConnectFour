package algorithms;

import java.util.concurrent.ThreadLocalRandom;

import mainBoard.Board;

public class ZobristHash {
	private int[][] initScores;
	
	public ZobristHash(int size, int numOfPieces) {
		initScores = new int[size][numOfPieces];
		initialize();
	}
	
	private void initialize() {
		int randomMax = Integer.MAX_VALUE >> 4;
		for (int i = 0; i < initScores.length; i++) {
			for (int j = 0; j < initScores[i].length; j++) {
				initScores[i][j] = ThreadLocalRandom.current().nextInt(0, randomMax);
			}
		}
	}
	
	public String getHash(int[][] board) {
		int h = 0;
		
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				int initScoreIndex = i * board.length + j;
				// If max player
				if (board[i][j] == Board.PLAYER_MAX) {
					h = h ^ initScores[initScoreIndex][0];
				}
				// If min player
				else if (board[i][j] == Board.PLAYER_MIN) {
					h = h ^ initScores[initScoreIndex][1];
				}
			}
		}
		
		return String.valueOf(h);
	}
}
