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

        Object[] options = {"Human vs. Computer", "Human vs. Human"};
        int choice = JOptionPane.showOptionDialog(null, "Choose your game type", "Colour Option", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            Object[] options2 = {"White", "Black"};
            choice = JOptionPane.showOptionDialog(null, "Do you want to be White or Black?", "Colour Option", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options2, options[0]);

            ComputerPlayer computerPlayer;
            HumanPlayer humanPlayer;
            if (choice == 0) {
                computerPlayer = new ComputerPlayer(Colour.BLACK, board, gui);
                gui.addSidePanel(new ControlPanel(gui));
                humanPlayer = new HumanPlayer(Colour.WHITE, board, gui);
                board.setWhitePlayer(humanPlayer);
                board.setBlackPlayer(computerPlayer);
            } else {
                computerPlayer = new ComputerPlayer(Colour.WHITE, board, gui);
                gui.addSidePanel(new ControlPanel(gui));
                humanPlayer = new HumanPlayer(Colour.BLACK, board, gui);
                board.setWhitePlayer(computerPlayer);
                board.setBlackPlayer(humanPlayer);
            }
            Thread humanThread = new Thread(humanPlayer);
            humanThread.start();
        } else {
            HumanPlayer humanPlayer1;
            HumanPlayer humanPlayer2;
            humanPlayer2 = new HumanPlayer(Colour.BLACK, board, gui);
            gui.addSidePanel(new ControlPanel(gui));
            humanPlayer1 = new HumanPlayer(Colour.WHITE, board, gui);
            board.setWhitePlayer(humanPlayer1);
            board.setBlackPlayer(humanPlayer2);

            //todo Do something with threads
            Thread player1 = new Thread(humanPlayer1);
            Thread player2 = new Thread(humanPlayer2);
            player1.start();
            player2.start();
        }
        gui.startGUI(); //makes the gui visible
    }


    public static void main(String[] args) {
        new Chess();
    }
}
