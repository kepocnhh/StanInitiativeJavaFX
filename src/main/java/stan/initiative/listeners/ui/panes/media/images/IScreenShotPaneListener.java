package stan.initiative.listeners.ui.panes.media.images;

import java.awt.image.BufferedImage;

public interface IScreenShotPaneListener
{
    void folder(BufferedImage image);
    void telegram(BufferedImage image);
    void exit();
    BufferedImage grabScreen(int x, int y, int w, int h);
}