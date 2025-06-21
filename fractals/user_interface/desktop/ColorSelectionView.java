package fractals.user_interface.desktop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

public class ColorSelectionView extends JDialog {

    public JButton colorUpdateButton;
    public JButton colorCollectionSizeButton;
    private JScrollPane colorCollectionPane;
    private JPanel buttonPanel;
    public Component[] panels;

    public ColorSelectionView(Frame owner) {
        super(owner);

        setTitle("Color Selection");
        setResizable(false);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        colorCollectionPane = new JScrollPane();
        add(colorCollectionPane, BorderLayout.SOUTH);

        buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.LIGHT_GRAY);

        colorCollectionSizeButton = new JButton("Anzahl Farben");
        buttonPanel.add(colorCollectionSizeButton);

        colorUpdateButton = new JButton("Farbverlauf aktualisieren");
        buttonPanel.add(colorUpdateButton);

        add(buttonPanel, BorderLayout.CENTER);
    }

    public void addController(ActionListener controller) {
        colorCollectionSizeButton.addActionListener(controller);
        colorUpdateButton.addActionListener(controller);
    }

    public Color[] getColorsFromPanels() {
        Color[] colors = new Color[panels.length];
        for (int i = 0; i < panels.length; i++) {
            colors[i] = panels[i].getBackground();
        }
        return colors;
    }

    public void updateColorPanels(Color[] colorCollection, MouseListener listener) {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(Color.LIGHT_GRAY);
        panels = new Component[colorCollection.length];
        for (int i = 0; i < colorCollection.length; i++) {
            JPanel p = new JPanel();
            p.setPreferredSize(new Dimension(75, 75));
            Color background = colorCollection[i];
            if (background != null) {
                p.setBackground(background);
                p.setToolTipText("R " + background.getRed() + ", G " + background.getGreen() + ", B " + background.getBlue());
            }
            p.addMouseListener(listener);
            panel.add(p);
            panels[i] = p;
        }
        colorCollectionPane.setViewportView(panel);
    }
}
