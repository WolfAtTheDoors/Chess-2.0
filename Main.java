/**
 * Chess 2.0 is a chess game. It is a work in progress.
 * @author: Gisela Wolf
 * @version: 0.8
 * @since: 9.03.2023
 * TODO:
 * - State based actions (check, checkmate, risk and castle legality)
 * - moves in check, moves to protect, move when the enemy king is checked
 * - remis
 * - edge cases: King checked by a pawn over two steps (pawn has not moved)
 * - colored Strings (ANSI escape?)
 * - error in diagonal moves
 * */

import com.sun.source.tree.WhileLoopTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
class Chessboard {
    public static boolean isWhiteTurn;
    public static boolean blackKingLives = true;
    public static boolean whiteKingLives = true;
    public static boolean blackKingChecked = false;
    public static boolean whiteKingChecked = false;
    public static int turnCounter = 1;

    //colored strings
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    static String coloredString(String s, String c){
        return c + s + ANSI_RESET;
    }
    static String WP =  coloredString("WP", ANSI_WHITE);
    static String __ =  coloredString("__", ANSI_BLACK);

    final static String[][] chessBoardOrigin = {
            {" ", " a", " b", " c", " d", " e", " f", " g", " h", " "},
            {"8", "__", "BR", "BB", "BQ", "BK", "BB", "BN", "BR", "8"},
            {"7", "__", "__", "__", "BP", "BP", "BP", "BP", "BP", "7"},
            {"6", "__", "__", "__", "__", "__", "__", "__", "__", "6"},
            {"5", "BR", "WK", "__", "__", "__", "__", "__", "BR", "5"},
            {"4", "__", "__", "__", "__", "__", "__", "__", "__", "4"},
            {"3", "__", "__", "__", "__", "__", "__", "__", "__", "3"},
            {"2", "__", "__", "__", "WP", "WP", "WP", "WP", "WP", "2"},
            {"1", "__", "BR", "WB", "WQ", "__", "WB", "WN", "WR", "1"},
            {" ", " a", " b", " c", " d", " e", " f", " g", " h", " "},
    };

    static String[][] chessBoardNew = chessBoardOrigin;
    static String[][][] chessBoardTurnByTurn = new String[900][10][10];

    //constructor
    public void chessboard() {
    }

