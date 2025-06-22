package fractals.user_interface.desktop;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class FractalViewScrollable extends JScrollPane
{
    private JPanel panelCenteringView;

    public FractalViewScrollable(FractalView view)
    {
        panelCenteringView = new JPanel();
        panelCenteringView.add(view);
        setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setViewportView(panelCenteringView);
    }

    public void update()
    {
        setViewportView(panelCenteringView);
    }
}