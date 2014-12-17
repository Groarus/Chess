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
    private int numMoves = 0;
    protected JLabel numMovesValue = new JLabel(Integer.toString(numMoves));
    private int piecesLeft = 16;
    protected JLabel piecesLeftValue = new JLabel(Integer.toString(piecesLeft));
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

    public int getNumMoves() {
        return numMoves;
    }

    public void incrementMoves() {
        this.numMoves++;
        numMovesValue.setText(Integer.toString(numMoves));
    }

    public void incPiecesLeft() {
        this.piecesLeft++;
    }

    public void decPiecesLeft() {
        this.piecesLeft--;
        piecesLeftValue.setText(Integer.toString(piecesLeft));
    }
}
