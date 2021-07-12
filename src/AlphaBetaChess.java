import javax.swing.*;
import java.util.*;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

public class AlphaBetaChess {
    //lower case is black, capital is white, a is king while n is night
    
    
    public static int humanAsWhite = -1;
    public static String bestMove;
    public static boolean THINKING = false;
    static int globalDepth = 3;
    public static int CHECKMATE = 200000;
    public static boolean book = true;
    public static void main(String[] args) {
//        UCI.UCICommunication();
        Board.start();
        JFrame f = new JFrame("Chess Program");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UserInterface ui = new UserInterface();
        f.add(ui);
        f.setSize(512, 540);
        f.setVisible(true);
        Object[] option = {"Computer", "Human"};
        humanAsWhite = JOptionPane.showOptionDialog(null, "Who should play as white?", "Option", JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE, null, option, option[1]);
        if(humanAsWhite == 0){
            if(book){
                useBook(Board.moveHistory);
            } else {
                computerMove();
            }
            f.repaint();
        }
        
//        for (int i = 0; i < 8; i++) {
//            System.out.println(Arrays.toString(chessBoard[i]));
//        }

    }
    
    public static String computerMove(){
        int pieces = 0;
        for (int i = 0; i < 64; i++) {
            if(!" ".equals(Board.chessBoard[i / 8][i % 8])){
                pieces++;
            }
        }
        
        //sets midgame or endgame depending on the number of pieces left on the board
        if(pieces < 18 && !Rating.middleGame && !Rating.endGame){
            Rating.middleGame();
        } else if (pieces < 10 && !Rating.endGame){
            Rating.endGame();
        }
        
        alphaBeta(globalDepth, -1000000, 1000000, Board.whiteToMove?1:-1);
        Board.makeMove(bestMove);
        Board.moveHistory = Board.moveHistory + bestMove;
        
        
        String tempBestMove = bestMove;
        bestMove = null;
        return tempBestMove;
    }
    
