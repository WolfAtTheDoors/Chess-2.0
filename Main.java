import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class Chessboard {
    boolean blackKingLives = true;
    boolean whiteKingLives = true;
    boolean isWhiteturn = false;

    static String[][] chessBoardOrigin = {
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



    }

//Display
    public void displayBoard(){
        for (int i = 0; i < 9; i++) {
            System.out.println(Arrays.deepToString(chessBoardNew[i]));
        }
    }

}

class Player {
    ArrayList<String> piecesTaken = new ArrayList<String>();
    String destination;
    String origin;

    //constructor
    public void player() {
    }

    //moves
    public void move() {
        String move = "abcdefgh";
        int[] moveCoordinates = {0, 0, 0, 0};
        int[] originCoordinates = {0, 0};
        int[] destinationCoordinates = {0, 0};
        char[] columns = {'-', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};

        //enter the move in standard format "g1e1"
        System.out.println("Face your fear. Make your move.");
        Scanner in = new Scanner(System.in);
        move = in.nextLine();

        //Determine the move's origin and goal
        origin = "" + move.charAt(0) + move.charAt(1);
        destination = "" + move.charAt(2) + move.charAt(3);

        //convert origin and destination to board coordinates.
        for (int i = 0; i < 9; i++) {
            if (columns[i] == move.charAt(0)) {
                originCoordinates[0] = i;
            }
        }
        originCoordinates[1] = 9 - Character.getNumericValue(move.charAt(1)) ;
        for (int i = 0; i < 9; i++) {
            if (columns[i] == move.charAt(2)) {
                destinationCoordinates[0] = i;
            }
        }
        destinationCoordinates[1] = 9 - Character.getNumericValue(move.charAt(3));

        System.out.println("DEBUG: "
                +"\r\n"  + originCoordinates[0]
                +"\r\n"  + originCoordinates[1]
                +"\r\n"  + destinationCoordinates[0]
                +"\r\n"  + destinationCoordinates[1]
        );

        //parse if there is a piece and what color
        boolean isPiece = false;
        boolean isWhite = false;
        boolean isBlack = false;
        String piece = "AA";
        if (!Chessboard.chessBoardNew[originCoordinates[0]][originCoordinates[1]].equals("00")) {
            isPiece = true;
            piece = Chessboard.chessBoardNew[originCoordinates[0]][originCoordinates[1]];
            if(piece.charAt(0) == 'B'){
                isBlack = true;
            }else if(piece.charAt(0) == 'W'){
                isWhite = true;
            }
        }

        //parse what kind of piece
        if (isPiece) {
            switch (piece) {
                case "WP":
                    break;
                case "WR":
                    break;
                case "WN":
                    break;
                case "WB":
                    break;
                case "WQ":
                    break;
                case "WK":
                    break;
                case "BP":
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
        }
    }

        //compare the move to the rules
            //piece is correct colour
            //piece moveset disallows
            //move crosses other piece
            //move endangers King
            //move ignores checked King (enemy and own)
            //destination contains own piece

        //If move is legal
            //delete piece from destination
            //insert piece into destination
        //case: there was a enemy piece there = piece gets taken
        //case: pawn lands on last row = promotion
        //case: castle move

       // System.out.printline("I can't let you do that");



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
      //while(chessboard.blackKingLives && chessboard.whiteKingLives) {

            //change player turn (white true, black false)
            chessboard.isWhiteturn = !chessboard.isWhiteturn;
                //Check colour of piece. isWhite oder isBlack.

        //Display options for player black or white

        //player move
              if(chessboard.isWhiteturn) {
                  playerWhite.move();
                 }else{
                  playerBlack.move();
                         }

            //display the new board and pieces taken
            chessboard.displayBoard();
            System.out.println("Player Black: " +playerBlack.piecesTaken);
            System.out.println("Player White: " +playerWhite.piecesTaken);



            //check the board for winstate
             chessboard.checkState();
     //   }
    }
}



