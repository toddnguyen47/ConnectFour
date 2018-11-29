package algorithms;

import mainBoard.Board;
import successors.*;
import java.util.concurrent.ThreadLocalRandom;

public class MiniMax_IterativeDeepening {
    private final int LARGE_POS = Integer.MAX_VALUE >> 2;
    private final int LARGE_NEG = -(LARGE_POS);
    private final int INIT_MAX_DEPTH = 3;
    private int FINAL_MAX_DEPTH = 8;
    
    private int curMaxDepth;
    private int nodesGen;
    private int returnMove[];
    private int numPieces;
    
    private Board mainBoard;
    private long startTime, endTime;
    private long maxTime = 30000; 
    private Successors succ;
    
    public MiniMax_IterativeDeepening(int maxTime, int maxDepth) {
        this.maxTime = maxTime * 1000;
        this.FINAL_MAX_DEPTH = maxDepth;
        
        returnMove = new int[2];
        mainBoard = Board.getInstance();
        succ = new Spiral();
    }
    
    public int[] execute(int prevRow, int prevCol) {
        startTime = System.currentTimeMillis();
        int move[];
        curMaxDepth = INIT_MAX_DEPTH;
        
        numPieces = mainBoard.getNumOfPiecesPlayed();
        if (numPieces < 3) {
            return randomStartingMoves(prevRow, prevCol);
        }
        else {
            // Begin Iterative Deepening
            while (true) {
                nodesGen = 0;
                boolean isNewMaxDepth = (curMaxDepth > INIT_MAX_DEPTH);
                move = MiniMax(LARGE_NEG, LARGE_POS, 0, prevRow, prevCol, isNewMaxDepth);
                endTime = System.currentTimeMillis();
                
                // Return best move
                if (endTime - startTime >= maxTime || curMaxDepth >= FINAL_MAX_DEPTH) {
                    System.out.println("\nFinished executing for this turn.");
                    System.out.println("Time elapsed: " + getTimeDiff(startTime, endTime) / 1000.0 +
                            " sec");

                    if (curMaxDepth < FINAL_MAX_DEPTH) {
                        System.out.println("Did not reach ply " + curMaxDepth + ", " + nodesGen +
                                " nodes generated");
                    } else {
                        System.out.println("ply: " + (curMaxDepth) + ", time: " + 
                                getTimeDiff(startTime, endTime) / 1000.0 + " sec, " + 
                                nodesGen + " nodes generated");
                    }

                    return new int[] {returnMove[0], returnMove[1]};
                }
                
                System.out.println("ply: " + (curMaxDepth) + ", time: " + 
                        getTimeDiff(startTime, endTime) / 1000.0 + " sec, " + 
                        nodesGen + " nodes generated");
                
                curMaxDepth++;
                returnMove[0] = move[1];
                returnMove[1] = move[2];
            }
        }
    }
    
    private int[] randomStartingMoves(int prevRow, int prevCol) {
        int moves[][] = new int[0][0];
        
        // If first two moves
        if (numPieces < 2) {
            moves = new int[][] {{3,3}, {3,4}, {4,3}, {4,4}};
        }
        
        else {
            int[] startingMove = mainBoard.getFirstMove();
            if (startingMove != null) {
                int startingRow = startingMove[0];
                int startingCol = startingMove[1];
                int newRow = 0, newCol = 0;
                
                if (startingRow == 3) {newRow = startingRow + 1;}
                else if (startingRow == 4) {newRow = startingRow - 1;}
                
                if (startingCol == 3) {newCol = startingCol + 1;}
                else if (startingCol == 4) {newCol = startingCol - 1;}
                
                moves = new int[][] {{startingRow, newCol}, {newRow, startingCol}};
            }
        }
        
        int[] returnMoveT;
        boolean validMove = false;
        do {
            int randomNum = ThreadLocalRandom.current().nextInt(0, moves.length);
            returnMoveT = moves[randomNum];
            validMove = mainBoard.isValidMove(returnMoveT[0], returnMoveT[1]);
        } while (!validMove);
        return returnMoveT;
        
        // If prevMov and prevCol was NOT in the center
        /*if (prevRow < 3 || prevRow > 4 || prevCol < 3 || prevCol > 4) {
            
        }
        // If prevMov and prevCol WAS in the center
        else {
            moves = new int[][]{{prevRow, prevCol + 1},
                             {prevRow, prevCol -1},
                             {prevRow + 1, prevCol},
                             {prevRow - 1, prevCol}};
        }*/
    }
    
