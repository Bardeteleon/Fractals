import java.awt.Toolkit;
import javax.swing.JFrame;

public class MStarter {
  
  public static void main(String[] args) {
    MView wnd = new MView();
    wnd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);      
    wnd.setExtendedState(JFrame.MAXIMIZED_BOTH);
    wnd.setLocation((int) (Toolkit.getDefaultToolkit().getScreenSize()
        .getWidth() / 2 - wnd.getWidth() / 2), (int) (Toolkit
        .getDefaultToolkit().getScreenSize().getHeight() / 2 - wnd
        .getHeight() / 2));
    wnd.setVisible(true);
  }
}
