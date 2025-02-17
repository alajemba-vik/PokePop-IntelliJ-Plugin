package com.alaje.intellijplugins.pokepop.image

import com.alaje.intellijplugins.pokepop.utils.ImageUtil.createImageIcon
import javax.swing.ImageIcon

class ImageIconIterator(
    private var imagePaths: List<String>
): Iterator<ImageIcon?> {
    private var index = 0

    override fun hasNext(): Boolean {
        return index < imagePaths.size
    }

    override fun next(): ImageIcon? {
        val imagePath = imagePaths.getOrNull(index) ?: return null
        index++
        return createImageIcon(imagePath, null)
    }

    fun restart(): ImageIconIterator {
        index = 0
        imagePaths = imagePaths.shuffled()
        return this
    }
}