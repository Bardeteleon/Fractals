package fractals.user_interface.desktop;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import javax.swing.JComponent;
import javax.swing.JColorChooser;

public class ColorSelectionController implements MouseListener, ActionListener
{
    private mandelbrot.MFrame frame;
    private Color[] selection;
    
    public ColorSelectionController(mandelbrot.MFrame frame, Color[] initSelection)
    {
        this.frame = frame;
        this.selection = initSelection;
    }

    public void makeInteractive()
    {
		frame.colorUpdateButton.addActionListener(this);
		frame.colorCollectionSizeButton.addActionListener(this);
		frame.configurationView.colorButton.addActionListener(this);
    }

    public Color[] getSelection()
    {
        return selection;
    }

    @Override
    public void actionPerformed(ActionEvent arg)
    {
        String cmd = arg.getActionCommand();
        if (cmd.equals("Anzahl Farben"))
        {
            String eingabe = JOptionPane.showInputDialog(frame, "Anzahl der Farben");
            if (eingabe != null)
            {
                try
                {
                    frame.colorCollectionPane.setViewportView(frame.setColorCollectionSize(Integer.parseInt(eingabe)));
                } catch (NumberFormatException ex)
                {
                    frame.configurationView.exceptionTextArea.setText("\n\n Fehler bei der Eingabe\n der Farbanzahl.\n Bitte nur zahlen Eingeben!");
                }
            }
        }
        if (cmd.equals("Farbverlauf aktualisieren"))
        {
            Color[] c = new Color[frame.panels.length];
            for (int i = 0; i < c.length; i++)
            {
                c[i] = frame.panels[i].getBackground();
            }
            selection = c;
            frame.colorDialog.setVisible(false);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        Color background = new JColorChooser().showDialog(frame, "Farbauswahl", Color.CYAN);
        if (background != null)
        {
            e.getComponent().setBackground(background);
            ((JComponent) e.getComponent()).setToolTipText("R " + background.getRed() + ", G " + background.getGreen() + ", B " + background.getBlue());
        }
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
    }

}