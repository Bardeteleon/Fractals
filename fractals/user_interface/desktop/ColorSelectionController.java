package fractals.user_interface.desktop;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import java.util.Arrays;

public class ColorSelectionController implements MouseListener, ActionListener {
	private ColorSelectionView view;
	private Color[] selection;
	private ConfigurationView configurationView;

	public ColorSelectionController(ColorSelectionView view, Color[] initSelection, ConfigurationView configurationView) {
		this.view = view;
		this.selection = initSelection;
		this.configurationView = configurationView;
		this.view.updateColorPanels(this.selection, this);
		makeInteractive();
	}

	private void makeInteractive() {
		view.addController(this);
	}

	public Color[] getSelection() {
		return selection;
	}

	@Override
	public void actionPerformed(ActionEvent arg) {
		String cmd = arg.getActionCommand();
		if (cmd.equals("Anzahl Farben")) {
			handleChangeColorCount();
		}
		if (cmd.equals("Farbverlauf aktualisieren")) {
			handleUpdateColorGradient();
		}
	}

	private void handleChangeColorCount() {
		String input = JOptionPane.showInputDialog(view, "Anzahl der Farben");
		if (input == null) {
			return;
		}

		try {
			int newSize = Integer.parseInt(input);
			resizeSelection(newSize);
			view.updateColorPanels(selection, this);
			view.pack();
		} catch (NumberFormatException ex) {
			showInputError();
		}
	}

	private void resizeSelection(int newSize) {
		int oldSize = selection.length;
		selection = Arrays.copyOf(selection, newSize);

		if (newSize > oldSize) {
			Arrays.fill(selection, oldSize, newSize, Color.BLACK);
		}
	}

	private void showInputError() {
		configurationView.exceptionTextArea
				.setText("\n\n Fehler bei der Eingabe\n der Farbanzahl.\n Bitte nur zahlen Eingeben!");
	}

	private void handleUpdateColorGradient() {
		selection = view.getColorsFromPanels();
		view.setVisible(false);
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