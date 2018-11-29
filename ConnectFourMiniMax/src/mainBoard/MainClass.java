package mainBoard;

import java.util.Scanner;

import algorithms.MiniMax_IterativeDeepening;

public class MainClass {

    Board board;
    int move[];
    Scanner sc;
    MiniMax_IterativeDeepening mm_maxDepth;
    
    public MainClass() {
        sc = new Scanner(System.in);
        move = new int[2];
        move[0] = 0; // first and second moves are "hard coded"
        move[1] = 0; // first and second moves are "hard coded"
        board = Board.getInstance();
        
        int seconds = this.getSeconds();
        int maxDepth = this.getMaxDepth();
        mm_maxDepth = new MiniMax_IterativeDeepening(seconds, maxDepth);
        
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
        if (!board.maxTurn()) {
            System.out.println("Our AI won!");
        } else {
            System.out.println("Their AI won... better luck next time.");
        }
    }
    
    private int[] getMove() {
        int[] boardInput = {0,1,2,3,4,5,6,7};
        int[] move = new int[2];
        int tempRow, tempCol;
        boolean correctInput = false;
        String input;
        do {
            System.out.print("Enter move. Enter -1 to quit: ");
            input = sc.nextLine();
            if (input.length() == 2) {            
                tempRow = input.charAt(0) - 'a';
                tempCol = input.charAt(1) - '1';
                
                if (tempRow >= 0 && tempRow <= Board.BOARD_SIZE - 1 &&
                        tempCol >= 0 && tempCol <= Board.BOARD_SIZE - 1) {
                    move[0] = boardInput[tempRow];
                    move[1] = boardInput[tempCol];
                    correctInput = true;
                }
            }
            
        } while (!input.equalsIgnoreCase("-1") && (input.length() != 2 || !correctInput));
        
        if (input.equalsIgnoreCase("-1")) {
            System.out.println("Thank you for playing!");
            System.exit(0);
        }
        
        return move;
    }
    
    private int getSeconds() {
        System.out.print("Enter time to think: ");
        int input = Integer.parseInt(this.sc.nextLine());
        return input;
    }
    
    
    private int getMaxDepth() {
        System.out.print("Enter max depth: ");
        int input = Integer.parseInt(this.sc.nextLine());
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