    /**
     * Main alpha-beta MiniMax algorithm
     * @param alpha
     * @param beta
     * @param depth
     * @param prevRow - row of previous move
     * @param prevCol - col of previous move
     * @param isNewMaxDepth - if this is true, the first successor will be the move
     * from the previous iteration. This should only be true the first time a new
     * iteration is ran.
     * @return
     */
    private int[] MiniMax(int alpha, int beta, int depth, int prevRow,
            int prevCol, boolean isNewMaxDepth) {
        nodesGen++;
        int[][] board = mainBoard.getCurrentBoard();
        if (cutOffTest(board, depth)) {
            return new int[] {HeuristicFunction.getInstance().getScore(board, depth)};
        }
        
        int successors[][];
        // Use the last iteration's best move as the starting child
        if (isNewMaxDepth) {
            successors = succ.getSuccessors(prevRow, prevCol, returnMove[0], returnMove[1]);
        } else {
            successors = succ.getSuccessors(prevRow, prevCol);
            successors = sortMoves(successors, prevRow, prevCol, isNewMaxDepth);
        }
        
        // Max player's turn
        if (mainBoard.maxTurn()) {
            int[] value = {LARGE_NEG, 0, 0};
            for (int[] successor : successors) {
                int row = successor[0];
                int col = successor[1];
                mainBoard.placeOnBoard(row, col); // Make the move
                int[] result = MiniMax(alpha, beta, depth + 1, row, col, false);
                mainBoard.resetBoard(row, col); // Reset the move
                if (result[0] > value[0]) {
                    value[0] = result[0];
                    value[1] = row;
                    value[2] = col;
                }
                if (value[0] >= beta) {    return value;}
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
                int[] result = MiniMax(alpha, beta, depth + 1, row, col, false);
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
    
    public long getTimeDiff(long startTime, long endTime) {
        return endTime - startTime;
    }
    
    private int[][] sortMoves(int[][] successors, int prevRow, int prevCol, boolean isNewMaxDepth) {
        int successorsSize = successors.length;
        int score[] = new int[successorsSize];
        boolean scoreTaken[] = new boolean[successorsSize];
        int topChildren = Math.min(7, successorsSize);
        HeuristicFunction h = HeuristicFunction.getInstance();
        
        int[][] orderedList = new int[successorsSize][successorsSize];
        int orderedListIndex = 0;
        int nonZeroCounter = 0;
        int beginIndex;
        beginIndex = 0;
        
        // Get scores
        for (int i = beginIndex; i < successorsSize; i++) {
            int row = successors[i][0];
            int col = successors[i][1];
            mainBoard.placeOnBoard(row, col);
            score[i] = h.getScoreSortMoves(mainBoard.getCurrentBoard());
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
            
            if (score[i] != 0) {nonZeroCounter++;}
        }
        
        // Organize scores
        for (int repeat = 0; repeat < topChildren; repeat++) {
            int maxScore = -16380, minScore = 16380, desiredIndex = 0;
            for (int j = beginIndex; j < successorsSize; j++) {
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
        
        // Random ordering?
        if (nonZeroCounter > 2) {
            // Random ordering?
        }
        
        // Add back into the list
        for (int i = beginIndex; i < successorsSize; i++) {
            if (!scoreTaken[i]) {
                orderedList[orderedListIndex++] = successors[i];
            }
        }
        
        return orderedList;
    }
}
