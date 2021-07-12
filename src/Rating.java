
import java.util.Arrays;

public class Rating {

    static int pawnBoard[][] = {//attribute to https://www.chessprogramming.org/Simplified_Evaluation_Function
        {0, 0, 0, 0, 0, 0, 0, 0},
        {50, 50, 50, 50, 50, 50, 50, 50},
        {10, 10, 20, 30, 30, 20, 10, 10},
        {5, 5, 10, 25, 25, 10, 5, 5},
        {0, 0, 0, 20, 20, 0, 0, 0},
        {5, -5, -10, 0, 0, -10, -5, 5},
        {5, 10, 10, -20, -20, 10, 10, 5},
        {0, 0, 0, 0, 0, 0, 0, 0}};
    static int pawnEndBoard[][] = {
        {0, 0, 0, 0, 0, 0, 0, 0},
        {150, 150, 150, 150, 150, 150, 150, 150},
        {50, 80, 80, 80, 80, 80, 80, 50},
        {20, 40, 40, 40, 40, 40, 40, 20},
        {15, 20, 20, 20, 20, 20, 20, 15},
        {10, 10, 10, 10, 10, 10, 10, 10},
        {-20, -10, -10, -10, -10, -10, -10, -20},
        {0, 0, 0, 0, 0, 0, 0, 0}
    };

