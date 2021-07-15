
import java.util.Arrays;

public class Board {

    public static String chessBoard[][] = {
        {"r", "n", "b", "q", "k", "b", "n", "r"},
        {"p", "p", "p", "p", "p", "p", "p", "p"},
        {" ", " ", " ", " ", " ", " ", " ", " "},
        {" ", " ", " ", " ", " ", " ", " ", " "},
        {" ", " ", " ", " ", " ", " ", " ", " "},
        {" ", " ", " ", " ", " ", " ", " ", " "},
        {"P", "P", "P", "P", "P", "P", "P", "P"},
        {"R", "N", "B", "Q", "K", "B", "N", "R"},};

    public static String moveHistory = "";
    public static int whiteKingPosition, blackKingPosition;
    public static boolean whiteToMove = true;
    public static String castleRights = "KQkq";

    public static void start() {
        while (!"K".equals(chessBoard[whiteKingPosition / 8][whiteKingPosition % 8])) {
            whiteKingPosition++;
        } // gets white king location
        while (!"k".equals(chessBoard[blackKingPosition / 8][blackKingPosition % 8])) {
            blackKingPosition++;
        } // gets black king location
//        importFEN("2rr2k1/2q2pp1/R1B1p2p/4P3/2pP2Pb/4BQ1P/1P3P2/5RK1 w Kkq - 0 1");
    }

    public static void importFEN(String FEN) {
        moveHistory = "";

        String[] split = FEN.split(" ");
        String[] boardRows = split[0].split("/");

//        System.out.println(Arrays.toString(split));
        //loop through each row in FEN
        int position = 0;
        for (int i = 0; i < 8; i++) {
            //loop through each letter of each row
            for (int j = 0; j < boardRows[i].length(); j++) {
                char c = boardRows[i].charAt(j);
                if (Character.isDigit(c)) {
                    for (int k = 0; k < Character.getNumericValue(c); k++) {
                        chessBoard[position / 8][position % 8] = " ";
                        position++;
                    }
                } else {
                    chessBoard[position / 8][position % 8] = "" + c;
                    position++;
                }
            }
        }
        whiteToMove = "w".equals(split[1]);
        
        //create the 4 digit castle rights
        if(split[2].contains("K")){
            castleRights = castleRights + "K";
        } else {
            castleRights = castleRights + " ";
        }
        if(split[2].contains("Q")){
            castleRights = castleRights + "Q";
        } else {
            castleRights = castleRights + " ";
        }
        if(split[2].contains("k")){
            castleRights = castleRights + "k";
        } else {
            castleRights = castleRights + " ";
        }
        if(split[2].contains("q")){
            castleRights = castleRights + "q";
        } else {
            castleRights = castleRights + " ";
        }
        
//        System.out.println(castleRights);
        
        whiteKingPosition = 0;
        blackKingPosition = 0;
        while (!"K".equals(chessBoard[whiteKingPosition / 8][whiteKingPosition % 8])) {
            whiteKingPosition++;
        } // gets white king location
        while (!"k".equals(chessBoard[blackKingPosition / 8][blackKingPosition % 8])) {
            blackKingPosition++;
        } // gets black king location

        //don't use book if its not start position
        if (!"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR".equals(split[0])) {
//            System.out.println("does not equal");
            AlphaBetaChess.book = false;
        }
//        for (int i = 0; i < 8; i++) {
//            System.out.println(Arrays.toString(chessBoard[i]));
//        }
    }

