package fractals.user_interface.desktop;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;

import fractals.core.Complex;
import fractals.core.Colorizer.Mode;

public class ConfigurationController implements ActionListener, ItemListener
{
    private mandelbrot.Fractal fractals;
    private mandelbrot.MFrame frame;

    public ConfigurationController(mandelbrot.Fractal fractals, mandelbrot.MFrame frame)
    {
        this.fractals = fractals;
        this.frame = frame;
    }

    public void makeInteractive()
    {
        frame.repaintButton.addActionListener(this);
        frame.juliaCheckBox.addActionListener(this);
        frame.iterationCheckBox.addActionListener(this);
        frame.complexPlaneCheckBox.addActionListener(this);
        frame.colorModeChooser.addItemListener(this);
        frame.colorButton.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e)
    {
        String cmd = e.getActionCommand();
        if (cmd.equals("Neu Zeichnen"))
        {
            frame.repaintButton.setText("Abbrechen!");
            frame.fractalPresenter.setInteractive(false);
            frame.exceptionTextArea.setText("");
            try
            {
                fractals.setMinCoordinate(new Complex(Double.parseDouble(frame.reelField2.getText()), Double.parseDouble(frame.imagField2.getText())));
                fractals.setMaxCoordinate(new Complex(Double.parseDouble(frame.reelField3.getText()), Double.parseDouble(frame.imagField3.getText())));
                fractals.setIterationRange(Integer.parseInt(frame.iterationField.getText()));
                fractals.setWidthHeight(Integer.parseInt(frame.dimensionField.getText()));
                fractals.setColorCollection(frame.colorSelectionController.getSelection());
                frame.graphicsPane.setViewportView(frame.buffPanel); // falls die Größe
                                                            // des Fractals
                                                            // Objekts
                                                            // geändert wird
                                                            // muss sich das
                                                            // ScrollPane
                                                            // aktualisieren,
                                                            // was mit dem
                                                            // wiederholten
                                                            // setzen des
                                                            // Viewports
                                                            // erreicht wird
            } catch (NumberFormatException ex)
            {
                frame.exceptionTextArea.setText("\n\n Fehler beim Einlesen!\n Mögliche Fehlerquelle:\n -Komma statt Punkt\n als Trennzeichen\n -Buchstaben in der Eingabe ");
            }
            if (frame.juliaCheckBox.isSelected())
            {
                try
                {
                    fractals.paintFractals(new Complex(Double.parseDouble(frame.reelField1.getText()), Double.parseDouble(frame.imagField1.getText())), frame.progressBar);
                } catch (NumberFormatException ex)
                {
                    frame.exceptionTextArea.setText("\n\n Fehler beim Einlesen!\n Mögliche Fehlerquelle:\n -Komma statt Punkt\n als Trennzeichen\n -Buchstaben in der Eingabe ");
                }
            } else
                fractals.paintFractals(null, frame.progressBar);

            if (frame.complexPlaneCheckBox.isSelected())
            {
                fractals.paintKoordinateSystem();
            }
            frame.setAktTextFieldText();
        }

        if (cmd.equals("Abbrechen!"))
        {
            fractals.stop();
            frame.progressBar.setValue(0);
            frame.progressBar.setString("Abgebrochen!");
        }
        if (cmd.equals("Farbverlauf"))
        {
            frame.colorDialog.setSize(555, 200);
            frame.colorDialog.setLocation(frame.getWidth() / 2 - frame.colorDialog.getWidth() / 2, frame.getHeight() / 2 - frame.colorDialog.getHeight() / 2);
            frame.colorDialog.setVisible(true);
        }

    }

    public void itemStateChanged(ItemEvent ie)
    {
        JComboBox selectedItem = (JComboBox) ie.getSource();
        if (selectedItem.getSelectedItem().equals("Schwarz Weiß"))
        {
            fractals.setColorMode(Mode.BLACK_WHITE);
        }
        if (selectedItem.getSelectedItem().equals("Schwarz Weiß Modulo"))
        {
            fractals.setColorMode(Mode.BLACK_WHITE_MODULO);
        }
        if (selectedItem.getSelectedItem().equals("Farbabstufungen"))
        {
            frame.colorButton.setEnabled(true);
            fractals.setColorMode(Mode.COLOR);
        } else
            frame.colorButton.setEnabled(false);

    }
}