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
		fractalView = new FractalView();
		fractalViewScrollable = new FractalViewScrollable(fractalView);
		menuBarView = new MenuBarView();
		configurationView = new ConfigurationView();
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
	
	public void setAktTextFieldText(FractalPresenter fractalPresenter)
	{
		configurationView.iterationField.setText(fractalPresenter.getIterationRange() + "");
		configurationView.dimensionField.setText(fractalPresenter.getWidthHeight() + "");
		if (fractalPresenter.getParamC() != null)
		{
			statusBarView.imagField1.setText("" + fractalPresenter.getParamC().getImag());
			statusBarView.reelField1.setText("" + fractalPresenter.getParamC().getReal());
		}
		configurationView.imagField2.setText(fractalPresenter.getMinIm() + "");
		configurationView.imagField2.setToolTipText(fractalPresenter.getMinIm() + "");
		configurationView.imagField3.setText(fractalPresenter.getMaxIm() + "");
		configurationView.imagField3.setToolTipText(fractalPresenter.getMaxIm() + "");
		configurationView.reelField2.setText(fractalPresenter.getMinRe() + "");
		configurationView.reelField2.setToolTipText(fractalPresenter.getMinRe() + "");
		configurationView.reelField3.setText(fractalPresenter.getMaxRe() + "");
		configurationView.reelField3.setToolTipText(fractalPresenter.getMaxRe() + "");
	}
}
