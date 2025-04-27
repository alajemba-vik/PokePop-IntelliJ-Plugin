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
        val settings get(): ApplicationSettings {
            return ApplicationManager.getApplication().getService(ApplicationSettings::class.java)
        }
    }

    /**
     * Holds the state of the settings.
     * This class is used to store the settings in a persistent way.
     * @property displayDurationInMillis How long each image stays on the screen (in milliseconds)
     * @property delayTimeInMillis How long to wait before showing the next image (in milliseconds)
     * @property isPokePopEnabled Whether the PokePop feature is enabled or not
     * @property startTimeInMillis Start time in milliseconds, only contains the time portion
     * @property endTimeInMillis End time in milliseconds, only contains the time portion
     */
    class State(
        var displayDurationInMillis: Long = DEFAULT_DISPLAY_DURATION,
        var delayTimeInMillis: Long = DEFAULT_DELAY_TIME,
        var isPokePopEnabled: Boolean = true,
        var startTimeInMillis: Long = 0L,
        var endTimeInMillis: Long = DEFAULT_END_TIME,
        var imageSizeInPx: Int = DEFAULT_IMAGE_SIZE
    ) {
        /**
         * Updates the state with the new values.
         * Passing null will keep the current value.
         */
        fun updateState(
            displayDuration: Long? = null,
            delayTime: Long? = null,
            isPokePopEnabled: Boolean? = null,
            startTime: Long? = null,
            endTime: Long? = null,
            imageSize: Int? = null
        ) {
            if (displayDuration != null) this.displayDurationInMillis = displayDuration
            if (delayTime != null) this.delayTimeInMillis = delayTime
            if (isPokePopEnabled != null) this.isPokePopEnabled = isPokePopEnabled
            if (startTime != null) this.startTimeInMillis = startTime
            if (endTime != null) this.endTimeInMillis = endTime
            if (imageSize != null) this.imageSizeInPx = imageSize
        }
    }

}

private const val DEFAULT_DISPLAY_DURATION = 4000L
private const val DEFAULT_DELAY_TIME = 5000L
private const val DEFAULT_END_TIME = 86_340_000L // 23:59:59 in milliseconds
private const val DEFAULT_IMAGE_SIZE = 150