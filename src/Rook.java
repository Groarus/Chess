import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


/**
 * Course: COSC 2P03
 * Name: Graham Burgsma
 * Date: 14-09-20
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