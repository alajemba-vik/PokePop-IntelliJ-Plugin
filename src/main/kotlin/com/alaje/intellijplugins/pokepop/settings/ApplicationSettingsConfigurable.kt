package com.alaje.intellijplugins.pokepop.settings

import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

/**
 * Allows [ApplicationSettingsView] to communicate with and update [ApplicationSettings].
 * Acts as a bridge between the UI and the settings model.
 * Methods are called by IntelliJ Platform.
 */
class ApplicationSettingsConfigurable: Configurable {
    private var settingsComponent: ApplicationSettingsView? = null
    private val applicationSettings get() = ApplicationSettings.getInstance()

    private val state get() = applicationSettings.state

    override fun createComponent(): JComponent? {
        settingsComponent = ApplicationSettingsView()
        return settingsComponent?.mainPanel
    }

    override fun isModified(): Boolean {
        val newDisplayDuration = settingsComponent?.displayDurationText?.toLongOrNull()
        val newDelayTime = settingsComponent?.delayTimeText?.toLongOrNull()

        return newDisplayDuration != null &&
                newDelayTime != null &&
                (newDisplayDuration != state.displayDuration || newDelayTime != state.delayTime)

    }

    override fun apply() {
        // save user input to persistent state
        state.updateState(
            settingsComponent?.displayDurationText?.toLongOrNull(),
            settingsComponent?.delayTimeText?.toLongOrNull()
        )
    }

    override fun reset() {
        // reset UI to match persistent state
        settingsComponent?.displayDurationText = state.displayDuration.toString()
        settingsComponent?.delayTimeText = state.delayTime.toString()
    }

    override fun disposeUIResources() {
        settingsComponent = null
    }

    override fun getDisplayName(): String  = "Pokepop"

    override fun getPreferredFocusedComponent(): JComponent? = settingsComponent?.getPreferredFocusedComponent
}