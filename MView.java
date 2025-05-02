package mandelbrot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;


public class MView extends JFrame {
  
  private MController myController = new MController(this);
  public JButton optionButton = new JButton("Optionen");
  public JButton repaintButton = new JButton("Neu Zeichnen");
  public JButton saveButton = new JButton("Speichern");
  public JButton helpButton = new JButton("Hilfe");
  public JButton colorButton = new JButton("Farbverlauf");
  public JButton colorUpdateButton = new JButton("Farbverlauf aktualisieren");
  public JButton colorCollectionSizeButton = new JButton("Anzahl Farben");
  public JPanel optionPanelRight = new JPanel();
  private JPanel koordinatesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
  private JPanel intervalPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
  private JPanel intervalPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
  private JPanel dimensionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
  private JPanel iterationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
  private JPanel colorModePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
  public JPanel buffPanel = new JPanel();
  public JTextField reelField1 = new JTextField(13);
  public JTextField imagField1 = new JTextField(13);
  public JTextField reelField2 = new JTextField(4);
  public JTextField imagField2 = new JTextField(4);
  public JTextField reelField3 = new JTextField(4);
  public JTextField imagField3 = new JTextField(4);
  public JTextField dimensionField = new JTextField(4);
  public JTextField iterationField = new JTextField(5);
  private JLabel koordinatesLabel = new JLabel("Koordinaten: Reel");
  private JLabel imagLabel = new JLabel("Imag");
  private JLabel intervalLabel = new JLabel("----------Intervall-------");
  private JLabel minLabel = new JLabel("Min: ");
  private JLabel maxLabel = new JLabel("Max: ");
  private JLabel dimensionLabel = new JLabel("Größe: ");
  private JLabel iterationLabel = new JLabel("Iterationstiefe: ");
  private JLabel colorModeLabel = new JLabel("Farb Modus");
  public JCheckBox juliaCheckBox = new JCheckBox("JuliaMenge");
  public JCheckBox iterationCheckBox = new JCheckBox("ZoomAnpassung");
  public JCheckBox complexPlaneCheckBox = new JCheckBox("GaußscheZahlenebene");
  private String[] comboBoxContent = {"Schwarz Weiß","Schwarz Weiß Modulo","Farbabstufungen"};
  public JComboBox colorModeChooser = new JComboBox(comboBoxContent);
  public FractalD fractals = new FractalD();
  public JScrollPane graphicsPane = new JScrollPane();
  public JScrollPane colorCollectionPane = new JScrollPane();
  public JDialog helpDialog= new JDialog();
  public JDialog colorDialog = new JDialog();
  private JTextArea helpTextArea = new JTextArea();
  public JTextArea exceptionTextArea = new JTextArea();
  public Component[] panels;
  
