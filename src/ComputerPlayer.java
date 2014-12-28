import javax.swing.*;
import java.awt.*;
import java.util.*;


/**
 * Project: Chess
 * Course: COSC 3P71 - Final Project
 * Created: December, 2014
 */
public class ComputerPlayer extends Player implements Runnable {
    int row = 0; //Test variables
    int column = 6;
    private Piece selected = new Empty();
    private MoveEngine move;
    private Boolean runBool = true;


    public ComputerPlayer(Colour colour, Board board, GUI gui, Turn turn, MoveHistory moveHistory) {
        super(colour, board, gui, turn, moveHistory);
        //set up the GUI
        this.move = new MoveEngine(moveHistory);
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

    private State getBestMove(int depth) {
        State bestState = null;
        double bestStateScore = Double.NEGATIVE_INFINITY;
        Node moves = new Node(null, board, 0);
        Queue<Node> fringe = new LinkedList<Node>();
        fringe.add(moves);

        // moves.addChild(new Node(board, moveEngine.evaluateState(board, board, getColour())));
        //tries all available moves and picks the one with the best evaluation score
        while (fringe.size() > 0) {
            Node temp = fringe.poll();
            for (int i = 0; i < board.getState().length; i++) {
                for (int j = 0; j < board.getState().length; j++) {
                    if (temp.getState().getPiece(i, j).getColour() == getColour()) {
                        Stack<State> stateStack = moveEngine.getPossibleStates(temp.getState().getPiece(i, j), temp.getState()); //get possible states
                        for (State s : stateStack) {
                            double evaluation = moveEngine.evaluateState(s, temp.getState(), getColour());
                            Node child = new Node(temp, s, evaluation);
                            temp.addChild(child);
                            if (temp.getDepth() < depth - 1) {
                                fringe.add(child);
                            }
                            if (evaluation > bestStateScore) {
                                Node tempParent = child;
                                while ((tempParent.getDepth() > 1)) {
                                    tempParent = tempParent.getParent();
                                }
                                bestState = tempParent.getState(); //Goes up the tree to the parent that produced the best child.
                                bestStateScore = evaluation;
                            }
                        }
                    }
                }
            }
        } //while loop
        return bestState;
    }

    private void selectAndMove() {
        State bestState = getBestMove(3);
        if (bestState != null) {
            board.setState(bestState.getState()); //Set the new board state
            board.setLastMoveStart(bestState.getLastMoveStart());
            board.setLastMoveEnd(bestState.getLastMoveEnd());
            moveHistory.addMove(getColour(), board.getLastMoveStart(), board.getLastMoveEnd()); //history
            board.getPiece(board.getLastMoveEnd()).setSelected(true); //select the newly moved piece
            moveEngine.highlightCheck(board); //in check checker
            gui.repaint();
            getTurn().next();
        } else {
            System.out.println("Checkmate");
            runBool = false;
        }
    }


 /*   private void selectAndMove() {
    //Eric try. Might be better for when we deal with a tree...Dont delete
        Location bestMoveStart = null;
        Location bestMoveTo = null;
        double bestMoveScore = Double.NEGATIVE_INFINITY;
        Stack<State> allStates = new Stack<State>();
        for (int i = 0; i < board.getState().length; i++) {
            for (int j = 0; j < board.getState().length; j++) {
                if (board.getPiece(i,j).getColour() == getColour()) {
                    Stack<Location> moves = moveEngine.getPossibleMoves(board.getPiece(i, j), board);
                    for (int k = 0; k < moves.size(); k++) {
                        State state = board.clone();
                        state.movePiece(new Location(i, j), moves.get(k));
                        allStates.push(state);
                    }
                }
            }
        }
        for (int i = 0; i < allStates.size(); i++) {
            State checkState = allStates.pop();
            double evaluation = moveEngine.evaluateState(checkState, board, Colour.BLACK);

            if (evaluation > bestMoveScore) {
                //Test
                board.setState(checkState.getState());
*//*                bestMoveStart = new Location(i, j);
                bestMoveTo = moves.get(k);
               *//*
                bestMoveScore = evaluation;
            }
        }
*//*        board.movePiece(bestMoveStart, bestMoveTo);
        board.getPiece(bestMoveTo).setSelected(true);*//*
        gui.repaint();
        getTurn().next();
    }*/

    public void run() {
        while (runBool) {
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (getTurn().getTurn() == getColour()) {
                selectAndMove();
            }
        }

    }
}
