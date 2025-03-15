package com.alaje.intellijplugins.pokepop

import com.alaje.intellijplugins.pokepop.settings.ApplicationSettings
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.util.application

class PostStartupActivity: ProjectActivity, DumbAware {
    override suspend fun execute(project: Project) {
        val service = application.getService(ImageDisplayService::class.java)
        val applicationSettings = application.getService(ApplicationSettings::class.java)

        service.showPopup(
            delayTime = applicationSettings.state.delayTime,
            displayDuration = applicationSettings.state.displayDuration,
        )
    }

}