    public static void makeMove(String move) {
        //All moves are 6 characters long
        
        if (move.charAt(5) != 'A') {
//            regular move
//            x1, y1, x2, y2, moved piece, captured piece
            int r1 = Character.getNumericValue(move.charAt(0));
            int c1 = Character.getNumericValue(move.charAt(1));
            int r2 = Character.getNumericValue(move.charAt(2));
            int c2 = Character.getNumericValue(move.charAt(3));

            chessBoard[r2][c2] = chessBoard[r1][c1];
            chessBoard[r1][c1] = " ";

            if ("K".equals(chessBoard[r2][c2])) {
                whiteKingPosition = 8 * r2 + c2;
                //remove whites castle rights next turn
                castleRights = castleRights + "  " + castleRights.substring(castleRights.length() - 2, castleRights.length());
            } else if ("k".equals(chessBoard[r2][c2])) {
                blackKingPosition = 8 * r2 + c2;
                //remove blacks castle rights next turn
                castleRights = castleRights + castleRights.substring(castleRights.length() - 4, castleRights.length() - 2) + "  ";
            } //check if castle has moved out and remove castle right
            else if (8 * r1 + c1 == 0 || 8 * r2 + c2 == 0) {
                castleRights = castleRights + castleRights.substring(castleRights.length() - 4, castleRights.length() - 1) + " ";
//                System.out.println("lost queen side black");
            } else if (8 * r1 + c1 == 7 || 8 * r2 + c2 == 7) {
                castleRights = castleRights + castleRights.substring(castleRights.length() - 4, castleRights.length() - 2) + " " + castleRights.substring(castleRights.length() - 1, castleRights.length());
//                System.out.println("lost king side black");
            } else if (8 * r1 + c1 == 56 || 8 * r2 + c2 == 56) {
                castleRights = castleRights + castleRights.substring(castleRights.length() - 4, castleRights.length() - 3) + " " + castleRights.substring(castleRights.length() - 2, castleRights.length());
//                System.out.println("lost queen side white");
            } else if (8 * r1 + c1 == 63 || 8 * r2 + c2 == 63) {
                castleRights = castleRights + " " + castleRights.substring(castleRights.length() - 3, castleRights.length());
//                System.out.println("lost king side white");
            } else {
                //continue same castle rights
                castleRights = castleRights + castleRights.substring(castleRights.length() - 4, castleRights.length());
            }

            //moves the castle in a castling
            if (move.charAt(5) == 'C') {
                //x1, y1, x2, y2, moved piece, C
                //move castle when caslting
                if (move.charAt(3) == '6') {
                    chessBoard[r1][c1 + 1] = chessBoard[r2][7];
                    chessBoard[r2][7] = " ";
                } else {
                    chessBoard[r1][c1 - 1] = chessBoard[r2][0];
                    chessBoard[r2][0] = " ";
                }
            }
        } else {
            //pawn promotion
            //format: column1, column2, captured piece, new piece, moved piece, A
            if (whiteToMove) {
                chessBoard[1][Character.getNumericValue(move.charAt(0))] = " ";
                chessBoard[0][Character.getNumericValue(move.charAt(1))] = String.valueOf(move.charAt(3));
            } else {
                chessBoard[6][Character.getNumericValue(move.charAt(0))] = " ";
                chessBoard[7][Character.getNumericValue(move.charAt(1))] = String.valueOf(move.charAt(3));
            }

            //checks the edge case if pawn promotes while capturing a rook, disabling castling rights
            if (move.charAt(2) == 'r' && move.charAt(1) == '0') {
                castleRights = castleRights + castleRights.substring(castleRights.length() - 4, castleRights.length() - 1) + " ";
//                System.out.println("lost queen side black");
            } else if (move.charAt(2) == 'r' && move.charAt(1) == '7') {
                castleRights = castleRights + castleRights.substring(castleRights.length() - 4, castleRights.length() - 2) + " " + castleRights.substring(castleRights.length() - 1, castleRights.length());
//                System.out.println("lost king side black");
            } else if (move.charAt(2) == 'R' && move.charAt(1) == '0') {
                castleRights = castleRights + castleRights.substring(castleRights.length() - 4, castleRights.length() - 3) + " " + castleRights.substring(castleRights.length() - 2, castleRights.length());
//                System.out.println("lost queen side white");
            } else if (move.charAt(2) == 'R' && move.charAt(1) == '7') {
                castleRights = castleRights + " " + castleRights.substring(castleRights.length() - 3, castleRights.length());
//                System.out.println("lost king side white");
            } else {
                //continue same castle rights
                castleRights = castleRights + castleRights.substring(castleRights.length() - 4, castleRights.length());
            }
        }

        //change side to move
        whiteToMove = !whiteToMove;
    }

