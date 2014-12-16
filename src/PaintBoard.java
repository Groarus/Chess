import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
 * Project: Chess
 * Course: COSC 3P71 - Final Project
 * Created: December, 2014
 */
public class PaintBoard extends JPanel {

    private Board board;

    public PaintBoard(Board board) {
        this.board = board;
        setSize(850, 850);
    }

    @Override
    public void paintComponent(Graphics g) throws NullPointerException {
        //BACKGROUND//
        BufferedImage background = null, selected = null, possibleMove = null, inCheck = null;
        try {
            if (board.getWhitePlayer().getClass() == HumanPlayer.class)
                background = ImageIO.read(new File("Images/WhiteChessBoard.png"));
            else
                background = ImageIO.read(new File("Images/BlackChessBoard.png"));

            selected = ImageIO.read(new File("Images/Selected.png"));
            possibleMove = ImageIO.read(new File("Images/PossibleMove.png"));
            inCheck = ImageIO.read(new File("Images/InCheck.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        g.drawImage(background, 0, 0, null);

        //PIECES//
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                try {
                    int x, y;
                    if (board.getWhitePlayer().getClass() == HumanPlayer.class) {
                        x = ((i * 100) + 25);
                        y = (750 - ((j * 100) + 25));
                    } else {
                        x = 750 - ((i * 100) + 25);
                        y = ((j * 100) + 25);
                    }
                    if (board.getCurrentState().getPiece(i, j).isSelected())
                        g.drawImage(selected, x, y, null);
                    if (board.getCurrentState().getPiece(i, j).isPossibleMove())
                        g.drawImage(possibleMove, x, y, null);
                    if (board.getCurrentState().getPiece(i, j).isInCheck())
                        g.drawImage(inCheck, x, y, null);
                    g.drawImage(board.getCurrentState().getPiece(i, j).getImage(), x, y, null);
                } catch (NullPointerException e) {
                    //Do nothing - occurs when getting image of blank space on board
                }
            }
        }
    }
}
