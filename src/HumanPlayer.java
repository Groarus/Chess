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

    private Piece selected = new Empty(Piece.Name.EMPTY, Colour.NEUTRAL);
    private JPanel panel;
    private JLabel selectedLabel = new JLabel("None");
    private JLabel numMovesLabel = new JLabel("0");
    private JLabel turnLabel = new JLabel("Your Turn");
    private Board board;
    private MoveEngine move;
    private Boolean clickFlag = false;
    private MouseEvent mouseEvent;

    public HumanPlayer(Colour colour, Board board, GUI gui) {
        super(colour, board, gui);
        this.move = new MoveEngine(board);
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
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
                    if (mouseEvent.getButton() == MouseEvent.BUTTON1) { //left click
                        if (board.getCurrentState().getPiece(column, row).getName() != Piece.Name.EMPTY && board.getCurrentState().getPiece(column, row).getColour() == getColour()) { //if its not an empty selection and the right colour is selected
                            resetHighlight(); //clears all highlighted pieces
                            moveEngine.highlightCheck(); //highlight the king if in check
                            selected = board.getCurrentState().getPiece(column, row);
                            selected.setLocation(new Location(column, row)); //setting the location of the piece as it is not set prior
                            selected.setSelected(true);
                            possibleMove();
                            gui.repaint();
                        }
                    } else if (mouseEvent.getButton() == MouseEvent.BUTTON3) { //right click
                        if (selected.getName() != Piece.Name.EMPTY) {
                            resetHighlight();
                            move.move(selected, new Location(column, row));
                            gui.repaint(); //refreshes the board
                            selected = new Empty(Piece.Name.EMPTY, Colour.NEUTRAL); //unselect it
                        }
                    }
                }
            }
        }
    }

    private void infoPanel() {
        //main panel
        panel = new JPanel(new BorderLayout());


        //nested panels
        JPanel centerPanel = new JPanel(new GridLayout(3, 1));
        panel.add(centerPanel, BorderLayout.CENTER);
        JPanel statsPanel = new JPanel(new GridBagLayout());

        JLabel human = new JLabel("Human");
        human.setFont(new Font("Serif", Font.BOLD, 20));
        panel.add(human, BorderLayout.NORTH);

        turnLabel = new JLabel("Your Turn", JLabel.CENTER);
        turnLabel.setFont(new Font("Serif", Font.BOLD, 16));
        turnLabel.setForeground(Color.RED);
        centerPanel.add(turnLabel);


        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.weightx = .3;
        constraints.weighty = .3;
        constraints.gridx = 0;
        constraints.gridy = 0;

        JLabel numMovesLabel = new JLabel("Moves:");
        numMovesLabel.setFont(new Font("Serif", Font.BOLD, 16));
        statsPanel.add(numMovesLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        statsPanel.add(numMovesValue, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        JLabel piecesLeftLabel = new JLabel("Pieces: ");
        piecesLeftLabel.setFont(new Font("Serif", Font.BOLD, 16));
        statsPanel.add(piecesLeftLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        statsPanel.add(piecesLeftValue, constraints);
        centerPanel.add(statsPanel);
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
        Stack<Location> possible = moveEngine.getPossibleMoves(selected);

        while (!possible.isEmpty())
            board.getCurrentState().getPiece(possible.pop()).setPossibleMove(true);
    }

    private void resetHighlight() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece temp = board.getCurrentState().getPiece(j, i);
                temp.setPossibleMove(false);
                temp.setSelected(false);
                temp.setInCheck(false);
            }
        }
    }

}
