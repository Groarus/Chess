/**
 * Project: Chess
 * Course: COSC 3P71 - Final Project
 * Created: December, 2014
 */


public class State {

    private Piece[][] state;

    public State(Piece[][] state) {
        this.state = state;
    }

    public State() {
        this.state = new Piece[8][8];
    }

    public Piece[][] getState() {
        return state;
    }

    public void setState(Piece[][] state) {
        this.state = state;
    }

    public void setPiece(int x, int y, Piece piece) {
        state[x][y] = piece;
        if (!(piece == null)) {
            state[x][y].setLocation(new Location(x, y));
        }
    }

    public Piece getPiece(int x, int y) {
        return state[x][y];
    }

    public Piece getPiece(Location location) {
        return state[location.getX()][location.getY()];
    }
}
