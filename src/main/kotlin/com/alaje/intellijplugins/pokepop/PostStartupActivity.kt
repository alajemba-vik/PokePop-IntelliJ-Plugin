package com.alaje.intellijplugins.pokepop

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.util.application

class PostStartupActivity: ProjectActivity, DumbAware {
    override suspend fun execute(project: Project) {
        val service = application.getService(ImageDisplayService::class.java)

        val time = 5000L//300_000L // for 5 mins
        service.showPopup(time)
    }

}