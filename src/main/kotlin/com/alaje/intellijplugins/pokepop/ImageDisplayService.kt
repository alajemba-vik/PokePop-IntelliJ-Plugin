package com.alaje.intellijplugins.pokepop

import com.alaje.intellijplugins.pokepop.image.ImageIconIterator
import com.alaje.intellijplugins.pokepop.image.PokemonImageLoader
import com.alaje.intellijplugins.pokepop.settings.ApplicationSettings
import com.alaje.intellijplugins.pokepop.utils.ImageUtil.scaleImageIcon
import com.intellij.openapi.application.EDT
import com.intellij.openapi.components.Service
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.ui.JBColor
import com.intellij.ui.awt.RelativePoint
import com.intellij.util.application
import kotlinx.coroutines.*
import java.awt.Color
import java.awt.Point
import java.awt.Toolkit
import javax.swing.ImageIcon
import javax.swing.JLabel
import kotlin.random.Random


@Service
class ImageDisplayService(
  private val coroutineScope: CoroutineScope
)  {

  private var job: Job? = null
  private val pokemonImageLoader by lazy{ PokemonImageLoader() }
  private val imageIconIterator by lazy {
    ImageIconIterator(pokemonImageLoader.loadImages())
  }

  private val toolkitScreenSize get() = Toolkit.getDefaultToolkit().screenSize
  private val maxWidth get() = toolkitScreenSize.width
  private val maxHeight get() = toolkitScreenSize.height;

  fun showPopup() {
    val applicationSettings = application.getService(ApplicationSettings::class.java)

    val delayTime = applicationSettings.state.delayTime
    val displayDuration = applicationSettings.state.displayDuration

    if (pokemonImageLoader.isLoaded && pokemonImageLoader.pokemonImagePaths.isEmpty()) {
      System.err.println("No images found")
      // TODO: Show toast telling user there are no images to display
      return
    }

    job = coroutineScope.launch(Dispatchers.EDT) {
      while(isActive) {
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

        displayImage(label, displayDuration)

        delay(delayTime)
      }

    }
  }

  fun cancelPopup() {
    job?.cancel()
    job = null
  }

  private fun displayImage(
    label: JLabel, 
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

    val randPositionX = Random.nextInt(0, maxWidth)
    val randPositionY = Random.nextInt(0, maxHeight)

    balloon.show(
      RelativePoint.fromScreen(
        Point(randPositionX, randPositionY)
      ),
      Balloon.Position.atRight
    )
  }

  companion object {
    val service get(): ImageDisplayService = application.getService(ImageDisplayService::class.java)
  }
}

private val transparentJBColor = JBColor(Color(0, 0, 0, 0), Color(0, 0, 0, 0))

