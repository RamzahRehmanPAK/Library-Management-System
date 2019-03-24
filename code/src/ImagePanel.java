/**
 * Created by Ramzah Rehman on 10/23/2016.
 */
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

    Image image;

    public void setBackground(Image image) {
        this.image = image;
    }

    @Override
    public void paintComponent(Graphics G) {
        super.paintComponent(G);
        G.drawImage(image, 0, 0, this.getWidth(),this.getHeight(), null);

    }

}