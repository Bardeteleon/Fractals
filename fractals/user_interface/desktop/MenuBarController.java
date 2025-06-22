package fractals.user_interface.desktop;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.JOptionPane;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.Objects;
import javax.imageio.ImageIO;

import fractals.user_interface.desktop.WindowView;

public class MenuBarController implements ActionListener
{
    private WindowView windowView;
    private JFileChooser chooser;

    public MenuBarController(WindowView windowView)
    {
        this.windowView = windowView;
        makeInteractive();
    }

    private void makeInteractive()
    {
        windowView.menuBarView.optionButton.addActionListener(this);
        windowView.menuBarView.saveButton.addActionListener(this);
        windowView.menuBarView.helpButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String cmd = e.getActionCommand();
        if (cmd.equals("Optionen"))
        {
            windowView.configurationView.setVisible(!windowView.configurationView.isVisible());
        }
        if (cmd.equals("Speichern"))
        {
            chooser = new JFileChooser();
            chooser.setFileHidingEnabled(true);
            chooser.setDialogTitle("Speichern");
            chooser.setMultiSelectionEnabled(false);
            chooser.setFileFilter(new FileFilter()
            {
                @Override
                public boolean accept(File f)
                {
                    return f.getName().toLowerCase().endsWith(".png") || f.isDirectory();
                }
                @Override
                public String getDescription()
                {
                    return "(*.png)";
                }
            });

            if (chooser.showSaveDialog(windowView) == JFileChooser.APPROVE_OPTION)
            {
                String path = chooser.getSelectedFile().getPath();
                if (path != null)
                {
                    storeFractalImage(new File(path));
                }
            }
        }
        if (cmd.equals("Hilfe"))
        {
            JOptionPane.showMessageDialog(windowView, dateiLaden("Hilfe.txt"), "Hilfe", JOptionPane.INFORMATION_MESSAGE);
        }
    }

	private String dateiLaden(String name)
	{
		InputStream stream = this.getClass().getResourceAsStream(name);
		char[] data = null;
		if (stream != null)
		{
			InputStreamReader reader = new InputStreamReader(stream);
			try
			{
				int size = stream.available();
				data = new char[size];
				reader.read(data, 0, size);
			} catch (IOException e)
			{
				e.printStackTrace();
			} finally
			{
				try
				{
					stream.close();
					reader.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return new String(data);
	}

	public void storeFractalImage(File destination)
	{
		if (Objects.isNull(windowView.fractalView.getImage())) return;

		if (destination.getName().toLowerCase().endsWith(".png"))
		{
			try
			{
				ImageIO.write(windowView.fractalView.getImage(), "png", destination);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}