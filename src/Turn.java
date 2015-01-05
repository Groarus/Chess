/**
 * Project: Chess
 * Course: COSC 3P71 - Final Project
 * Created: December, 2014
 */

public class Turn {
    //Used to keep track of who's turn it is
    private Colour turn;

    public Turn() {
        turn = Colour.WHITE;
    }

    public Colour getTurn() {
        return this.turn;
    }

    public void next() {
        if (this.turn == Colour.WHITE) {
            this.turn = Colour.BLACK;
        } else {
            this.turn = Colour.WHITE;
        }
    }
}
