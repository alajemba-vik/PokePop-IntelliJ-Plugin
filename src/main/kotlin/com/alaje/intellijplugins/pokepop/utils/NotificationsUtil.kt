package com.alaje.intellijplugins.pokepop.utils

import com.intellij.notification.Notification
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType

object NotificationsUtil {
    fun showNotification(
        message: String,
        project: com.intellij.openapi.project.Project,
        type:  NotificationType = NotificationType.INFORMATION,
    ): Notification {
        val notification: Notification = NotificationGroupManager.getInstance()
            .getNotificationGroup("General Status Update")
            .createNotification(message, type)

        notification.notify(project)

        return notification
    }
}