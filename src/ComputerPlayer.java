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
    private Node root;
    private GUI gui;

    public ComputerPlayer(Colour colour, Board board, GUI gui, Turn turn, MoveHistory moveHistory) {
        super(colour, board, gui, turn, moveHistory);
        //set up the GUI
        this.move = new MoveEngine(moveHistory);
        this.gui = gui;
        infoPanel();
        gui.addSidePanel(panel);
        //root = new Node(null, board);
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
        //For minimax, root is always max and then it alternates, (even depth = max)(odd depth = min)
        //if ( x & 1 == 0 ) { even... } else { odd... }
                /*
        This is 1 ply. I changed it so it no longer works with states, I pretty much removed all cloning
        from the program except once (above). Cloning is what was slowing things down so much.
        Also added StatePieces, state.getWhite or getBlack pieces .getAll returns all the white/black pieces.
        This allows us to use a simple for each loop instead of a nested loop.
        So what I do is move the piece, evaluate the state. Then move is back. But of course all parameters need to
        be put back to how they were before. Thus the 5 lines that move the piece.
         */
        Location result[] = new Location[2];
        Node root = new Node(null, board);//rootNode
        Queue<Node> fringe = new LinkedList<Node>();
        Stack<Node> parents = new Stack<Node>();
        fringe.add(root);
        parents.push(root);
        Colour evaluationColour = null;

        while (fringe.size() > 0) {
            Node tempNode = fringe.poll();
            State temp = tempNode.getState().clone();
            StatePieces pieces = null;
            if (tempNode.getMax()) {
                //If its a max move, its children will be cpu moves
                pieces = temp.getBlackPieces();
                evaluationColour = Colour.WHITE;
            } else if (tempNode.getMin()) {
                //If its a min move, its children will be player moves
                pieces = temp.getWhitePieces();
                evaluationColour = Colour.BLACK;
            }
            for (Piece piece : pieces.getAll()) {
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
                        parents.push(child);
                    } else {
                        //Evaluate child node when at the deepest depth
                        double evaluation = moveEngine.evaluateState(tempNode.getState(), evaluationColour);
                        child.setEvaluation(evaluation);
                        child.setStartLocation(startLocation);
                        child.setEndLocation(move);
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
        //Tree is made and we have all the parents on a stack to go back up to the root.
        while (!parents.isEmpty()) {
            if (parents.peek().getMax()) {
                parents.pop().maximize();
            } else if (parents.peek().getMin()) {
                parents.pop().maximize();
            }
        }
        result[0] = root.getBestChild().getStartLocation();
        result[1] = root.getBestChild().getEndLocation();
        System.out.println(root.getMax());
        System.out.println(root.getEvaluation());
        for (int i = 0; i < root.getChildren().size(); i++) {
            System.out.println("Child: " + i + " " + root.getChildren().get(i).getEvaluation());
        }
        return result;
    }

    private void ericSelectAndMove() {
        //Get the best start and end locations
        Location bestLocations[] = ericBestMove(1);
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

//    private void grahamBestMove() {
//        Location bestStart = null, bestEnd = null;
//
//        double bestScore = Double.NEGATIVE_INFINITY;
//        State temp = board.clone();
//        for (Piece piece : temp.getPieces(getColour()).getAll()) {
//            Pair<Location, Double> results = moveEngine.getBestMove(piece, temp, board);
//            if (results.getValue() > bestScore) {
//                bestStart = piece.getLocation();
//                bestEnd = results.getKey();
//                bestScore = results.getValue();
//            }
//        }
//        moveEngine.move(board.getPiece(bestStart), bestEnd, board);
//        moveHistory.addMove(getColour(), board.getLastMoveStart(), board.getLastMoveEnd());//history 
//        board.getPiece(board.getLastMoveEnd()).setSelected(true); //select the newly moved piece 
//        moveEngine.highlightCheck(board); //in check checker 
//        gui.repaint();
//        getTurn().next();
//    }


    private GNode grahamMiniMax(GNode node, int depth, Boolean maximizingPlayer) {
        if (depth == 0) {
            node.setEvaluation(moveEngine.evaluateState(node.getState(), maximizingPlayer ? Colour.BLACK : Colour.WHITE));
            return node;
        }
        if (maximizingPlayer) {
            GNode max = new GNode(null);
            max.setEvaluation(Double.NEGATIVE_INFINITY);
            for (Piece piece : node.getState().getPieces(Colour.BLACK).getAll()) {
                LinkedList<GNode> children = moveEngine.getPossibleStates(piece, node.getState());
                for (GNode child : children) {
                    GNode temp = grahamMiniMax(child, depth - 1, false);
                    if (max.getEvaluation() < temp.getEvaluation()) {
                        max = temp;
                    }
                }
            }
            return max;
        } else {
            GNode min = new GNode(null);
            min.setEvaluation(Double.POSITIVE_INFINITY);
            for (Piece piece : node.getState().getPieces(Colour.WHITE).getAll()) {
                LinkedList<GNode> children = moveEngine.getPossibleStates(piece, node.getState());
                for (GNode child : children) {
                    GNode temp = grahamMiniMax(child, depth - 1, true);
                    if (min.getEvaluation() > temp.getEvaluation()) {
                        min = temp;
                    }
                }
            }
            return min;
        }
    }


    public void run() {
        while (runBool) {
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (getTurn().getTurn() == getColour()) {
//                ericSelectAndMove();
//                grahamBestMove();
                GNode root = new GNode(board);
                GNode temp = grahamMiniMax(root, 2, false);

                moveEngine.move(board.getPiece(temp.getState().getLastMoveStart()), temp.getState().getLastMoveEnd(), board);

                temp.getState().displayBoard();

                gui.repaint();
                getTurn().next();

            }
        }

    }
}
