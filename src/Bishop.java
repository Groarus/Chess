import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


/**
 * Project: Chess
 * Course: COSC 3P71 - Final Project
 * Created: December, 2014
 */
public class Bishop extends Piece {


    public Bishop(Colour colour) {
        super(Name.BISHOP, colour);

        try {
            if (colour == Colour.BLACK)
                image = ImageIO.read(new File("Images/BlackBishop.png"));
            else if (colour == Colour.WHITE)
                image = ImageIO.read(new File("Images/WhiteBishop.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
