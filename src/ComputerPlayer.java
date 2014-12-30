import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;


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


    private Location[] ericBestMove(int depth) {
                /*
        This is 1 ply. I changed it so it no longer works with states, I pretty much removed all cloning
        from the program except once (above). Cloning is what was slowing things down so much.
        Also added StatePieces, state.getWhite or getBlack pieces .getAll returns all the white/black pieces.
        This allows us to use a simple for each loop instead of a nested loop.
        So what I do is move the piece, evaluate the state. Then move is back. But of course all parameters need to
        be put back to how they were before. Thus the 5 lines that move the piece.
         */
        Location result[] = new Location[2];
        Location bestStart = null, bestEnd = null;
        double bestEvaluation = Double.NEGATIVE_INFINITY;
        Node bestNode = null;
        Node root = new Node(null, board);//rootNode
        Queue<Node> fringe = new LinkedList<Node>();
        fringe.add(root);

        while (fringe.size() > 0) {
            Node tempNode = fringe.poll();
            State temp = tempNode.getState().clone();

            for (Piece piece : temp.getBlackPieces().getAll()) {
                Stack<Location> moves = moveEngine.getPossibleMoves(piece, temp);

                for (Location move : moves) {
                    //Move the piece
                    Location startLocation = piece.getLocation();
                    Location prevLocation = piece.getPrevLocation();
                    Piece tempPiece = temp.getPiece(move);
                    Location lastMoveStart = temp.getLastMoveStart();
                    Location lastMoveEnd = temp.getLastMoveEnd();
                    temp.movePiece(startLocation, move);
                    //Done moving piece

                    Node child = new Node(tempNode, temp);
                    tempNode.addChild(child);
                    //Create a child node and link to parent

                    if (tempNode.getDepth() < depth - 1) {
                        fringe.add(child);
                    } else {
                        //Evaluate child node when at the highest depth
                        double evaluation = moveEngine.evaluateState(tempNode.getState(), temp, getColour());
                        child.setEvaluation(evaluation);
                        if (evaluation > bestEvaluation) {
                            Node tempParent = child;
                            while ((tempParent.getDepth() > 1)) {
                                tempParent = tempParent.getParent();
                            }
                            bestNode = tempParent; //Goes up the tree to the parent that produced the best child.
                            bestEvaluation = evaluation;
                            bestStart = startLocation;
                            bestEnd = move;
                        }
                    }
                    //Move the piece back
                    if (tempPiece.getName() != Piece.Name.EMPTY && tempPiece.getColour() == Colour.WHITE)
                        temp.getWhitePieces().addPiece(tempPiece);
                    else if (tempPiece.getName() != Piece.Name.EMPTY && tempPiece.getColour() == Colour.BLACK)
                        temp.getBlackPieces().addPiece(tempPiece);

                    temp.movePiece(move, startLocation);
                    temp.setPiece(move.getX(), move.getY(), tempPiece);
                    temp.setLastMoveStart(lastMoveStart);
                    temp.setLastMoveEnd(lastMoveEnd);
                    piece.setPrevLocation(prevLocation);
                }
            }
        }
        result[0] = bestStart;
        result[1] = bestEnd;
        // bestNode.getState().movePiece(bestStart, bestEnd); //Move the best piece to its spot
        return result;
    }

    private void ericSelectAndMove() {
        //Get the best start and end locations
        Location bestLocations[] = ericBestMove(3);
        board.movePiece(bestLocations[0], bestLocations[1]);

        moveHistory.addMove(getColour(), board.getLastMoveStart(), board.getLastMoveEnd()); //history
        board.getPiece(board.getLastMoveEnd()).setSelected(true); //select the newly moved piece
        moveEngine.highlightCheck(board); //in check checker
        gui.repaint();
        getTurn().next();
    }


    private void selectAndMove() {
  /*      State bestState = getBestMove(2);
//        State bestState = grahamBestMove();
        if (bestState != null) {
            board.movePiece(bestState.getLastMoveStart(), bestState.getLastMoveEnd());
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
        }*/
    }

    private void grahamBestMove() {
        Location bestStart = null, bestEnd = null;

        double bestStateScore = Double.NEGATIVE_INFINITY;
        State temp = board.clone();

        /*
        This is 1 ply. I changed it so it no longer works with states, I pretty much removed all cloning
        from the program except once (above). Cloning is what was slowing things down so much.
        Also added StatePieces, state.getWhite or getBlack pieces .getAll returns all the white/black pieces.
        This allows us to use a simple for each loop instead of a nested loop.
        So what I do is move the piece, evaluate the state. Then move is back. But of course all parameters need to
        be put back to how they were before. Thus the 5 lines that move the piece.
         */

        for (Piece piece : temp.getBlackPieces().getAll()) {
            Stack<Location> moves = moveEngine.getPossibleMoves(piece, temp);

            for (Location move : moves) {
                //Move the piece
                Location startLocation = piece.getLocation();
                Location prevLocation = piece.getPrevLocation();
                Piece tempPiece = temp.getPiece(move);
                Location lastMoveStart = temp.getLastMoveStart();
                Location lastMoveEnd = temp.getLastMoveEnd();
                temp.movePiece(startLocation, move);

                //Evaluate the State
                double evaluation = moveEngine.evaluateState(board, temp, getColour());

                if (evaluation > bestStateScore) {
                    bestStart = startLocation;
                    bestEnd = move;
                    bestStateScore = evaluation;
                }

                //Move the piece back
                if (tempPiece.getName() != Piece.Name.EMPTY && tempPiece.getColour() == Colour.WHITE)
                    temp.getWhitePieces().addPiece(tempPiece);
                else if (tempPiece.getName() != Piece.Name.EMPTY && tempPiece.getColour() == Colour.BLACK)
                    temp.getBlackPieces().addPiece(tempPiece);

                temp.movePiece(move, startLocation);
                temp.setPiece(move.getX(), move.getY(), tempPiece);
                temp.setLastMoveStart(lastMoveStart);
                temp.setLastMoveEnd(lastMoveEnd);
                piece.setPrevLocation(prevLocation);
            }

        }


    }

    private State getBestMove(int depth) {
     /*   State bestState = null;
        double bestStateScore = Double.NEGATIVE_INFINITY;
        Node moves = new Node(null, board, 0);
        Queue<Node> fringe = new LinkedList<Node>();
        fringe.add(moves);

        // moves.addChild(new Node(board, moveEngine.evaluateState(board, board, getColour())));
        //tries all available moves and picks the one with the best evaluation score
        while (fringe.size() > 0) {
            Node temp = fringe.poll();
            for (Piece piece : temp.getState().getBlackPieces().getAll()) {
                Stack<State> stateStack = moveEngine.getPossibleStates(piece, temp.getState()); //get possible states
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
        } //while loop
        return bestState;*/
        return null;
    }

    public void run() {
        while (runBool) {
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (getTurn().getTurn() == getColour()) {
                ericSelectAndMove();
                // grahamBestMove();
            }
        }

    }
}
