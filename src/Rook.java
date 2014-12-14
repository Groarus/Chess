import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


/**
 * Project: Chess
 * Course: COSC 3P71 - Final Project
 * Created: December, 2014
 */

public class Rook extends Piece {
    public Rook(Colour colour) {
        super(Name.ROOK, colour);

        try {
            if (colour == Colour.BLACK)
                image = ImageIO.read(new File("Images/BlackRook.png"));
            else if (colour == Colour.WHITE)
                image = ImageIO.read(new File("Images/WhiteRook.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}