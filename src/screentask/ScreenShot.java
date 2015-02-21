/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screentask;

import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Ahmad
 */
public class ScreenShot {

    private static final ScreenShotManager smanager = ScreenShotManager.getInstance();
    private static Robot robot;

    public static void takeScreenshot(Boolean drawCursor) throws AWTException, IOException, Exception {
        if (robot == null)  robot = new Robot();
        /**
         * Create a screen capture of the active window and then create a
         * buffered image to be saved to disk.
         */
        Rectangle screen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage screenCapture = robot.createScreenCapture(screen);

        if (drawCursor) {
            drawMousePointer(screenCapture);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(screenCapture, "jpg", baos);
        byte[] bytes = baos.toByteArray();
        smanager.updateImage(bytes);
    }

    private static void drawMousePointer(BufferedImage screenCapture) throws IOException, URISyntaxException {
        Point p = MouseInfo.getPointerInfo().getLocation();
        int x = p.x;
        int y = p.y;

        Resources resource = new Resources();
        String startDir = resource.appStartUpPath();

        Image cursor = null;

        cursor = ImageIO.read(new File(startDir + "/WebServer/cursor_arrow.png"));

        Graphics graphics2D = screenCapture.createGraphics();
        graphics2D.drawImage(cursor, x, y, 25, 25, null);
    }

    public static void PreviewImage(JLabel lblImage) throws URISyntaxException, MalformedURLException {

        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(smanager.getImage()));
            int width = lblImage.getWidth();
            int height = lblImage.getHeight();

            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
            ImageIcon icon = new ImageIcon(scaledImage);
            lblImage.setIcon(icon);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
