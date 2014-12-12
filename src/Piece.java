import java.awt.image.BufferedImage;


/**
 * Project: Chess
 * Course: COSC 3P71 - Final Project
 * Created: December, 2014
 */

public abstract class Piece {

	protected BufferedImage image = null;
	private Location location, prevLocation = null;
	private Colour colour;
	private Name name;
	private Status status;
	private double value;

	public Piece(Name name, Colour colour) {
		this.colour = colour;
		this.name = name;
		this.status = Status.ALIVE;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String printLocation() {
		String string = Character.toString((char) (location.getX() + 65));
		string = string + (location.getY() + 1);
		return string;
	}

	public Colour getColour() {
		return colour;
	}

	public Name getName() {
		return name;
	}

	public Status getStatus() {
		return status;
	}

	public BufferedImage getImage() {
		return image;
	}

	public Location getPrevLocation() {
		return prevLocation;
	}

	public void setPrevLocation(Location prevLocation) {
		this.prevLocation = prevLocation;
	}

	enum Status {
		ALIVE, DEAD;
	}

	enum Name {
		BISHOP, KING, KNIGHT, PAWN, QUEEN, ROOK;
	}
}
