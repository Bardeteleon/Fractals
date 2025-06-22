package fractals.user_interface.desktop;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;

import fractals.core.Complex;
import fractals.core.Colorizer.Mode;
import fractals.user_interface.desktop.FractalView;
import fractals.user_interface.desktop.WindowView;
import fractals.user_interface.desktop.FractalPresenter;
import fractals.user_interface.desktop.ColorSelectionController;

public class ConfigurationController implements ActionListener, ItemListener
{
    private FractalView fractals;
    private WindowView frame;
    private FractalPresenter fractalPresenter;
    private ColorSelectionController colorSelectionController;

    public ConfigurationController(FractalView fractals, WindowView frame, FractalPresenter fractalPresenter, ColorSelectionController colorSelectionController)
    {
        this.fractals = fractals;
        this.frame = frame;
        this.fractalPresenter = fractalPresenter;
        this.colorSelectionController = colorSelectionController;
        makeInteractive();
    }

    private void makeInteractive()
    {
        frame.configurationView.repaintButton.addActionListener(this);
        frame.configurationView.juliaCheckBox.addActionListener(this);
        frame.configurationView.iterationCheckBox.addActionListener(this);
        frame.configurationView.complexPlaneCheckBox.addActionListener(this);
        frame.configurationView.colorModeChooser.addItemListener(this);
        frame.configurationView.colorButton.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e)
    {
        String cmd = e.getActionCommand();
        if (cmd.equals("Neu Zeichnen"))
        {
            frame.configurationView.repaintButton.setText("Abbrechen!");
            fractalPresenter.setInteractive(false);
            frame.configurationView.exceptionTextArea.setText("");
            try
            {
                fractals.setMinCoordinate(new Complex(Double.parseDouble(frame.configurationView.reelField2.getText()), Double.parseDouble(frame.configurationView.imagField2.getText())));
                fractals.setMaxCoordinate(new Complex(Double.parseDouble(frame.configurationView.reelField3.getText()), Double.parseDouble(frame.configurationView.imagField3.getText())));
                fractals.setIterationRange(Integer.parseInt(frame.configurationView.iterationField.getText()));
                fractals.setWidthHeight(Integer.parseInt(frame.configurationView.dimensionField.getText()));
                fractals.setColorCollection(colorSelectionController.getSelection());
                frame.fractalViewScrollable.update();
            } catch (NumberFormatException ex)
            {
                frame.configurationView.exceptionTextArea.setText("\n\n Fehler beim Einlesen!\n Mögliche Fehlerquelle:\n -Komma statt Punkt\n als Trennzeichen\n -Buchstaben in der Eingabe ");
            }
            if (frame.configurationView.juliaCheckBox.isSelected())
            {
                try
                {
                    fractals.paintFractals(new Complex(Double.parseDouble(frame.statusBarView.reelField1.getText()), Double.parseDouble(frame.statusBarView.imagField1.getText())));
                } catch (NumberFormatException ex)
                {
                    frame.configurationView.exceptionTextArea.setText("\n\n Fehler beim Einlesen!\n Mögliche Fehlerquelle:\n -Komma statt Punkt\n als Trennzeichen\n -Buchstaben in der Eingabe ");
                }
            } else
                fractals.paintFractals(null);

            if (frame.configurationView.complexPlaneCheckBox.isSelected())
            {
                fractals.paintKoordinateSystem();
            }
            frame.setAktTextFieldText();
        }

        if (cmd.equals("Abbrechen!"))
        {
            fractals.stop();
            frame.statusBarView.progressBar.setValue(0);
            frame.statusBarView.progressBar.setString("Abgebrochen!");
        }
        if (cmd.equals("Farbverlauf"))
        {
            frame.colorSelectionView.pack();
            frame.colorSelectionView.setLocationRelativeTo(frame);
            frame.colorSelectionView.setVisible(true);
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
            frame.configurationView.colorButton.setEnabled(true);
            fractals.setColorMode(Mode.COLOR);
        } else
            frame.configurationView.colorButton.setEnabled(false);

    }
}