    //Gamestate checks
    public static void checkState() {
        boolean blackKingFound = false;
        boolean whiteKingFound = false;

        //Is the King alive? Long live the king
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (chessBoardNew[i][j].equals("BK")) {
                    blackKingFound = true;
                }
                if (chessBoardNew[i][j].equals("WK")) {
                    whiteKingFound = true;
                }
            }
        }
        if (!blackKingFound) {
            blackKingLives = false;
            System.out.println(" And the Winner is: WHITE");

        } else if (!whiteKingFound) {
            whiteKingLives = false;
            System.out.println(" And the Winner is: BLACK");
        } else {
            blackKingFound = false;
            whiteKingFound = false;
        }

        //Is the white king checked?
        if(whiteKingLives) {
            checkIsWhiteChecked();
        }
        //Is the black King checked?
        if(blackKingLives) {
            checkIsBlackChecked();
        }
        //can both be checked? Can both be checkmate?

        //Checkmate. Is the checked king in checkmate?


        //Remis
        //Are there no legal moves left?
        //Has a check become impossible? (King and King, King and King-Knight, king and King-Bishop)
        //Have the players agreed to draw?

        //Promotion
        //White
        for (int i = 1; i < 8; i++) {
            if (chessBoardNew[1][i].equals("WP")) {
                String pawnsChoice;
                Scanner in = new Scanner(System.in);
                System.out.println("Your Pawn has achieved great things.");
                System.out.println("She may now choose who she wants to be:" + "\r\n"
                        + "(R) Rook, (K) Knight, (B) Bishop (Q) Queen");
                pawnsChoice = in.nextLine();

                switch (pawnsChoice) {
                    case "R", "r" -> {
                        chessBoardNew[1][i] = "WR";
                        System.out.println("A Rook She shall be.");
                        Chessboard.displayBoard();
                    }
                    case "K", "k" -> {
                        chessBoardNew[1][i] = "WN";
                        System.out.println("A Knight She shall be.");
                        Chessboard.displayBoard();
                    }
                    case "B", "b" -> {
                        chessBoardNew[1][i] = "WB";
                        System.out.println("A Bishop She shall be.");
                        Chessboard.displayBoard();
                    }
                    case "Q", "q" -> {
                        chessBoardNew[1][i] = "WQ";
                        System.out.println("A Queen She shall be.");
                        Chessboard.displayBoard();
                    }
                }
            }
        }
        //Black
        for (int i = 1; i < 8; i++) {
            if (chessBoardNew[8][i].equals("BP")) {
                String pawnsChoice;
                Scanner in = new Scanner(System.in);
                System.out.println("                     Your Pawn has achieved great things.");
                System.out.println("She may now choose who she wants to be: (R) Rook, (K) Knight, (B) Bishop (Q) Queen");
                pawnsChoice = in.nextLine();

                switch (pawnsChoice) {
                    case "R", "r" -> {
                        chessBoardNew[8][i] = "BR";
                        System.out.println("A Rook She shall be.");
                        Chessboard.displayBoard();
                    }
                    case "K", "k" -> {
                        chessBoardNew[8][i] = "BN";
                        System.out.println("A Knight She shall be.");
                        Chessboard.displayBoard();
                    }
                    case "B", "b" -> {
                        chessBoardNew[8][i] = "BB";
                        System.out.println("A Bishop She shall be.");
                        Chessboard.displayBoard();
                    }
                    case "Q", "q" -> {
                        chessBoardNew[8][i] = "BQ";
                        System.out.println("A Queen She shall be.");
                        Chessboard.displayBoard();
                    }
                }
            }
        }
    }

    public static boolean checkIsWhiteChecked() {

            int[] whiteKingCoordinates = {0, 0};

            //where is the dude?
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (chessBoardNew[i][j].equals("WK")) {
                        whiteKingCoordinates[0] = i;
                        whiteKingCoordinates[1] = j;
                    }
                }
            }

            //Is there a pawn?
            if ((chessBoardNew[whiteKingCoordinates[0] - 1][whiteKingCoordinates[1] - 1].equals("BP")
                    || chessBoardNew[whiteKingCoordinates[0] - 1][whiteKingCoordinates[1] + 1].equals("BP"))
            ) {
                System.out.println("A pawn has the white king by the throat. Check!");
                whiteKingChecked = true;
            }

            System.out.println(
                            "*************DEBUG*****************" + "\r\n"
                            + "White King Coordinate 0: " + whiteKingCoordinates[0] + "\r\n"
                            + "White King Coordinate 1: " + whiteKingCoordinates[1] + "\r\n"
                            + "Who is here?" + chessBoardNew[whiteKingCoordinates[0] - 3][whiteKingCoordinates[1]] + "\r\n"
                            + "*************DEBUG*****************"
            );

            //Is there a rook?
            //North
            for (int i = 1; i < 10; i++) {
                if (chessBoardNew[whiteKingCoordinates[0] - i][whiteKingCoordinates[1]].equals("BR")) {
                    System.out.println("A rook has the white king by the throat. Check!");
                    whiteKingChecked = true;
                }
                if (chessBoardNew[whiteKingCoordinates[0] - i][whiteKingCoordinates[1]].equals("__")) {
                    continue;
                } else {
                    break;
                }
            }
            //East
            for (int i = 1; i < 10; i++) {
                    if (chessBoardNew[whiteKingCoordinates[0]][whiteKingCoordinates[1] + i].equals("BR")) {
                        System.out.println("A rook has the white king by the throat. Check!");
                        whiteKingChecked = true;
                    }
                    if (chessBoardNew[whiteKingCoordinates[0]][whiteKingCoordinates[1] +i].equals("__")) {
                        continue;
                    } else {
                        break;
                    }
                }
            //South
            for (int i = 1; i < 10; i++) {
                if (chessBoardNew[whiteKingCoordinates[0] +i][whiteKingCoordinates[1]].equals("BR")) {
                    System.out.println("A rook has the white king by the throat. Check!");
                    whiteKingChecked = true;
                }
                if (chessBoardNew[whiteKingCoordinates[0] +i][whiteKingCoordinates[1]].equals("__")) {
                    continue;
                } else {
                    break;
                }
            }
            //West
            for (int i = 1; i < 10; i++) {
                if (chessBoardNew[whiteKingCoordinates[0]][whiteKingCoordinates[1] - i].equals("BR")) {
                    System.out.println("A rook has the white king by the throat. Check!");
                    whiteKingChecked = true;
                }
                if (chessBoardNew[whiteKingCoordinates[0]][whiteKingCoordinates[1] - i].equals("__")) {
                    continue;
                } else {
                    break;
                }
            }

            return whiteKingChecked;
    }

    public static boolean checkIsBlackChecked() {
        int[] blackKingCoordinates = {0, 0};
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (chessBoardNew[i][j].equals("BK")) {
                    blackKingCoordinates[0] = i;
                    blackKingCoordinates[1] = j;
                }
            }
        }

        return blackKingChecked;
    }

   //Display
   public static void displayBoard() {
            for (int i = 0; i < 10; i++) {
                System.out.println(Arrays.deepToString(chessBoardNew[i]));
                //  System.out.println("\r\n");
            }
        }

}

