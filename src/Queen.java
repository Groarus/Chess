import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


/**
 * Course: COSC 2P03
 * Name: Graham Burgsma
 * Date: 14-09-20
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
