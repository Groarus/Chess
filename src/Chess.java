import javax.swing.*;


/**
 * Project: Chess
 * Course: COSC 3P71 - Final Project
 * Created: December, 2014
 */
public class Chess {

    public Chess() {
        GUI gui = new GUI();
        Board board = new Board(gui);

        if (false) {
            Object[] options = {"White", "Black"};
            int choice = JOptionPane.showOptionDialog(null, "Do you want to be White or Black?", "Colour Option", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            ComputerPlayer computerPlayer;
            HumanPlayer humanPlayer;
            if (choice == 0) {
                computerPlayer = new ComputerPlayer(Colour.BLACK, board, gui);
                humanPlayer = new HumanPlayer(Colour.WHITE, board, gui);
                board.setWhitePlayer(humanPlayer);
                board.setBlackPlayer(computerPlayer);
            } else {
                computerPlayer = new ComputerPlayer(Colour.WHITE, board, gui);
                humanPlayer = new HumanPlayer(Colour.BLACK, board, gui);
                board.setWhitePlayer(computerPlayer);
                board.setBlackPlayer(humanPlayer);
            }
        } else {
            HumanPlayer humanPlayer1;
            HumanPlayer humanPlayer2;
            humanPlayer1 = new HumanPlayer(Colour.WHITE, board, gui);
            humanPlayer2 = new HumanPlayer(Colour.BLACK, board, gui);
            board.setWhitePlayer(humanPlayer1);
            board.setBlackPlayer(humanPlayer2);

        }
        gui.startGUI(); //makes the gui visible
    }


    public static void main(String[] args) {
        new Chess();
    }
}
