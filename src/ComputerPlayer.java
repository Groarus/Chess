import javax.swing.*;
import javax.swing.text.html.BlockView;
import java.awt.*;
import java.util.Stack;


/**
 * Project: Chess
 * Course: COSC 3P71 - Final Project
 * Created: December, 2014
 */
public class ComputerPlayer extends Player implements Runnable {
    int row = 0; //Test variables
    int column = 6;
    private Piece selected = new Empty(Piece.Name.EMPTY, Colour.NEUTRAL);
    private MoveEngine move;


    public ComputerPlayer(Colour colour, Board board, GUI gui, Turn turn, MoveHistory moveHistory) {
        super(colour, board, gui, turn, moveHistory);
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

        Location bestMoveOrig = null;
        Location bestMove = null;
        double bestMoveScore = Double.NEGATIVE_INFINITY;

        //tries all available moves and picks the one with the best evaluation score
        for (int i = 0; i < board.getState().length; i++) {
            for (int j = 0; j < board.getState().length; j++) {
                if (board.getPiece(i,j).getColour() == getColour()) {
                    Stack<Location> moves = moveEngine.getPossibleMoves(board.getPiece(i, j), board);
                    for (int k = 0; k < moves.size(); k++) {
                        State state = board.clone();
                        state.movePiece(new Location(i, j), moves.get(k));
                        double evaluation = moveEngine.evaluateState(state, board, Colour.BLACK);

                        if (evaluation > bestMoveScore) {
                            bestMoveOrig = new Location(i, j);
                            bestMove = moves.get(k);
                            bestMoveScore = evaluation;
                        }
                    }
                }
            }
        }
        board.movePiece(bestMoveOrig, bestMove);
        board.getPiece(bestMove).setSelected(true);
        gui.repaint();
        getTurn().next();


//
//
//        selected = board.getPiece(row, column);
//        selected.setLocation(new Location(row, column)); //setting the location of the piece as it is not set prior
//        selected.setSelected(true);
//
//
//        if (selected.getName() != Piece.Name.EMPTY) {
//            column--;
//            if (move.move(selected, new Location(row, column), board)) { //MAIN MOVEMENT OF PLAYER
//
//                getTurn().next();
//            }
//            gui.repaint(); //refreshes the board
//            selected = new Empty(Piece.Name.EMPTY, Colour.NEUTRAL); //unselect it
//        }

    }

    public void run() {
        while (true) {
            if (getTurn().getTurn() == getColour()) {
                selectAndMove();
            }
        }

    }
}