    public static void undoMove(String move) {
        int r1 = Character.getNumericValue(move.charAt(0));
        int c1 = Character.getNumericValue(move.charAt(1));
        int r2 = Character.getNumericValue(move.charAt(2));
        int c2 = Character.getNumericValue(move.charAt(3));
        whiteToMove = !whiteToMove;
        if (move.charAt(5) != 'A') {
            //regular move
            //x1, y1, x2, y2, captured piece
            chessBoard[r1][c1] = chessBoard[r2][c2];
            chessBoard[r2][c2] = move.charAt(5) != 'C' ? String.valueOf(move.charAt(5)) : " ";

            if ("K".equals(chessBoard[r1][c1])) {
                whiteKingPosition = 8 * r1 + c1;
            } else if ("k".equals(chessBoard[r1][c1])) {
                blackKingPosition = 8 * r1 + c1;
            }

            //moves the castle in a castling
            if (move.charAt(5) == 'C') {
                //move castle when caslting
                if (move.charAt(3) == '6') {
                    chessBoard[r2][7] = chessBoard[r1][c1 + 1];
                    chessBoard[r1][c1 + 1] = " ";
                } else {
                    chessBoard[r2][0] = chessBoard[r1][c1 - 1];
                    chessBoard[r1][c1 - 1] = " ";
                }
            }
        } else {
            //pawn promotion
            //format: column1, column2, captured piece, new piece, P
            if (whiteToMove) {
                chessBoard[1][r1] = "P";
                chessBoard[0][c1] = String.valueOf(move.charAt(2));
            } else {
                chessBoard[6][r1] = "p";
                chessBoard[7][c1] = String.valueOf(move.charAt(2));
            }
        }
        castleRights = castleRights.substring(0, castleRights.length() - 4);
    }

    public static String possibleMoves() {
        String list = "";
        //loops through the entire board and generates moves for each piece
        for (int i = 0; i < 64; i++) {
            if (Character.isUpperCase(chessBoard[i / 8][i % 8].charAt(0)) != whiteToMove) {
                continue;
            }
            switch (chessBoard[i / 8][i % 8].toUpperCase()) {
                case "P":
                    list += possibleP(i);
                    break;
                case "R":
                    list += possibleR(i);
                    break;
                case "N":
                    list += possibleN(i);
                    break;
                case "B":
                    list += possibleB(i);
                    break;
                case "Q":
                    list += possibleQ(i);
                    break;
                case "K":
                    list += possibleK(i);
                    break;
            }
        }

        return list; //x1, y1, x2, y2, captured piece
    }

