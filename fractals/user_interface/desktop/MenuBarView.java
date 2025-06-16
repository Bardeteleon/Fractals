package fractals.user_interface.desktop;

import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JToolBar;

public class MenuBarView extends JToolBar
{
    public JButton optionButton, helpButton, saveButton;

    public MenuBarView()
    {
		setBackground(Color.WHITE);
		setLayout(new FlowLayout(FlowLayout.CENTER));
		optionButton = new JButton("Optionen");
		add(optionButton);
		helpButton = new JButton("Hilfe");
		add(helpButton);
		saveButton = new JButton("Speichern");
		add(saveButton);
    }
}