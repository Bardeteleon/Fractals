package mandelbrot;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;


public class MController implements ActionListener, MouseMotionListener, MouseListener,ItemListener{

  private MView aktView;
  private int xPress, yPress,xOld,yOld, checker;
  private Complex c;
  
  public MController(MView v)
  {
    aktView = v;
  }

  public void actionPerformed(ActionEvent e) {
    
    String cmd = e.getActionCommand();
    if(cmd.equals("Optionen"))
    {
      //macht das Optionen Panel sichtbar
      if(aktView.optionPanelRight.isVisible())
      {
        aktView.optionPanelRight.setVisible(false);
      }
      else{
        aktView.optionPanelRight.setVisible(true);
      }
    }
    if(cmd.equals("Neu Zeichnen"))
    {
      aktView.exceptionTextArea.setText("");  
      try
      {
        aktView.fractals.setKoordinates(Double.parseDouble(aktView.reelField2.getText()), Double.parseDouble(aktView.reelField3.getText()), Double.parseDouble(aktView.imagField2.getText()), Double.parseDouble(aktView.imagField3.getText()));
        aktView.fractals.setIterationRange(Integer.parseInt(aktView.iterationField.getText())); 
        aktView.fractals.setWidthHeight(Integer.parseInt(aktView.dimensionField.getText()));
        aktView.graphicsPane.setViewportView(aktView.buffPanel); //falls die Größe des Fractals Objekts geändert wird muss sich das ScrollPane aktualisieren, was mit dem wiederholten setzen des Viewports erreicht wird 
      }
      catch(NumberFormatException ex){aktView.exceptionTextArea.setText("\n\n Fehler beim Einlesen!\n Mögliche Fehlerquelle:\n -Komma statt Punkt\n als Trennzeichen\n -Buchstaben in der Eingabe ");}  
      
      if(aktView.juliaCheckBox.isSelected())
      {   
        try
        {
          aktView.fractals.setKoordinates(Double.parseDouble(aktView.reelField2.getText()), Double.parseDouble(aktView.reelField3.getText()), Double.parseDouble(aktView.imagField2.getText()), Double.parseDouble(aktView.imagField3.getText()));
          int eingabe = Integer.parseInt(aktView.iterationField.getText());
          if(eingabe>0)aktView.fractals.setIterationRange(eingabe); 
          eingabe = Integer.parseInt(aktView.dimensionField.getText());
          if(eingabe>0)
          {
            aktView.fractals.setWidthHeight(eingabe);
            aktView.graphicsPane.getViewport().setView(aktView.buffPanel); 
          }
          c = new Complex(Double.parseDouble(aktView.reelField1.getText()),Double.parseDouble(aktView.imagField1.getText()));
          aktView.fractals.paintJuliaMenge(c);
        }
        catch(NumberFormatException ex){aktView.exceptionTextArea.setText("\n\n Fehler beim Einlesen!\n Mögliche Fehlerquelle:\n -Komma statt Punkt\n als Trennzeichen\n -Buchstaben in der Eingabe ");}
      }
      else aktView.fractals.paintMandelbrotmenge();
      
      if(aktView.complexPlaneCheckBox.isSelected())
      {
        aktView.fractals.paintKoordinateSystem();
      }
      aktView.fractals.repaint();       
      setAktTextFieldText();
    }
    if(cmd.equals("Farbverlauf"))
    {
      aktView.colorDialog.setSize(555, 200);
      aktView.colorDialog.setLocation( aktView.getWidth()/2-aktView.colorDialog.getWidth()/2,aktView.getHeight()/2-aktView.colorDialog.getHeight()/2);
      aktView.colorDialog.setVisible(true);
    }
    if(cmd.equals("Speichern"))
    {
      JFileChooser chooser = new JFileChooser();
      chooser.setFileHidingEnabled(true);
      if(chooser.showSaveDialog(aktView)==JFileChooser.APPROVE_OPTION)
      {
        aktView.fractals.store(chooser.getSelectedFile());
      }
    }
    if(cmd.equals("Hilfe"))
    {
      aktView.helpDialog.setSize(760, 290);
      aktView.helpDialog.setLocation( aktView.getWidth()/2-aktView.helpDialog.getWidth()/2,aktView.getHeight()/2-aktView.helpDialog.getHeight()/2);
      aktView.helpDialog.setVisible(true);
    }
    if(cmd.equals("Anzahl Farben"))
    {
      String eingabe = JOptionPane.showInputDialog(aktView, "Anzahl der Farben");
      if(eingabe!=null)
      {
        try
        {
          aktView.colorCollectionPane.setViewportView(aktView.setColorCollectionSize(Integer.parseInt(eingabe)));
        }catch(NumberFormatException ex){aktView.exceptionTextArea.setText("\n\n Fehler bei der Eingabe\n der Farbanzahl.\n Bitte nur zahlen Eingeben!");}
      }
    }
    if(cmd.equals("Farbverlauf aktualisieren"))
    {
      Color[] c = new Color[aktView.panels.length];
      for(int i = 0; i<c.length; i++)
      {
        c[i] = aktView.panels[i].getBackground();
      }
      aktView.fractals.setColorCollection(c);
      aktView.colorDialog.setVisible(false);  
    }
  }
  
