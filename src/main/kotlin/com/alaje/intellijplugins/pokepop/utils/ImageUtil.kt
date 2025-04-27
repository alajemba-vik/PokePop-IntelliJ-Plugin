package com.alaje.intellijplugins.pokepop.utils

import com.alaje.intellijplugins.pokepop.ImageDisplayService
import java.net.URL
import javax.swing.ImageIcon

object ImageUtil {
    fun createImageIcon(
        path: String,
        description: String?
    ): ImageIcon? {
            val imgURL: URL? = ImageDisplayService::class.java.getResource(path)

        return if (imgURL != null) {
            ImageIcon(imgURL, description)
        } else {
            System.err.println("Couldn't find file: $path")
            null
        }
    }

    fun scaleImageIcon(imageIcon: ImageIcon): ImageIcon {
        var currentImageWidth = imageIcon.iconWidth
        var currentImageHeight = imageIcon.iconHeight

        if (currentImageWidth > DEFAULT_IMAGE_SIZE || currentImageHeight > DEFAULT_IMAGE_SIZE) {
            // maintain aspect ratio
            val ratio = currentImageWidth.toFloat() / currentImageHeight.toFloat()

            if (currentImageWidth > currentImageHeight) {
                currentImageWidth = DEFAULT_IMAGE_SIZE
                currentImageHeight = (DEFAULT_IMAGE_SIZE / ratio).toInt()
            } else {
                currentImageHeight = DEFAULT_IMAGE_SIZE
                currentImageWidth = (DEFAULT_IMAGE_SIZE * ratio).toInt()
            }

        }

        val scaledImage = ImageIcon(
            imageIcon.image?.getScaledInstance(currentImageWidth, currentImageHeight, 0)
        )
        return scaledImage
    }
}

const val DEFAULT_IMAGE_SIZE = 150