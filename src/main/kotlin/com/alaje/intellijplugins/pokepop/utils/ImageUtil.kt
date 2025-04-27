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

    fun scaleImageIcon(imageIcon: ImageIcon, imageSizeInPx: Int): ImageIcon {
        var currentImageWidth = imageIcon.iconWidth
        var currentImageHeight = imageIcon.iconHeight

        if (currentImageWidth > imageSizeInPx || currentImageHeight > imageSizeInPx) {
            // maintain aspect ratio
            val ratio = currentImageWidth.toFloat() / currentImageHeight.toFloat()

            if (currentImageWidth > currentImageHeight) {
                currentImageWidth = imageSizeInPx
                currentImageHeight = (imageSizeInPx / ratio).toInt()
            } else {
                currentImageHeight = imageSizeInPx
                currentImageWidth = (imageSizeInPx * ratio).toInt()
            }

        }

        val scaledImage = ImageIcon(
            imageIcon.image?.getScaledInstance(currentImageWidth, currentImageHeight, 0)
        )
        return scaledImage
    }
}