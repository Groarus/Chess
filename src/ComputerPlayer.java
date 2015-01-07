import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;


/**
 * Project: Chess
 * Course: COSC 3P71 - Final Project
 * Created: December, 2014
 */
public class ComputerPlayer extends Player implements Runnable {
    private JLabel evaluationValue = new JLabel("0");
    private GUI gui;
    private TempMove bestMove[];
    private int ply;


    public ComputerPlayer(Colour colour, Board board, GUI gui, Turn turn, MoveHistory moveHistory, int ply) {
        super(colour, board, gui, turn, moveHistory);
        //set up the GUI
        this.ply = ply;
        this.gui = gui;
        infoPanel();
        gui.addSidePanel(panel);
    }


    public double Max(State board, int depth, double alpha, double beta) {
        if (depth == 0) {
            return moveEngine.evaluateState(board, getColour());
        }
        for (Piece piece : board.getPieces(getColour()).getAll()) {
            LinkedList<Pair<Location, MoveEngine.SpecialMove>> moves = moveEngine.getPossibleMoves(piece, board);
            if (moves.size() != 0)
                for (Pair<Location, MoveEngine.SpecialMove> move : moves) {
                    TempMove tempMove = new TempMove(piece, board);
                    tempMove.move(move.getKey(), move.getValue());

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
            return -(moveEngine.evaluateState(board, getColour() == Colour.WHITE ? Colour.BLACK : Colour.WHITE));
        }
        for (Piece piece : board.getPieces(getColour() == Colour.WHITE ? Colour.BLACK : Colour.WHITE).getAll()) {
            LinkedList<Pair<Location, MoveEngine.SpecialMove>> moves = moveEngine.getPossibleMoves(piece, board);
            if (moves.size() != 0)

                for (Pair<Location, MoveEngine.SpecialMove> move : moves) {
                    TempMove tempMove = new TempMove(piece, board);
                    tempMove.move(move.getKey(), move.getValue());

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
        while (true) {
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (getTurn().getTurn() == getColour()) {
                gui.setBorder(panel, Color.red, 3);

                bestMove = new TempMove[ply + 1];

                State tempBoard = board.clone();
                double evaluation = Max(tempBoard, ply, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
                evaluationValue.setText(String.format("%.3f", evaluation));

                //If there is no best move

                while (bestMove[ply] == null && ply > 0){
                    ply--;
                    bestMove = new TempMove[ply + 1];
                    tempBoard = board.clone();
                    evaluation = Max(tempBoard, ply, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
                    evaluationValue.setText(String.format("%.3f", evaluation));
                }

                if (bestMove[ply] != null) {
                    System.out.println("Hit");
                    Location start = bestMove[ply].getStartLocation();
                    Location end = bestMove[ply].getToLocation();

                    moveEngine.move(board.getPiece(start), end, board);

                    moveHistory.addMove(getColour(), start, end); //history
                    board.getPiece(board.getLastMoveEnd()).setSelected(true); //select the newly moved piece
                    moveEngine.highlightCheck(board); //in check checker
                }

                gui.setBorder(panel, Color.darkGray, 1); //Info panel border
                gui.repaint();
                getTurn().next();

                if (getColour() == Colour.WHITE && moveEngine.isInCheckmate(board, Colour.BLACK)) {
                    JOptionPane.showMessageDialog(null, "White Wins! You have checkmated the opponent", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                } else if (getColour() == Colour.BLACK && moveEngine.isInCheckmate(board, Colour.WHITE)) {
                    JOptionPane.showMessageDialog(null, "Black Wins! You have checkmated the opponent", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                } else if (getColour() == Colour.WHITE && moveEngine.isInStalemate(board, Colour.BLACK)) {
                    JOptionPane.showMessageDialog(null, "Stalemate! Tie game", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                } else if (getColour() == Colour.BLACK && moveEngine.isInStalemate(board, Colour.WHITE)) {
                    JOptionPane.showMessageDialog(null, "Stalemate! Tie game", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }

            }
        }
    }


    private void infoPanel() {
        //main panel
        panel = new JPanel(new BorderLayout());

        //nested panels
        JPanel statsPanel = new JPanel(new GridBagLayout());

        JLabel human = new JLabel("Computer");
        human.setFont(new Font("Serif", Font.BOLD, 20));
        panel.add(human, BorderLayout.NORTH);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.weightx = .3;
        constraints.weighty = 0;
        constraints.gridx = 0;
        constraints.gridy = 0;

        JLabel numMovesLabel = new JLabel("Moves:");
        numMovesLabel.setFont(new Font("Serif", Font.BOLD, 16));
        statsPanel.add(numMovesLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        statsPanel.add(numMovesValue, constraints);

        //SPACER
        constraints.gridx = 0;
        constraints.gridy = 1;
        statsPanel.add(new JLabel("  "), constraints);
        constraints.gridx = 1;
        constraints.gridy = 1;
        statsPanel.add(new JLabel("  "), constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        JLabel piecesLeftLabel = new JLabel("Pieces: ");
        piecesLeftLabel.setFont(new Font("Serif", Font.BOLD, 16));
        statsPanel.add(piecesLeftLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        statsPanel.add(piecesLeftValue, constraints);

        //SPACER
        constraints.gridx = 0;
        constraints.gridy = 3;
        statsPanel.add(new JLabel("  "), constraints);
        constraints.gridx = 1;
        constraints.gridy = 3;
        statsPanel.add(new JLabel("  "), constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        JLabel evaluationLabel = new JLabel("Evaluation: ");
        evaluationLabel.setFont(new Font("Serif", Font.BOLD, 16));
        statsPanel.add(evaluationLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 4;
        statsPanel.add(evaluationValue, constraints);

        panel.add(statsPanel, BorderLayout.CENTER);
    }
}