    public static String possibleR(int i) {
        String list = "", pieceMoved = chessBoard[i / 8][i % 8], pieceTaken;
        int r = i / 8, c = i % 8;
        int temp = 1;
        for (int j = -1; j <= 1; j += 2) {
            //Horizontal movement for the rook
            try {
                while (" ".equals(chessBoard[r][c + temp * j])) {
                    chessBoard[r][c] = " ";
                    chessBoard[r][c + temp * j] = pieceMoved;
                    if (kingSafe(whiteToMove ? whiteKingPosition : blackKingPosition)) {
                        list = list + r + c + r + (c + temp * j) + pieceMoved + " ";
                    }
                    chessBoard[r][c] = pieceMoved;
                    chessBoard[r][c + temp * j] = " ";
                    temp++;
                }
                if (Character.isLowerCase(pieceMoved.charAt(0)) != Character.isLowerCase(chessBoard[r][c + temp * j].charAt(0))) {
                    pieceTaken = chessBoard[r][c + temp * j];
                    chessBoard[r][c] = " ";
                    chessBoard[r][c + temp * j] = pieceMoved;
                    if (kingSafe(whiteToMove ? whiteKingPosition : blackKingPosition)) {
                        list = list + r + c + r + (c + temp * j) + pieceMoved + pieceTaken;
                    }
                    chessBoard[r][c] = pieceMoved;
                    chessBoard[r][c + temp * j] = pieceTaken;
                }
            } catch (Exception e) {
            }
            temp = 1;
            //Vertical movement for the rook
            try {
                while (" ".equals(chessBoard[r + temp * j][c])) {
                    chessBoard[r][c] = " ";
                    chessBoard[r + temp * j][c] = pieceMoved;
                    if (kingSafe(whiteToMove ? whiteKingPosition : blackKingPosition)) {
                        list = list + r + c + (r + temp * j) + c + pieceMoved + " ";
                    }
                    chessBoard[r][c] = pieceMoved;
                    chessBoard[r + temp * j][c] = " ";
                    temp++;
                }
                if (Character.isLowerCase(pieceMoved.charAt(0)) != Character.isLowerCase(chessBoard[r + temp * j][c].charAt(0))) {
                    pieceTaken = chessBoard[r + temp * j][c];
                    chessBoard[r][c] = " ";
                    chessBoard[r + temp * j][c] = pieceMoved;
                    if (kingSafe(whiteToMove ? whiteKingPosition : blackKingPosition)) {
                        list = list + r + c + (r + temp * j) + c + pieceMoved + pieceTaken;
                    }
                    chessBoard[r][c] = pieceMoved;
                    chessBoard[r + temp * j][c] = pieceTaken;
                }
            } catch (Exception e) {
            }
            temp = 1;
        }

        return list;
    }

    public static String possibleN(int i) {
        String list = "", pieceMoved = chessBoard[i / 8][i % 8], pieceTaken;
        int r = i / 8, c = i % 8;
        for (int j = -1; j <= 1; j += 2) {
            for (int k = -1; k <= 1; k += 2) {
                // first 4
                try {
                    if (Character.isLowerCase(pieceMoved.charAt(0)) != Character.isLowerCase(chessBoard[r + j][c + k * 2].charAt(0)) || " ".equals(chessBoard[r + j][c + k * 2])) {
                        pieceTaken = chessBoard[r + j][c + k * 2];
                        chessBoard[r][c] = " ";
                        chessBoard[r + j][c + k * 2] = pieceMoved;
                        if (kingSafe(whiteToMove ? whiteKingPosition : blackKingPosition)) {
                            list = list + r + c + (r + j) + (c + k * 2) + pieceMoved + pieceTaken;
                        }
                        chessBoard[r][c] = pieceMoved;
                        chessBoard[r + j][c + k * 2] = pieceTaken;
                    }
                } catch (Exception e) {
                }
                // other 4
                try {
                    if (Character.isLowerCase(pieceMoved.charAt(0)) != Character.isLowerCase(chessBoard[r + j * 2][c + k].charAt(0)) || " ".equals(chessBoard[r + j * 2][c + k])) {
                        pieceTaken = chessBoard[r + j * 2][c + k];
                        chessBoard[r][c] = " ";
                        chessBoard[r + j * 2][c + k] = pieceMoved;
                        if (kingSafe(whiteToMove ? whiteKingPosition : blackKingPosition)) {
                            list = list + r + c + (r + j * 2) + (c + k) + pieceMoved + pieceTaken;
                        }
                        chessBoard[r][c] = pieceMoved;
                        chessBoard[r + j * 2][c + k] = pieceTaken;
                    }
                } catch (Exception e) {
                }
            }
        }

        return list;
    }

