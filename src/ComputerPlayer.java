import javax.swing.*;
import java.awt.*;
import java.util.Stack;


/**
 * Project: Chess
 * Course: COSC 3P71 - Final Project
 * Created: December, 2014
 */
public class ComputerPlayer extends Player implements Runnable {
    private MoveEngine move;
    private Boolean runBool = true;
    private GUI gui;
    private TempMove bestMove[];


    public ComputerPlayer(Colour colour, Board board, GUI gui, Turn turn, MoveHistory moveHistory) {
        super(colour, board, gui, turn, moveHistory);
        //set up the GUI
        this.move = new MoveEngine(moveHistory);
        this.gui = gui;
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


    public double Max(State board, int depth, double alpha, double beta) {
        if (depth == 0) {
            return moveEngine.evaluateState(board, Colour.BLACK);
        }
        for (Piece piece : board.getPieces(Colour.BLACK).getAll()) {
            Stack<Location> children = moveEngine.getPossibleMoves(piece, board);
            for (Location child : children) {
                TempMove tempMove = new TempMove(piece, board);
                tempMove.move(child);

                double score = Min(board, depth - 1, alpha, beta);

                tempMove.undoMove();
                if (score > alpha) {
                    alpha = score;
                    this.bestMove[depth] = tempMove;
                }
                if (alpha >= beta)
                    return alpha;
            }
        }
        return alpha;
    }

    public double Min(State board, int depth, double alpha, double beta) {
        if (depth == 0) {
            return -(moveEngine.evaluateState(board, Colour.WHITE));
        }
        for (Piece piece : board.getPieces(Colour.WHITE).getAll()) {
            Stack<Location> children = moveEngine.getPossibleMoves(piece, board);
            for (Location child : children) {
                TempMove tempMove = new TempMove(piece, board);
                tempMove.move(child);

                double score = Max(board, depth - 1, alpha, beta);

                tempMove.undoMove();
                if (score < beta) {
                    beta = score;
                    this.bestMove[depth] = tempMove;
                }
                if (beta <= alpha)
                    return beta;
            }
        }
        return beta;
    }


    public void run() {
        while (runBool) {
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (getTurn().getTurn() == getColour()) {
                int depth = 3;
                bestMove = new TempMove[depth + 1];

                State tempBoard = board.clone();
                Max(tempBoard, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

                Location start = bestMove[depth].getStartLocation();
                Location end = bestMove[depth].getToLocation();

                moveEngine.move(board.getPiece(start), end, board);

                moveHistory.addMove(getColour(), start, end); //history
                board.getPiece(board.getLastMoveEnd()).setSelected(true); //select the newly moved piece
                moveEngine.highlightCheck(board); //in check checker
                gui.repaint();
                getTurn().next();

            }
        }

    }
}
