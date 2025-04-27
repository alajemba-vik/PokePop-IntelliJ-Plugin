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
@Suppress("DialogTitleCapitalization")
class ApplicationSettingsView {
    var mainPanel : JPanel
        private set

    val getPreferredFocusedComponent get(): JComponent = displayDurationTextField.component

    private lateinit var displayDurationTextField: Cell<JBTextField>
    private lateinit var delayTimeTextField: Cell<JBTextField>
    private lateinit var enablePokePop:  Cell<JBCheckBox>

    private lateinit var startTimeTextField: Cell<JBTextField>
    private lateinit var endTimeTextField: Cell<JBTextField>

    private lateinit var imageSizeTextField: Cell<JBTextField>

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

    var startTime: Long?
        get()  = startTimeTextField.component.text.toMilliseconds()
        set(value) {
            startTimeTextField.text(value?.toTimeString() ?: "")
        }

    var endTime: Long?
        get() = endTimeTextField.component.text.toMilliseconds()
        set(value) {
            endTimeTextField.text(value?.toTimeString() ?: "")
        }

    var imageSize: Int?
        get() = imageSizeTextField.component.text?.toIntOrNull()
        set(value) {
            imageSizeTextField.text(value?.toString() ?: "")
        }

    init {
        mainPanel = FormBuilder.createFormBuilder()
            .addComponent(
                panel {
                    row {
                        enablePokePop = checkBox("Distract me with cuteness")
                            .apply { this.enabled(true) }
                    }

                    group("Timing") {
                        row("Display Duration:") {
                            displayDurationTextField = durationField()
                        }.bottomGap(BottomGap.SMALL)

                        row("Delay Duration:") {
                            delayTimeTextField = durationField()
                        }.bottomGap(BottomGap.SMALL)
                    }

                    group("Schedule") {
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
                            Not supplying the mm portion will default to 0.
                            """.trimIndent()
                            )
                        }
                    }

                    group("Appearance") {
                        row("Image Size:") {
                            imageSizeTextField = intTextField(range = 1..1000, keyboardStep = 1)
                                .columns(6)
                                .gap(RightGap.SMALL)
                        }.label("pixels")
                    }

                    row {
                        comment(
                            """
                            Restart the IDE or Pokepop to apply any changes. Not doing so will cause inconsistent behavior.
                            Pokepop can be restarted by clicking Pokepop's ◁ or || button in the status bar.
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