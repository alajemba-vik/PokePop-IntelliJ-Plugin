package com.alaje.intellijplugins.pokepop

import com.alaje.intellijplugins.pokepop.image.ImageIconIterator
import com.alaje.intellijplugins.pokepop.image.PokemonImageLoader
import com.alaje.intellijplugins.pokepop.utils.ImageUtil.scaleImageIcon
import com.intellij.openapi.application.EDT
import com.intellij.openapi.components.Service
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.ui.JBColor
import com.intellij.ui.awt.RelativePoint
import kotlinx.coroutines.*
import java.awt.*
import javax.swing.*
import kotlin.random.Random


@Service
class ImageDisplayService(
  private val coroutineScope: CoroutineScope
)  {

  private val pokemonImageLoader by lazy{ PokemonImageLoader() }
  private val imageIconIterator by lazy {
    ImageIconIterator(pokemonImageLoader.loadImages())
  }

  fun showPopup(delayTime: Long, displayDuration: Long) {
    if (pokemonImageLoader.isLoaded && pokemonImageLoader.pokemonImagePaths.isEmpty()) {
      System.err.println("No images found")
      // TODO: Show toast telling user there are no images to display
      return
    }

    coroutineScope.launch(Dispatchers.EDT) {
      while(isActive) {
        delay(delayTime)

        val transparentJBColor = JBColor(Color(0, 0, 0, 0), Color(0, 0, 0, 0))

        val imageIcon: ImageIcon? = if (imageIconIterator.hasNext()) {
          imageIconIterator.next()
        } else {
          imageIconIterator.restart().next()
        }

        if (imageIcon == null) return@launch

        val scaledImage = scaleImageIcon(imageIcon)

        val label = JLabel(scaledImage)
        label.isOpaque = false
        label.setBounds(100, 100, scaledImage.iconWidth, scaledImage.iconHeight)

        displayImage(label, transparentJBColor, displayDuration)

      }

    }
  }

  private fun displayImage(
    label: JLabel, 
    transparentJBColor: JBColor,
    displayDuration: Long
    ) {
    val balloon = JBPopupFactory.getInstance()
      .createBalloonBuilder(label)
      .setFillColor(transparentJBColor)
      .setBorderColor(transparentJBColor)
      .setShadow(false)
      .setClickHandler(
        {
          // TODO: Do something
        },
        true
      )
      .setHideOnKeyOutside(false)
      .setHideOnClickOutside(false)
      .setHideOnFrameResize(false)
      .setFadeoutTime(displayDuration)
      .createBalloon()

    val toolkitScreenSize = Toolkit.getDefaultToolkit().screenSize
    val maxWidth = toolkitScreenSize.width
    val maxHeight = toolkitScreenSize.height;

    val randPositionX = Random.nextInt(0, maxWidth)
    val randPositionY = Random.nextInt(0, maxHeight)

    balloon.show(
      RelativePoint.fromScreen(
        Point(randPositionX, randPositionY)
      ),
      Balloon.Position.atRight
    )
  }
}
