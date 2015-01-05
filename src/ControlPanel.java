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

    private Boolean freeMode = false;
    private HumanPlayer player, player2;

    public ControlPanel(final MoveHistory moveHistory) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.black, 3));

        JLabel controlsLabel = new JLabel("Controls");
        controlsLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(controlsLabel, BorderLayout.NORTH);

        JButton saveButton = new JButton("Save");
        JButton quitButton = new JButton("Quit");
        JToggleButton modeButton = new JToggleButton("Free Mode");
        final JLabel freeLabel = new JLabel("OFF");

        JPanel centerPanel = new JPanel(new FlowLayout());
        JPanel modePanel = new JPanel(new GridLayout(2, 2, 25, 15));
        centerPanel.add(saveButton);
        modePanel.add(new Label());
        modePanel.add(new Label());
        modePanel.add(modeButton);
        modePanel.add(freeLabel);
        centerPanel.add(modePanel);
        add(centerPanel, BorderLayout.CENTER);


        add(quitButton, BorderLayout.SOUTH);

        modeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JToggleButton btn = (JToggleButton) e.getSource();
                freeMode = btn.getModel().isSelected();
                String text = freeMode ? "ON" : "OFF";
                freeLabel.setText(text);
                player.setMode(freeMode);
                player2.setMode(freeMode);
            }
        });

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

    }

    public void setPlayer(HumanPlayer player) {
        this.player = player;
        this.player2 = player;
    }

    public void setPlayer2(HumanPlayer player2) {
        this.player2 = player2;
    }

}
