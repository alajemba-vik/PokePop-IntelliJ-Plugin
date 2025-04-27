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
    private val applicationSettings get() = ApplicationSettings.settings

    private val state get() = applicationSettings.state

    override fun createComponent(): JComponent? {
        settingsComponent = ApplicationSettingsView()
        return settingsComponent?.mainPanel
    }

    override fun isModified(): Boolean {
        val newDisplayDuration = settingsComponent?.displayDuration
        val newDelayTime = settingsComponent?.delayTime
        val newEnablePokePop = settingsComponent?.enablePokePopValue
        val newStartTime = settingsComponent?.startTimeText
        val newEndTime = settingsComponent?.endTimeText

        val isNewUpdate = newDisplayDuration != state.displayDurationInMillis ||
                newDelayTime != state.delayTimeInMillis ||
                newEnablePokePop != state.isPokePopEnabled ||
                newStartTime != state.startTimeInMillis ||
                newEndTime != state.endTimeInMillis

        return newDisplayDuration != null &&
                newDelayTime != null &&
                newStartTime != null &&
                newEndTime != null &&
                isNewUpdate
    }

    override fun apply() {
        // save user input to persistent state
        state.updateState(
            settingsComponent?.displayDuration,
            settingsComponent?.delayTime,
            settingsComponent?.enablePokePopValue,
            startTime = settingsComponent?.startTimeText,
            endTime = settingsComponent?.endTimeText
        )
    }

    override fun reset() {
        // reset UI to match persistent state
        settingsComponent?.displayDuration = state.displayDurationInMillis
        settingsComponent?.delayTime = state.delayTimeInMillis
        settingsComponent?.enablePokePopValue = state.isPokePopEnabled
        settingsComponent?.startTimeText = state.startTimeInMillis
        settingsComponent?.endTimeText = state.endTimeInMillis
    }

    override fun disposeUIResources() {
        settingsComponent = null
    }

    override fun getDisplayName(): String  = "Pokepop"

    override fun getPreferredFocusedComponent(): JComponent? = settingsComponent?.getPreferredFocusedComponent
}