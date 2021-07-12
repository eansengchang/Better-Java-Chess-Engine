
import java.util.*;

public class UCI {

    static String ENGINENAME = "ESC BOT";

    public static void UCICommunication() {
        Scanner input = new Scanner(System.in);
        while (true) {

            String inputString = input.nextLine();
            if ("uci".equals(inputString)) {
                inputUCI();
            } else if (inputString.startsWith("setoption")) {
                inputSetOption(inputString);
            } else if ("isready".equals(inputString)) {
                inputIsReady();
            } else if ("ucinewgame".equals(inputString)) {
                inputUCINewGame();
            } else if (inputString.startsWith("position")) {
                inputPosition(inputString);
            } else if ("go".equals(inputString)) {
                inputGo();
            } else if ("print".equals(inputString)) {
                inputPrint();
            }

        }
    }

    private static void inputUCI() {
        System.out.println("id name " + ENGINENAME);
        System.out.println("id author Ean Seng");
        //options go here
        System.out.println("uciok");
    }

    private static void inputSetOption(String inputString) {
        //set option
    }

    private static void inputIsReady() {
        System.out.println("readyok");
    }

    private static void inputUCINewGame() {
        //clears move history
        Board.moveHistory = "";
    }

    private static void inputPosition(String input) {
        input = input.substring(9).concat(" ");
        if (input.contains("startpos")) {
            input = input.substring(0);
            Board.importFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        } else if (input.contains("fen")) {
            input = input.substring(4);
            Board.importFEN(input);
        }

        //check for more moves
        if (input.contains("moves")) {
            input = input.substring(input.indexOf("moves") + 6);
            //make each of the moes
            while (input.length() > 0) {
                int r1 = ('8' - input.charAt(1));
                int c1 = (input.charAt(0) - 'a');
                int r2 = ('8' - input.charAt(3));
                int c2 = (input.charAt(2) - 'a');

                String move;
                if (r2 == 0 && r1 == 1 && "P".equals(Board.chessBoard[r1][c1])) {
                    //white pawn promotion
                    move = "" + c1 + c2 + Board.chessBoard[r2][c2] + input.charAt(4) + "A";
                } else if (r2 == 7 && r1 == 6 && "p".equals(Board.chessBoard[r1][c1])) {
                    //black pawn promotion
                    move = "" + c1 + c2 + Board.chessBoard[r2][c2] + "qA";
                } else if (Math.abs(c1 - c2) == 2 && "k".equals(Board.chessBoard[r1][c1].toLowerCase())) {
                    move = "" + r1 + c1 + r2 + c2 + "C";
                } else {
                    //regular move
                    move = "" + r1 + c1 + r2 + c2 + Board.chessBoard[r2][c2];
                }
                Board.makeMove(move);
                input = input.substring(input.indexOf(" ") + 1);
            }
        }
    }

    //6343
    private static void inputGo() {
        String bestMove = AlphaBetaChess.computerMove();

        if (!bestMove.endsWith("A")) {
            char c1 = (char) ('a' + Character.getNumericValue(bestMove.charAt(1)));
            int r1 = 8 - Character.getNumericValue(bestMove.charAt(0));
            char c2 = (char) ('a' + Character.getNumericValue(bestMove.charAt(3)));
            int r2 = 8 - Character.getNumericValue(bestMove.charAt(2));
            bestMove = "" + c1 + r1 + c2 + r2;
        } else {

            char c1 = (char) ('a' + Character.getNumericValue(bestMove.charAt(0)));
            char c2 = (char) ('a' + Character.getNumericValue(bestMove.charAt(1)));
            int r1 = Board.whiteToMove ? 2 : 7;
            int r2 = Board.whiteToMove ? 1 : 8;

            bestMove = "" + c1 + r1 + c2 + r2 + Character.toLowerCase(bestMove.charAt(3));
        }
        System.out.println("bestmove " + bestMove);
    }

    private static void inputPrint() {
        for (int i = 0; i < 8; i++) {
            System.out.println(Arrays.toString(Board.chessBoard[i]));
        }
    }
}
