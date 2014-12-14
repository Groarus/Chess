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
        BufferedImage background = null;
        try {
            if (board.getHumanPlayer().getColour() == Colour.WHITE)
                background = ImageIO.read(new File("Images/WhiteChessBoard.png"));
            else
                background = ImageIO.read(new File("Images/BlackChessBoard.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        g.drawImage(background, 0, 0, null);

        //PIECES//
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                try {
                    if (board.getHumanPlayer().getColour() == Colour.WHITE)
                        g.drawImage(board.getCurrentState().getPiece(i, j).getImage(), ((i * 100) + 25), (750 - ((j * 100) + 25)), null);
                    else
                        g.drawImage(board.getCurrentState().getPiece(i, j).getImage(), 750 - ((i * 100) + 25), ((j * 100) + 25), null);
                } catch (NullPointerException e) {
                    //Do nothing - occurs when getting image of blank space on board
                }
            }
        }
    }
}
