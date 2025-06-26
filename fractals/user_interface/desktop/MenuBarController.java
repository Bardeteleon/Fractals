package fractals.user_interface.desktop;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.JOptionPane;
import java.io.IOException;
import java.net.URISyntaxException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.Objects;
import javax.imageio.ImageIO;
import java.util.stream.Collectors;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
            JOptionPane.showMessageDialog(windowView, readTextFile("Hilfe.txt"), "Hilfe", JOptionPane.INFORMATION_MESSAGE);
        }
    }

	private String readTextFile(String name)
	{
        String content = "";
        try {
            Path path = Paths.get(getClass().getClassLoader().getResource(name).toURI());
            content = Files.lines(path).collect(Collectors.joining("\n"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
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