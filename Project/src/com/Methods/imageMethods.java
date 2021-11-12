package com.Methods;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class imageMethods {
    //Obtain a sprite from a path.
    public static BufferedImage getExternalImage(String spritePath) {
        try {
            File file = new File(spritePath);
            return ImageIO.read(file);
        } catch (NullPointerException | IOException e) {
            return null;
        }
    }

    //Code sourced from stack overflow users Suken Shah and Mr. Polywhirl - https://stackoverflow.com/questions/6714045/how-to-resize-jlabel-imageicon
    public static BufferedImage getScaledImage(BufferedImage srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }
    public static BufferedImage getScaledImage(String filePath, int size) {
        BufferedImage image = getExternalImage(filePath);
        try {
            return getScaledImage(image, (int) ((double) image.getWidth() / image.getHeight() * size), size);
        }
        catch (NullPointerException e) {
            return null;
        }
    }

    public static ImageIcon createIcon(String filePath, int size) {
        BufferedImage image = getExternalImage(filePath);
        try {
            return createIcon(filePath, size, (int) ((double) image.getHeight() / image.getWidth() * size));
        }
        catch (NullPointerException e) {
            return null;
        }
    }
    public static ImageIcon createIcon(String filePath, int w, int h) {
        return new ImageIcon(getScaledImage(getExternalImage(filePath), w, h));
    }

    //Code sourced from stack overflow users Sri Harsha Chilakapati and Dave Jarvis - https://stackoverflow.com/questions/13605248/java-converting-image-to-bufferedimage#13605411
    public static BufferedImage flipImage(BufferedImage image, int xs, int ys) {
        AffineTransform tx = AffineTransform.getScaleInstance(xs, ys);
        tx.translate(Math.min(0, xs) * image.getWidth(null), Math.min(0, ys) * image.getHeight(null));
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = op.filter(image, null);
        return image;
    }
}