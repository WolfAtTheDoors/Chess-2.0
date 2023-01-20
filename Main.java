/**
 * Chess 2.0
 * author: Gisela Wolf
 * date 19.01.2023
 * Issues:
 * - check and checkmate states
 * - draw states
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class Chessboard {
    public static boolean isWhiteTurn;
    public static boolean blackKingLives = true;
    public static boolean whiteKingLives = true;

    final static String[][] chessBoardOrigin = {
            {"-", " a", " b", " c", " d", " e", " f", " g", " h"},
            {"8", "BR", "BN", "BB", "BQ", "BK", "BB", "BN", "BR"},
            {"7", "BP", "BP", "BP", "BP", "BP", "BP", "BP", "BP"},
            {"6", "00", "00", "00", "00", "00", "00", "00", "00"},
            {"5", "00", "00", "00", "00", "00", "00", "00", "00"},
            {"4", "00", "00", "00", "00", "00", "00", "00", "00"},
            {"3", "00", "00", "00", "00", "00", "00", "00", "00"},
            {"2", "WP", "WP", "WP", "WP", "WP", "WP", "WP", "WP"},
            {"1", "WR", "WN", "WB", "WQ", "WK", "WB", "WN", "WR"},
                                    };
    static String[][] chessBoardNew = chessBoardOrigin;

//constructor
    public void chessboard(){}

//Gamestate checks
    public void checkState(){
        boolean blackKingFound = false;
        boolean whiteKingFound = false;

        //Is the King alive?
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(chessBoardNew[i][j].equals("BK")){
                    blackKingFound = true;
                }
                if(chessBoardNew[i][j].equals("WK")){
                    whiteKingFound = true;
                }
            }
        }

        if(!blackKingFound){
            blackKingLives = false;
            System.out.println(" And the Winner is: WHITE" );

        }else if(!whiteKingFound){
            whiteKingLives = false;
            System.out.println(" And the Winner is: BLACK" );
        }else{
            blackKingFound = false;
            whiteKingFound = false;
        }

        //Are only two pieces left alive?
        //Is the King checked? Is he in checkmate?
        //Is there a pawn on the last row of the other colour? Queening or underpromotion
    }

//Display
    public void displayBoard(){
        for (int i = 0; i < 9; i++) {
            System.out.println(Arrays.deepToString(chessBoardNew[i]));
        }
    }

}

class Player extends Chessboard {
    ArrayList<String> piecesTaken = new ArrayList<String>();
    String origin;

    //constructor
    public void player() {
    }

    //moves
    public void move() {
        boolean moveIsLegal = true;
        String move = "abcdefgh";
        int[] originCoordinates = {0, 0};
        int[] destinationCoordinates = {0, 0};
        int[] moveCoordinates = {0, 0};
        char[] columns = {'-', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        boolean isPiece = false;
        boolean isWhite = false;
        boolean isBlack = false;
        String piece = "AA";
        char destinationChar = 'A';
        String destinationString ="AA";

        while(true) {
            //enter the move in standard format "c2c4"
            System.out.println("Make your move. Face your fear.");
            Scanner in = new Scanner(System.in);
            move = in.nextLine();

            //Determine the move's origin and goal
            origin = "" + move.charAt(0) + move.charAt(1);
            destinationString = "" + move.charAt(2) + move.charAt(3);

            //convert origin and destination to board coordinates.
            for (int i = 0; i < 9; i++) {
                if (columns[i] == move.charAt(0)) {
                    originCoordinates[1] = i;
                }
            }
            originCoordinates[0] = 9 - Character.getNumericValue(move.charAt(1));
            for (int i = 0; i < 9; i++) {
                if (columns[i] == move.charAt(2)) {
                    destinationCoordinates[1] = i;
                }
            }
            destinationCoordinates[0] = 9 - Character.getNumericValue(move.charAt(3));

            //determine the moveCoordinates
            moveCoordinates[0] = destinationCoordinates[0] - originCoordinates[0];
            moveCoordinates[1] = destinationCoordinates[1] - originCoordinates[1];

            //parse if there is a piece and what color
            piece = Chessboard.chessBoardNew[originCoordinates[0]][originCoordinates[1]];
            if (!Chessboard.chessBoardNew[originCoordinates[0]][originCoordinates[1]].equals("00")) {
                isPiece = true;
                if (piece.charAt(0) == 'B') {
                    isBlack = true;
                } else if (piece.charAt(0) == 'W') {
                    isWhite = true;
                }
            }
            destinationChar = Chessboard.chessBoardNew[destinationCoordinates[0]][destinationCoordinates[1]].charAt(0);

            //compare the move to the rules
            //piece is a piece
            if (!isPiece) {
                System.out.println("there is no one here.");
                moveIsLegal = false;
            }
            //piece is correct colour
            if (isPiece && (!((Chessboard.isWhiteTurn && isWhite) || (!isWhiteTurn && isBlack)))) {
                System.out.println("That life is not yours to spend.");
                moveIsLegal = false;
            }
            //destination contains own piece
            if ((destinationChar == 'W' && Chessboard.isWhiteTurn) || (destinationChar == 'B' && !Chessboard.isWhiteTurn)) {
                System.out.println("That space is taken.");
                moveIsLegal = false;
            }

            //move endangers King
            //move ignores checked King (enemy and own)

            //piece rules:
            //piece moveset disallows
            //move crosses other piece (except for knight)

            //case: castle move

            switch (piece) {
                case "WP":
                    //moves up the board by one
                    if(!(moveCoordinates[0] == 0 && moveCoordinates[1] == 1
                      ||(moveCoordinates[0] == 0 && moveCoordinates[1] == 2)
                      ||
                      ||
                    ))
                    {
                        moveIsLegal = false;
                    }
                    //Moves up the board by one or two on first move
                    //captures diagonally
                    //captures en-passant

                    break;
                case "WR":
                    //moves in a straight line, up or down
                    //cannot skip
                    //can castle long or short

                    break;
                case "WN":
                    //Moves by 2/1
                    //can skip other pieces and move on turn 1
                    break;
                case "WB":
                    //moves diagonally
                    //cannot skip other pieces
                    break;
                case "WQ":
                    //moves in all directions
                    //cannot skip?
                    break;
                case "WK":
                    //moves by one in any direction
                    //has to be protected
                    break;
                case "BP":
                    //moves down the board by one
                    //Moves down the board by one or two on first move
                    //captures diagonally
                    //captures en-passant
                    break;
                case "BR":
                    break;
                case "BN":
                    break;
                case "BB":
                    break;
                case "BQ":
                    break;
                case "BK":
                    break;
                default:
                    break;
            }

            System.out.println("------------------------ "
                    + "\r\n" + moveCoordinates[0]
                    + "\r\n" + moveCoordinates[1]
                    + "\r\n" + "move is legal: " + moveIsLegal
                    + "\r\n" + "its white's turn: " + isWhiteTurn
                    + "\r\n" + "------------------------"
            );

            if (!moveIsLegal) {
                System.out.println("I can't let you do that.");
                moveIsLegal = true;
            } else {
                System.out.println("next up!");
                break;
            }

            //If move is legal:
            //delete piece from destination (=00)
            //insert piece into destination (=BP)
            //case: there was a enemy piece there = piece gets taken
            //case: pawn lands on last row = promotion

        }
    }

//choices
    public void concede(){}
    public void offerDraw(){}
    public void announceCheck(){}

}

public class Main {
    public static void main(String[] args) {
        Chessboard chessboard = new Chessboard();
        Player playerWhite = new Player();
        Player playerBlack = new Player();

        //Display reset Board
        for (int i = 0; i < 9; i++) {
            System.out.println(Arrays.deepToString(Chessboard.chessBoardOrigin[i]));
        }

        //Game Loop
        while (Chessboard.blackKingLives && Chessboard.whiteKingLives) {
            //change player turn (white true, black false)
            Chessboard.isWhiteTurn = !Chessboard.isWhiteTurn;

            //Display options for player black or white

            //player move
            if (Chessboard.isWhiteTurn) {
                playerWhite.move();
            } else {
                playerBlack.move();
            }

        //display the new board and pieces taken
        chessboard.displayBoard();
        System.out.println("Player Black: " + playerBlack.piecesTaken);
        System.out.println("Player White: " + playerWhite.piecesTaken);

        //check the board for state based action
        chessboard.checkState();

           }
        }
    }



