import javax.swing.*;

/**
 * Project: Chess
 * Course: COSC 3P71 - Final Project
 * Created: December, 2014
 */
public class Player {

    protected JPanel panel;
    protected Board board;
    protected MoveEngine moveEngine;
    protected GUI gui;
    protected JLabel piecesLeftValue, numMovesValue;
    protected MoveHistory moveHistory;
    private int numMoves = 0, piecesLeft = 16;
    private Colour colour;
    private Turn turn;

    public Player(Colour colour, Board board, GUI gui, Turn turn, MoveHistory moveHistory) {
        this.turn = turn;
        this.colour = colour;
        this.board = board;
        this.gui = gui;
        this.piecesLeftValue = new JLabel(Integer.toString(piecesLeft));
        this.numMovesValue = new JLabel(Integer.toString(numMoves));
        this.moveHistory = moveHistory;
        this.moveEngine = new MoveEngine(board);
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

    public synchronized Turn getTurn() {
        return this.turn;
    }

}