class Player extends Chessboard {
    ArrayList<String> blackPiecesTaken = new ArrayList<>();
    ArrayList<String> whitePiecesTaken = new ArrayList<>();
    String originString;
    private String move = "a1h8";
    boolean moveFormatIsCorrect = false;

    //Get und Set
    public String getMove() {
        return move;
    }
    public void setMove(String move) {
        String columnsString = "abcdefgh";
        String rowString = "12345678";

        if(
           (columnsString.contains(String.valueOf(move.charAt(0))))
        && (rowString.contains(String.valueOf(move.charAt(1))))
        && (columnsString.contains(String.valueOf(move.charAt(2))))
        && (rowString.contains(String.valueOf(move.charAt(3))))
        && move.length() == 4
        ){
        this.move = move;
        moveFormatIsCorrect = true;
        }
    }

    //constructor
    public void player() {
    }
    int moveCoordinateAbsolute = 0;
    //moves
    public void move() {
        int[] originCoordinates = {0, 0};
        int[] destinationCoordinates = {0, 0};
        int[] moveCoordinates = {0, 0};

        String piece;
        String pieceTaken;
        char destinationChar;
        char[] columns = {'-', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        boolean isPiece = false;
        boolean isWhite = false;
        boolean isBlack = false;
        boolean whiteKingHasMoved = false;
        boolean blackKingHasMoved = false;
        boolean blackQueensRookHasMoved = false;
        boolean blackKingsRookHasMoved = false;
        boolean whiteQueensRookHasMoved = false;
        boolean whiteKingsRookHasMoved = false;

        while(true) {
            boolean pathIsClear = true;
            boolean moveIsLegal = true;  //Remember: Any move might be illegal in check!
            boolean babaIsYou;
            //enter the move in standard format "c2c4"
            System.out.println("  Make your move. Face your fear.");
            Scanner in = new Scanner(System.in);
            setMove(in.nextLine());

            //Determine the move's origin string
            originString = "" + getMove().charAt(0) + getMove().charAt(1);

            //convert origin and destination to board coordinates.
            for (int i = 0; i < 9; i++) {
                if (columns[i] == getMove().charAt(0)) {
                    originCoordinates[1] = i;
                }
            }
            originCoordinates[0] = 9 - Character.getNumericValue(getMove().charAt(1));
            for (int i = 0; i < 9; i++) {
                if (columns[i] == getMove().charAt(2)) {
                    destinationCoordinates[1] = i;
                }
            }
            destinationCoordinates[0] = 9 - Character.getNumericValue(getMove().charAt(3));

            //determine the moveCoordinates and their absolute value
            moveCoordinates[0] = destinationCoordinates[0] - originCoordinates[0];
            moveCoordinates[1] = destinationCoordinates[1] - originCoordinates[1];
            int absolute = 0;
            if(moveCoordinates[0] > 0){
                absolute = moveCoordinates[0];
            }
            if(moveCoordinates[0] < 0) {
                absolute = moveCoordinates[0] * -1;
            }
            if(moveCoordinates[1] > 0){
                absolute = moveCoordinates[1];
            }
            if(moveCoordinates[1] < 0){
                absolute = moveCoordinates[1]*-1;
            }

            //parse if there is a piece and what color
            piece = Chessboard.chessBoardNew[originCoordinates[0]][originCoordinates[1]];
            pieceTaken = Chessboard.chessBoardNew[destinationCoordinates[0]][destinationCoordinates[1]];
            if (!Chessboard.chessBoardNew[originCoordinates[0]][originCoordinates[1]].equals("__")) {
                        isPiece = true;
                        if (piece.charAt(0) == 'B') {
                            isBlack = true;
                        } else if (piece.charAt(0) == 'W') {
                            isWhite = true;
                        }
                    }
            destinationChar = Chessboard.chessBoardNew[destinationCoordinates[0]][destinationCoordinates[1]].charAt(0);

            //individual pieces' movesets. Depending on which piece has been chosen
            switch (piece) {
                case "WP" -> {
                    moveIsLegal = false;
                    //Moves up the board by one if unblocked
                    if ((moveCoordinates[0] == -1 && moveCoordinates[1] == 0)
                            && ((Chessboard.chessBoardNew[destinationCoordinates[0]][destinationCoordinates[1]].charAt(0)) == '_'))
                    {
                        moveIsLegal = true;
                        break;
                    }
                    //or two on the first move
                    if ((originCoordinates[0] == 7)
                            && (moveCoordinates[0] == -2 && moveCoordinates[1] == 0)) {
                        moveIsLegal = true;
                    }

                    //captures diagonally
                    if((Chessboard.chessBoardNew[originCoordinates[0] -1][originCoordinates[1] -1]).charAt(0) == 'B'
                            && (moveCoordinates[0] == -1 && moveCoordinates[1] == -1))
                    {
                        moveIsLegal = true;
                    }
                    if((chessBoardNew[originCoordinates[0] -1][originCoordinates[1] +1]).charAt(0) == 'B'
                            && (moveCoordinates[0] == -1 && moveCoordinates[1] == +1))
                    {
                        moveIsLegal = true;
                    }

                    //captures en-passant left
                    if(
                            destinationCoordinates[0] == 3
                                    && (moveCoordinates[0] == -1 && moveCoordinates[1] == -1)
                                    && chessBoardNew[originCoordinates[0]][originCoordinates[1] -1].equals("BP")
                                    && chessBoardTurnByTurn[turnCounter -1][2][originCoordinates[1] -1].equals("BP")

                    ){
                        moveIsLegal = true;
                        chessBoardNew[originCoordinates[0]][originCoordinates[1] -1] = "__";
                        blackPiecesTaken.add("BP");

                    }

                    //captures en-passant right
                    if(
                            destinationCoordinates[0] == 3
                                    && (moveCoordinates[0] == -1 && moveCoordinates[1] == +1)
                                    && chessBoardNew[originCoordinates[0]][originCoordinates[1] +1].equals("BP")
                                    && chessBoardTurnByTurn[turnCounter -1][2][originCoordinates[1] +1].equals("BP")

                    ){
                        moveIsLegal = true;
                        chessBoardNew[originCoordinates[0]][originCoordinates[1] +1] = "__";
                        blackPiecesTaken.add("BP");

                    }
                }
                case "BP" -> {
                    moveIsLegal = false;
                    //moves down the board by one
                    if ((moveCoordinates[0] == +1 && moveCoordinates[1] == 0)
                            && ((Chessboard.chessBoardNew[destinationCoordinates[0]][destinationCoordinates[1]].charAt(0)) == '_'))
                    {
                        moveIsLegal = true;
                        break;
                    }

                    //or two on first move
                    if ((originCoordinates[0] == 2)
                            && (moveCoordinates[0] == 2 && moveCoordinates[1] == 0)) {
                        moveIsLegal = true;
                    }

                    //captures diagonally
                    if((chessBoardNew[originCoordinates[0] +1][originCoordinates[1] -1]).charAt(0) == 'W'
                            && (moveCoordinates[0] == +1 && moveCoordinates[1] == -1))
                    {
                        moveIsLegal = true;
                    }
                    if((chessBoardNew[originCoordinates[0] +1][originCoordinates[1] +1]).charAt(0) == 'W'
                            && (moveCoordinates[0] == +1 && moveCoordinates[1] == +1))
                    {
                        moveIsLegal = true;
                    }

                    //captures en-passant left
                    if(
                            destinationCoordinates[0] == 6
                                    && (moveCoordinates[0] == +1 && moveCoordinates[1] == -1)
                                    && chessBoardNew[originCoordinates[0]][originCoordinates[1] -1].equals("WP")
                                    && chessBoardTurnByTurn[turnCounter -1][7][originCoordinates[1] -1].equals("WP")

                    ){
                        moveIsLegal = true;
                        chessBoardNew[originCoordinates[0]][originCoordinates[1] -1] = "__";
                        whitePiecesTaken.add("WP");

                    }

                    //captures en-passant right
                    if(
                            destinationCoordinates[0] == 6
                                    && (moveCoordinates[0] == +1 && moveCoordinates[1] == +1)
                                    && chessBoardNew[originCoordinates[0]][originCoordinates[1] +1].equals("WP")
                                    && chessBoardTurnByTurn[turnCounter -1][7][originCoordinates[1] +1].equals("WP")

                    ){
                        moveIsLegal = true;
                        chessBoardNew[originCoordinates[0]][originCoordinates[1] +1] = "__";
                        whitePiecesTaken.add("WP");

                    }
                }
                case "WR" -> {
                    moveIsLegal = false;
                    //moves in a straight line, vertical and horizontal
                    if (moveCoordinates[0] < 8 && moveCoordinates[0] > -8 && moveCoordinates[1] == 0) {
                        moveIsLegal = true;
                        //check if the rook moved
                        if(originCoordinates[0] == 8 && originCoordinates[1] == 1) {
                            whiteQueensRookHasMoved = true;
                        }else if (originCoordinates[0] == 8 && originCoordinates[1] == 8){
                            whiteKingsRookHasMoved = true;
                        }
                        break;
                    }
                    if (moveCoordinates[0] == 0 && moveCoordinates[1] < 8 && moveCoordinates[1] > -8) {
                        moveIsLegal = true;
                        //check if the rook moved
                        if(originCoordinates[0] == 8 && originCoordinates[1] == 1) {
                            whiteQueensRookHasMoved = true;
                        }else if (originCoordinates[0] == 8 && originCoordinates[1] == 8){
                            whiteKingsRookHasMoved = true;
                        }
                    }
                    //cannot skip
                }
                case "BR" -> {
                    moveIsLegal = false;
                    //moves in a straight line, vertical and horizontal
                    if (moveCoordinates[0] < 8 && moveCoordinates[0] > -8 && moveCoordinates[1] == 0) {
                        moveIsLegal = true;
                        //check if the rook moved
                        if(originCoordinates[0] == 1 && originCoordinates[1] == 1) {
                            blackQueensRookHasMoved = true;
                        }else if (originCoordinates[0] == 1 && originCoordinates[1] == 8){
                            blackKingsRookHasMoved = true;
                        }
                        break;
                    }
                    if (moveCoordinates[0] == 0 && moveCoordinates[1] < 8 && moveCoordinates[1] > -8) {
                        moveIsLegal = true;

                    }

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
                    }
                    //can skip other pieces (and thus move on turn 1)
                }
                case "BB", "WB" -> {
                    moveIsLegal = false;
                    //moves diagonally
                    if(moveCoordinates[0] == moveCoordinates[1] || moveCoordinates[0] == moveCoordinates[1]*-1 ) {
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
                        }
                    }
                    //cannot skip other pieces
                }
                case "BQ", "WQ" -> {
                    moveIsLegal = false;
                    //moves diagonally
                    if(moveCoordinates[0] == moveCoordinates[1] || moveCoordinates[0] == moveCoordinates[1]*-1 ) {
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
                        }
                    }
                    //cannot skip
                }
                case "BK" -> {
                    moveIsLegal = false;
                    //moves by one in any direction
                    if (moveCoordinates[0] == 1 && moveCoordinates[1] == 1) {
                        moveIsLegal = true;
                        blackKingHasMoved = true;
                        break;
                    }
                    if (moveCoordinates[0] == 1 && moveCoordinates[1] == 0) {
                        moveIsLegal = true;
                        blackKingHasMoved = true;
                        break;
                    }
                    if (moveCoordinates[0] == 1 && moveCoordinates[1] == -1) {
                        moveIsLegal = true;
                        blackKingHasMoved = true;
                        break;
                    }
                    if (moveCoordinates[0] == 0 && moveCoordinates[1] == -1) {
                        moveIsLegal = true;
                        blackKingHasMoved = true;
                        break;
                    }
                    if (moveCoordinates[0] == -1 && moveCoordinates[1] == -1) {
                        moveIsLegal = true;
                        blackKingHasMoved = true;
                        break;
                    }
                    if (moveCoordinates[0] == -1 && moveCoordinates[1] == 0) {
                        moveIsLegal = true;
                        blackKingHasMoved = true;
                        break;
                    }
                    if (moveCoordinates[0] == -1 && moveCoordinates[1] == 1) {
                        moveIsLegal = true;
                        blackKingHasMoved = true;
                        break;
                    }
                    if (moveCoordinates[0] == 0 && moveCoordinates[1] == 1) {
                        moveIsLegal = true;
                        blackKingHasMoved = true;
                        break;
                    }

                    //has to be protected
                    //May not be endangered

                    //can castle if (*White king moves two right, *three left, *King not moved, *rook not moved, -squares not occupied, -king not checked, -squares not threatened)

                    //castle Kingside
                    if ((moveCoordinates[0] == 0 && moveCoordinates[1] == 2) && !blackKingHasMoved && !blackKingsRookHasMoved) {
                        moveIsLegal = true;
                        Chessboard.chessBoardNew[8][8] = "__";
                        Chessboard.chessBoardNew[8][5] = "__";
                        Chessboard.chessBoardNew[8][7] = "WK";
                        Chessboard.chessBoardNew[8][6] = "WR";
                        blackKingHasMoved = true;
                        blackKingsRookHasMoved = true;
                        break;
                    }

                    //castle Queenside
                    if ((moveCoordinates[0] == 0 && moveCoordinates[1] == -3) && !blackKingHasMoved && !blackQueensRookHasMoved) {
                        moveIsLegal = true;
                        Chessboard.chessBoardNew[8][1] = "__";
                        Chessboard.chessBoardNew[8][5] = "__";
                        Chessboard.chessBoardNew[8][2] = "WK";
                        Chessboard.chessBoardNew[8][3] = "WR";
                        blackKingHasMoved = true;
                        blackQueensRookHasMoved = true;
                    }


                }
                case "WK" -> {
                    moveIsLegal = false;
                    //moves by one in any direction
                    if (moveCoordinates[0] == 1 && moveCoordinates[1] == 1) {
                        moveIsLegal = true;
                        whiteKingHasMoved = true;
                        break;
                    }
                    if (moveCoordinates[0] == 1 && moveCoordinates[1] == 0) {
                        moveIsLegal = true;
                        whiteKingHasMoved = true;
                        break;
                    }
                    if (moveCoordinates[0] == 1 && moveCoordinates[1] == -1) {
                        moveIsLegal = true;
                        whiteKingHasMoved = true;
                        break;
                    }
                    if (moveCoordinates[0] == 0 && moveCoordinates[1] == -1) {
                        moveIsLegal = true;
                        whiteKingHasMoved = true;
                        break;
                    }
                    if (moveCoordinates[0] == -1 && moveCoordinates[1] == -1) {
                        moveIsLegal = true;
                        whiteKingHasMoved = true;
                        break;
                    }
                    if (moveCoordinates[0] == -1 && moveCoordinates[1] == 0) {
                        moveIsLegal = true;
                        whiteKingHasMoved = true;
                        break;
                    }
                    if (moveCoordinates[0] == -1 && moveCoordinates[1] == 1) {
                        moveIsLegal = true;
                        whiteKingHasMoved = true;
                        break;
                    }
                    if (moveCoordinates[0] == 0 && moveCoordinates[1] == 1) {
                        moveIsLegal = true;
                        whiteKingHasMoved = true;
                        break;
                    }

                    //has to be protected
                    //May not be endangered
                    //can castle if (*White king moves two right, *three left, *King not moved, *rook not moved, -squares not occupied, -king not checked, -squares not threatened)

                    //castle Kingside
                    if ((moveCoordinates[0] == 0 && moveCoordinates[1] == 2) && !whiteKingHasMoved && !whiteKingsRookHasMoved) {
                        moveIsLegal = true;
                        Chessboard.chessBoardNew[8][8] = "__";
                        Chessboard.chessBoardNew[8][5] = "__";
                        Chessboard.chessBoardNew[8][7] = "WK";
                        Chessboard.chessBoardNew[8][6] = "WR";
                        whiteKingHasMoved = true;
                        whiteKingsRookHasMoved = true;
                        break;
                    }

                    //castle Queenside
                    if ((moveCoordinates[0] == 0 && moveCoordinates[1] == -3) && !whiteKingHasMoved && !whiteQueensRookHasMoved) {
                        moveIsLegal = true;
                        Chessboard.chessBoardNew[8][1] = "__";
                        Chessboard.chessBoardNew[8][5] = "__";
                        Chessboard.chessBoardNew[8][2] = "WK";
                        Chessboard.chessBoardNew[8][3] = "WR";
                        whiteKingHasMoved = true;
                        whiteQueensRookHasMoved = true;
                    }
                }
                default -> {
                }
            }