    public static String possibleB(int i) {
        String list = "", pieceMoved = chessBoard[i / 8][i % 8], pieceTaken;
        int r = i / 8, c = i % 8;
        int temp = 1;
        for (int j = -1; j <= 1; j += 2) {
            for (int k = -1; k <= 1; k += 2) {
                try {
                    while (" ".equals(chessBoard[r + temp * j][c + temp * k])) {
                        chessBoard[r][c] = " ";
                        chessBoard[r + temp * j][c + temp * k] = pieceMoved;
                        if (kingSafe(whiteToMove ? whiteKingPosition : blackKingPosition)) {
                            list = list + r + c + (r + temp * j) + (c + temp * k) + pieceMoved + " ";
                        }
                        chessBoard[r][c] = pieceMoved;
                        chessBoard[r + temp * j][c + temp * k] = " ";
                        temp++;
                    }
                    if (Character.isLowerCase(pieceMoved.charAt(0)) != Character.isLowerCase(chessBoard[r + temp * j][c + temp * k].charAt(0))) {
                        pieceTaken = chessBoard[r + temp * j][c + temp * k];
                        chessBoard[r][c] = " ";
                        chessBoard[r + temp * j][c + temp * k] = pieceMoved;
                        if (kingSafe(whiteToMove ? whiteKingPosition : blackKingPosition)) {
                            list = list + r + c + (r + temp * j) + (c + temp * k) + pieceMoved + pieceTaken;
                        }
                        chessBoard[r][c] = pieceMoved;
                        chessBoard[r + temp * j][c + temp * k] = pieceTaken;
                    }
                } catch (Exception e) {
                }
                temp = 1;
            }
        }

        return list;
    }

    public static String possibleQ(int i) {
        String list = "", pieceMoved = chessBoard[i / 8][i % 8], pieceTaken;
        int r = i / 8, c = i % 8;
        int temp = 1;
        for (int j = -1; j <= 1; j++) {
            for (int k = -1; k <= 1; k++) {
                if (j != 0 || k != 0) {
                    try {
                        while (" ".equals(chessBoard[r + temp * j][c + temp * k])) {
                            chessBoard[r][c] = " ";
                            chessBoard[r + temp * j][c + temp * k] = pieceMoved;
                            if (kingSafe(whiteToMove ? whiteKingPosition : blackKingPosition)) {
                                list = list + r + c + (r + temp * j) + (c + temp * k) + pieceMoved + " ";
                            }
                            chessBoard[r][c] = pieceMoved;
                            chessBoard[r + temp * j][c + temp * k] = " ";
                            temp++;
                        }
                        if (Character.isLowerCase(pieceMoved.charAt(0)) != Character.isLowerCase(chessBoard[r + temp * j][c + temp * k].charAt(0))) {
                            pieceTaken = chessBoard[r + temp * j][c + temp * k];
                            chessBoard[r][c] = " ";
                            chessBoard[r + temp * j][c + temp * k] = pieceMoved;
                            if (kingSafe(whiteToMove ? whiteKingPosition : blackKingPosition)) {
                                list = list + r + c + (r + temp * j) + (c + temp * k) + pieceMoved + pieceTaken;
                            }
                            chessBoard[r][c] = pieceMoved;
                            chessBoard[r + temp * j][c + temp * k] = pieceTaken;
                        }
                    } catch (Exception e) {
                    }
                    temp = 1;
                }
            }
        }

        return list;
    }

