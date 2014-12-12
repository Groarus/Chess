import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


/**
 * Project: Chess
 * Course: COSC 3P71 - Final Project
 * Created: December, 2014
 */

public class Queen extends Piece {
	public Queen(Colour colour) {
		super(Name.QUEEN, colour);

		try {
			if (colour == Colour.BLACK)
				image = ImageIO.read(new File("Images/BlackQueen.png"));
			else if (colour == Colour.WHITE)
				image = ImageIO.read(new File("Images/WhiteQueen.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
