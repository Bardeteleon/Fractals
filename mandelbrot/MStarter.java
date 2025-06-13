package mandelbrot;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MStarter
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedLookAndFeelException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				MFrame wnd = new MFrame();
				wnd.setExtendedState(JFrame.MAXIMIZED_BOTH);
				wnd.setLocationRelativeTo(null);
				wnd.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				wnd.setVisible(true);
			}
		});

	}
}
/*
 * layout verbessern Hilfe Dialog Text laden aus jar file...
 */
