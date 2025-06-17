package fractals.user_interface.desktop;

import fractals.core.Complex;
import mandelbrot.Fractal;
import mandelbrot.MFrame;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.SwingUtilities;

public class FractalPresenter implements MouseListener, MouseMotionListener
{
    private int xPress, yPress, xOld, yOld, checker;
    private mandelbrot.Fractal fractals;
    private MFrame frame;

    public FractalPresenter(mandelbrot.Fractal fractalView, MFrame frame)
    {
        this.fractals = fractalView;
        this.frame = frame;
    }

    @Override
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

    @Override
    public void mouseReleased(MouseEvent me)
    {
        // Zoom-Funktion
        if (SwingUtilities.isLeftMouseButton(me))
        {
            if ((me.getX() > xPress) && (yPress < me.getY()))
            {
                int yMin, xMax;
                if (me.getX() - xPress > me.getY() - yPress)
                {
                    yMin = yPress + (me.getX() - xPress);
                    xMax = me.getX();
                } 
                else
                {
                    yMin = me.getY();
                    xMax = xPress + (me.getY() - yPress);
                }
                fractals.setMinCoordinate(fractals.getCoordinate(xPress, yMin));
                fractals.setMaxCoordinate(fractals.getCoordinate(xMax, yPress));
                if (frame.iterationCheckBox.isSelected())
                {
                    fractals.setIterationRange(-1);
                }
                setInteractive(false);
                frame.repaintButton.setText("Abbrechen!");
                if (fractals.mandelbrotmengePainted)
                    fractals.paintFractals(null, frame.statusBarView.progressBar);
                else
                    fractals.paintFractals(fractals.getParamC(), frame.statusBarView.progressBar);
            }
            frame.setAktTextFieldText();
        }
    }

    @Override
    public void mouseClicked(MouseEvent me)
    {
        if (fractals.mandelbrotmengePainted)
        {
            if (SwingUtilities.isLeftMouseButton(me))
            {
                frame.repaintButton.setText("Abbrechen!");
                setInteractive(false);
                fractals.setMinCoordinate(new Complex(-2.0, -2.0));
                fractals.setMaxCoordinate(new Complex(2.0, 2.0));
                fractals.setIterationRange(40);
                fractals.paintFractals(fractals.getCoordinate(me.getX(), me.getY()), frame.statusBarView.progressBar);
                frame.juliaCheckBox.setSelected(true);
            }
            if (SwingUtilities.isRightMouseButton(me))
            {
                frame.repaintButton.setText("Abbrechen!");
                setInteractive(false);
                fractals.setMinCoordinate(new Complex(-2.0, -2.0));
                fractals.setMaxCoordinate(new Complex(2.0, 2.0));
                fractals.setIterationRange(40);
                fractals.paintFractals(null, frame.statusBarView.progressBar);
                frame.juliaCheckBox.setSelected(false);
            }
        } else
        {
            if (SwingUtilities.isLeftMouseButton(me))
            {
                frame.repaintButton.setText("Abbrechen!");
                setInteractive(false);
                fractals.setMinCoordinate(new Complex(-2.0, -2.0));
                fractals.setMaxCoordinate(new Complex(2.0, 2.0));
                fractals.setIterationRange(40);
                fractals.paintFractals(null, frame.statusBarView.progressBar);
                frame.juliaCheckBox.setSelected(false);
            }
            if (SwingUtilities.isRightMouseButton(me))
            {
                frame.repaintButton.setText("Abbrechen!");
                setInteractive(false);
                fractals.setMinCoordinate(new Complex(-2.0, -2.0));
                fractals.setMaxCoordinate(new Complex(2.0, 2.0));
                fractals.setIterationRange(40);
                fractals.paintFractals(fractals.getParamC(), frame.statusBarView.progressBar);
                frame.juliaCheckBox.setSelected(true);
            }
        }
        frame.setAktTextFieldText();
    }

    @Override
    public void mouseMoved(MouseEvent me)
    {
        // aktuelle Koordinaten des Cursers in den jeweiligen TextFeldern
        // anzeigen lassen
        if (!frame.juliaCheckBox.isSelected())
        {
            final Complex coordinate = fractals.getCoordinate(me.getX(), me.getY());
            frame.statusBarView.reelField1.setText("" + coordinate.getReal());
            frame.statusBarView.imagField1.setText("" + coordinate.getImag());
        }
    }

    @Override
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

    @Override
    public void mouseEntered(MouseEvent me)
    {
    }

    @Override
    public void mouseExited(MouseEvent me)
    {
    }

    public void setInteractive(boolean interactive)
    {
        if (interactive)
        {
            if (fractals.getMouseListeners().length < 1)
            {
                fractals.addMouseListener(this);
            }
            if (fractals.getMouseMotionListeners().length < 1)
            {
                fractals.addMouseMotionListener(this);
            }
        } else
        {
            fractals.removeMouseListener(this);
            fractals.removeMouseMotionListener(this);
        }
    }
}