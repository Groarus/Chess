import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


/**
 * Course: COSC 2P03
 * Name: Graham Burgsma
 * Date: 14-09-20
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
