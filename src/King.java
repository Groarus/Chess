import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


/**
 * Project: Chess
 * Course: COSC 3P71 - Final Project
 * Created: December, 2014
 */
public class King extends Piece {
    public King(Colour colour) {
        super(Name.KING, colour);

        try {
            if (colour == Colour.BLACK)
                image = ImageIO.read(new File("Images/BlackKing.png"));
            else if (colour == Colour.WHITE)
                image = ImageIO.read(new File("Images/WhiteKing.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