  public void mouseClicked(MouseEvent me) {
    if(!aktView.colorDialog.isVisible())
    {
        if(aktView.fractals.mandelbrotmengePainted == true)
        {
          if(SwingUtilities.isLeftMouseButton(me))
          {
            aktView.fractals.setIterationRange(40);
            c = new Complex(aktView.fractals.getKoordinatesRe(me.getX()),aktView.fractals.getKoordinatesIm(me.getY()));
            aktView.fractals.setKoordinates(-2.0, 2.0, -2.0, 2.0);
            aktView.fractals.paintJuliaMenge(c);
            aktView.juliaCheckBox.setSelected(true);
          }
          if(SwingUtilities.isRightMouseButton(me))
          {
            aktView.fractals.setKoordinates(-2.0, 2.0, -2.0, 2.0);
            aktView.fractals.setIterationRange(40);
            aktView.fractals.paintMandelbrotmenge(); 
            aktView.juliaCheckBox.setSelected(false);
          }
        }
        else 
        {
          if(SwingUtilities.isLeftMouseButton(me))
          {
            aktView.fractals.setKoordinates(-2.0, 2.0, -2.0, 2.0);
            aktView.fractals.setIterationRange(40);
            aktView.fractals.paintMandelbrotmenge(); 
            aktView.juliaCheckBox.setSelected(false);
          }
          if(SwingUtilities.isRightMouseButton(me))
          {
            aktView.fractals.setKoordinates(-2.0, 2.0, -2.0, 2.0);
            aktView.fractals.setIterationRange(40);
            aktView.fractals.paintJuliaMenge(c); 
            aktView.juliaCheckBox.setSelected(false);
          }
      
        }
      aktView.fractals.repaint();
      setAktTextFieldText();
    }
    else
    {
      Color background = new JColorChooser().showDialog(aktView, "Farbauswahl", Color.CYAN);
      me.getComponent().setBackground(background);
      ((JComponent)me.getComponent()).setToolTipText("R "+background.getRed()+", G "+background.getGreen()+", B "+background.getBlue());
    }
  }
  
  public void mousePressed(MouseEvent me) {
    //temporäre Daten für Zoom-Funktion, Zoom-Quadrat
    if(SwingUtilities.isLeftMouseButton(me))
    {
      xPress = me.getX();
      yPress = me.getY();
      xOld = me.getX();
      yOld = me.getY();
    }
  }

  public void mouseReleased(MouseEvent me) 
  {
    if(!aktView.colorDialog.isVisible())
    {
      //Zoom-Funktion
      if((me.getX()>xPress)&&(yPress<me.getY()))
      {
        if(me.getX()-xPress>me.getY()-yPress)
        {
          aktView.fractals.setKoordinates(aktView.fractals.getKoordinatesRe(xPress), aktView.fractals.getKoordinatesRe(me.getX()), aktView.fractals.getKoordinatesIm(yPress+(me.getX()-xPress)), aktView.fractals.getKoordinatesIm(yPress));
          if(aktView.iterationCheckBox.isSelected())
          {
            aktView.fractals.setIterationRange(-1);
          }
          aktView.fractals.repaintFractal();
          
        }
        else if(me.getX()-xPress<me.getY()-yPress)
        {
          aktView.fractals.setKoordinates(aktView.fractals.getKoordinatesRe(xPress), aktView.fractals.getKoordinatesRe(xPress+(me.getY()-yPress)), aktView.fractals.getKoordinatesIm(me.getY()), aktView.fractals.getKoordinatesIm(yPress));
          if(aktView.iterationCheckBox.isSelected())
          {
            aktView.fractals.setIterationRange(-1);
          }
          aktView.fractals.repaintFractal();
        }
      }
      setAktTextFieldText();
    }
  }

