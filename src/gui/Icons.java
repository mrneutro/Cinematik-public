package gui;

import logger.InLogger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Icon holder
 */
public class Icons {
    public static final String DEFAULT_ICON = "/resources/";
    public static BufferedImage appIcon = loadIcon(DEFAULT_ICON + "icon.png");
    public static BufferedImage maestroLogo = loadIcon(DEFAULT_ICON + "maestro.png");
    public static BufferedImage mastercardLogo = loadIcon(DEFAULT_ICON + "mastercard.png");
    public static BufferedImage amexLogo = loadIcon(DEFAULT_ICON + "amex.png");
    public static BufferedImage visaLogo = loadIcon(DEFAULT_ICON + "visa.png");
    public static BufferedImage films = loadIcon(DEFAULT_ICON + "films.png");
    public static BufferedImage halls = loadIcon(DEFAULT_ICON + "halls.png");
    public static BufferedImage addIcon = loadIcon(DEFAULT_ICON + "add.png");
    public static BufferedImage halls_xl = loadIcon(DEFAULT_ICON + "halls-xl.png");
    public static BufferedImage programm = loadIcon(DEFAULT_ICON + "programm.png");
    public static BufferedImage calendar = loadIcon(DEFAULT_ICON + "calendar.png");
    public static BufferedImage user = loadIcon(DEFAULT_ICON + "user.png");
    public static BufferedImage basket = loadIcon(DEFAULT_ICON + "basket.png");
    public static BufferedImage chart = loadIcon(DEFAULT_ICON + "chart.png");
    public static BufferedImage basketAdd = loadIcon(DEFAULT_ICON + "basket-add.png");
    public static BufferedImage basketEdit = loadIcon(DEFAULT_ICON + "basket-edit.png");
    public static BufferedImage save = loadIcon(DEFAULT_ICON + "save.png");
    public static BufferedImage cancel = loadIcon(DEFAULT_ICON + "cancel.png");
    public static BufferedImage edit = loadIcon(DEFAULT_ICON + "edit.png");
    public static BufferedImage door_in = loadIcon(DEFAULT_ICON + "door-in.png");
    public static BufferedImage register = loadIcon(DEFAULT_ICON + "register.png");

    /**
     * Loads image into memorys
     *
     * @param resource resource path
     * @return BufferedImage
     */
    private static BufferedImage loadIcon(String resource) {
        BufferedImage result = null;
        try {
            result = ImageIO.read(Icons.class.getResource(resource));
        } catch (IOException ex) {
            InLogger.exception("Loading icons exception on: " + ex.getMessage());
        }
        return result;
    }
}
