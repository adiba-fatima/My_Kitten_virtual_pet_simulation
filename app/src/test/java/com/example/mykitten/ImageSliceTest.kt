package com.example.mykitten

import org.junit.Test
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class ImageSliceTest {

    @Test
    fun sliceCatSpriteSheet() {
        val imgPath = File("src/main/res/drawable/image_28795b23__1_.jpg")
        if (!imgPath.exists()) {
            println("Image not found at ${imgPath.absolutePath}")
            return
        }

        val img = ImageIO.read(imgPath)
        val width = img.width
        val height = img.height

        val cols = 3
        val rows = 3
        val cellW = width / cols
        val cellH = height / rows

        val frames = listOf(
            Triple(0, 0, "cat_idle_1.png"),
            Triple(0, 1, "cat_idle_2.png"),
            Triple(0, 2, "cat_idle_3.png"),
            Triple(1, 2, "cat_idle_4.png"),
            Triple(2, 0, "cat_idle_5.png")
        )

        val outputDir = File("src/main/res/drawable")

        for ((r, c, filename) in frames) {
            val subImage = img.getSubimage(c * cellW, r * cellH, cellW, (cellH * 0.85).toInt())
            val transparentImg = BufferedImage(subImage.width, subImage.height, BufferedImage.TYPE_INT_ARGB)

            for (x in 0 until subImage.width) {
                for (y in 0 until subImage.height) {
                    val rgb = subImage.getRGB(x, y)
                    val color = Color(rgb)
                    if (color.red > 240 && color.green > 240 && color.blue > 240) {
                        transparentImg.setRGB(x, y, 0x00000000)
                    } else {
                        transparentImg.setRGB(x, y, rgb)
                    }
                }
            }

            val outFile = File(outputDir, filename)
            ImageIO.write(transparentImg, "PNG", outFile)
            println("Successfully sliced and saved ${outFile.absolutePath}")
        }
    }
}