  public void mouseMoved(MouseEvent me) {
    //aktuelle Koordinaten des Cursers in den jeweiligen TextFeldern anzeigen lassen
    if(!aktView.juliaCheckBox.isSelected())
    {
      aktView.reelField1.setText(""+aktView.fractals.getKoordinatesRe(me.getX()));
      aktView.imagField1.setText(""+aktView.fractals.getKoordinatesIm(me.getY()));
    }
  }
  

  public void mouseDragged(MouseEvent me)
  {
    if(!aktView.colorDialog.isVisible())
    {
      if(SwingUtilities.isLeftMouseButton(me))
      {
        //Zoom-Quadrat bei gedrückter Maus
        //Anzeige nur im 4.Quadranten (wenn die Koordinate, die gedrückt wird, den Ursprung darstellt)
        if((me.getX()>xPress)&&(yPress<me.getY()))
        {
          //Einteilung der Zeichenoperation in 2Fälle(um immer ein Quadrat zu zeichnen und kein Rechteck)
          //1Fall: x-Länge ist größer als die y-Länge des gezogenen Rechtecks(mit der Maus wird mit hoher Wahrscheinlichkeit ein Rechteck gezogen)->x-Länge=QuadratSeitenLänge 
          if((me.getX()-xPress)>(me.getY()-yPress))
          {
            //Falls ein Wechsel zwischen den Fällen stattfindet muss die Zeichenoperation des 1Falls nochmal ausgeführt werden um das Quadrat verschwinden zu lassen, ansonsten gibt es Überlappungen
            if(checker==2)
            {
              aktView.fractals.paintZoomRec(xPress, yPress, yOld-yPress);
              aktView.fractals.paintZoomRec(xPress, yPress, xOld-xPress);
            }
            aktView.fractals.paintZoomRec(xPress, yPress, xOld-xPress);
            xOld = me.getX();
            aktView.fractals.paintZoomRec(xPress, yPress, xOld-xPress);
            checker = 1;
          }
          //2Fall: y-Länge > x-Länge des gezogenen Rechtecks->y-Länge=QuadratSeitenLänge
          else
          {
            if((me.getX()-xPress)<(me.getY()-yPress))
            {
              //Falls ein Wechsel zwischen den Fällen stattfindet muss die Zeichenoperation des 1Falls nochmal ausgeführt werden um das Quadrat verschwinden zu lassen, ansonsten gibt es Überlappungen
              if(checker==1)
              {
                aktView.fractals.paintZoomRec(xPress, yPress, xOld-xPress);
                aktView.fractals.paintZoomRec(xPress, yPress, yOld-yPress);
              }
              aktView.fractals.paintZoomRec(xPress, yPress, yOld-yPress);
              yOld = me.getY();
              aktView.fractals.paintZoomRec(xPress, yPress, yOld-yPress);
              checker = 2;
            }
          }
        }
      }
    }
  }

  public void itemStateChanged(ItemEvent ie) {
    JComboBox selectedItem = (JComboBox) ie.getSource();
    if(selectedItem.getSelectedItem().equals("Schwarz Weiß"))
    {
      aktView.fractals.setColorMode(Fractal.MODE_BLACK_WHITE);
    }
    if(selectedItem.getSelectedItem().equals("Schwarz Weiß Modulo"))
    {
      aktView.fractals.setColorMode(Fractal.MODE_BLACK_WHITE_MODULO);
    }
    if(selectedItem.getSelectedItem().equals("Farbabstufungen"))
    {
      aktView.fractals.setColorMode(Fractal.MODE_COLOR);
    }
    
  }
  
  public void mouseEntered(MouseEvent me) {}
  public void mouseExited(MouseEvent me) {}
  
  private void setAktTextFieldText()
  {
    aktView.iterationField.setText(aktView.fractals.getValues()[4].toString());
    aktView.dimensionField.setText(aktView.fractals.getValues()[5].toString());
    if(c!=null)
    {
      aktView.imagField1.setText(""+c.getImag());
      aktView.reelField1.setText(""+c.getReal());
    }
    aktView.imagField2.setText(aktView.fractals.getValues()[2].toString());
    aktView.imagField2.setToolTipText(aktView.fractals.getValues()[2].toString());
    aktView.imagField3.setText(aktView.fractals.getValues()[3].toString());
    aktView.imagField3.setToolTipText(aktView.fractals.getValues()[3].toString());
    aktView.reelField2.setText(aktView.fractals.getValues()[0].toString());
    aktView.reelField2.setToolTipText(aktView.fractals.getValues()[0].toString());
    aktView.reelField3.setText(aktView.fractals.getValues()[1].toString());
    aktView.reelField3.setToolTipText(aktView.fractals.getValues()[1].toString());
  }
  
}
