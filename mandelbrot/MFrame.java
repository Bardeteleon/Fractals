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
import fractals.user_interface.desktop.ColorSelectionController;
import fractals.user_interface.desktop.ConfigurationController;
import fractals.user_interface.desktop.WindowController;

public class MFrame extends JFrame
{

	public Fractal fractals;
	public JButton optionButton, repaintButton, saveButton, helpButton, colorButton, colorUpdateButton, colorCollectionSizeButton;
	public JTextField reelField1, imagField1, reelField2, imagField2, reelField3, imagField3, dimensionField, iterationField;
	private JLabel koordinatesLabel, imagLabel, minLabel, maxLabel, dimensionLabel, iterationLabel;
	public JCheckBox juliaCheckBox, iterationCheckBox, complexPlaneCheckBox;
	public JScrollPane graphicsPane, colorCollectionPane;
	public JPanel optionPanelRight, buffPanel;
	private JPanel koordinatesPanel, ladeBalkenPanel, southPanel, repaintPanel, checkBoxPanel, iterationDimensionPanel, intervalPanel, colorModePanel, buttonPanel;
	public JComboBox colorModeChooser;
	public JDialog colorDialog;
	public JTextArea exceptionTextArea;
	public JProgressBar progressBar;
	private JToolBar buttonLeiste;
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
		addWindowListener(windowController);

		// ZeichenPane(Center)
		fractals = new Fractal();
		fractalPresenter = new FractalPresenter(fractals, this);
		fractalPresenter.setInteractive(true);
		buffPanel = new JPanel();
		buffPanel.add(fractals);
		graphicsPane = new JScrollPane();
		graphicsPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		graphicsPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		graphicsPane.setViewportView(buffPanel);

		// Panel(South)
		koordinatesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		koordinatesLabel = new JLabel("Koordinaten: Reel");
		koordinatesPanel.add(koordinatesLabel);
		reelField1 = new JTextField(13);
		koordinatesPanel.add(reelField1);
		imagLabel = new JLabel("Imag");
		koordinatesPanel.add(imagLabel);
		imagField1 = new JTextField(13);
		koordinatesPanel.add(imagField1);
		ladeBalkenPanel = new JPanel();
		progressBar = new JProgressBar(SwingConstants.HORIZONTAL, 0, 100);
		progressBar.setStringPainted(true);
		progressBar.setString("Fertig!");
		progressBar.addChangeListener(windowController);
		progressBar.setPreferredSize(new Dimension(300, 25));
		ladeBalkenPanel.add(progressBar);
		southPanel = new JPanel(new GridLayout(1, 2));
		southPanel.add(koordinatesPanel);
		southPanel.add(ladeBalkenPanel);

		// ButtonLeiste(North)
		buttonLeiste = new JToolBar();
		buttonLeiste.setBackground(Color.WHITE);
		buttonLeiste.setLayout(new FlowLayout(FlowLayout.CENTER));
		menuBarController = new MenuBarController(fractals, this);
		optionButton = new JButton("Optionen");
		optionButton.addActionListener(menuBarController);
		buttonLeiste.add(optionButton);
		helpButton = new JButton("Hilfe");
		helpButton.addActionListener(menuBarController);
		buttonLeiste.add(helpButton);
		saveButton = new JButton("Speichern");
		saveButton.addActionListener(menuBarController);
		buttonLeiste.add(saveButton);

		// OptionPanelRight
		optionPanelRight = new JPanel();
		optionPanelRight.setVisible(false);
		optionPanelRight.setLayout(new BoxLayout(optionPanelRight, BoxLayout.Y_AXIS));
		optionPanelRight.setBackground(Color.WHITE);

		repaintPanel = new JPanel();
		configurationController = new ConfigurationController(fractals, this);
		repaintButton = new JButton("Neu Zeichnen");
		repaintButton.addActionListener(configurationController);
		repaintPanel.add(repaintButton);
		optionPanelRight.add(repaintPanel);

		checkBoxPanel = new JPanel(new GridLayout(3, 1, 5, 5));
		juliaCheckBox = new JCheckBox("JuliaMenge");
		juliaCheckBox.setToolTipText("Bei Auswahl wird die Julia Menge entsprechend der eingegebenen Koordinaten gezeichnet");
		juliaCheckBox.addActionListener(configurationController);
		iterationCheckBox = new JCheckBox("ZoomAnpassung");
		iterationCheckBox.setToolTipText("Die Iterationszahl wird beim zoomen automatisch erhöht.");
		iterationCheckBox.setSelected(true);
		iterationCheckBox.addActionListener(configurationController);
		complexPlaneCheckBox = new JCheckBox("GaußscheZahlenebene");
		complexPlaneCheckBox.setToolTipText("Gaußsche Zahlenebene beim nächsten Neu Zeichnen ein-/ausblenden (nur beim Standrad-Intervall möglich)");
		complexPlaneCheckBox.addActionListener(configurationController);
		checkBoxPanel.add(juliaCheckBox);
		checkBoxPanel.add(complexPlaneCheckBox);
		checkBoxPanel.add(iterationCheckBox);
		optionPanelRight.add(checkBoxPanel);

