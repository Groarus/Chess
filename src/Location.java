/**
 * Course: COSC 2P03
 * Name: Graham Burgsma
 * Date: 14-09-20
 */
public class Location {

	private int x, y;

	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void changeLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}
}
