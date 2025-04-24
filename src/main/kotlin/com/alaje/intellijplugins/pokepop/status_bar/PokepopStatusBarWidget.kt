package com.alaje.intellijplugins.pokepop.status_bar

import com.alaje.intellijplugins.pokepop.ImageDisplayService
import com.alaje.intellijplugins.pokepop.settings.ApplicationSettings
import com.alaje.intellijplugins.pokepop.utils.NotificationsUtil
import com.intellij.icons.AllIcons
import com.intellij.notification.Notification
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.util.Consumer
import com.intellij.util.ui.EDT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.event.MouseEvent
import javax.swing.Icon
import javax.swing.JPanel

class PokepopStatusBarWidget(
    private val project: Project,
    private val coroutineScope: CoroutineScope
): StatusBarWidget, StatusBarWidget.IconPresentation {
    private var statusBar: StatusBar? = null
    private var notification: Notification? = null

    private val component = lazy {
        if (!EDT.isCurrentThreadEdt()) return@lazy

        val component = JPanel()
    }

    private val appSettings = ApplicationSettings.settings
    private val isPokePopEnabled: Boolean get() = appSettings.state.isPokePopEnabled

    override fun ID(): String = ID

    override fun getPresentation(): StatusBarWidget.WidgetPresentation = this

    override fun getIcon(): Icon {

        return if (isPokePopEnabled) {
            AllIcons.Process.ProgressPause
        } else {
            AllIcons.Process.ProgressResume
        }
    }

    override fun getTooltipText(): String {
        return if (isPokePopEnabled) {
            "Pause Pokepop"
        } else {
            "Resume Pokepop"
        }
    }

    override fun install(statusBar: StatusBar) {
        this.statusBar = statusBar
    }

    override fun getClickConsumer(): Consumer<MouseEvent> {
        return Consumer<MouseEvent> { mouseEvent ->

            appSettings.state.isPokePopEnabled = !appSettings.state.isPokePopEnabled
            val message = if (isPokePopEnabled) "Pokepop resumed" else "Pokepop paused"

            // Update the icon
            statusBar?.updateWidget(ID)

            // Show a toast informing the user of the change
            manageNotification(message)

            // Show or cancel the service
            ImageDisplayService.service.apply {
                if (isPokePopEnabled) {
                    showPopup(project)
                } else {
                    cancelPopup()
                }
            }
        }
    }

    private fun manageNotification(message: String) {
        notification?.expire()

        notification = NotificationsUtil.showNotification(
            message,
            project
        )

        coroutineScope.launch {
            delay(3000)
            notification?.expire()
        }

        notification?.whenExpired {
            notification = null
        }
    }

    /*private fun showPopup(mouseEvent: MouseEvent) {
        statusBar?.component?.let { component ->
            //JBPopupFactory.createListPopup(
            val popup = JBPopupFactory.getInstance().createListPopup(
                ListPopupStep(),
            )

            val dataContext = DataManager.getInstance().getDataContext(mouseEvent.component)
            popup.showInBestPositionFor(dataContext)
        }
    }*/

    override fun dispose() {
        statusBar = null
    }

    companion object {
        const val ID: String = "com.alaje.intellijplugins.pokepop.status_bar.StatusBarWidget"
    }

    /*class ListPopupStep: BaseListPopupStep<String>(
        "Pokepop",
        listOf("Pause Pokepop", "Resume Pokepop")
    ) {

        override fun getTextFor(value: String): String = value

        override fun onChosen(selectedValue: String, finalChoice: Boolean): PopupStep<*>? {
            return null
        }
    }*/
}