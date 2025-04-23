package com.alaje.intellijplugins.pokepop

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

class PostStartupActivity: ProjectActivity, DumbAware {
    override suspend fun execute(project: Project) {
        ImageDisplayService.service.showPopup()
    }

}