import javax.swing.*;
import java.awt.*;
import java.util.Stack;


/**
 * Project: Chess
 * Course: COSC 3P71 - Final Project
 * Created: December, 2014
 */
public class ComputerPlayer extends Player implements Runnable {
    private Piece selected = new Empty(Piece.Name.EMPTY, Colour.NEUTRAL);
    private MoveEngine move;


    int row = 0; //Test variables
    int column = 6;


    public ComputerPlayer(Colour colour, Board board, GUI gui, Turn turn) {
        super(colour, board, gui, turn);
        //set up the GUI
        this.move = new MoveEngine(board);
        infoPanel();
        gui.addSidePanel(panel);
    }

    private void infoPanel() {
        panel = new JPanel(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;

        JLabel computer = new JLabel("Computer");
        computer.setFont(new Font("Serif", Font.BOLD, 20));
        panel.add(computer, constraints);
    }

    private void selectAndMove() {
        //Method to select a piece and move piece.

        selected = board.getCurrentState().getPiece(row, column);
        selected.setLocation(new Location(row, column)); //setting the location of the piece as it is not set prior
        selected.setSelected(true);
     //   possibleMove(); //Could use for selecting a move out of the possible moves


        if (selected.getName() != Piece.Name.EMPTY) {
            column--;
            if (move.move(selected, new Location(row, column))) { //MAIN MOVEMENT OF PLAYER
                getTurn().next();
            }
            gui.repaint(); //refreshes the board
            selected = new Empty(Piece.Name.EMPTY, Colour.NEUTRAL); //unselect it
        }

    }

    public void run() {
        while (true) {
            if (getTurn().getTurn() == getColour()) {
                selectAndMove();
            }
        }

    }

    private void possibleMove() {
        Stack<Location> possible = moveEngine.getPossibleMoves(selected);

        while (!possible.isEmpty())
            board.getCurrentState().getPiece(possible.pop()).setPossibleMove(true);
    }
}
