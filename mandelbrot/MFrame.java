package mandelbrot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import fractals.core.Complex;
import fractals.core.Colorizer.Mode;
import fractals.user_interface.desktop.FractalPresenter;
import fractals.user_interface.desktop.MenuBarController;
import fractals.user_interface.desktop.MenuBarView;
import fractals.user_interface.desktop.StatusBarView;
import fractals.user_interface.desktop.ConfigurationView;
import fractals.user_interface.desktop.ColorSelectionController;
import fractals.user_interface.desktop.ConfigurationController;
import fractals.user_interface.desktop.WindowController;

public class MFrame extends JFrame
{

	public Fractal fractals;
	public JButton colorUpdateButton, colorCollectionSizeButton;
	public JCheckBox complexPlaneCheckBox;
	public JScrollPane graphicsPane, colorCollectionPane;
	public JPanel buffPanel;
	private JPanel buttonPanel;
	public JDialog colorDialog;
	public MenuBarView menuBarView;
	public StatusBarView statusBarView;
	public ConfigurationView configurationView;
	public Component[] panels;
	public ColorSelectionController colorSelectionController;
	public FractalPresenter fractalPresenter;
	private WindowController windowController;
	private ConfigurationController configurationController;
	private MenuBarController menuBarController;
	private Container content;
	private JFileChooser chooser;

	public MFrame()
	{
		super("Fractal v2.0");
		content = this.getContentPane();
		content.setLayout(new BorderLayout());
		windowController = new WindowController(this);

		// ZeichenPane(Center)
		fractals = new Fractal();
		fractalPresenter = new FractalPresenter(fractals, this);
		buffPanel = new JPanel();
		buffPanel.add(fractals);
		graphicsPane = new JScrollPane();
		graphicsPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		graphicsPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		graphicsPane.setViewportView(buffPanel);

		statusBarView = new StatusBarView();

		menuBarView = new MenuBarView();
		menuBarController = new MenuBarController(fractals, this);

		configurationView = new ConfigurationView(fractals);
		configurationController = new ConfigurationController(fractals, this);

		// Farb-Dialog
		colorSelectionController = new ColorSelectionController(this, fractals.getColorCollection());
		colorUpdateButton = new JButton("Farbverlauf aktualisieren");
		colorCollectionSizeButton = new JButton("Anzahl Farben");
		colorDialog = new JDialog();
		colorDialog.setResizable(false);
		colorDialog.setLayout(new BorderLayout());
		colorCollectionPane = new JScrollPane();
		colorCollectionPane.setViewportView(setColorCollectionSize(fractals.getColorCollection().length));
		colorDialog.add(colorCollectionPane, BorderLayout.SOUTH);
		buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.setBackground(Color.LIGHT_GRAY);
		buttonPanel.add(colorCollectionSizeButton);
		buttonPanel.add(colorUpdateButton);
		colorDialog.add(buttonPanel, BorderLayout.CENTER);
		colorDialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

		content.add(configurationView, BorderLayout.EAST);
		content.add(menuBarView, BorderLayout.NORTH);
		content.add(statusBarView, BorderLayout.SOUTH);
		content.add(graphicsPane, BorderLayout.CENTER);
		pack();

		windowController.makeInteractive();
		menuBarController.makeInteractive();
		configurationController.makeInteractive();
		colorSelectionController.makeInteractive();
		fractalPresenter.setInteractive(true);
	}

	public JPanel setColorCollectionSize(int size)
	{
		JPanel panel = new JPanel(new FlowLayout());
		panel.setBackground(Color.LIGHT_GRAY);
		panels = new Component[size];
		for (int i = 0; i < size; i++)
		{
			JPanel p = new JPanel();
			p.setPreferredSize(new Dimension(75, 75));
			if (fractals.getColorCollection().length > i)
			{
				Color background = (fractals.getColorCollection())[i];
				p.setBackground(background);
				p.setToolTipText("R " + background.getRed() + ", G " + background.getGreen() + ", B " + background.getBlue());
			}
			p.addMouseListener(colorSelectionController);
			panel.add(p);
			panels[i] = p;
		}
		return panel;
	}
	
	public void setAktTextFieldText()
	{
		configurationView.iterationField.setText(fractals.getIterationRange() + "");
		configurationView.dimensionField.setText(fractals.getWidthHeight() + "");
		if (fractals.getParamC() != null)
		{
			statusBarView.imagField1.setText("" + fractals.getParamC().getImag());
			statusBarView.reelField1.setText("" + fractals.getParamC().getReal());
		}
		configurationView.imagField2.setText(fractals.getMinIm() + "");
		configurationView.imagField2.setToolTipText(fractals.getMinIm() + "");
		configurationView.imagField3.setText(fractals.getMaxIm() + "");
		configurationView.imagField3.setToolTipText(fractals.getMaxIm() + "");
		configurationView.reelField2.setText(fractals.getMinRe() + "");
		configurationView.reelField2.setToolTipText(fractals.getMinRe() + "");
		configurationView.reelField3.setText(fractals.getMaxRe() + "");
		configurationView.reelField3.setToolTipText(fractals.getMaxRe() + "");
	}
}
