import java.awt.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Project: Chess
 * Course:
 * Created on 19 December, 2014
 */
public class MoveHistory {

    Queue<String> moves = new LinkedList<String>();
    private Player player1, player2;
    private String mode;

    public void addMove(Colour colour, Location fromLocation, Location toLocation) {
        String moveString = colour.name() + "\t" + fromLocation.getX() + "\t" + fromLocation.getY() + "\t" + toLocation.getX() + "\t" + toLocation.getY() + "\n";
        moves.add(moveString);
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

        if (player1 instanceof ComputerPlayer)
            output.write("CH\n");
        else
            output.write("HH\n");

        for (String move : moves)
            output.write(move);
        output.close();
    }

    public String getMode() {
        return mode;
    }


    public void loadGame() throws IOException {
        FileDialog fileDialog = new FileDialog((Frame) null, "Save Game", FileDialog.LOAD);
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