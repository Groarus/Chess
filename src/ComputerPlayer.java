import javax.swing.*;
import java.awt.*;


/**
 * Project: Chess
 * Course: COSC 3P71 - Final Project
 * Created: December, 2014
 */
public class ComputerPlayer extends Player implements Runnable {

    public ComputerPlayer(Colour colour, Board board, GUI gui, Turn turn) {
        super(colour, board, gui, turn);
        //set up the GUI
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

    private void selectAndMove(){
        //Method to select a piece and move piece.
        getTurn().next();
    }

    public void run() {
        while (true) {
            if (getTurn().getTurn() == getColour()){
                selectAndMove();
            }
        }

    }

}