    public static String possibleK(int i) {
        String list = "", pieceMoved = chessBoard[i / 8][i % 8], pieceTaken;
        int r = i / 8, c = i % 8;
        for (int j = 0; j < 9; j++) {
            if (j != 4) {
                try {
                    if (Character.isLowerCase(pieceMoved.charAt(0)) != Character.isLowerCase(chessBoard[r - 1 + j / 3][c - 1 + j % 3].charAt(0)) || " ".equals(chessBoard[r - 1 + j / 3][c - 1 + j % 3])) {
                        pieceTaken = chessBoard[r - 1 + j / 3][c - 1 + j % 3];
                        chessBoard[r][c] = " ";
                        chessBoard[r - 1 + j / 3][c - 1 + j % 3] = pieceMoved;
                        int kingTemp;
                        if (whiteToMove) {
                            kingTemp = whiteKingPosition;
                            whiteKingPosition = i + (j / 3) * 8 + j % 3 - 9;
                        } else {
                            kingTemp = blackKingPosition;
                            blackKingPosition = i + (j / 3) * 8 + j % 3 - 9;
                        }
                        if (kingSafe(whiteToMove ? whiteKingPosition : blackKingPosition)) {
                            list = list + r + c + (r - 1 + j / 3) + (c - 1 + j % 3) + pieceMoved + pieceTaken;
                        }
                        chessBoard[r][c] = pieceMoved;
                        chessBoard[r - 1 + j / 3][c - 1 + j % 3] = pieceTaken;
                        if (whiteToMove) {
                            whiteKingPosition = kingTemp;
                        } else {
                            blackKingPosition = kingTemp;
                        }
                    }
                } catch (Exception e) {
                }
            }
        }

        //castling
        int kingPosition = whiteToMove ? whiteKingPosition : blackKingPosition;
        if (kingSafe(kingPosition)) {
            String currentCastleRights = whiteToMove ? castleRights.substring(castleRights.length() - 4, castleRights.length() - 2)
                    : castleRights.substring(castleRights.length() - 2, castleRights.length());
            currentCastleRights = currentCastleRights.toLowerCase();

            //king side castling
            if (currentCastleRights.charAt(0) == 'k') {
                if (" ".equals(chessBoard[kingPosition / 8][kingPosition % 8 + 1]) && " ".equals(chessBoard[kingPosition / 8][kingPosition % 8 + 2])
                        && kingSafe(kingPosition) && kingSafe(kingPosition + 1) && kingSafe(kingPosition + 2)) {
                    list = list + r + c + r + (c + 2) + pieceMoved + "C";
                }
            }
            //queen side castling
            if (currentCastleRights.charAt(1) == 'q') {
                if (" ".equals(chessBoard[kingPosition / 8][kingPosition % 8 - 1]) && " ".equals(chessBoard[kingPosition / 8][kingPosition % 8 - 2]) && " ".equals(chessBoard[kingPosition / 8][kingPosition % 8 - 3])
                        && kingSafe(kingPosition) && kingSafe(kingPosition - 1) && kingSafe(kingPosition - 2)) {
                    list = list + r + c + r + (c - 2) + pieceMoved + "C";
                }
            }
        }

        return list;
    }