  public MView()
  {
    super("Fractal v1.0");
    Container content = this.getContentPane();
    content.setLayout(new BorderLayout());
    
    //ZeichenPane(Center)
    fractals.addMouseMotionListener(myController);
    fractals.addMouseListener(myController);
    buffPanel.add(fractals);
    graphicsPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    graphicsPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    graphicsPane.setViewportView(buffPanel);

    
    //KoordinatenPanel(South)
    koordinatesPanel.setBackground(Color.WHITE);
    koordinatesPanel.add(koordinatesLabel);
    koordinatesPanel.add(reelField1);
    koordinatesPanel.add(imagLabel);
    koordinatesPanel.add(imagField1);
    
    //ButtonLeiste(North)
    JToolBar buttonLeiste = new JToolBar();
    buttonLeiste.setBackground(Color.WHITE);
    buttonLeiste.setLayout(new FlowLayout(FlowLayout.CENTER));
    optionButton.addActionListener(myController);
    buttonLeiste.add(optionButton);
    helpButton.addActionListener(myController);
    buttonLeiste.add(helpButton);
    saveButton.addActionListener(myController);
    buttonLeiste.add(saveButton);
    
    //OptionPanelRight
    optionPanelRight.setVisible(false);
    optionPanelRight.setLayout(new BoxLayout(optionPanelRight,BoxLayout.Y_AXIS));
    optionPanelRight.setBackground(Color.WHITE);
    repaintButton.addActionListener(myController);
    optionPanelRight.add(repaintButton);
    juliaCheckBox.setToolTipText("Bei Auswahl wird die Julia Menge entsprechend der eingegebenen Koordinaten gezeichnet");
    juliaCheckBox.setBackground(Color.WHITE);
    juliaCheckBox.addActionListener(myController);
    optionPanelRight.add(juliaCheckBox);
    iterationPanel.setMaximumSize(new Dimension(500,25));
    iterationPanel.setBackground(Color.LIGHT_GRAY);
    iterationPanel.add(iterationLabel);
    iterationField.setText(fractals.getValues()[4].toString());
    iterationPanel.add(iterationField);
    optionPanelRight.add(iterationPanel);
    optionPanelRight.add(intervalLabel);
    intervalPanel1.setMaximumSize(new Dimension(500,25));
    intervalPanel1.setBackground(Color.WHITE);
    intervalPanel1.add(minLabel);
    reelField2.setText(fractals.getValues()[0].toString());
    intervalPanel1.add(reelField2);
    imagField2.setText(fractals.getValues()[2].toString());
    intervalPanel1.add(imagField2);
    optionPanelRight.add(intervalPanel1);
    intervalPanel2.setMaximumSize(new Dimension(500,50));
    intervalPanel2.setBackground(Color.WHITE);
    intervalPanel2.add(maxLabel);
    reelField3.setText(fractals.getValues()[1].toString());
    intervalPanel2.add(reelField3);
    imagField3.setText(fractals.getValues()[3].toString());
    intervalPanel2.add(imagField3);
    optionPanelRight.add(intervalPanel2);
    dimensionPanel.setMaximumSize(new Dimension(500,25));
    dimensionPanel.setToolTipText("Größe des zu zeichnenden Bereichs in Pixel(quadratische Form)");
    dimensionPanel.setBackground(Color.LIGHT_GRAY);
    dimensionPanel.add(dimensionLabel);
    dimensionField.setText(fractals.getValues()[5].toString());
    dimensionPanel.add(dimensionField);
    optionPanelRight.add(dimensionPanel);
    iterationCheckBox.setToolTipText("Die Iterationszahl wird beim zoomen automatisch erhöht.");
    iterationCheckBox.setSelected(true);
    iterationCheckBox.setBackground(Color.WHITE);
    iterationCheckBox.addActionListener(myController);
    optionPanelRight.add(iterationCheckBox);
    complexPlaneCheckBox.setToolTipText("Gaußsche Zahlenebene beim nächsten Neu Zeichnen ein-/ausblenden (nur beim Standrad-Intervall möglich)");
    complexPlaneCheckBox.setBackground(Color.WHITE);
    complexPlaneCheckBox.addActionListener(myController);
    optionPanelRight.add(complexPlaneCheckBox);
    optionPanelRight.add(colorModeLabel);
    colorModeChooser.setMaximumSize(new Dimension(150, 18));
    colorModeChooser.addItemListener(myController);
    colorModePanel.setBackground(Color.LIGHT_GRAY);
    colorModePanel.setMaximumSize(new Dimension(500,30));
    colorModePanel.add(colorModeChooser);
    optionPanelRight.add(colorModePanel);
    colorButton.addActionListener(myController);
    optionPanelRight.add(colorButton);
    exceptionTextArea.setEditable(false);
    optionPanelRight.add(exceptionTextArea);
    
    //Hilfe-Dialog
    helpButton.addActionListener(myController);
    helpTextArea.setText(dateiLaden("Help.txt"));
    JScrollPane pane = new JScrollPane();
    pane.setViewportView(helpTextArea);
    helpDialog.add(pane);
    helpDialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    
    //Farb-Dialog 
    colorUpdateButton.addActionListener(myController);
    colorCollectionSizeButton.addActionListener(myController);
    colorDialog.setResizable(false);
    colorDialog.setLayout(new BorderLayout());
    colorCollectionPane.setViewportView(setColorCollectionSize(((Color[])fractals.getValues()[6]).length));
    colorDialog.add(colorCollectionPane, BorderLayout.SOUTH);
    JPanel buttonPanel = new JPanel(new FlowLayout());
    buttonPanel.setBackground(Color.LIGHT_GRAY);
    buttonPanel.add(colorCollectionSizeButton);
    buttonPanel.add(colorUpdateButton);
    colorDialog.add(buttonPanel, BorderLayout.CENTER);
    colorDialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    colorButton.addActionListener(myController);
    
    
    content.add(optionPanelRight,BorderLayout.EAST);
    content.add(buttonLeiste,BorderLayout.NORTH);
    content.add(koordinatesPanel,BorderLayout.SOUTH);
    content.add(graphicsPane,BorderLayout.CENTER);
    this.pack();  
  }
  public JPanel setColorCollectionSize(int size)
  {
    JPanel panel = new JPanel(new FlowLayout());
    panel.setBackground(Color.LIGHT_GRAY);
    panels = new Component[size];
    for(int i = 0; i<size; i++)
    {
      JPanel p = new JPanel();
      p.setPreferredSize(new Dimension(75,75));
      if(((Color[])fractals.getValues()[6]).length>i)
      {
        Color background = ((Color[])fractals.getValues()[6])[i];
        p.setBackground(background);
        p.setToolTipText("R "+background.getRed()+", G "+background.getGreen()+", B "+background.getBlue());
      }
      p.addMouseListener(myController);
      panel.add(p);
      panels[i] = p;
    }
    return panel;
  }
  
	private String dateiLaden(String name)
	{	
		InputStream stream = this.getClass().getResourceAsStream(name);
		char[] data = null;
		if(stream!= null)
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
			}
		}
		return new String(data);
	}
}
