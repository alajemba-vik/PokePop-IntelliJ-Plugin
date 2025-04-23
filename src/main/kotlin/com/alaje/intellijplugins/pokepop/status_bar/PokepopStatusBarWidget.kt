package com.alaje.intellijplugins.pokepop.status_bar

import com.alaje.intellijplugins.pokepop.ImageDisplayService
import com.alaje.intellijplugins.pokepop.settings.ApplicationSettings
import com.intellij.icons.AllIcons
import com.intellij.notification.Notification
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
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
    private val isPaused: Boolean get() = appSettings.state.isPokePopEnabled

    override fun ID(): String = ID

    override fun getPresentation(): StatusBarWidget.WidgetPresentation = this

    override fun getIcon(): Icon? {

        return if (!appSettings.state.isPokePopEnabled) {
            // If Pokepop is turned off, return null
            null
        } else if (isPaused) {
            AllIcons.Process.ProgressResume
        } else {
            AllIcons.Process.ProgressPause
        }
    }

    override fun getTooltipText(): String {
        return if (isPaused) {
            "Resume Pokepop"
        } else {
            "Pause Pokepop"
        }
    }

    override fun install(statusBar: StatusBar) {
        this.statusBar = statusBar
    }

    override fun getClickConsumer(): Consumer<MouseEvent> {
        return Consumer<MouseEvent> { mouseEvent ->

            appSettings.state.isPokePopEnabled = !appSettings.state.isPokePopEnabled
            val message = if (isPaused) "Pokepop paused" else "Pokepop resumed"

            // Update the icon
            statusBar?.updateWidget(ID)

            // Show a toast informing the user of the change
            notification?.expire()
            NotificationGroupManager.getInstance()
                .getNotificationGroup("General Status Update")
                .createNotification(message, NotificationType.INFORMATION)
                .apply {
                    notification = this
                    coroutineScope.launch {
                        delay(3000)
                        expire()
                    }
                }
                .notify(project)
            notification?.whenExpired {
                notification = null
            }

            // Show or cancel the service
            ImageDisplayService.service.apply {
                if (isPaused) {
                    cancelPopup()
                } else {
                    showPopup()
                }
            }
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