    public static String possibleP(int i) {
        String list = "", pieceMoved = chessBoard[i / 8][i % 8], pieceTaken;
        //assume its white first
        String[] possibilities = {"Q", "R", "B", "N"};
        String[] possibilitiesBlack = {"q", "r", "b", "n"};
        int turnMultiplier = whiteToMove ? 1 : -1;
        if (!whiteToMove) {
            possibilities = possibilitiesBlack;
        }
        int r = i / 8, c = i % 8;
        for (int j = -1; j <= 1; j += 2) {
            try {
                //captures
                if (!" ".equals(chessBoard[r - 1 * turnMultiplier][c + j]) && Character.isLowerCase(pieceMoved.charAt(0)) != Character.isLowerCase(chessBoard[r - 1 * turnMultiplier][c + j].charAt(0)) && !(r - 1 * turnMultiplier == 0 || r - 1 * turnMultiplier == 7)) {
                    pieceTaken = chessBoard[r - 1 * turnMultiplier][c + j];
                    chessBoard[r][c] = " ";
                    chessBoard[r - 1 * turnMultiplier][c + j] = pieceMoved;
                    if (kingSafe(whiteToMove ? whiteKingPosition : blackKingPosition)) {
                        list = list + r + c + (r - 1 * turnMultiplier) + (c + j) + pieceMoved + pieceTaken;
                    }
                    chessBoard[r][c] = pieceMoved;
                    chessBoard[r - 1 * turnMultiplier][c + j] = pieceTaken;
                }
            } catch (Exception e) {
            }
            try {
                //promotions and captures
                if (!" ".equals(chessBoard[r - 1 * turnMultiplier][c + j]) && Character.isLowerCase(pieceMoved.charAt(0)) != Character.isLowerCase(chessBoard[r - 1 * turnMultiplier][c + j].charAt(0)) && (r - 1 * turnMultiplier == 0 || r - 1 * turnMultiplier == 7)) {
                    for (int k = 0; k < 4; k++) {
                        pieceTaken = chessBoard[r - 1 * turnMultiplier][c + j];
                        chessBoard[r][c] = " ";
                        chessBoard[r - 1 * turnMultiplier][c + j] = possibilities[k];
                        if (kingSafe(whiteToMove ? whiteKingPosition : blackKingPosition)) {
                            //format: column1, column2, captured piece, new piece, P
                            list = list + c + (c + j) + pieceTaken + possibilities[k] + pieceMoved + "A";
                        }
                        chessBoard[r][c] = pieceMoved;
                        chessBoard[r - 1 * turnMultiplier][c + j] = pieceTaken;
                    }
                }
            } catch (Exception e) {
            }
        }
        try {
            //move one up
            if (" ".equals(chessBoard[r - 1 * turnMultiplier][c]) && !(r - 1 * turnMultiplier == 0 || r - 1 * turnMultiplier == 7)) {
                chessBoard[r][c] = " ";
                chessBoard[r - 1 * turnMultiplier][c] = pieceMoved;
                if (kingSafe(whiteToMove ? whiteKingPosition : blackKingPosition)) {
                    list = list + r + c + (r - 1 * turnMultiplier) + (c) + pieceMoved + " ";
                }
                chessBoard[r][c] = pieceMoved;
                chessBoard[r - 1 * turnMultiplier][c] = " ";
            }
        } catch (Exception e) {
        }
        try {
            //promotion with no capture
            if (" ".equals(chessBoard[r - 1 * turnMultiplier][c]) && (r - 1 * turnMultiplier == 0 || r - 1 * turnMultiplier == 7)) {
                for (int k = 0; k < 4; k++) {
                    pieceTaken = chessBoard[r - 1 * turnMultiplier][c];
                    chessBoard[r][c] = " ";
                    chessBoard[r - 1 * turnMultiplier][c] = possibilities[k];
                    if (kingSafe(whiteToMove ? whiteKingPosition : blackKingPosition)) {
                        //format: column1, column2, captured piece, new piece, P
                        list = list + c + (c) + pieceTaken + possibilities[k] + pieceMoved + "A";
                    }
                    chessBoard[r][c] = pieceMoved;
                    chessBoard[r - 1 * turnMultiplier][c] = pieceTaken;
                }
            }
        } catch (Exception e) {
        }
        try {
            //move two up
            if (" ".equals(chessBoard[r - 1 * turnMultiplier][c]) && " ".equals(chessBoard[r - 2 * turnMultiplier][c]) && (r + 2 * turnMultiplier == 8 || r + 2 * turnMultiplier == -1)) {
                pieceTaken = chessBoard[r - 2 * turnMultiplier][c];
                chessBoard[r][c] = " ";
                chessBoard[r - 2 * turnMultiplier][c] = pieceTaken;
                if (kingSafe(whiteToMove ? whiteKingPosition : blackKingPosition)) {
                    list = list + r + c + (r - 2 * turnMultiplier) + (c) + pieceMoved + pieceTaken;
                }
                chessBoard[r][c] = pieceMoved;
                chessBoard[r - 2 * turnMultiplier][c] = pieceTaken;
            }
        } catch (Exception e) {
        }

        return list;
    }

