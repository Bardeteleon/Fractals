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
	private FarbDialogeListener fdl;
	private FraktalPanelListener fpl;
	private FensterListener fl;
	private OptionListener ol;
	private MenuBarListener mbl;
	private Container content;
	private JFileChooser chooser;

	private boolean withWindowClosingDialog = false;

	public MFrame()
	{
		super("Fractal v2.0");
		content = this.getContentPane();
		content.setLayout(new BorderLayout());
		fl = new FensterListener();
		addWindowListener(fl);

		// ZeichenPane(Center)
		fpl = new FraktalPanelListener();
		fractals = new Fractal();
		fractals.addMouseMotionListener(fpl);
		fractals.addMouseListener(fpl);
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
		progressBar.addChangeListener(fl);
		progressBar.setPreferredSize(new Dimension(300, 25));
		ladeBalkenPanel.add(progressBar);
		southPanel = new JPanel(new GridLayout(1, 2));
		southPanel.add(koordinatesPanel);
		southPanel.add(ladeBalkenPanel);

		// ButtonLeiste(North)
		buttonLeiste = new JToolBar();
		buttonLeiste.setBackground(Color.WHITE);
		buttonLeiste.setLayout(new FlowLayout(FlowLayout.CENTER));
		mbl = new MenuBarListener();
		optionButton = new JButton("Optionen");
		optionButton.addActionListener(mbl);
		buttonLeiste.add(optionButton);
		helpButton = new JButton("Hilfe");
		helpButton.addActionListener(mbl);
		buttonLeiste.add(helpButton);
		saveButton = new JButton("Speichern");
		saveButton.addActionListener(mbl);
		buttonLeiste.add(saveButton);

		// OptionPanelRight
		optionPanelRight = new JPanel();
		optionPanelRight.setVisible(false);
		optionPanelRight.setLayout(new BoxLayout(optionPanelRight, BoxLayout.Y_AXIS));
		optionPanelRight.setBackground(Color.WHITE);

		repaintPanel = new JPanel();
		ol = new OptionListener();
		repaintButton = new JButton("Neu Zeichnen");
		repaintButton.addActionListener(ol);
		repaintPanel.add(repaintButton);
		optionPanelRight.add(repaintPanel);

		checkBoxPanel = new JPanel(new GridLayout(3, 1, 5, 5));
		juliaCheckBox = new JCheckBox("JuliaMenge");
		juliaCheckBox.setToolTipText("Bei Auswahl wird die Julia Menge entsprechend der eingegebenen Koordinaten gezeichnet");
		juliaCheckBox.addActionListener(ol);
		iterationCheckBox = new JCheckBox("ZoomAnpassung");
		iterationCheckBox.setToolTipText("Die Iterationszahl wird beim zoomen automatisch erhöht.");
		iterationCheckBox.setSelected(true);
		iterationCheckBox.addActionListener(ol);
		complexPlaneCheckBox = new JCheckBox("GaußscheZahlenebene");
		complexPlaneCheckBox.setToolTipText("Gaußsche Zahlenebene beim nächsten Neu Zeichnen ein-/ausblenden (nur beim Standrad-Intervall möglich)");
		complexPlaneCheckBox.addActionListener(ol);
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
		colorModeChooser.addItemListener(ol);
		colorModePanel.setMaximumSize(new Dimension(500, 30));
		colorModePanel.add(colorModeChooser);
		colorButton = new JButton("Farbverlauf");
		colorButton.addActionListener(ol);
		colorButton.setEnabled(false);
		colorModePanel.add(colorButton);
		optionPanelRight.add(colorModePanel);

		exceptionTextArea = new JTextArea();
		exceptionTextArea.setEditable(false);
		optionPanelRight.add(exceptionTextArea);

		// Farb-Dialog
		fdl = new FarbDialogeListener();
		colorUpdateButton = new JButton("Farbverlauf aktualisieren");
		colorUpdateButton.addActionListener(fdl);
		colorCollectionSizeButton = new JButton("Anzahl Farben");
		colorCollectionSizeButton.addActionListener(fdl);
		colorDialog = new JDialog();
		colorDialog.setResizable(false);
		colorDialog.setLayout(new BorderLayout());
		colorCollectionPane = new JScrollPane();
		colorCollectionPane.setViewportView(setColorCollectionSize(fractals.getColorCollection().length, fdl));
		colorDialog.add(colorCollectionPane, BorderLayout.SOUTH);
		buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.setBackground(Color.LIGHT_GRAY);
		buttonPanel.add(colorCollectionSizeButton);
		buttonPanel.add(colorUpdateButton);
		colorDialog.add(buttonPanel, BorderLayout.CENTER);
		colorDialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		colorButton.addActionListener(fdl);

		content.add(optionPanelRight, BorderLayout.EAST);
		content.add(buttonLeiste, BorderLayout.NORTH);
		content.add(southPanel, BorderLayout.SOUTH);
		content.add(graphicsPane, BorderLayout.CENTER);
		pack();

	}

	public JPanel setColorCollectionSize(int size, FarbDialogeListener fdl)
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
			p.addMouseListener(fdl);
			panel.add(p);
			panels[i] = p;
		}
		return panel;
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
	
	private void setAktTextFieldText()
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

	private class MenuBarListener implements ActionListener
	{

		public void actionPerformed(ActionEvent e)
		{

			String cmd = e.getActionCommand();
			if (cmd.equals("Optionen"))
			{
				// macht das Optionen Panel sichtbar
				if (optionPanelRight.isVisible())
				{
					optionPanelRight.setVisible(false);
				} else
				{
					optionPanelRight.setVisible(true);
				}
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
						return f.getName().toLowerCase().endsWith(".png") || f.getName().toLowerCase().endsWith(".ser") || f.isDirectory();
					}
					@Override
					public String getDescription()
					{
						return "(*.png) (*.ser)";
					}
				});

				if (chooser.showSaveDialog(MFrame.this) == JFileChooser.APPROVE_OPTION)
				{
					String path = chooser.getSelectedFile().getPath();
					if (path != null)
					{
						fractals.store(new File(path));
					}
				}
			}
			if (cmd.equals("Hilfe"))
			{
				JOptionPane.showMessageDialog(MFrame.this, dateiLaden("Hilfe.txt"), "Hilfe", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	private class FraktalPanelListener implements MouseListener, MouseMotionListener
	{

		private int xPress, yPress, xOld, yOld, checker;

		public void mousePressed(MouseEvent me)
		{
			// temporäre Daten für Zoom-Funktion, Zoom-Quadrat
			if (SwingUtilities.isLeftMouseButton(me))
			{
				xPress = me.getX();
				yPress = me.getY();
				xOld = me.getX();
				yOld = me.getY();
			}
		}

		public void mouseReleased(MouseEvent me)
		{
			// Zoom-Funktion
			if (SwingUtilities.isLeftMouseButton(me))
			{
				if ((me.getX() > xPress) && (yPress < me.getY()))
				{
					if (me.getX() - xPress > me.getY() - yPress)
					{
						fractals.setKoordinates(fractals.getKoordinatesRe(xPress), fractals.getKoordinatesRe(me.getX()), fractals.getKoordinatesIm(yPress + (me.getX() - xPress)), fractals.getKoordinatesIm(yPress));
						if (iterationCheckBox.isSelected())
						{
							fractals.setIterationRange(-1);
						}

					} else if (me.getX() - xPress < me.getY() - yPress)
					{
						fractals.setKoordinates(fractals.getKoordinatesRe(xPress), fractals.getKoordinatesRe(xPress + (me.getY() - yPress)), fractals.getKoordinatesIm(me.getY()), fractals.getKoordinatesIm(yPress));
						if (iterationCheckBox.isSelected())
						{
							fractals.setIterationRange(-1);
						}
					}
					fractals.removeMouseListener(fpl);
					fractals.removeMouseMotionListener(fpl);
					repaintButton.setText("Abbrechen!");
					if (fractals.mandelbrotmengePainted)
						fractals.paintFractals(null, progressBar);
					else
						fractals.paintFractals(fractals.getParamC(), progressBar);
				}
				setAktTextFieldText();
			}
		}

		public void mouseClicked(MouseEvent me)
		{
			if (fractals.mandelbrotmengePainted)
			{
				if (SwingUtilities.isLeftMouseButton(me))
				{
					repaintButton.setText("Abbrechen!");
					fractals.removeMouseListener(fpl);
					fractals.removeMouseMotionListener(fpl);
					fractals.setKoordinates(-2.0, 2.0, -2.0, 2.0);
					fractals.setIterationRange(40);
					fractals.paintFractals(new Complex(fractals.getKoordinatesRe(me.getX()), fractals.getKoordinatesIm(me.getY())), progressBar);
					juliaCheckBox.setSelected(true);
				}
				if (SwingUtilities.isRightMouseButton(me))
				{
					repaintButton.setText("Abbrechen!");
					fractals.removeMouseListener(fpl);
					fractals.removeMouseMotionListener(fpl);
					fractals.setKoordinates(-2.0, 2.0, -2.0, 2.0);
					fractals.setIterationRange(40);
					fractals.paintFractals(null, progressBar);
					juliaCheckBox.setSelected(false);
				}
			} else
			{
				if (SwingUtilities.isLeftMouseButton(me))
				{
					repaintButton.setText("Abbrechen!");
					fractals.removeMouseListener(fpl);
					fractals.removeMouseMotionListener(fpl);
					fractals.setKoordinates(-2.0, 2.0, -2.0, 2.0);
					fractals.setIterationRange(40);
					fractals.paintFractals(null, progressBar);
					juliaCheckBox.setSelected(false);
				}
				if (SwingUtilities.isRightMouseButton(me))
				{
					repaintButton.setText("Abbrechen!");
					fractals.removeMouseListener(fpl);
					fractals.removeMouseMotionListener(fpl);
					fractals.setKoordinates(-2.0, 2.0, -2.0, 2.0);
					fractals.setIterationRange(40);
					fractals.paintFractals(fractals.getParamC(), progressBar);
					juliaCheckBox.setSelected(true);
				}
			}
			setAktTextFieldText();
		}

		public void mouseMoved(MouseEvent me)
		{
			// aktuelle Koordinaten des Cursers in den jeweiligen TextFeldern
			// anzeigen lassen
			if (!juliaCheckBox.isSelected())
			{
				reelField1.setText("" + fractals.getKoordinatesRe(me.getX()));
				imagField1.setText("" + fractals.getKoordinatesIm(me.getY()));
			}
		}

		public void mouseDragged(MouseEvent me)
		{
			if (SwingUtilities.isLeftMouseButton(me))
			{
				// Zoom-Quadrat bei gedrückter Maus
				// Anzeige nur im 4.Quadranten (wenn die Koordinate, die
				// gedrückt wird, den Ursprung darstellt)
				if ((me.getX() > xPress) && (yPress < me.getY()))
				{
					// Einteilung der Zeichenoperation in 2Fälle(um immer ein
					// Quadrat zu zeichnen und kein Rechteck)
					// 1Fall: x-Länge ist größer als die y-Länge des gezogenen
					// Rechtecks(mit der Maus wird mit hoher Wahrscheinlichkeit
					// ein Rechteck gezogen)->x-Länge=QuadratSeitenLänge
					if ((me.getX() - xPress) > (me.getY() - yPress))
					{
						// Falls ein Wechsel zwischen den Fällen stattfindet
						// muss die Zeichenoperation des 1Falls nochmal
						// ausgeführt werden um das Quadrat verschwinden zu
						// lassen, ansonsten gibt es Überlappungen
						if (checker == 2)
						{
							fractals.paintZoomRec(xPress, yPress, yOld - yPress);
							fractals.paintZoomRec(xPress, yPress, xOld - xPress);
						}
						fractals.paintZoomRec(xPress, yPress, xOld - xPress);
						xOld = me.getX();
						fractals.paintZoomRec(xPress, yPress, xOld - xPress);
						checker = 1;
					}
					// 2Fall: y-Länge > x-Länge des gezogenen
					// Rechtecks->y-Länge=QuadratSeitenLänge
					else
					{
						if ((me.getX() - xPress) < (me.getY() - yPress))
						{
							// Falls ein Wechsel zwischen den Fällen stattfindet
							// muss die Zeichenoperation des 1Falls nochmal
							// ausgeführt werden um das Quadrat verschwinden zu
							// lassen, ansonsten gibt es Überlappungen
							if (checker == 1)
							{
								fractals.paintZoomRec(xPress, yPress, xOld - xPress);
								fractals.paintZoomRec(xPress, yPress, yOld - yPress);
							}
							fractals.paintZoomRec(xPress, yPress, yOld - yPress);
							yOld = me.getY();
							fractals.paintZoomRec(xPress, yPress, yOld - yPress);
							checker = 2;
						}
					}

				}
			}
		}

		public void mouseEntered(MouseEvent me)
		{
		}

		public void mouseExited(MouseEvent me)
		{
		}
	}

	private class FarbDialogeListener implements MouseListener, ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg)
		{
			String cmd = arg.getActionCommand();
			if (cmd.equals("Anzahl Farben"))
			{
				String eingabe = JOptionPane.showInputDialog(MFrame.this, "Anzahl der Farben");
				if (eingabe != null)
				{
					try
					{
						colorCollectionPane.setViewportView(setColorCollectionSize(Integer.parseInt(eingabe), fdl));
					} catch (NumberFormatException ex)
					{
						exceptionTextArea.setText("\n\n Fehler bei der Eingabe\n der Farbanzahl.\n Bitte nur zahlen Eingeben!");
					}
				}
			}
			if (cmd.equals("Farbverlauf aktualisieren"))
			{
				Color[] c = new Color[panels.length];
				for (int i = 0; i < c.length; i++)
				{
					c[i] = panels[i].getBackground();
				}
				fractals.setColorCollection(c);
				colorDialog.setVisible(false);
			}
		}

		@Override
		public void mouseClicked(MouseEvent e)
		{
			Color background = new JColorChooser().showDialog(MFrame.this, "Farbauswahl", Color.CYAN);
			if (background != null)
			{
				e.getComponent().setBackground(background);
				((JComponent) e.getComponent()).setToolTipText("R " + background.getRed() + ", G " + background.getGreen() + ", B " + background.getBlue());
			}
		}

		public void mouseEntered(MouseEvent e)
		{
		}

		public void mouseExited(MouseEvent e)
		{
		}

		public void mousePressed(MouseEvent e)
		{
		}

		public void mouseReleased(MouseEvent e)
		{
		}

	}

	private class OptionListener implements ActionListener, ItemListener
	{
		public void actionPerformed(ActionEvent e)
		{

			String cmd = e.getActionCommand();
			if (cmd.equals("Neu Zeichnen"))
			{
				repaintButton.setText("Abbrechen!");
				fractals.removeMouseListener(fpl);
				fractals.removeMouseMotionListener(fpl);
				exceptionTextArea.setText("");
				try
				{
					fractals.setKoordinates(Double.parseDouble(reelField2.getText()), Double.parseDouble(reelField3.getText()), Double.parseDouble(imagField2.getText()), Double.parseDouble(imagField3.getText()));
					fractals.setIterationRange(Integer.parseInt(iterationField.getText()));
					fractals.setWidthHeight(Integer.parseInt(dimensionField.getText()));
					graphicsPane.setViewportView(buffPanel); // falls die Größe
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
					exceptionTextArea.setText("\n\n Fehler beim Einlesen!\n Mögliche Fehlerquelle:\n -Komma statt Punkt\n als Trennzeichen\n -Buchstaben in der Eingabe ");
				}
				if (juliaCheckBox.isSelected())
				{
					try
					{
						fractals.paintFractals(new Complex(Double.parseDouble(reelField1.getText()), Double.parseDouble(imagField1.getText())), progressBar);
					} catch (NumberFormatException ex)
					{
						exceptionTextArea.setText("\n\n Fehler beim Einlesen!\n Mögliche Fehlerquelle:\n -Komma statt Punkt\n als Trennzeichen\n -Buchstaben in der Eingabe ");
					}
				} else
					fractals.paintFractals(null, progressBar);

				if (complexPlaneCheckBox.isSelected())
				{
					fractals.paintKoordinateSystem();
				}
				setAktTextFieldText();
			}

			if (cmd.equals("Abbrechen!"))
			{
				fractals.stop();
				progressBar.setValue(0);
				progressBar.setString("Abgebrochen!");
			}
			if (cmd.equals("Farbverlauf"))
			{
				colorDialog.setSize(555, 200);
				colorDialog.setLocation(getWidth() / 2 - colorDialog.getWidth() / 2, getHeight() / 2 - colorDialog.getHeight() / 2);
				colorDialog.setVisible(true);
			}

		}

		public void itemStateChanged(ItemEvent ie)
		{
			JComboBox selectedItem = (JComboBox) ie.getSource();
			if (selectedItem.getSelectedItem().equals("Schwarz Weiß"))
			{
				fractals.setColorMode(Fractal.MODE_BLACK_WHITE);
			}
			if (selectedItem.getSelectedItem().equals("Schwarz Weiß Modulo"))
			{
				fractals.setColorMode(Fractal.MODE_BLACK_WHITE_MODULO);
			}
			if (selectedItem.getSelectedItem().equals("Farbabstufungen"))
			{
				colorButton.setEnabled(true);
				fractals.setColorMode(Fractal.MODE_COLOR);
			} else
				colorButton.setEnabled(false);

		}

	}

	private class FensterListener extends WindowAdapter implements ChangeListener
	{

		@Override
		public void windowClosing(WindowEvent e)
		{
			int i = withWindowClosingDialog 
						? JOptionPane.showConfirmDialog(MFrame.this, "Wollen Sie wirklich beenden?", "WindowClosing", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)
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
					if (fractals.getMouseListeners().length < 1)
					{
						fractals.addMouseListener(fpl);
					}
					if (fractals.getMouseMotionListeners().length < 1)
					{
						fractals.addMouseMotionListener(fpl);
					}
					repaintButton.setText("Neu Zeichnen");
				}
			}
		}
	}
}
