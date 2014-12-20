import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;


/**
 * Project: Chess
 * Course: COSC 3P71 - Final Project
 * Created: December, 2014
 */

public class Pawn extends Piece {

    public Pawn(Colour colour) {
        super(Name.PAWN, colour);

        try {
            if (colour == Colour.BLACK)
                image = ImageIO.read(new File("Images/BlackPawn.png"));
            else if (colour == Colour.WHITE)
                image = ImageIO.read(new File("Images/WhitePawn.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Piece convert() {
        //convert to a queen
        Object[] option = {"Queen", "Knight", "Rook", "Bishop"};
        int choice = JOptionPane.showOptionDialog(null, "What would you like to promote your piece to?", "Piece Promotion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[0]);

        Piece returnPiece = this;
        switch (choice) {
            case 0:
                returnPiece = new Queen(this.getColour());
                break;
            case 1:
                returnPiece = new Knight(this.getColour());
                break;
            case 2:
                returnPiece = new Rook(this.getColour());
                break;
            case 3:
                returnPiece = new Bishop(this.getColour());
                break;
        }
        returnPiece.setLocation(this.getLocation());
        returnPiece.setPrevLocation(this.getPrevLocation());
        return returnPiece;
    }

}
