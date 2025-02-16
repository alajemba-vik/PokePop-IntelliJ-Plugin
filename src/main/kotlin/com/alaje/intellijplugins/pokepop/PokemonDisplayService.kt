package com.alaje.intellijplugins.pokepop

import com.intellij.openapi.application.EDT
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.ui.JBColor
import com.intellij.ui.awt.RelativePoint
import com.intellij.util.application
import kotlinx.coroutines.*
import java.awt.*
import java.net.URL
import javax.swing.*


@Service
class PokemonDisplayService(
  private val coroutineScope: CoroutineScope
)  {
  fun showPopup(timeInMillis: Long) {
    coroutineScope.launch(Dispatchers.EDT) {
      while(isActive) {
        delay(timeInMillis)
        val imageHeight = 150
        val imageWidth = 150
        val transparentColor = Color(0, 0, 0, 0)
        val transparentJBColor = JBColor(transparentColor, transparentColor)


        val image = createImageIcon("/pokemon.gif", null)

        val scaledImage = ImageIcon(image?.image?.getScaledInstance(imageWidth, imageHeight, 0))

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

class PokemonDisplayPostStartupActivity: ProjectActivity, DumbAware {
  override suspend fun execute(project: Project) {
    val service = application.getService(PokemonDisplayService::class.java)

    val time = 5000L//300_000L // for 5 mins
    service.showPopup(time)
  }

}


fun createImageIcon(
  path: String,
  description: String?
): ImageIcon? {
  val imgURL: URL? = PokemonDisplayService::class.java.getResource(path)

  return if (imgURL != null) {
    println("Found file: $path")
    ImageIcon(imgURL, description)
  } else {
    System.err.println("Couldn't find file: $path")
    null
  }
}

private const val fadeoutTime = 4000L