    public static boolean kingSafe(int position) {
        String[] pieces = {"k", "q", "r", "b", "n", "p"};
        String[] whitePieces = {"K", "Q", "R", "B", "N", "P"};

        int turnMultiplier = whiteToMove ? 1 : -1;
        if (!whiteToMove) {
            pieces = whitePieces;
        }

        //bishops/queen
        int temp = 1;
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                try {
                    while (" ".equals(chessBoard[position / 8 + temp * i][position % 8 + temp * j])) {
                        temp++;
                    }
                    if (pieces[3].equals(chessBoard[position / 8 + temp * i][position % 8 + temp * j])
                            || pieces[1].equals(chessBoard[position / 8 + temp * i][position % 8 + temp * j])) {
                        return false;
                    }
                } catch (Exception e) {
                }
                temp = 1;
            }
        }
        //rooks and queens
        for (int i = -1; i <= 1; i += 2) {
            //horizontal moves
            try {
                while (" ".equals(chessBoard[position / 8][position % 8 + temp * i])) {
                    temp++;
                }
                if (pieces[2].equals(chessBoard[position / 8][position % 8 + temp * i])
                        || pieces[1].equals(chessBoard[position / 8][position % 8 + temp * i])) {
                    return false;
                }
            } catch (Exception e) {
            }
            temp = 1;
            //vertical moves
            try {
                while (" ".equals(chessBoard[position / 8 + temp * i][position % 8])) {
                    temp++;
                }
                if (pieces[2].equals(chessBoard[position / 8 + temp * i][position % 8])
                        || pieces[1].equals(chessBoard[position / 8 + temp * i][position % 8])) {
                    return false;
                }
            } catch (Exception e) {
            }
            temp = 1;
        }

        //knight
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                try {
                    if (pieces[4].equals(chessBoard[position / 8 + i][position % 8 + j * 2])) {
                        return false;
                    }
                } catch (Exception e) {
                }
                try {
                    if (pieces[4].equals(chessBoard[position / 8 + i * 2][position % 8 + j])) {
                        return false;
                    }
                } catch (Exception e) {
                }
            }
        }
        //pawn
        try {
            if (pieces[5].equals(chessBoard[position / 8 - 1 * turnMultiplier][position % 8 - 1])) {
                return false;
            }
        } catch (Exception e) {
        }
        try {
            if (pieces[5].equals(chessBoard[position / 8 - 1 * turnMultiplier][position % 8 + 1])) {
                return false;
            }
        } catch (Exception e) {
        }

        //king
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 || j != 0) {
                    try {
                        if (pieces[0].equals(chessBoard[position / 8 + i][position % 8 + j])) {
                            return false;
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }

        return true;
    }

    public static String toSAN(String move) {
        String sanMove;

        if (move.charAt(5) == 'C') {
            //castling move
            if (move.charAt(3) == '6') {
                sanMove = "O-O";
            } else {
                sanMove = "O-O-O";
            }
        } else if (move.charAt(5) == 'A') {
            //if move is a promotion move
            //structure: start col - end col - piece captured - promoted piece - piece moved - A
            int endRow = Character.isUpperCase(move.charAt(4)) ? 8 : 1;
            char c1 = (char) ('a' + Character.getNumericValue(move.charAt(0)));
            char c2 = (char) ('a' + Character.getNumericValue(move.charAt(1)));

            if (move.charAt(0) != move.charAt(1)) {
                //if the start col is not the same as end col, a capture is made
                sanMove = c1 + "x" + c2 + endRow + "=" + Character.toUpperCase(move.charAt(3));
            } else {
                //no capture is made while promoting
                sanMove = "" + c1 + endRow + "=" + Character.toUpperCase(move.charAt(3));
            }

        } else {
            //if its a normal move
            char c1 = (char) ('a' + Character.getNumericValue(move.charAt(1)));
            char c2 = (char) ('a' + Character.getNumericValue(move.charAt(3)));
            int r2 = 8 - Character.getNumericValue(move.charAt(2));

            char piece = Character.toUpperCase(move.charAt(4));

            //capture move
            if (move.charAt(5) != ' ') {
                sanMove = (piece == 'P' ? c1 : piece) + "x" + c2 + r2;
            } else {
                sanMove = (piece == 'P' ? "" : piece) + (move.charAt(5) == ' ' ? "" : "x") + c2 + r2;
            }

        }

        return sanMove;
    }
}
