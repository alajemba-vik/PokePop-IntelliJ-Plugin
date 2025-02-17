package com.alaje.intellijplugins.pokepop

import com.alaje.intellijplugins.pokepop.image.ImageIconIterator
import com.alaje.intellijplugins.pokepop.image.PokemonImageLoader
import com.intellij.openapi.application.EDT
import com.intellij.openapi.components.Service
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.ui.JBColor
import com.intellij.ui.awt.RelativePoint
import kotlinx.coroutines.*
import java.awt.*
import javax.swing.*


@Service
class ImageDisplayService(
  private val coroutineScope: CoroutineScope
)  {

  private val pokemonImageLoader by lazy{ PokemonImageLoader() }
  private val imageIconIterator by lazy {
    ImageIconIterator(pokemonImageLoader.loadImages())
  }

  fun showPopup(timeInMillis: Long) {
    if (pokemonImageLoader.isLoaded && pokemonImageLoader.pokemonImagePaths.isEmpty()) {
      System.err.println("No images found")
      // TODO: Show toast telling user there are no images to display
      return
    }

    coroutineScope.launch(Dispatchers.EDT) {
      while(isActive) {
        delay(timeInMillis)
        val imageHeight = 150
        val imageWidth = 150
        val transparentJBColor = JBColor(Color(0, 0, 0, 0), Color(0, 0, 0, 0))

        val imageIcon: ImageIcon? = if (imageIconIterator.hasNext()) {
          imageIconIterator.next()
        } else {
          imageIconIterator.restart().next()
        }

        val scaledImage = ImageIcon(imageIcon?.image?.getScaledInstance(imageWidth, imageHeight, 0))

        val label = JLabel(scaledImage)
        label.isOpaque = false
        label.setBounds(100, 100, imageWidth, imageHeight)

        val balloon = JBPopupFactory.getInstance()
          .createBalloonBuilder(label)
          .setFillColor(transparentJBColor)
          .setBorderColor(transparentJBColor)
          .setShadow(false)
          .setFadeoutTime(fadeoutTime) // Auto-close after 5 seconds
          .createBalloon()

        balloon.show(
          RelativePoint.fromScreen(Point(100, 100)),
          Balloon.Position.below
        )
      }

    }
  }
}

private const val fadeoutTime = 4000L