            //move doesn't cross other pieces
            for (int i = 1; i < absolute; i++) {
            if (moveCoordinates[0] > 0 && moveCoordinates[1] > 0) {
                if (!(Chessboard.chessBoardNew[originCoordinates[0] + i][originCoordinates[1] + i].equals("__"))) {
                    pathIsClear = false;
                //    break;
                }
            }
            if (moveCoordinates[0] > 0 && moveCoordinates[1] == 0) {
                if (!(Chessboard.chessBoardNew[originCoordinates[0] + i][originCoordinates[1]].equals("__"))) {
                    pathIsClear = false;
                //    break;
                }
            }
            if (moveCoordinates[0] < 0 && moveCoordinates[1] > 0) {
                if (!(Chessboard.chessBoardNew[originCoordinates[0] - i][originCoordinates[1] + i].equals("__"))) {
                    pathIsClear = false;
                //    break;
                }
            }
            if (moveCoordinates[0] < 0 && moveCoordinates[1] == 0) {
                if (!(Chessboard.chessBoardNew[originCoordinates[0] - i][originCoordinates[1]].equals("__"))) {
                    pathIsClear = false;
                //    break;
                }
            }
            if (moveCoordinates[0] < 0 && moveCoordinates[1] < 0) {
                if (!(Chessboard.chessBoardNew[originCoordinates[0] - i][originCoordinates[1] - i].equals("__"))) {
                    pathIsClear = false;
                //    break;
                }
            }
            if (moveCoordinates[0] == 0 && moveCoordinates[1] < 0) {
                if (!(Chessboard.chessBoardNew[originCoordinates[0]][originCoordinates[1] - i].equals("__"))) {
                    pathIsClear = false;
                //    break;
                }
            }
            if (moveCoordinates[0] > 0 && moveCoordinates[1] < 0) {
                if (!(Chessboard.chessBoardNew[originCoordinates[0] + i][originCoordinates[1] - i].equals("__"))) {
                    pathIsClear = false;
                //    break;
                }
            }
            if (moveCoordinates[0] > 0 && moveCoordinates[1] == 0) {
                if (!(Chessboard.chessBoardNew[originCoordinates[0] + i][originCoordinates[1]].equals("__"))) {
                    pathIsClear = false;
                //  break;
                }
            }
        }
            //except for the knights
            if(piece.equals("BN") || piece.equals("WN")){
                pathIsClear = true;
            }



