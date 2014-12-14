import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


/**
 * Project: Chess
 * Course: COSC 3P71 - Final Project
 * Created: December, 2014
 */
public class Knight extends Piece {
    public Knight(Colour colour) {
        super(Name.KNIGHT, colour);

        try {
            if (colour == Colour.BLACK)
                image = ImageIO.read(new File("Images/BlackKnight.png"));
            else if (colour == Colour.WHITE)
                image = ImageIO.read(new File("Images/WhiteKnight.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