    public static void useBook(String moveHistory){
        //loop through the string where each move is length 5
        String sanMoves = "";
        for (int i = 0; i < moveHistory.length(); i+=6) {
            String move = moveHistory.substring(i, i+6);
            //convert each move to a string of standard algebraic notation
            sanMoves = sanMoves + Board.toSAN(move);
        }
        
        String possibilities = "";
        try {
            //get database file with all games
            File myObj = new File("games.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
              String game = myReader.nextLine();
              
              //check if game starts with same moves
              if (game.startsWith(sanMoves)){
                  String[] moves = game.split(" ");
                  String nextMove = moves[moveHistory.length()/6];
                  
                  possibilities = possibilities + nextMove + " ";
              }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
          System.out.println("An error occurred.");
          e.printStackTrace();
        };
        
        if(possibilities.length() == 0 || moveHistory.length()/6 > 15){
            book = false;
            computerMove();
            return;
            //calculate normally
        }
        
        //pick a random move and use it
        String[] possibleMoves = possibilities.split(" ");
        int rnd = new Random().nextInt(possibleMoves.length);
        String chosenMove = possibleMoves[rnd];
        chosenMove = chosenMove.replace("+", "");
        
        //non-castling
        int r2 = ('8'-chosenMove.charAt(chosenMove.length() - 1));
        int c2 = (chosenMove.charAt(chosenMove.length() - 2)-'a');
        char pieceMoved = Character.isLowerCase(chosenMove.charAt(0)) ? 'P' : chosenMove.charAt(0);
        if(!Board.whiteToMove){
            pieceMoved = Character.toLowerCase(pieceMoved);
        }
        
        
        String legalMoves = Board.possibleMoves();
        String finalMove = "";
        for (int i = 0; i < legalMoves.length(); i+=6) {
            if(legalMoves.charAt(i+4) == pieceMoved && Character.getNumericValue(legalMoves.charAt(i+3)) == c2 && Character.getNumericValue(legalMoves.charAt(i+2)) == r2){
                finalMove = legalMoves.substring(i, i+6);
            }
        }
        
        if ("O-O".equals(chosenMove)){
            if(Board.whiteToMove){
                finalMove = "7476KC";
            } else {
                finalMove = "0406kC";
            }
        } else if ("O-O-O".equals(chosenMove)){
            if(Board.whiteToMove){
                finalMove = "7472KC";
            } else {
                finalMove = "0402kC";
            }
        }
        
        bestMove = finalMove;
        Board.moveHistory = Board.moveHistory + bestMove;
        Board.makeMove(bestMove);
    }
    
    public static int alphaBeta(int depth, int alpha, int beta, int turnMultiplier){
        //return in form 1234b################
        String list = Board.possibleMoves();
        
        if(list.length() == 0){
            return Rating.rating(list.length(), depth, turnMultiplier) * turnMultiplier;
        } 
        else if (depth == 0){
            return searchAllCaptures(alpha, beta, turnMultiplier, 1);
        }
        
        //fix sortmoves
        list = sortMoves(list, turnMultiplier);
        
        int maxScore = -CHECKMATE*100;
        
        for (int i = 0; i < list.length(); i+=6) {
            Board.makeMove(list.substring(i, i+6));
            int score = -alphaBeta(depth - 1, -beta, -alpha, -turnMultiplier);
            Board.undoMove(list.substring(i, i+6));
            
            
            if (score > maxScore){
                
                maxScore = score;
                if(depth == globalDepth){
                    bestMove = list.substring(i, i+6);
                    System.out.println(Board.toSAN(bestMove) + ": " + (float)score * turnMultiplier / 100);
                }
            }
            if (maxScore > alpha){  // pruning happens
                alpha = maxScore;
            }
            if (beta <= alpha){
                break;
            }
        }
       
        
        return maxScore;
    }
    
    public static int searchAllCaptures(int alpha, int beta, int turnMultiplier, int depth){
        String list = Board.possibleMoves();
        
        int evaluation = turnMultiplier * Rating.rating(list.length(), 1, 0);
        
        if(evaluation == CHECKMATE || evaluation == -CHECKMATE){
            evaluation /= depth;
        }
        
        if(evaluation >= beta){
            return beta;
        }
        alpha = Math.max(alpha, evaluation);
        
        String captureList = list;
        if(list.length() > 1){
            captureList = getCaptures(list);
        }
        
        
        captureList = sortMoves(captureList, turnMultiplier);
        
        for (int i = 0; i < captureList.length(); i+=6) {
            Board.makeMove(captureList.substring(i, i+6));
            evaluation = -searchAllCaptures(-beta, -alpha, -turnMultiplier, depth + 1);
            Board.undoMove(captureList.substring(i, i+6));
            if(evaluation == CHECKMATE || evaluation == -CHECKMATE){
                evaluation /= depth;
            }
            
            if(evaluation >= beta){
                return beta;
            }
            alpha = Math.max(alpha, evaluation);
        }
        
        return alpha;
    }
    
    public static String getCaptures(String list){
        for (int i = list.length()-6; i >= 0; i-=6) {
            if(list.charAt(i+5) == ' '){
                list = list.replace(list.substring(i, i+6), "");
            }
        }
        return list;
    }
    
    public static String sortMoves(String list, int turnMultiplier){
        int[] score = new int[list.length()/6];
        for (int i = 0; i < list.length(); i+=6) {
            Board.makeMove(list.substring(i, i+6));
            score[i/6] = turnMultiplier * Rating.rating(-1, 0, 0);
            Board.undoMove(list.substring(i, i+6));
        }
        String newListA = "", newListB = list;
        for (int i = 0; i < Math.min(6, list.length()/6); i++) { //first few moves only
            int max = -1000000, maxLocation = 0;
            for (int j = 0; j < list.length()/6; j++) {
                if(score[j]>max){
                    max = score[j];
                    maxLocation = j;
                }
            }
            score[maxLocation] = -1000000;
            newListA += list.substring(maxLocation*6, maxLocation*6+6);
            newListB = newListB.replace(list.substring(maxLocation*6, maxLocation*6+6), "");
        }
        
        return newListA + newListB;
    }
    
    
    
    
}
