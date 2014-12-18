import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Project: Chess
 * Course:
 * Created on 16 December, 2014
 */
public class ControlPanel extends JPanel {

    public ControlPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.black, 3));

        JLabel controlsLabel = new JLabel("Controls");
        controlsLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(controlsLabel, BorderLayout.NORTH);

        JButton loadButton = new JButton("Load");
        JButton saveButton = new JButton("Save");
        JButton quitButton = new JButton("Quit");

        JPanel centerPanel = new JPanel(new FlowLayout());
        centerPanel.add(loadButton);
        centerPanel.add(saveButton);
        add(centerPanel, BorderLayout.CENTER);

        add(quitButton, BorderLayout.SOUTH);

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

    }
}
