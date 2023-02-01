/**
 * Chess 2.0
 * author: Gisela Wolf
 * date 19.01.2023
 * Issues:
 * - Statebased actions (check, checkmate, remis and caste legality)
 * - userproof move inputs (a1h8)
 * - no skipping of pieces
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class Chessboard {
    public static boolean isWhiteTurn;
    public static boolean blackKingLives = true;
    public static boolean whiteKingLives = true;
    public static int turnCounter;
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
    public static void checkState(){
        boolean blackKingFound = false;
        boolean whiteKingFound = false;

    //Check and Checkmate
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
        //Is the king checked? Is he in checkmate?

    //Remis
        //Are there no legal moves left?
        //Has a check become impossible? King and King, King and King-Knight, king and King-Bishop)
        //Have the players agreed to draw?

    //Promition
        //Is there a pawn on the last row of the other colour? Queening or underpromotion

    //Castling
        //Is castling a legal move? King and rook not moved and free space between them?
    }

//Display
    public void displayBoard(){
        for (int i = 0; i < 9; i++) {
            System.out.println(Arrays.deepToString(chessBoardNew[i]));
        }
    }

}

class Player extends Chessboard {
    ArrayList<String> blackPiecesTaken = new ArrayList<String>();
    ArrayList<String> whitePiecesTaken = new ArrayList<String>();
    String originString;

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
        String pieceTaken = "AA";
        char destinationChar = 'A';
        boolean kingHasMoved = false;
        boolean rookHasMoved = false;

        while(true) {
            //enter the move in standard format "c2c4"
            System.out.println("Make your move. Face your fear.");
            Scanner in = new Scanner(System.in);
            move = in.nextLine();

            //Determine the move's origin string
            originString = "" + move.charAt(0) + move.charAt(1);

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
            pieceTaken = Chessboard.chessBoardNew[destinationCoordinates[0]][destinationCoordinates[1]];

            if (!Chessboard.chessBoardNew[originCoordinates[0]][originCoordinates[1]].equals("00")) {
                isPiece = true;
                if (piece.charAt(0) == 'B') {
                    isBlack = true;
                } else if (piece.charAt(0) == 'W') {
                    isWhite = true;
                }
            }
            destinationChar = Chessboard.chessBoardNew[destinationCoordinates[0]][destinationCoordinates[1]].charAt(0);

            //piece rules still to implement:
            //move crosses other piece (except for knight)
            //Pawn capturing moves
            //endangering/risking the king
            //moves in check
            //castling (!kingHasMoved and !rookHasMoved and space between)
            //promotion

            for(int i = 0; i < moveCoordinates[0]; i++) {
                Chessboard.chessBoardNew[destinationCoordinates[0] + (moveCoordinates[0]-i)]
                                        [destinationCoordinates[1] + moveCoordinates[1]].equals("00");


            }
            switch (piece) {
                case "WP" -> {
                    moveIsLegal = false;
                    //Moves up the board by one or two on first move
                    if (moveCoordinates[0] == -1 && moveCoordinates[1] == 0) {
                        moveIsLegal = true;
                        break;
                    }
                    if ((turnCounter == 1) && (moveCoordinates[0] == -2 && moveCoordinates[1] == 0)) {
                        moveIsLegal = true;
                        break;
                    }
                    //captures diagonally
                    //captures en-passant
                }

                case "BP" -> {
                    moveIsLegal = false;
                    //moves down the board by one
                    //Moves down the board by one or two on first move
                    //Moves up the board by one or two on first move
                    if (moveCoordinates[0] == 1 && moveCoordinates[1] == 0) {
                        moveIsLegal = true;
                        break;
                    }
                    if ((turnCounter == 2) && (moveCoordinates[0] == 2 && moveCoordinates[1] == 0)) {
                        moveIsLegal = true;
                        break;
                    }

                    //captures diagonally
                    //captures en-passant
                }

                case "WR", "BR" -> {
                    moveIsLegal = false;
                    //moves in a straight line, vertical and horizontal
                    if (moveCoordinates[0] < 8 && moveCoordinates[0] > -8 && moveCoordinates[1] == 0) {
                        moveIsLegal = true;
                        rookHasMoved = true;
                        break;
                    }
                    if (moveCoordinates[0] == 0 && moveCoordinates[1] < 8 && moveCoordinates[1] > -8) {
                        moveIsLegal = true;
                        rookHasMoved = true;
                        break;
                    }
                    //cannot skip
                    //describe the spaces between origin and destination. then test if == "00"
                    //can castle long or short
                }

                case "BN", "WN" -> {
                    moveIsLegal = false;
                    //Moves by 2/1
                    if (moveCoordinates[0] == 1 && moveCoordinates[1] == 2) {
                        moveIsLegal = true;
                        break;
                    }
                    if (moveCoordinates[0] == 2 && moveCoordinates[1] == 1) {
                        moveIsLegal = true;
                        break;
                    }
                    if (moveCoordinates[0] == -1 && moveCoordinates[1] == -2) {
                        moveIsLegal = true;
                        break;
                    }
                    if (moveCoordinates[0] == -2 && moveCoordinates[1] == -1) {
                        moveIsLegal = true;
                        break;
                    }
                    if (moveCoordinates[0] == 1 && moveCoordinates[1] == -2) {
                        moveIsLegal = true;
                        break;
                    }
                    if (moveCoordinates[0] == 2 && moveCoordinates[1] == -1) {
                        moveIsLegal = true;
                        break;
                    }
                    if (moveCoordinates[0] == -1 && moveCoordinates[1] == 2) {
                        moveIsLegal = true;
                        break;
                    }
                    if (moveCoordinates[0] == -2 && moveCoordinates[1] == 1) {
                        moveIsLegal = true;
                        break;
                    }
                    //can skip other pieces (and thus move on turn 1)
                }

                case "BB", "WB" -> {
                    moveIsLegal = false;
                    //moves diagonally
                    if ((moveCoordinates[0] < 8 && moveCoordinates[0] > 0) && (moveCoordinates[1] < 8 && moveCoordinates[1] > 0)) {
                        moveIsLegal = true;
                        break;
                    }
                    if ((moveCoordinates[0] > -8 && moveCoordinates[0] < 0) && (moveCoordinates[1] > -8 && moveCoordinates[1] < 0)) {
                        moveIsLegal = true;
                        break;
                    }
                    if ((moveCoordinates[0] < 8 && moveCoordinates[0] > 0) && (moveCoordinates[1] > -8 && moveCoordinates[1] < 0)) {
                        moveIsLegal = true;
                        break;
                    }
                    if ((moveCoordinates[0] > -8 && moveCoordinates[0] < 0) && (moveCoordinates[1] < 8 && moveCoordinates[1] > 0)) {
                        moveIsLegal = true;
                        break;
                    }
                    //cannot skip other pieces
                }

                case "BQ", "WQ" -> {
                    moveIsLegal = false;
                    //moves diagonally
                    if ((moveCoordinates[0] < 8 && moveCoordinates[0] > 0) && (moveCoordinates[1] < 8 && moveCoordinates[1] > 0)) {
                        moveIsLegal = true;
                        break;
                    }
                    if ((moveCoordinates[0] > -8 && moveCoordinates[0] < 0) && (moveCoordinates[1] > -8 && moveCoordinates[1] < 0)) {
                        moveIsLegal = true;
                        break;
                    }
                    if ((moveCoordinates[0] < 8 && moveCoordinates[0] > 0) && (moveCoordinates[1] > -8 && moveCoordinates[1] < 0)) {
                        moveIsLegal = true;
                        break;
                    }
                    if ((moveCoordinates[0] > -8 && moveCoordinates[0] < 0) && (moveCoordinates[1] < 8 && moveCoordinates[1] > 0)) {
                        moveIsLegal = true;
                        break;
                    }
                    //moves in a straight line, vertical and horizontal
                    if (moveCoordinates[0] < 8 && moveCoordinates[0] > -8 && moveCoordinates[1] == 0) {
                        moveIsLegal = true;
                        break;
                    }
                    if (moveCoordinates[0] == 0 && moveCoordinates[1] < 8 && moveCoordinates[1] > -8) {
                        moveIsLegal = true;
                        break;
                    }
                    //cannot skip
                }

                case "BK", "WK" -> {
                    moveIsLegal = false;
                    //moves by one in any direction
                    if (moveCoordinates[0] == 1 && moveCoordinates[1] == 1) {
                        moveIsLegal = true;
                        kingHasMoved = true;
                        break;
                    }
                    if (moveCoordinates[0] == 1 && moveCoordinates[1] == 0) {
                        moveIsLegal = true;
                        kingHasMoved = true;
                        break;
                    }
                    if (moveCoordinates[0] == 1 && moveCoordinates[1] == -1) {
                        moveIsLegal = true;
                        kingHasMoved = true;
                        break;
                    }
                    if (moveCoordinates[0] == 0 && moveCoordinates[1] == -1) {
                        moveIsLegal = true;
                        kingHasMoved = true;
                        break;
                    }
                    if (moveCoordinates[0] == -1 && moveCoordinates[1] == -1) {
                        moveIsLegal = true;
                        kingHasMoved = true;
                        break;
                    }
                    if (moveCoordinates[0] == -1 && moveCoordinates[1] == 0) {
                        moveIsLegal = true;
                        kingHasMoved = true;
                        break;
                    }
                    if (moveCoordinates[0] == -1 && moveCoordinates[1] == 1) {
                        moveIsLegal = true;
                        kingHasMoved = true;
                        break;
                    }
                    if (moveCoordinates[0] == 0 && moveCoordinates[1] == 1) {
                        moveIsLegal = true;
                        kingHasMoved = true;
                        break;
                    }

                    //has to be protected
                    //May not be endangered

                }

                default -> {
                }
            }

            //compare the move to the general rules
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


            System.out.println("------------------------ "
                    + "\r\n" + "Move coordinates: " + moveCoordinates[0] + moveCoordinates[1]
                    + "\r\n" + "move is legal: "    + moveIsLegal
                    + "\r\n" + "the piece you chose "    + piece
                    + "\r\n" + "its white's turn: " + isWhiteTurn
                    + "\r\b" + "the piece you took "     + pieceTaken
                    + "\r\n" + "------------------------"
            );

            if (!moveIsLegal) {
                System.out.println("I can't let you do that.");
            } else {
                //move is legal:
                //case: there was a enemy piece there = piece gets taken
                if((pieceTaken.charAt(0) == 'B' && isWhiteTurn) || (pieceTaken.charAt(0) == 'W' && !isWhiteTurn)){
                    if(pieceTaken.charAt(0) == 'B'){
                        blackPiecesTaken.add(pieceTaken);
                    }else if(pieceTaken.charAt(0) == 'W'){
                        whitePiecesTaken.add(pieceTaken);
                    }
                }
                //delete piece from destination (=00)
                Chessboard.chessBoardNew[originCoordinates[0]][originCoordinates[1]] = "00";
                //insert piece into destination (=BP)
                Chessboard.chessBoardNew[destinationCoordinates[0]][destinationCoordinates[1]] = piece;

                System.out.println("next up!");
                break;
            }


        }
    }

    //choices
    public void concede(){}
    public void offerRemis(){}
    public void castle(){}
    public void resetTheBoard(){}


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
            //Counts the turns
            Chessboard.turnCounter ++;
            //Display options for player black or white?
            if (Chessboard.isWhiteTurn) {
                System.out.println("Player White, choose wisely: " + "Move " + "Castle " + "Offer Remis " + "Concede " + "Reset the board ");
            } else {
                System.out.println("Player Black, choose wisely: " + "Move " + "Castle " + "Offer Remis " + "Concede " + "Reset the board ");
            }

            //Checks gamestates
            Chessboard.checkState();


            //+++++PLAYER MOVE++++++
            if (Chessboard.isWhiteTurn) {
                playerWhite.move();
            } else {
                playerBlack.move();
            }
            //*****PLAYER MOVE******


            //display the new board and pieces taken
            chessboard.displayBoard();
            System.out.println("Player Black: " + playerBlack.whitePiecesTaken);
            System.out.println("Player White: " + playerWhite.blackPiecesTaken);

        }
    }
}



