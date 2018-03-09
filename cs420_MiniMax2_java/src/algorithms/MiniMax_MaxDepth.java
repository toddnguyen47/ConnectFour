package algorithms;

import cs420_p3.Board;
import successors.*;
import java.util.concurrent.ThreadLocalRandom;

public class MiniMax_MaxDepth {
	private final int LARGE_POS = Integer.MAX_VALUE >> 2;
	private final int LARGE_NEG = -(LARGE_POS);
	private int curMaxDepth;
	private int nodesGen;
	
	private Board mainBoard;
	private long startTime, endTime;
	private long maxTime = 30000; 
	private Successors succ;
	
	public MiniMax_MaxDepth(int maxTime) {
		this.maxTime = maxTime * 1000;
		mainBoard = Board.getInstance();
		succ = new Spiral();
	}
	
	public int[] execute(int prevRow, int prevCol) {
		startTime = System.currentTimeMillis();
		int move[];
		curMaxDepth = 3;
		
		int numPieces = mainBoard.getNumOfPiecesPlayed();
		if (numPieces < 3) {
			return randomStartingMoves(prevRow, prevCol);
		}
		else {
			// Begin Iterative Deepening
			int[] returnMove = new int[2];
			while (true) {
				nodesGen = 0;
				move = MiniMax(LARGE_NEG, LARGE_POS, 0, prevRow, prevCol);
				endTime = System.currentTimeMillis();
				
				System.out.println("ply: " + (curMaxDepth) + ", time: " + 
						getTimeDifference(startTime, endTime) / 1000.0 + " sec, " + nodesGen);
				
				if (endTime - startTime >= maxTime || curMaxDepth >= 8)
					return new int[] {returnMove[0], returnMove[1]};
				
				curMaxDepth++;
				returnMove[0] = move[1];
				returnMove[1] = move[2];
			}
		}
	}
	
	private int[] randomStartingMoves(int prevRow, int prevCol) {
		int moves[][];
		// If prevMov and prevCol was NOT in the center
		if (prevRow < 3 || prevRow > 4 || prevCol < 3 || prevCol > 4) {
			moves= new int[][] {{3,3}, {3,4}, {4,3}, {4,4}};
		}
		// If prevMov and prevCol WAS in the center
		else {
			moves = new int[][]{{prevRow, prevCol + 1},
							 {prevRow, prevCol -1},
							 {prevRow + 1, prevCol},
							 {prevRow - 1, prevCol}};
		}
		int[] returnMove;
		boolean validMove = false;
		do {
			int randomNum = ThreadLocalRandom.current().nextInt(0, 4);
			returnMove = moves[randomNum];
			validMove = mainBoard.isValidMove(returnMove[0], returnMove[1]);
		} while (!validMove);
		return returnMove;
	}
	
	private int[] MiniMax(int alpha, int beta, int depth, int prevRow, int prevCol) {
		nodesGen++;
		int[][] board = mainBoard.getCurrentBoard();
		if (cutOffTest(board, depth)) {
			return new int[] {HeuristicFunction.getInstance().getScore(board, depth)};
		}
		
		int successors[][] = succ.getSuccessors(prevRow, prevCol);
		successors = sortMoves(successors, prevRow, prevCol);
		
		// Max player's turn
		if (mainBoard.maxTurn()) {
			int[] value = {LARGE_NEG, 0, 0};
			for (int[] successor : successors) {
				int row = successor[0];
				int col = successor[1];
				mainBoard.placeOnBoard(row, col); // Make the move
				int[] result = MiniMax(alpha, beta, depth + 1, row, col);
				mainBoard.resetBoard(row, col); // Reset the move
				if (result[0] > value[0]) {
					value[0] = result[0];
					value[1] = row;
					value[2] = col;
				}
				if (value[0] >= beta) {	return value;}
				alpha = Math.max(alpha, value[0]);
			}
			return value;
		}
		// Min player's turn
		else {
			int[] value = {LARGE_POS, 0, 0};
			for (int[] successor : successors) {
				int row = successor[0];
				int col = successor[1];
				mainBoard.placeOnBoard(row, col); // Make the move
				int[] result = MiniMax(alpha, beta, depth + 1, row, col);
				mainBoard.resetBoard(row, col); // Reset the move
				if (result[0] < value[0]) {
					value[0] = result[0];
					value[1] = row;
					value[2] = col;
				}
				if (value[0] <= alpha) {return value;}
				beta = Math.min(beta, value[0]);
			}
			return value;
		}
	}
	
	private boolean cutOffTest(int[][] board, int depth) {
		endTime = System.currentTimeMillis();
		if (endTime - startTime >= maxTime)
			return true;
		
		if (depth >= curMaxDepth)
			return true;
		
		return mainBoard.isTerminalBoard();
	}
	
	public long getTimeDifference(long startTime, long endTime) {
		return endTime - startTime;
	}
	
	private int[][] sortMoves(int[][] successors, int prevRow, int prevCol) {
		int successorsSize = successors.length;
		int score[] = new int[successorsSize];
		boolean scoreTaken[] = new boolean[successorsSize];
		int topChildren = Math.min(7, successorsSize);
		HeuristicFunction h = HeuristicFunction.getInstance();
		
		int[][] orderedList = new int[successorsSize][successorsSize];
		int orderedListIndex = 0;
		
		// Get scores
		for (int i = 0; i < successorsSize; i++) {
			int row = successors[i][0];
			int col = successors[i][1];
			mainBoard.placeOnBoard(row, col);
			score[i] = h.getScoreSortMoves(mainBoard.getCurrentBoard(), 0);
			mainBoard.resetBoard(row, col);
			scoreTaken[i] = false; // reset scoreValid array
			
			if (score[i] == 0 && mainBoard.getNumOfPiecesPlayed() < 6 &&
					row >= 3 && row <= 5 &&
					col >= 3 && col <= 4 &&
					prevRow < 3 && prevRow > 4 &&
					prevCol < 3 && prevCol > 4) {
				int randomNum = ThreadLocalRandom.current().nextInt(20, 51);
				if (mainBoard.maxTurn()) score[i] += randomNum;
				else score[i] -= randomNum;
			}
		}
		
		// Organize scores
		for (int repeat = 0; repeat < topChildren; repeat++) {
			int maxScore = -16380, minScore = 16380, desiredIndex = 0;
			for (int j = 0; j < successorsSize; j++) {
				if (mainBoard.maxTurn()) {
					if (!scoreTaken[j] && score[j] > maxScore) {
						desiredIndex = j;
						maxScore = score[j];
					}
				}
				// If min player
				else {
					if (!scoreTaken[j] && score[j] < minScore) {
						desiredIndex = j;
						minScore = score[j];
					}
				}
			}
			scoreTaken[desiredIndex] = true;
			orderedList[orderedListIndex++] = successors[desiredIndex];		
		}
		
		// Add back into the list
		for (int i = 0; i < successorsSize; i++) {
			if (!scoreTaken[i]) {
				orderedList[orderedListIndex++] = successors[i];
			}
		}
		
		return orderedList;
	}
}
