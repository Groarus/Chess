import javax.swing.*;
import java.io.IOException;
import java.util.Queue;


/**
 * Project: Chess
 * Course: COSC 3P71 - Final Project
 * Created: December, 2014
 */
public class Chess {

    private GUI gui = new GUI();
    private Board board = new Board(gui);
    private Turn turn = new Turn();
    private MoveHistory moveHistory = new MoveHistory();
    private MoveEngine moveEngine = new MoveEngine(moveHistory);
    private ControlPanel controlPanel = new ControlPanel(moveHistory);
    private Boolean showMoves = false;

    public Chess() {
        Object[] options = {"Human vs. Computer", "Human vs. Human", "Load Game"};
        int choice = JOptionPane.showOptionDialog(null, "Choose your game type", "Colour Option", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);


        switch (choice) {
            case 0:
                SinglePlay(false, Colour.NEUTRAL);
                break;
            case 1:
                DoublePlay(false);
                break;
            case 2:
                try {
                    LoadGame();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public static void main(String[] args) {
        new Chess();
    }

    private void LoadGame() throws IOException {
        moveHistory.loadGame();

        Object[] loadOption = {"Show Moves", "Skip"};
        int loadChoice = JOptionPane.showOptionDialog(null, "Show moves or skip to saved state", "Load", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, loadOption, loadOption[0]);
        showMoves = loadChoice == 0;

        if (moveHistory.getMode().equals("HH"))
            DoublePlay(true);
        else if (moveHistory.getMode().equals("CH\tWHITE"))
            SinglePlay(true, Colour.WHITE);
        else if (moveHistory.getMode().equals("CH\tBLACK"))
            SinglePlay(true, Colour.BLACK);
    }

    private void SinglePlay(Boolean loadGame, Colour colour) {
        int choice;
        if (colour == Colour.WHITE) {
            choice = 0;
        } else if (colour == Colour.BLACK)
            choice = 1;
        else {
            Object[] options2 = {"White", "Black"};
            choice = JOptionPane.showOptionDialog(null, "Do you want to be White or Black?", "Colour Option", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options2, options2[0]);
        }
        Object[] plyOptions = {1, 2, 3, 4, 5, 6};
        int ply = (Integer) JOptionPane.showInputDialog(null, "Enter the ply for the computer AI", "Difficulty", JOptionPane.QUESTION_MESSAGE, null, plyOptions, plyOptions[2]);

        ComputerPlayer computerPlayer;
        HumanPlayer humanPlayer;
        if (choice == 0) {
            computerPlayer = new ComputerPlayer(Colour.BLACK, board, gui, turn, moveHistory, ply);
            gui.addSidePanel(controlPanel);
            humanPlayer = new HumanPlayer(Colour.WHITE, board, gui, turn, moveHistory);
            board.setWhitePlayer(humanPlayer);
            board.setBlackPlayer(computerPlayer);

        } else {
            computerPlayer = new ComputerPlayer(Colour.WHITE, board, gui, turn, moveHistory, ply);
            gui.addSidePanel(controlPanel);
            humanPlayer = new HumanPlayer(Colour.BLACK, board, gui, turn, moveHistory);
            board.setWhitePlayer(computerPlayer);
            board.setBlackPlayer(humanPlayer);
        }
        controlPanel.setPlayer(humanPlayer);
        moveHistory.setData(computerPlayer, humanPlayer);

        Thread humanThread = new Thread(humanPlayer);
        Thread computerThread = new Thread(computerPlayer);
        gui.startGUI(); //makes the gui visible


        //If LOAD GAME
        if (loadGame) {
            Queue<String> moves = moveHistory.getMoves();
            String lastColour = "";
            for (String move : moves) {
                if (showMoves)
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                String[] strings = move.split("\t");
                Location fromLocation = new Location(Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
                Location toLocation = new Location(Integer.parseInt(strings[3]), Integer.parseInt(strings[4]));
                moveEngine.move(board.getPiece(fromLocation), toLocation, board);

                board.resetHighlight();
                board.getPiece(toLocation).setSelected(true);
                moveEngine.highlightCheck(board);

                gui.repaint();
                lastColour = strings[0];
            }
            if (lastColour.equals("WHITE"))
                board.getWhitePlayer().getTurn().next();
        }
        humanThread.start();
        computerThread.start();
    }

    private void DoublePlay(Boolean loadGame) {
        HumanPlayer humanPlayer1;
        HumanPlayer humanPlayer2;
        humanPlayer2 = new HumanPlayer(Colour.BLACK, board, gui, turn, moveHistory);
        gui.addSidePanel(controlPanel);
        humanPlayer1 = new HumanPlayer(Colour.WHITE, board, gui, turn, moveHistory);
        board.setWhitePlayer(humanPlayer1);
        board.setBlackPlayer(humanPlayer2);
        moveHistory.setData(humanPlayer1, humanPlayer2);
        controlPanel.setPlayer(humanPlayer1);
        controlPanel.setPlayer2(humanPlayer2);

        Thread player1 = new Thread(humanPlayer1);
        Thread player2 = new Thread(humanPlayer2);
        gui.startGUI(); //makes the gui visible

        //If LOAD GAME
        if (loadGame) {
            Queue<String> moves = moveHistory.getMoves();
            String lastColour = "";

            for (String move : moves) {
                if (showMoves)
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                String[] strings = move.split("\t");
                Location fromLocation = new Location(Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
                Location toLocation = new Location(Integer.parseInt(strings[3]), Integer.parseInt(strings[4]));
                moveEngine.move(board.getPiece(fromLocation), toLocation, board);

                board.resetHighlight();
                board.getPiece(toLocation).setSelected(true);
                moveEngine.highlightCheck(board);
                gui.repaint();


                lastColour = strings[0];
            }
            if (lastColour.equals("WHITE")) {
                board.getWhitePlayer().getTurn().next();
            }
        }
        player1.start();
        player2.start();
    }
}
