import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


/**
 * Course: COSC 2P03
 * Name: Graham Burgsma
 * Date: 14-09-20
 */
public class Pawn extends Piece {

	public Pawn(Colour colour) {
		super(Name.PAWN, colour);

		try {
			if (colour == Colour.BLACK)
				image = ImageIO.read(new File("Images/BlackPawn.png"));
			else if (colour == Colour.WHITE)
				image = ImageIO.read(new File("Images/WhitePawn.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void convert() {
		//convert to a queen
	}

}
