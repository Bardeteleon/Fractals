package fractals.user_interface.desktop;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

public class ColorSelectionController implements MouseListener, ActionListener {
	private ColorSelectionView view;
	private Color[] selection;
	private ConfigurationView configurationView;

	public ColorSelectionController(ColorSelectionView view, Color[] initSelection, ConfigurationView configurationView) {
		this.view = view;
		this.selection = initSelection;
		this.configurationView = configurationView;
		this.view.updateColorPanels(this.selection, this);
	}

	public void makeInteractive() {
		view.addController(this);
	}

	public Color[] getSelection() {
		return selection;
	}

	@Override
	public void actionPerformed(ActionEvent arg) {
		String cmd = arg.getActionCommand();
		if (cmd.equals("Anzahl Farben")) {
			String eingabe = JOptionPane.showInputDialog(view, "Anzahl der Farben");
			if (eingabe != null) {
				try {
					int newSize = Integer.parseInt(eingabe);
					Color[] oldSelection = selection;
					selection = new Color[newSize];
					System.arraycopy(oldSelection, 0, selection, 0, Math.min(oldSelection.length, newSize));
					for (int i = oldSelection.length; i < newSize; i++) {
						selection[i] = Color.BLACK;
					}
					view.updateColorPanels(selection, this);
					view.pack();
				} catch (NumberFormatException ex) {
					configurationView.exceptionTextArea
							.setText("\n\n Fehler bei der Eingabe\n der Farbanzahl.\n Bitte nur zahlen Eingeben!");
				}
			}
		}
		if (cmd.equals("Farbverlauf aktualisieren")) {
			Color[] c = new Color[view.panels.length];
			for (int i = 0; i < c.length; i++) {
				c[i] = view.panels[i].getBackground();
			}
			selection = c;
			view.setVisible(false);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Color background = new JColorChooser().showDialog(view, "Farbauswahl", Color.CYAN);
		if (background != null) {
			e.getComponent().setBackground(background);
			((JComponent) e.getComponent())
					.setToolTipText("R " + background.getRed() + ", G " + background.getGreen() + ", B " + background.getBlue());
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

}