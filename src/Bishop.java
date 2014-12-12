import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


/**
 * Course: COSC 2P03
 * Name: Graham Burgsma
 * Date: 14-09-20
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
