import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Project: Chess
 * Course: COSC 3P71 - Final Project
 * Created: December, 2014
 */
public class ControlPanel extends JPanel {

    private MoveHistory moveHistory;
    private Player player1, player2;
    public ControlPanel(final MoveHistory moveHistory, Player p1, Player p2, Boolean inFreePlay) {
        System.out.println("Test so I can commit??");
        player1 = p1;
        player2 = p2;
        this.moveHistory = moveHistory;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.black, 3));

        JLabel controlsLabel = new JLabel("Controls");
        controlsLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(controlsLabel, BorderLayout.NORTH);

        JButton saveButton = new JButton("Save");
        JButton quitButton = new JButton("Quit");
        JButton convertButton = new JButton("Convert");

        JPanel centerPanel = new JPanel(new FlowLayout());
        centerPanel.add(saveButton);
        if (inFreePlay) {
            centerPanel.add(convertButton);
        }
        add(centerPanel, BorderLayout.CENTER);

        add(quitButton, BorderLayout.SOUTH);

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    moveHistory.saveGame();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        });

        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    player1.kill();
                    player2.kill();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }
        });

    }


}