            //individual pieces movesets in check. If the current king is checked, only moves that end the check are legal

            //parse move legality
            if(!moveFormatIsCorrect){
                System.out.println("I don't understand what you are saying.");
            }
            else if (!moveIsLegal) {
                System.out.println("I can't let you do that.");
            }
            else if(!pathIsClear) {
                System.out.println("You! Shall! Not! Pass!");
                }
            else if(!isPiece){
                System.out.println("there is no one here.");
            }
            else if (!((Chessboard.isWhiteTurn && isWhite) || (!isWhiteTurn && isBlack))) {
                System.out.println("That life is not yours to spend.");
            }
            else if ((destinationChar == 'W' && Chessboard.isWhiteTurn) || (destinationChar == 'B' && !Chessboard.isWhiteTurn)) {
                System.out.println("That space is taken.");
            }

            //your king is checked

            //move is legal:
             else {
                //case: there was an enemy piece there = piece gets taken
                if(((pieceTaken.charAt(0) == 'B' && isWhiteTurn) || (pieceTaken.charAt(0) == 'W' && !isWhiteTurn))){
                    if(pieceTaken.charAt(0) == 'B'){
                        blackPiecesTaken.add(pieceTaken);
                    }else if(pieceTaken.charAt(0) == 'W'){
                        whitePiecesTaken.add(pieceTaken);
                    }
                }
                //delete piece from destination (=00)
                Chessboard.chessBoardNew[originCoordinates[0]][originCoordinates[1]] = "__";
                //insert piece into destination K K(=BP)
                Chessboard.chessBoardNew[destinationCoordinates[0]][destinationCoordinates[1]] = piece;
                break;
            }
        }
    }

    //choices
    public void concede(){}
    public void offerRemis(){}
    public void resetTheBoard(){}

}

