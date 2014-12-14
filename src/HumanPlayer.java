import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


/**
 * Project: Chess
 * Course: COSC 3P71 - Final Project
 * Created: December, 2014
 */
public class HumanPlayer {

    private Colour colour;
    private Board board;
    private GUI gui;
    private Piece selected = null;
    private JPanel panel;
    private JLabel selectedLabel = new JLabel("None");
    private JLabel numMovesLabel = new JLabel("0");
    private JLabel turnLabel = new JLabel("Your Turn");
    private JButton quitButton = new JButton("Quit");

    private MoveEngine move;


    public HumanPlayer(Colour colour, Board board, GUI gui) {
        this.colour = colour;
        this.board = board;
        this.gui = gui;
        this.move = new MoveEngine(board);

        //set up the gui
        infoPanel();
        gui.addSidePanel(panel);
        guiListener();
    }

    public Colour getColour() {
        return colour;
    }


    private void infoPanel() {
        //main panel
        panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(250, 400));

        //quit button
        panel.add(quitButton, BorderLayout.SOUTH);

        //nested panels
        JPanel centerPanel = new JPanel(new GridLayout(3, 1));
        panel.add(centerPanel, BorderLayout.CENTER);
        JPanel selectedPanel = new JPanel(new GridBagLayout());

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

        JLabel selected = new JLabel("Selected: ");
        selected.setFont(new Font("Serif", Font.BOLD, 16));
        selectedPanel.add(selected, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        selectedPanel.add(selectedLabel, constraints);
        centerPanel.add(selectedPanel);
    }


    private void guiListener() {
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        gui.addMouseListener(new MouseListener() {
                                 @Override
                                 public void mouseClicked(MouseEvent e) {
                                     int x = e.getX();
                                     int y = e.getY() - 25;
                                     int row = -1, column = -1;

                                     //This loop gets the column/row of where the mouse clicks
                                     loop:
                                     for (int i = 25; i < 825; i += 100) { //X
                                         for (int j = 25; j < 825; j += 100) { //Y
                                             if (x > i && x < i + 100 && y > j && y < j + 100) {
                                                 if (getColour() == Colour.WHITE) {
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
                                         if (e.getButton() == MouseEvent.BUTTON1) { //left click
                                             if (board.getCurrentState().getPiece(column, row) != null && board.getCurrentState().getPiece(column, row).getColour() == getColour()) { //if its not an empty selection and the right colour is selected
                                                 selected = board.getCurrentState().getPiece(column, row);
                                                 selected.setLocation(new Location(column, row)); //setting the location of the piece as it is not set prior
                                                 selectedLabel.setText(selected.printLocation() + "\t-\t" + selected.getName().name()); //change the selected piece in the side panel
                                             }
                                         } else if (e.getButton() == MouseEvent.BUTTON3) { //right click
                                             if (selected != null) {
                                                 move.move(selected, new Location(column, row));
                                                 gui.repaint(); //refreshes the board
                                                 selected = null; //unselect it
                                                 selectedLabel.setText("None");
                                             }
                                         }
                                     }
                                 }

                                 @Override
                                 public void mousePressed(MouseEvent e) {

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
}