    static int rookBoard[][] = {
        {0, 0, 0, 0, 0, 0, 0, 0},
        {5, 10, 10, 10, 10, 10, 10, 5},
        {-5, 0, 0, 0, 0, 0, 0, -5},
        {-5, 0, 0, 0, 0, 0, 0, -5},
        {-5, 0, 0, 0, 0, 0, 0, -5},
        {-5, 0, 0, 0, 0, 0, 0, -5},
        {-5, 0, 0, 0, 0, 0, 0, -5},
        {-5, 0, 0, 5, 5, 4, 0, -10}};
    static int knightBoard[][] = {
        {-50, -40, -30, -30, -30, -30, -40, -50},
        {-40, -20, 0, 0, 0, 0, -20, -40},
        {-30, 0, 10, 15, 15, 10, 0, -30},
        {-30, 5, 15, 20, 20, 15, 5, -30},
        {-30, 0, 15, 20, 20, 15, 0, -30},
        {-30, 5, 10, 15, 15, 10, 5, -30},
        {-40, -20, 0, 5, 5, 0, -20, -40},
        {-50, -30, -30, -30, -30, -30, -30, -50}};
    static int bishopBoard[][] = {
        {-20, -10, -10, -10, -10, -10, -10, -20},
        {-10, 0, 0, 0, 0, 0, 0, -10},
        {-10, 0, 5, 10, 10, 5, 0, -10},
        {-10, 5, 5, 10, 10, 5, 5, -10},
        {-10, 0, 10, 10, 10, 10, 0, -10},
        {-10, 10, 10, 10, 10, 10, 10, -10},
        {-10, 5, 0, 0, 0, 0, 5, -10},
        {-20, -10, -10, -10, -10, -10, -10, -20}};
    static int queenBoard[][] = {
        {-20, -10, -10, -5, -5, -10, -10, -20},
        {-10, 0, 0, 0, 0, 0, 0, -10},
        {-10, 0, 5, 5, 5, 5, 0, -10},
        {-5, 0, 5, 5, 5, 5, 0, -5},
        {0, 0, 5, 5, 5, 5, 0, -5},
        {-10, 5, 5, 5, 5, 5, 0, -10},
        {-10, 0, 5, 0, 0, 0, 0, -10},
        {-20, -10, -10, -5, -5, -10, -10, -20}};
    static int[][] kingStartBoard = {
        {-30, -40, -40, -50, -50, -40, -40, -30},
        {-30, -40, -40, -50, -50, -40, -40, -30},
        {-30, -40, -40, -50, -50, -40, -40, -30},
        {-30, -40, -40, -50, -50, -40, -40, -30},
        {-20, -30, -30, -40, -40, -30, -30, -20},
        {-10, -20, -20, -20, -20, -20, -20, -10},
        {20, 20, 0, 0, 0, 0, 20, 20},
        {20, 40, 30, 0, -10, 0, 40, 20}};
    static int[][] kingMidBoard = {
        {-50, -40, -30, -20, -20, -30, -40, -50},
        {-40, -20, -10, 0, 0, -10, -20, -40},
        {-30, -10, 20, 30, 30, 20, -10, -30},
        {-30, -10, 30, 30, 30, 30, -10, -30},
        {-30, -10, 30, 30, 30, 30, -10, -30},
        {-30, -10, 20, 30, 30, 20, -10, -30},
        {-40, -30, 0, 0, 0, 0, -30, -40},
        {-50, -40, -30, -30, -30, -30, -40, -50}
    };
    static int kingEndBoard[][] = {
        {0, 0, 0, -1, -1, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {-1, 0, 0, 0, 0, 0, 0, -1},
        {-1, 0, 0, 0, 0, 0, 0, -1},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, -1, -1, 0, 0, 0}};
    static int nothingBoard[][] = {
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0}
    };

    static boolean endGame = false;
    static boolean middleGame = false;

    public static int rating(int listLength, int depth, int turn) {
        if (listLength == 0) {
            return rateMoveability(listLength, depth);
        }

        int counter = 0;
//        counter += rateAttack();
        counter += rateMaterial();
        counter += ratePositional();

        if (endGame) {
            if (counter > 0) {
                //only force the king to the corner if you are on the winning side
                counter += forceKingCorner(Board.whiteKingPosition, Board.blackKingPosition);
            } else {
                counter -= forceKingCorner(Board.blackKingPosition, Board.whiteKingPosition);
            }
        }

        return counter + turn * depth * 50;
    }

    public static int rateAttack() {
        return 0;
    }

    public static int rateMaterial() {
        int counter = 0;
        for (int i = 0; i < 64; i++) {
            switch (Board.chessBoard[i / 8][i % 8]) {
                case "P":
                    counter += 100;
                    break;
                case "R":
                    counter += 500;
                    break;
                case "N":
                    counter += 320;
                    break;
                case "B":
                    counter += 330;
                    break;
                case "Q":
                    counter += 900;
                    break;
                case "p":
                    counter -= 100;
                    break;
                case "r":
                    counter -= 500;
                    break;
                case "n":
                    counter -= 300;
                    break;
                case "b":
                    counter -= 300;
                    break;
                case "q":
                    counter -= 900;
                    break;
            }
        }

        return counter;
    }

    public static int rateMoveability(int listLength, int depth) {
        int counter = 0;
//        counter += listLength;
        if (listLength == 0) {
            if (!Board.kingSafe(Board.whiteToMove ? Board.whiteKingPosition : Board.blackKingPosition)) {//if checkmate

                if (!Board.whiteToMove) {
                    counter += AlphaBetaChess.CHECKMATE * (depth + 1);
                } else {
                    counter -= AlphaBetaChess.CHECKMATE * (depth + 1);
                }
            } else { // stalemate
                counter = 0;
            }
        }
        return counter;
    }

    public static int ratePositional() {
        int counter = 0;
        for (int i = 0; i < 64; i++) {
            switch (Board.chessBoard[i / 8][i % 8]) {
                case "P":
                    counter += pawnBoard[i / 8][i % 8];
                    break;
                case "R":
                    counter += rookBoard[i / 8][i % 8];
                    break;
                case "N":
                    counter += knightBoard[i / 8][i % 8];
                    break;
                case "B":
                    counter += bishopBoard[i / 8][i % 8];
                    break;
                case "Q":
                    counter += queenBoard[i / 8][i % 8];
                    break;
                case "K":
                    counter += kingStartBoard[i / 8][i % 8];
                    break;
                case "p":
                    counter -= pawnBoard[7 - (i / 8)][i % 8];
                    break;
                case "r":
                    counter -= rookBoard[7 - (i / 8)][i % 8];
                    break;
                case "n":
                    counter -= knightBoard[7 - (i / 8)][i % 8];
                    break;
                case "b":
                    counter -= bishopBoard[7 - (i / 8)][i % 8];
                    break;
                case "q":
                    counter -= queenBoard[7 - (i / 8)][i % 8];
                    break;
                case "k":
                    counter -= kingStartBoard[7 - (i / 8)][i % 8];
                    break;
            }
        }
        return counter;
    }

    public static void endGame() {
        kingStartBoard = kingEndBoard;
//        AlphaBetaChess.globalDepth = 3;
        endGame = true;
        middleGame = false;
        AlphaBetaChess.globalDepth = 4;
    }

    public static void middleGame() {
        kingStartBoard = kingMidBoard;
        pawnBoard = pawnEndBoard;
        rookBoard = nothingBoard;
        AlphaBetaChess.globalDepth = 4;
        middleGame = true;
    }

    static int forceKingCorner(int friendlyKingPosition, int opponentKingSquare) {
        int evaluation = 0;
        int opponentKingRank = opponentKingSquare / 8;
        int opponentKingFile = opponentKingSquare % 8;

        int opponentKingDistToCenterFile = Math.max(3 - opponentKingFile, opponentKingFile - 4);
        int opponentKingDistToCenterRank = Math.max(3 - opponentKingRank, opponentKingRank - 4);
        // if the king is closer to the corner, its better
        evaluation += opponentKingDistToCenterRank + opponentKingDistToCenterFile;

        int friendlyKingRank = friendlyKingPosition / 8;
        int friendlyKingFile = friendlyKingPosition % 8;

        int dstBetweenKingsRank = Math.abs(friendlyKingRank - opponentKingRank);
        int dstBetweenKingsFile = Math.abs(friendlyKingFile - opponentKingFile);
        int dstBetweenKings = Math.abs(dstBetweenKingsRank + dstBetweenKingsFile);
        // if the king is closer to the opponents king, its easier to mate
        evaluation += 14 - dstBetweenKings;

        return evaluation * 15;
    }
}
