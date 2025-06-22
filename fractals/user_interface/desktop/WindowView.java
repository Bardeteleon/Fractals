package fractals.user_interface.desktop;

import java.awt.BorderLayout;
import javax.swing.JFrame;

import fractals.user_interface.desktop.MenuBarView;
import fractals.user_interface.desktop.StatusBarView;
import fractals.user_interface.desktop.ConfigurationView;
import fractals.user_interface.desktop.ColorSelectionView;
import fractals.user_interface.desktop.FractalView;
import fractals.user_interface.desktop.FractalViewScrollable;

public class WindowView extends JFrame
{
	public FractalView fractalView;
	public FractalViewScrollable fractalViewScrollable;
	public MenuBarView menuBarView;
	public StatusBarView statusBarView;
	public ConfigurationView configurationView;
	public ColorSelectionView colorSelectionView;

	public WindowView()
	{
		super("Fractal v2.0");

		statusBarView = new StatusBarView();
		fractalView = new FractalView(statusBarView);
		fractalViewScrollable = new FractalViewScrollable(fractalView);
		menuBarView = new MenuBarView();
		configurationView = new ConfigurationView(fractalView);
		colorSelectionView = new ColorSelectionView(this);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(configurationView, BorderLayout.EAST);
		getContentPane().add(menuBarView, BorderLayout.NORTH);
		getContentPane().add(statusBarView, BorderLayout.SOUTH);
		getContentPane().add(fractalViewScrollable, BorderLayout.CENTER);
		pack();

		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	public void setAktTextFieldText()
	{
		configurationView.iterationField.setText(fractalView.getIterationRange() + "");
		configurationView.dimensionField.setText(fractalView.getWidthHeight() + "");
		if (fractalView.getParamC() != null)
		{
			statusBarView.imagField1.setText("" + fractalView.getParamC().getImag());
			statusBarView.reelField1.setText("" + fractalView.getParamC().getReal());
		}
		configurationView.imagField2.setText(fractalView.getMinIm() + "");
		configurationView.imagField2.setToolTipText(fractalView.getMinIm() + "");
		configurationView.imagField3.setText(fractalView.getMaxIm() + "");
		configurationView.imagField3.setToolTipText(fractalView.getMaxIm() + "");
		configurationView.reelField2.setText(fractalView.getMinRe() + "");
		configurationView.reelField2.setToolTipText(fractalView.getMinRe() + "");
		configurationView.reelField3.setText(fractalView.getMaxRe() + "");
		configurationView.reelField3.setToolTipText(fractalView.getMaxRe() + "");
	}
}
