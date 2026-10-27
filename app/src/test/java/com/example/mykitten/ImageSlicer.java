package com.example.mykitten;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ImageSlicer {
    public static void main(String[] args) {
        try {
            File imgFile = new File("app/src/main/res/drawable/image_28795b23__1_.jpg");
            if (!imgFile.exists()) {
                System.out.println("Image not found: " + imgFile.getAbsolutePath());
                return;
            }
            BufferedImage img = ImageIO.read(imgFile);
            int width = img.getWidth();
            int height = img.getHeight();
            int cols = 3;
            int rows = 3;
            int cellW = width / cols;
            int cellH = height / rows;

            int[][] frames = {
                {0, 0}, {0, 1}, {0, 2}, {1, 2}, {2, 0}
            };
            String[] filenames = {
                "cat_idle_1.png", "cat_idle_2.png", "cat_idle_3.png", "cat_idle_4.png", "cat_idle_5.png"
            };

            File outputDir = new File("app/src/main/res/drawable");

            for (int i = 0; i < frames.length; i++) {
                int r = frames[i][0];
                int c = frames[i][1];
                BufferedImage subImage = img.getSubimage(c * cellW, r * cellH, cellW, (int)(cellH * 0.85));
                BufferedImage transparentImg = new BufferedImage(subImage.getWidth(), subImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

                for (int x = 0; x < subImage.getWidth(); x++) {
                    for (int y = 0; y < subImage.getHeight(); y++) {
                        int rgb = subImage.getRGB(x, y);
                        Color color = new Color(rgb);
                        if (color.getRed() > 240 && color.getGreen() > 240 && color.getBlue() > 240) {
                            transparentImg.setRGB(x, y, 0x00000000);
                        } else {
                            transparentImg.setRGB(x, y, rgb);
                        }
                    }
                }

                File outFile = new File(outputDir, filenames[i]);
                ImageIO.write(transparentImg, "PNG", outFile);
                System.out.println("Saved: " + outFile.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
