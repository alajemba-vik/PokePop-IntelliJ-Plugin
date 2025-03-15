package com.alaje.intellijplugins.pokepop.settings

import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.BottomGap
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.text
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

    var displayDurationText: String
        get() = displayDurationTextField.component.text
        set(value) {
            displayDurationTextField.text(value)
        }
    var delayTimeText: String
        get() = delayTimeTextField.component.text
        set(value) {
            delayTimeTextField.text(value)
        }

    init {
        mainPanel = FormBuilder.createFormBuilder()
            .addComponent(
                panel {
                    row("Display Duration (ms):") {
                        displayDurationTextField = textField()
                    }.bottomGap(BottomGap.SMALL)

                    row("Delay Duration (ms):") {
                        delayTimeTextField = textField()
                    }.bottomGap(BottomGap.SMALL)

                    row {
                        comment(
                            """
                            New durations will be applied to the IDE after restart.\n
                            Not supplying a valid value will result in the use of the last value provided.
                            .""".trimIndent()
                        )
                    }
                }
            )
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }
}
