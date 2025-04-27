package com.alaje.intellijplugins.pokepop

import com.alaje.intellijplugins.pokepop.image.ImageIconIterator
import com.alaje.intellijplugins.pokepop.image.PokemonImageLoader
import com.alaje.intellijplugins.pokepop.settings.ApplicationSettings
import com.alaje.intellijplugins.pokepop.utils.ImageUtil.scaleImageIcon
import com.alaje.intellijplugins.pokepop.utils.NotificationsUtil
import com.alaje.intellijplugins.pokepop.utils.TimeUtil
import com.intellij.notification.NotificationType
import com.intellij.openapi.application.EDT
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
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
  private val imageIconIterator by lazy { ImageIconIterator(pokemonImageLoader.loadImages()) }

  private val applicationSettingsState by lazy { application.getService(ApplicationSettings::class.java).state }

  private val isPokePopEnabled: Boolean get() {
    return with(applicationSettingsState){
      isPokePopEnabled && (TimeUtil.onlyCurrentTimeInMillis in startTimeInMillis..endTimeInMillis)
    }
  }

  private val toolkitScreenSize get() = Toolkit.getDefaultToolkit().screenSize
  private val maxWidth get() = toolkitScreenSize.width
  private val maxHeight get() = toolkitScreenSize.height

  fun showPopup(project: Project) {

    val hasNoImages = pokemonImageLoader.isLoaded && pokemonImageLoader.pokemonImagePaths.isEmpty()

    if (hasNoImages) {
      NotificationsUtil.showNotification(
        "No images found for Pokepop",
        project,
        NotificationType.ERROR
      )
      return
    }

    job = coroutineScope.launch(Dispatchers.EDT) {
      while(isActive && isPokePopEnabled) {
        val imageIcon: ImageIcon? = if (imageIconIterator.hasNext()) {
          imageIconIterator.next()
        } else {
          imageIconIterator.restart().next()
        }

        if (imageIcon == null) return@launch

        val scaledImage = scaleImageIcon(imageIcon, applicationSettingsState.imageSizeInPx)

        val label = JLabel(scaledImage)
        label.isOpaque = false
        label.setBounds(100, 100, scaledImage.iconWidth, scaledImage.iconHeight)

        displayImage(label,applicationSettingsState.displayDurationInMillis)

        delay(applicationSettingsState.delayTimeInMillis)
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

