package fractals.user_interface.desktop;

import java.awt.*;
import javax.swing.*;
import fractals.user_interface.desktop.FractalView;

public class ConfigurationView extends JPanel {
    public JButton repaintButton;
    public JButton colorButton;
    public JCheckBox juliaCheckBox;
    public JCheckBox iterationCheckBox;
    public JCheckBox complexPlaneCheckBox;
    public JTextField iterationField;
    public JTextField dimensionField;
    public JTextField reelField2;
    public JTextField imagField2;
    public JTextField reelField3;
    public JTextField imagField3;
    public JComboBox<String> colorModeChooser;
    public JTextArea exceptionTextArea;

    public ConfigurationView(FractalView fractals) {
        setVisible(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);

        // repaint panel
        JPanel repaintPanel = new JPanel();
        repaintButton = new JButton("Neu Zeichnen");
        repaintPanel.add(repaintButton);
        add(repaintPanel);

        // checkbox panel
        JPanel checkBoxPanel = new JPanel(new GridLayout(3,1,5,5));
        juliaCheckBox = new JCheckBox("JuliaMenge");
        juliaCheckBox.setToolTipText("Bei Auswahl wird die Julia Menge entsprechend der eingegebenen Koordinaten gezeichnet");
        iterationCheckBox = new JCheckBox("ZoomAnpassung");
        iterationCheckBox.setToolTipText("Die Iterationszahl wird beim zoomen automatisch erhöht.");
        iterationCheckBox.setSelected(true);
        complexPlaneCheckBox = new JCheckBox("GaußscheZahlenebene");
        complexPlaneCheckBox.setToolTipText("Gaußsche Zahlenebene beim nächsten Neu Zeichnen ein-/ausblenden (nur beim Standrad-Intervall möglich)");
        checkBoxPanel.add(juliaCheckBox);
        checkBoxPanel.add(complexPlaneCheckBox);
        checkBoxPanel.add(iterationCheckBox);
        add(checkBoxPanel);

        // iteration/dimension panel
        JPanel iterationDimensionPanel = new JPanel(new GridLayout(2,2,5,5));
        iterationDimensionPanel.add(new JLabel("Iterationstiefe: "));
        iterationField = new JTextField(5);
        iterationField.setText(String.valueOf(fractals.getIterationRange()));
        iterationDimensionPanel.add(iterationField);
        iterationDimensionPanel.setToolTipText("Größe des zu zeichnenden Bereichs in Pixel(quadratische Form)");
        iterationDimensionPanel.add(new JLabel("Größe: "));
        dimensionField = new JTextField(4);
        dimensionField.setText(String.valueOf(fractals.getWidthHeight()));
        iterationDimensionPanel.add(dimensionField);
        add(iterationDimensionPanel);

        // interval panel
        JPanel intervalPanel = new JPanel(new GridLayout(2,3,5,5));
        intervalPanel.setBorder(BorderFactory.createTitledBorder("Intervall"));
        intervalPanel.add(new JLabel("Min: "));
        reelField2 = new JTextField(4);
        reelField2.setText(String.valueOf(fractals.getMinRe()));
        intervalPanel.add(reelField2);
        imagField2 = new JTextField(4);
        imagField2.setText(String.valueOf(fractals.getMinIm()));
        intervalPanel.add(imagField2);
        intervalPanel.add(new JLabel("Max: "));
        reelField3 = new JTextField(4);
        reelField3.setText(String.valueOf(fractals.getMaxRe()));
        intervalPanel.add(reelField3);
        imagField3 = new JTextField(4);
        imagField3.setText(String.valueOf(fractals.getMaxIm()));
        intervalPanel.add(imagField3);
        add(intervalPanel);

        // color mode panel
        JPanel colorModePanel = new JPanel(new GridLayout(2,1,3,3));
        colorModePanel.setBorder(BorderFactory.createTitledBorder("Farbmodus"));
        String[] combo = {"Schwarz Weiß", "Schwarz Weiß Modulo", "Farbabstufungen"};
        colorModeChooser = new JComboBox<>(combo);
        colorModeChooser.setMaximumSize(new Dimension(150,18));
        colorModePanel.setMaximumSize(new Dimension(500,30));
        colorModePanel.add(colorModeChooser);
        colorButton = new JButton("Farbverlauf");
        colorButton.setEnabled(false);
        colorModePanel.add(colorButton);
        add(colorModePanel);

        exceptionTextArea = new JTextArea();
        exceptionTextArea.setEditable(false);
        add(exceptionTextArea);
    }
}