public class Main {
    public static void main(String[] args) {
        Player playerWhite = new Player();
        Player playerBlack = new Player();

        //Display reset Board
        for (int i = 0; i < 10; i++) {
            System.out.println(Arrays.deepToString(Chessboard.chessBoardOrigin[i]));
        }

        //Game Loop
        while (Chessboard.blackKingLives && Chessboard.whiteKingLives) {
            //change player turn (white true, black false)
            Chessboard.isWhiteTurn = !Chessboard.isWhiteTurn;

            //Display options for player black or white
            if (Chessboard.isWhiteTurn) {
                System.out.println("           Player White!" /*" + "Move " + "Offer Remis " + "Concede " + "Reset the board */);
            } else {
                System.out.println("           Player Black!" /*+ "Move " + "Offer Remis " + "Concede " + "Reset the board "*/);
            }

            //+++++PLAYER MOVE++++++
            if (Chessboard.isWhiteTurn) {
                playerWhite.move();
                Chessboard.turnCounter ++;
                //saving previous move to chessBoardTurnByTurn
                for(int i = 0; i<9; i++){
                    for(int j = 0; j<9; j++){
                        Chessboard.chessBoardTurnByTurn[Chessboard.turnCounter][i][j] = Chessboard.chessBoardNew[i][j];
                    }
                }
            } else {
                playerBlack.move();
                Chessboard.turnCounter ++;
                //saving previous move to chessBoardTurnByTurn
                for(int i = 0; i<9; i++){
                    for(int j = 0; j<9; j++){
                        Chessboard.chessBoardTurnByTurn[Chessboard.turnCounter][i][j] = Chessboard.chessBoardNew[i][j];
                    }
                }
            }

            //display the new board and pieces taken
            Chessboard.displayBoard();
            System.out.println("Player Black's Victims: " + playerBlack.whitePiecesTaken);
            System.out.println("Player White's Victims: " + playerWhite.blackPiecesTaken);

            //Checks game states
            Chessboard.checkState();
        }
    }
}




