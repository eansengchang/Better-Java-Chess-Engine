import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class UserInterface extends JPanel implements MouseListener, MouseMotionListener{
    static int mouseX, mouseY, newMouseX, newMouseY;
    
    static int squareSize = 64;
    @Override
    public void paintComponent(Graphics g){
//        long startTime = System.currentTimeMillis();
        super.paintComponent(g);
        this.setBackground(Color.yellow);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
//        g.setColor(Color.blue);
//        g.fillRect(x-20, y-20, 40, 40);
//        g.setColor(new Color(190, 81, 215));
//        g.fillRect(40, 20, 80, 50);
//        g.drawString("ESC", x, y);
        

        for (int i = 0; i < 64; i+=2) {
            g.setColor(new Color(240, 217, 181));
            g.fillRect((i % 8 + (i/8)%2) * squareSize, (i / 8) * squareSize, squareSize, squareSize);
            g.setColor(new Color(181, 136, 98));
            g.fillRect(((i+1) % 8 - ((i+1)/8)%2) * squareSize, ((i+1) / 8) * squareSize, squareSize, squareSize);
        }
        
        Image chessPiecesImage;
        chessPiecesImage = new ImageIcon("ChessPieces.png").getImage();
        for (int i = 0; i < 64; i++) {
            int j = -1, k = -1;
            switch(Board.chessBoard[i/8][i%8]){
                case "P": 
                    j = 5; k = 0;
                    break;
                case "p": 
                    j = 5; k = 1;
                    break;
                case "R": 
                    j = 2; k = 0;
                    break;
                case "r": 
                    j = 2; k = 1;
                    break;
                case "N": 
                    j = 4; k = 0;
                    break;
                case "n": 
                    j = 4; k = 1;
                    break;
                case "B": 
                    j = 3; k = 0;
                    break;
                case "b": 
                    j = 3; k = 1;
                    break;
                case "Q": 
                    j = 1; k = 0;
                    break;
                case "q": 
                    j = 1; k = 1;
                    break;
                case "K": 
                    j = 0; k = 0;
                    break;
                case "k": 
                    j = 0; k = 1;
                    break;
            }
            if (j != -1 && k != -1){
                g.drawImage(chessPiecesImage, (i%8)*squareSize, (i/8)*squareSize, ((i%8)+1)*squareSize, ((i/8)+1)*squareSize, j*64, k*64, (j+1)*64, (k+1)*64, this);
            }
        }
//        long endTime = System.currentTimeMillis();
//        System.out.println("painting took " + (endTime - startTime) + " milleseconds");
        
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//            // do huge work
//            
//        }
//}
//        );
    }
    @Override
    public void mouseMoved(MouseEvent e){
    }
    @Override
    public void mousePressed(MouseEvent e){
        if(e.getX() < 8 * squareSize && e.getY() < 8 * squareSize){
             //if inside the board
             mouseX = e.getX();
             mouseY = e.getY();
        }
    }
    @Override
    public void mouseReleased(MouseEvent e){
        if(e.getX() < 8 * squareSize && e.getY() < 8 * squareSize){
            //if inside the board
            newMouseX = e.getX();
            newMouseY = e.getY();
            
            if (e.getButton() == MouseEvent.BUTTON1){
                String dragMove;
                if (newMouseY/squareSize == 0 && mouseY/squareSize == 1 && "P".equals(Board.chessBoard[mouseY/squareSize][mouseX/squareSize])){
                    //white pawn promotion
                    dragMove = ""+mouseX/squareSize + newMouseX/squareSize + Board.chessBoard[newMouseY/squareSize][newMouseX/squareSize] + "QPA";
                } else if (newMouseY/squareSize == 7 && mouseY/squareSize == 6 && "p".equals(Board.chessBoard[mouseY/squareSize][mouseX/squareSize])){
                    //black pawn promotion
                    dragMove = ""+mouseX/squareSize + newMouseX/squareSize + Board.chessBoard[newMouseY/squareSize][newMouseX/squareSize] + "qpA";
                } else if (Math.abs(mouseX/squareSize - newMouseX/squareSize) == 2 && "k".equals(Board.chessBoard[mouseY/squareSize][mouseX/squareSize].toLowerCase())){
                    dragMove = ""+mouseY/squareSize + mouseX/squareSize+ newMouseY/squareSize + newMouseX/squareSize + Board.chessBoard[mouseY/squareSize][mouseX/squareSize] + "C";
                } else {
                    //regular move
                    dragMove = ""+mouseY/squareSize + mouseX/squareSize+ newMouseY/squareSize + newMouseX/squareSize + Board.chessBoard[mouseY/squareSize][mouseX/squareSize] + Board.chessBoard[newMouseY/squareSize][newMouseX/squareSize];
                }
                String userPossibilities = Board.possibleMoves();
//                System.out.println(userPossibilities);
//                System.out.println("dragMove: " + dragMove);
                if (userPossibilities.replace(dragMove, "").length() < userPossibilities.length()){
                    System.out.println("Move has been submitted!");
                    Board.moveHistory = Board.moveHistory + dragMove;
                    Board.makeMove(dragMove);
                    repaint();
                    
                    if(!AlphaBetaChess.THINKING){
                        AlphaBetaChess.THINKING = true;
                        long startTime = System.currentTimeMillis();
                        if(AlphaBetaChess.book){
                            AlphaBetaChess.useBook(Board.moveHistory);
                        } else {
                            AlphaBetaChess.computerMove();
                        }
                        long endTime = System.currentTimeMillis();
                        System.out.println("calculating took " + (endTime - startTime) + " milleseconds");
                        repaint();
                        AlphaBetaChess.THINKING = false;
                    }
                }

             }
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e){
    }
    @Override
    public void mouseDragged(MouseEvent e){
    
    }
    @Override
    public void mouseEntered(MouseEvent e){}
    @Override
    public void mouseExited(MouseEvent e){}
}


