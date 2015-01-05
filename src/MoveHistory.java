import java.awt.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Project: Chess
 * Course: COSC 3P71 - Final Project
 * Created: December, 2014
 */
public class MoveHistory {

    Queue<String> moves = new LinkedList<String>();
    private Player player1, player2;
    private String mode;
    private String lastMove = null;

    public void addMove(Colour colour, Location fromLocation, Location toLocation) {
        String moveString = colour.name() + "\t" + fromLocation.getX() + "\t" + fromLocation.getY() + "\t" + toLocation.getX() + "\t" + toLocation.getY() + "\n";
        lastMove = moveString;
        moves.add(moveString);
    }

    public Location lastMove() {
        if (lastMove != null) {
            String[] last = lastMove.split("\t");
            return new Location(Integer.parseInt(last[3]), Integer.parseInt(last[4].split("\n")[0]));
        }
        return new Location(0, 0);
    }

    public Queue<String> getMoves() {
        return moves;
    }


    public void setData(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public void saveGame() throws IOException {
        FileDialog fileDialog = new FileDialog((Frame) null, "Save Game", FileDialog.SAVE);
        fileDialog.setVisible(true);

        File file = new File(String.valueOf(fileDialog.getFile()));
        BufferedWriter output = new BufferedWriter(new FileWriter(file));

        if (player1 instanceof ComputerPlayer) {
            output.write("CH\t" + player2.getColour().name() + "\n");

        } else
            output.write("HH\n");

        for (String move : moves)
            output.write(move);
        output.close();
    }

    public String getMode() {
        return mode;
    }


    public void loadGame() throws IOException {
        FileDialog fileDialog = new FileDialog((Frame) null, "Load Game", FileDialog.LOAD);
        fileDialog.setVisible(true);

        File file = new File(String.valueOf(fileDialog.getFile()));
        BufferedReader input = new BufferedReader(new FileReader(file));

        mode = input.readLine();

        String temp;
        while ((temp = input.readLine()) != null) {
            moves.add(temp);
        }

        input.close();
    }
}
