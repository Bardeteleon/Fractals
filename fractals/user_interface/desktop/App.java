package fractals.user_interface.desktop;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import fractals.user_interface.desktop.WindowView;
import fractals.user_interface.desktop.ConfigurationController;
import fractals.user_interface.desktop.FractalPresenter;
import fractals.user_interface.desktop.MenuBarController;
import fractals.user_interface.desktop.WindowController;
import fractals.user_interface.desktop.ColorSelectionController;

public class App
{
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel"); // com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel
				} catch (ClassNotFoundException e)
				{
					e.printStackTrace();
				} catch (InstantiationException e)
				{
					e.printStackTrace();
				} catch (IllegalAccessException e)
				{
					e.printStackTrace();
				} catch (UnsupportedLookAndFeelException e)
				{
					e.printStackTrace();
				}
				makeApp();
			}
		});
	}

	public static void makeApp()
	{
		WindowView windowView = new WindowView();
		
		FractalPresenter fractalPresenter = new FractalPresenter(windowView.fractalView, windowView);
		WindowController windowController = new WindowController(windowView, fractalPresenter);
		MenuBarController menuBarController = new MenuBarController(windowView);
		ColorSelectionController colorSelectionController = new ColorSelectionController(windowView.colorSelectionView, windowView.fractalView.getColorCollection(), windowView.configurationView);
		ConfigurationController configurationController = new ConfigurationController(windowView.fractalView, windowView, fractalPresenter, colorSelectionController);

		windowView.setVisible(true);
	}
}
