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

    public ControlPanel(GUI gui) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.black, 3));

        JLabel controlsLabel = new JLabel("Controls");
        controlsLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(controlsLabel, BorderLayout.NORTH);

        JButton quitButton = new JButton("Quit");
        add(quitButton, BorderLayout.SOUTH);


        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

    }
}
