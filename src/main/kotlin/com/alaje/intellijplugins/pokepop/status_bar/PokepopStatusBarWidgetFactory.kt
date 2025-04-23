package com.alaje.intellijplugins.pokepop.status_bar

import com.intellij.ide.lightEdit.LightEditCompatible
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBarWidgetFactory
import kotlinx.coroutines.CoroutineScope

class PokepopStatusBarWidgetFactory: StatusBarWidgetFactory, LightEditCompatible {
    override fun getId(): String = PokepopStatusBarWidget.ID

    override fun getDisplayName(): String = "Pokepop"

    override fun createWidget(project: Project, scope: CoroutineScope): com.intellij.openapi.wm.StatusBarWidget {
        return PokepopStatusBarWidget(project, scope)
    }

}