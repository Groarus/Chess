import javax.swing.*;

/**
 * Project: Chess
 * Course:
 * Created on 15 December, 2014
 */
public class Player {

    protected JPanel panel;
    protected Board board;
    protected MoveEngine moveEngine;
    protected GUI gui;
    private Colour colour;

    public Player(Colour colour, Board board, GUI gui) {
        this.colour = colour;
        this.board = board;
        this.gui = gui;
        moveEngine = new MoveEngine(board);
    }

    public Colour getColour() {
        return colour;
    }
}
