package fractals.user_interface.desktop;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import fractals.user_interface.desktop.WindowView;

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
				WindowView view = new WindowView();
			}
		});

	}
}
