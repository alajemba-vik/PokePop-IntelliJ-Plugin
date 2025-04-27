package com.alaje.intellijplugins.pokepop.settings

import com.alaje.intellijplugins.pokepop.utils.TimeUtil.toMilliseconds
import com.alaje.intellijplugins.pokepop.utils.TimeUtil.toTimeString
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.*
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * Defines UI to display and accept data for settings
 */
class ApplicationSettingsView {
    var mainPanel : JPanel
        private set

    val getPreferredFocusedComponent get(): JComponent = displayDurationTextField.component

    private lateinit var displayDurationTextField: Cell<JBTextField>
    private lateinit var delayTimeTextField: Cell<JBTextField>
    private lateinit var enablePokePop:  Cell<JBCheckBox>

    private lateinit var startTimeTextField: Cell<JBTextField>
    private lateinit var endTimeTextField: Cell<JBTextField>

    var displayDuration: Long?
        get() = displayDurationTextField.component.text?.toLongOrNull()
        set(value) {
            displayDurationTextField.text(value?.toString() ?: "")
        }

    var delayTime: Long?
        get() = delayTimeTextField.component.text?.toLongOrNull()
        set(value) {
            delayTimeTextField.text(value?.toString() ?: "")
        }

    var enablePokePopValue: Boolean
        get() = enablePokePop.component.isSelected
        set(value) {
            enablePokePop.component.isSelected = value
        }

    var startTimeText: Long?
        get()  = startTimeTextField.component.text.toMilliseconds()
        set(value) {
            startTimeTextField.text(value?.toTimeString() ?: "")
        }

    var endTimeText: Long?
        get() = endTimeTextField.component.text.toMilliseconds()
        set(value) {
            endTimeTextField.text(value?.toTimeString() ?: "")
        }

    init {
        mainPanel = FormBuilder.createFormBuilder()
            .addComponent(
                panel {
                    row {
                        enablePokePop = checkBox("Distract me with cuteness")
                            .apply { this.enabled(true) }
                    }

                    row("Display Duration:") {
                        displayDurationTextField = durationField()
                    }.bottomGap(BottomGap.SMALL)

                    row("Delay Duration:") {
                        delayTimeTextField = durationField()
                    }.bottomGap(BottomGap.SMALL)

                    row {
                        // only show between [starting time] and [ending time]
                        startTimeTextField = timeField(
                            text = "00:00",
                            label = "Only show between",
                        )

                        endTimeTextField = timeField(
                            text = "23:59",
                            label = "and",
                        )

                    }.bottomGap(BottomGap.NONE)

                    row {
                        comment(
                            """
                            The times are in 24-hour format HH:mm. The default start time is 00:00 and default end time is 23:59.
                            Not supplying the mm portion will default to 00.
                            """.trimIndent()
                        )
                    }

                    row {
                        comment(
                            """
                            Settings will be fully applied to the IDE after restarting the IDE or Pokepop.
                            """.trimIndent()
                        )
                    }.bottomGap(BottomGap.SMALL)
                }
            )
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }
}

@Suppress("DialogTitleCapitalization")
private fun Row.durationField(): Cell<JBTextField> {
    return intTextField(range = 1000..86_400_000, keyboardStep = 1)
        .columns(8)
        .gap(RightGap.SMALL)
        .apply {
            this@durationField.label("ms")
        }
}

private fun Row.timeField(
    text: String,
    label: String,
): Cell<JBTextField> {
    return textField()
        .apply {
            this.component.emptyText.text = "HH:mm"
        }
        .columns(6)
        .text(text)
        .gap(RightGap.SMALL)
        .label(label, LabelPosition.LEFT)
}