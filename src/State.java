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

    public void setPiece(int m, int n, Piece piece) {
        state[m][n] = piece;
        if (!(piece == null)) {
            state[m][n].setLocation(new Location(m, n));
        }
    }

    public Piece getPiece(int m, int n) {
        return state[m][n];
    }
}
