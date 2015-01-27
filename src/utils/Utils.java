package utils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Utilities for project
 */
public class Utils {
    /**
     * Makes centerd frame in screen
     *
     * @param frame
     */
    public static void centreWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

    /**
     * Setups custom LAF, if exists
     */
    public static void setupLAF() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
        } catch (Exception ignore) {
            //NOP
        }
    }

    /**
     * Setts UI Fonts
     *
     * @param f
     */
    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value != null && value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put(key, f);
        }
    }

    /**
     * Scales image in perspective
     *
     * @param image  image IN
     * @param width  new width
     * @param height new height
     * @return scaled image
     */
    public static BufferedImage getScaledImage(BufferedImage image, int width, int height) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        double scaleInx = 0;
        if (imageWidth > imageHeight) {
            scaleInx = (double) width / (double) imageWidth;
        } else {
            scaleInx = (double) height / (double) imageHeight;
        }
        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleInx, scaleInx);
        AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

        return bilinearScaleOp.filter(image, new BufferedImage((int) (scaleInx * (double) imageWidth), (int) (scaleInx * (double) imageHeight), image.getType()));
    }

    /**
     * Rounds double
     *
     * @param value  input value
     * @param places places after point
     * @return
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
