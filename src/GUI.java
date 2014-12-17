import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;


/**
 * Project: Chess
 * Course: COSC 3P71 - Final Project
 * Created: December, 2014
 */
public class GUI extends JFrame {

    private JPanel boardPanel;
    private JPanel sidePanel;
    private BorderLayout borderLayout = new BorderLayout();

    public GUI() {
        setLayout(borderLayout);

        setSidePanel();
        setBoardPanel();

        add(sidePanel);

        setSize(1100, 875);
        setTitle("Chess");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void startGUI() {
        setVisible(true);
        setResizable(false);
        JOptionPane.showMessageDialog(this, "Left click to select. Right click to move.");
    }


    private void setBoardPanel() {
        boardPanel = new JPanel();
        boardPanel.setSize(850, 850);
        borderLayout.addLayoutComponent(boardPanel, BorderLayout.CENTER);
    }

    public void addBoardPanel(JPanel panel) {
        this.boardPanel = panel;
        add(boardPanel);
    }


    private void setSidePanel() {
        sidePanel = new JPanel(new GridLayout(3, 1));
        sidePanel.setPreferredSize(new Dimension(250, 875));
        borderLayout.addLayoutComponent(sidePanel, BorderLayout.EAST);
    }

    public void addSidePanel(JPanel panel) {
        Border border = BorderFactory.createLineBorder(Color.darkGray, 1);
        Border margin = new EmptyBorder(10, 10, 10, 10);
        panel.setBorder(new CompoundBorder(border, margin));
        sidePanel.add(panel);
    }
}
