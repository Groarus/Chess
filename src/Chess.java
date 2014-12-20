import javax.swing.*;
import java.io.IOException;
import java.util.Queue;


/**
 * Project: Chess
 * Course: COSC 3P71 - Final Project
 * Created: December, 2014
 */
public class Chess {

    public Chess() {
        GUI gui = new GUI();
        Board board = new Board(gui);
        Turn turn = new Turn();
        MoveHistory moveHistory = new MoveHistory();
        ControlPanel controlPanel = new ControlPanel(moveHistory);
        Boolean showMoves = false;
        Object[] options = {"Human vs. Computer", "Human vs. Human", "Load Game"};
        int choice = JOptionPane.showOptionDialog(null, "Choose your game type", "Colour Option", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice == 2) {
            try {
                moveHistory.loadGame();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (moveHistory.getMode().equals("CH"))
                choice = -1;
            else
                choice = -2;
            Object[] loadOption = {"Show Moves", "Skip"};
            int loadChoice = JOptionPane.showOptionDialog(null, "Show moves or skip to saved state", "Load", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, loadOption, loadOption[0]);
            showMoves = loadChoice == 0;
        }
        if (choice == 0 || choice == -1) {
            Object[] options2 = {"White", "Black"};
            int cChoice = JOptionPane.showOptionDialog(null, "Do you want to be White or Black?", "Colour Option", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options2, options2[0]);

            ComputerPlayer computerPlayer;
            HumanPlayer humanPlayer;
            if (cChoice == 0) {
                computerPlayer = new ComputerPlayer(Colour.BLACK, board, gui, turn, moveHistory);
                gui.addSidePanel(controlPanel);
                humanPlayer = new HumanPlayer(Colour.WHITE, board, gui, turn, moveHistory);
                board.setWhitePlayer(humanPlayer);
                board.setBlackPlayer(computerPlayer);

            } else {
                computerPlayer = new ComputerPlayer(Colour.WHITE, board, gui, turn, moveHistory);
                gui.addSidePanel(controlPanel);
                humanPlayer = new HumanPlayer(Colour.BLACK, board, gui, turn, moveHistory);
                board.setWhitePlayer(computerPlayer);
                board.setBlackPlayer(humanPlayer);
            }
            moveHistory.setData(computerPlayer, humanPlayer);

            Thread humanThread = new Thread(humanPlayer);
            Thread computerThread = new Thread(computerPlayer);
            humanThread.start();
            computerThread.start();
            gui.startGUI(); //makes the gui visible
            try {
                humanThread.join();
                computerThread.join();
            } catch (InterruptedException ex) {
                System.out.println("Didn't Work");
            }
        } else if (choice == 1 || choice == -2) {
            HumanPlayer humanPlayer1;
            HumanPlayer humanPlayer2;
            humanPlayer2 = new HumanPlayer(Colour.BLACK, board, gui, turn, moveHistory);
            gui.addSidePanel(controlPanel);
            humanPlayer1 = new HumanPlayer(Colour.WHITE, board, gui, turn, moveHistory);
            board.setWhitePlayer(humanPlayer1);
            board.setBlackPlayer(humanPlayer2);
            moveHistory.setData(humanPlayer1, humanPlayer2);

            Thread player1 = new Thread(humanPlayer1);
            Thread player2 = new Thread(humanPlayer2);
            player1.start();
            player2.start();
            gui.startGUI(); //makes the gui visible

            //If LOAD GAME
            if (choice == -2) {
                Queue<String> moves = moveHistory.getMoves();

                for (String move : moves) {
                    String[] strings = move.split("\t");
                    Location fromLocation = new Location(Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
                    Location toLocation = new Location(Integer.parseInt(strings[3]), Integer.parseInt(strings[4]));
                    board.move(fromLocation, toLocation);
                    if (strings[0].equals("WHITE"))
                        humanPlayer1.incrementMoves();
                    else
                        humanPlayer2.incrementMoves();
                    gui.repaint();
                    if (showMoves)
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                }
            }

            try {
                player1.join();
                player2.join();
            } catch (InterruptedException ex) {
                System.out.println("Didn't Work");
            }
        }
    }

    public static void main(String[] args) {
        new Chess();
    }
}
