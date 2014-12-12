import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


/**
 * Course: COSC 2P03
 * Name: Graham Burgsma
 * Date: 14-09-20
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
