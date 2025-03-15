package com.alaje.intellijplugins.pokepop.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.RoamingType
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

/**
 * Holds the settings.
 */
@Service
@State(
    name = "com.alaje.intellijplugins.pokepop.settings.ApplicationSettings",
    reloadable = false,
    storages = [
        Storage("pokepop.xml", roamingType = RoamingType.PER_OS)
    ]
)
class ApplicationSettings: PersistentStateComponent<ApplicationSettings.State>{
    private var state: State = State()

    override fun getState(): State = state

    override fun loadState(state: State) {
        this.state = state
    }

    override fun noStateLoaded() {
        state = State()
    }

    companion object {
        fun getInstance(): ApplicationSettings {
            return ApplicationManager.getApplication().getService(ApplicationSettings::class.java)
        }
    }

    class State(
        var displayDuration: Long = DEFAULT_DISPLAY_DURATION, // How long each image stays on the screen (in milliseconds)
        var delayTime: Long = DEFAULT_DELAY_TIME // How long to wait before showing the next image (in milliseconds)
    ) {
        /**
         * Updates the state with the new values.
         * Passing null will keep the current value.
         */
        fun updateState(
            displayDuration: Long?,
            delayTime: Long?
        ) {
            if (displayDuration != null) this.displayDuration = displayDuration
            if (delayTime != null) this.delayTime = delayTime
        }
    }

}

private const val DEFAULT_DISPLAY_DURATION = 4000L
private const val DEFAULT_DELAY_TIME = 5000L