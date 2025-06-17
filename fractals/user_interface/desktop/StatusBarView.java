package fractals.user_interface.desktop;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class StatusBarView extends JPanel {

	public JTextField reelField1, imagField1;
	public JProgressBar progressBar;

	public StatusBarView() {
		setLayout(new GridLayout(1, 2));

		JPanel koordinatesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel koordinatesLabel = new JLabel("Koordinaten: Reel");
		koordinatesPanel.add(koordinatesLabel);
		reelField1 = new JTextField(13);
		koordinatesPanel.add(reelField1);
		JLabel imagLabel = new JLabel("Imag");
		koordinatesPanel.add(imagLabel);
		imagField1 = new JTextField(13);
		koordinatesPanel.add(imagField1);

		JPanel ladeBalkenPanel = new JPanel();
		progressBar = new JProgressBar(SwingConstants.HORIZONTAL, 0, 100);
		progressBar.setStringPainted(true);
		progressBar.setString("Fertig!");
		progressBar.setPreferredSize(new Dimension(300, 25));
		ladeBalkenPanel.add(progressBar);

		add(koordinatesPanel);
		add(ladeBalkenPanel);
	}
}
