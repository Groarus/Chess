import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Stack;


/**
 * Project: Chess
 * Course: COSC 3P71 - Final Project
 * Created: December, 2014
 */
public class HumanPlayer extends Player implements Runnable {

    private Piece selected = new Empty();
    private JPanel panel;
    private Board board;
    private MoveEngine move;
    private Boolean clickFlag = false;
    private MouseEvent mouseEvent;

    public HumanPlayer(Colour colour, Board board, GUI gui, Turn turn, MoveHistory moveHistory) {
        super(colour, board, gui, turn, moveHistory);
        this.move = new MoveEngine(moveHistory);
        this.board = board;
        //set up the gui
        infoPanel();
        gui.addSidePanel(panel);
        guiListener();
    }


    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (getTurn().getTurn() == getColour()) {
                gui.setBorder(panel, Color.red, 3);
                if (clickFlag) {
                    int x = mouseEvent.getX();
                    int y = mouseEvent.getY() - 25;
                    int row = -1, column = -1;

                    //This loop gets the column/row of where the mouse clicks
                    loop:
                    for (int i = 25; i < 825; i += 100) { //X
                        for (int j = 25; j < 825; j += 100) { //Y
                            if (x > i && x < i + 100 && y > j && y < j + 100) {
                                if (board.getWhitePlayer().getClass() == HumanPlayer.class) {
                                    row = 7 - ((j - 25) / 100);
                                    column = (i - 25) / 100;
                                } else {
                                    row = (j - 25) / 100;
                                    column = 7 - (i - 25) / 100;
                                }
                                break loop;
                            }
                        }
                    }
                    if (row != -1 && column != -1) {  //if row/column has actually been clicked
                        //SELECT PIECE
                        if (board.getPiece(column, row).getColour() == getColour()) {
                            board.resetHighlight(); //clears all highlighted pieces
                            moveEngine.highlightCheck(board); //highlight the king if in check
                            selected = board.getPiece(column, row);
                            selected.setLocation(new Location(column, row)); //setting the location of the piece as it is not set prior
                            selected.setSelected(true);
                            possibleMove();
                            gui.repaint();
                        } else { //MOVE PIECE
                            if (selected.getName() != Piece.Name.EMPTY) {
                                board.resetHighlight();
                                if (move.move(selected, new Location(column, row), board)) { //MAIN MOVEMENT OF PLAYER
                                    moveHistory.addMove(selected.getColour(), selected.getPrevLocation(), new Location(column, row)); //History
                                    gui.setBorder(panel, Color.darkGray, 1); //Info panel border
                                    getTurn().next();
                                }
                                gui.repaint(); //refreshes the board
                                selected = new Empty(); //deselect it
                            }
                        }
                    }
                    clickFlag = false;
                }
            }
        }
    }

    private void infoPanel() {
        //main panel
        panel = new JPanel(new BorderLayout());


        //nested panels
        JPanel statsPanel = new JPanel(new GridBagLayout());

        JLabel human = new JLabel("Human");
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
        panel.add(statsPanel, BorderLayout.CENTER);
    }


    private void guiListener() {
        gui.addMouseListener(new MouseListener() {
                                 @Override
                                 public void mousePressed(MouseEvent e) { //Mouse pressed over mouse clicked works much much better
                                     mouseEvent = e;
                                     clickFlag = true;
                                 }

                                 @Override
                                 public void mouseClicked(MouseEvent e) {

                                 }

                                 @Override
                                 public void mouseReleased(MouseEvent e) {

                                 }

                                 @Override
                                 public void mouseEntered(MouseEvent e) {

                                 }

                                 @Override
                                 public void mouseExited(MouseEvent e) {

                                 }

                             }
        );
    }

    private void possibleMove() {
        Stack<Location> possible = moveEngine.getPossibleMoves(selected, board);

        while (!possible.isEmpty())
            board.getPiece(possible.pop()).setPossibleMove(true);
    }
}
