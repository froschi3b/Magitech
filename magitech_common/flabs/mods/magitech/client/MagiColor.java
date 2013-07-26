package flabs.mods.magitech.client;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MagiColor {
    private static int rbg[];
    
    static {
        try {
            BufferedImage data = ImageIO.read(MagiColor.class.getResourceAsStream("/assets/magitech/textures/magicolors.png"));
            rbg = new int[data.getWidth()];
            for (int i = 0; i < rbg.length; i++) {
                rbg[i]=data.getRGB(i, 0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static int getColor(int percent){
        if(percent < 0 || percent >100){
            throw new IllegalArgumentException("The Percentage must be between 0 and 100 (inclusive)");
        }
        return rbg[percent];
    }
}
