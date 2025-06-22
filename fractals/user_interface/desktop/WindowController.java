package fractals.user_interface.desktop;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fractals.user_interface.desktop.WindowView;
import fractals.user_interface.desktop.FractalPresenter;

public class WindowController extends WindowAdapter implements ChangeListener
{
    private WindowView frame;
    private FractalPresenter fractalPresenter;
    
	private boolean withWindowClosingDialog = false;

    public WindowController(WindowView frame, FractalPresenter fractalPresenter)
    {
        this.frame = frame;
        this.fractalPresenter = fractalPresenter;
        makeInteractive();
    }

    private void makeInteractive()
    {
		frame.addWindowListener(this);
		frame.statusBarView.progressBar.addChangeListener(this);
    }

    @Override
    public void windowClosing(WindowEvent e)
    {
        int i = withWindowClosingDialog 
                    ? JOptionPane.showConfirmDialog(frame, "Wollen Sie wirklich beenden?", "WindowClosing", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)
                    : 0;
        if (i == 0)
        {
            System.exit(0);
        }
    }

    @Override
    public void stateChanged(ChangeEvent c)
    {
        if (c.getSource() instanceof JProgressBar)
        {
            JProgressBar bar = (JProgressBar) c.getSource();
            if (bar.getValue() == 100)
            {
                fractalPresenter.setInteractive(true);
                frame.configurationView.repaintButton.setText("Neu Zeichnen");
            }
        }
    }
}