		iterationDimensionPanel = new JPanel(new GridLayout(2, 2, 5, 5));
		iterationLabel = new JLabel("Iterationstiefe: ");
		iterationDimensionPanel.add(iterationLabel);
		iterationField = new JTextField(5);
		iterationField.setText(fractals.getIterationRange() + "");
		iterationDimensionPanel.add(iterationField);
		iterationDimensionPanel.setToolTipText("Größe des zu zeichnenden Bereichs in Pixel(quadratische Form)");
		dimensionLabel = new JLabel("Größe: ");
		iterationDimensionPanel.add(dimensionLabel);
		dimensionField = new JTextField(4);
		dimensionField.setText(fractals.getWidthHeight() + "");
		iterationDimensionPanel.add(dimensionField);
		optionPanelRight.add(iterationDimensionPanel);

		intervalPanel = new JPanel(new GridLayout(2, 3, 5, 5));
		intervalPanel.setBorder(BorderFactory.createTitledBorder("Intervall"));
		minLabel = new JLabel("Min: ");
		intervalPanel.add(minLabel);
		reelField2 = new JTextField(4);
		reelField2.setText(fractals.getMinRe() + "");
		intervalPanel.add(reelField2);
		imagField2 = new JTextField(4);
		imagField2.setText(fractals.getMinIm() + "");
		intervalPanel.add(imagField2);
		maxLabel = new JLabel("Max: ");
		intervalPanel.add(maxLabel);
		reelField3 = new JTextField(4);
		reelField3.setText(fractals.getMaxRe() + "");
		intervalPanel.add(reelField3);
		imagField3 = new JTextField(4);
		imagField3.setText(fractals.getMaxIm() + "");
		intervalPanel.add(imagField3);
		optionPanelRight.add(intervalPanel);

		colorModePanel = new JPanel(new GridLayout(2, 1, 3, 3));
		colorModePanel.setBorder(BorderFactory.createTitledBorder("Farbmodus"));
		String[] comboBoxContent = {"Schwarz Weiß", "Schwarz Weiß Modulo", "Farbabstufungen"};
		colorModeChooser = new JComboBox(comboBoxContent);
		colorModeChooser.setMaximumSize(new Dimension(150, 18));
		colorModeChooser.addItemListener(configurationController);
		colorModePanel.setMaximumSize(new Dimension(500, 30));
		colorModePanel.add(colorModeChooser);
		colorButton = new JButton("Farbverlauf");
		colorButton.addActionListener(configurationController);
		colorButton.setEnabled(false);
		colorModePanel.add(colorButton);
		optionPanelRight.add(colorModePanel);

		exceptionTextArea = new JTextArea();
		exceptionTextArea.setEditable(false);
		optionPanelRight.add(exceptionTextArea);

		// Farb-Dialog
		colorSelectionController = new ColorSelectionController(this, fractals.getColorCollection());
		colorUpdateButton = new JButton("Farbverlauf aktualisieren");
		colorUpdateButton.addActionListener(colorSelectionController);
		colorCollectionSizeButton = new JButton("Anzahl Farben");
		colorCollectionSizeButton.addActionListener(colorSelectionController);
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
		colorButton.addActionListener(colorSelectionController);

		content.add(optionPanelRight, BorderLayout.EAST);
		content.add(buttonLeiste, BorderLayout.NORTH);
		content.add(southPanel, BorderLayout.SOUTH);
		content.add(graphicsPane, BorderLayout.CENTER);
		pack();

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
		iterationField.setText(fractals.getIterationRange() + "");
		dimensionField.setText(fractals.getWidthHeight() + "");
		if (fractals.getParamC() != null)
		{
			imagField1.setText("" + fractals.getParamC().getImag());
			reelField1.setText("" + fractals.getParamC().getReal());
		}
		imagField2.setText(fractals.getMinIm() + "");
		imagField2.setToolTipText(fractals.getMinIm() + "");
		imagField3.setText(fractals.getMaxIm() + "");
		imagField3.setToolTipText(fractals.getMaxIm() + "");
		reelField2.setText(fractals.getMinRe() + "");
		reelField2.setToolTipText(fractals.getMinRe() + "");
		reelField3.setText(fractals.getMaxRe() + "");
		reelField3.setToolTipText(fractals.getMaxRe() + "");
	}
}
