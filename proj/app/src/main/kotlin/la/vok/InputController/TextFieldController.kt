package la.vok.InputController

import la.vok.GameController.GameController
import la.vok.UI.Elements.*
import processing.event.KeyEvent

class TextFieldController(var gameController: GameController) {
    var lTextField: LTextField? = null
    var isEditing = false
    var repeatCounter = 0
    private val keyRepeatDelay = 15  // Задержка перед первым повтором
    private val keyRepeatRate = 2    // Интервал между повторами

    fun input(key: KeyEvent) {
        if (!isEditing || lTextField == null) return
        val tf = lTextField!!
        val char = key.key
        val code = key.keyCode
        val shift = key.isShiftDown
        
        fun prepareSelection() {
            if (shift) {
                if (tf.selectionStart == null) tf.selectionStart = tf.cursorPosition
            } else {
                tf.selectionStart = null
            }
        }
    
        when (code) {
            java.awt.event.KeyEvent.VK_LEFT -> {
                prepareSelection()
                tf.cursorPosition--
            }
    
            java.awt.event.KeyEvent.VK_RIGHT -> {
                prepareSelection()
                tf.cursorPosition++
            }
    
            java.awt.event.KeyEvent.VK_UP,
            java.awt.event.KeyEvent.VK_DOWN -> {
                prepareSelection()
    
                val lines = tf.inputString.split('\n')
                var remaining = tf.cursorPosition
                var currentLine = 0
                var indexInLine = 0
    
                for (i in lines.indices) {
                    if (remaining <= lines[i].length) {
                        currentLine = i
                        indexInLine = remaining
                        break
                    } else {
                        remaining -= lines[i].length + 1
                    }
                }
    
                val targetLine = when (code) {
                    java.awt.event.KeyEvent.VK_UP -> currentLine - 1
                    java.awt.event.KeyEvent.VK_DOWN -> currentLine + 1
                    else -> currentLine
                }
    
                if (targetLine in lines.indices) {
                    val newIndexInLine = indexInLine.coerceAtMost(lines[targetLine].length)
                    tf.cursorPosition = lines.take(targetLine).sumOf { it.length + 1 } + newIndexInLine
                }
            }
    
            java.awt.event.KeyEvent.VK_BACK_SPACE -> tf.deleteCharBeforeCursor()
    
            else -> {
                if (char.isLetterOrDigit() || char.isWhitespace() || (char.isDefined() && !char.isISOControl())) {
                    tf.insertCharAtCursor(char)
                }
            }
        }
    
        tf.handleNewChar(char)
    }
    
    fun elementStartEditing(lTextField: LTextField?) {
        if (lTextField == null) return
        this.lTextField?.isEditing = true
        this.lTextField?.handleStartEditing()
    }
    fun elementStopEditing(lTextField: LTextField?) {
        if (lTextField == null) return
        this.lTextField?.isEditing = false
        this.lTextField?.handleStopEditing()
    }
    fun startEditing(lTextField: LTextField) {
        elementStopEditing(this.lTextField)
        isEditing = true
        this.lTextField = lTextField
        elementStartEditing(this.lTextField)
    }
    fun stopEditing() {
        elementStopEditing(this.lTextField)
        isEditing = false
    }
    fun tick(keyTracker: KeyTracker) {
        val event = keyTracker.lastKeyEvent ?: return
        repeatCounter++

        val shouldRepeat = repeatCounter == keyRepeatDelay ||
            (repeatCounter > keyRepeatDelay && (repeatCounter - keyRepeatDelay) % keyRepeatRate == 0)

        if (shouldRepeat) {
            val repeatEvent = KeyEvent(
                event.native,
                System.currentTimeMillis(),
                KeyEvent.PRESS,
                event.getModifiers(),
                event.getKey(),
                event.getKeyCode(),
                true
            )

            gameController.textFieldController.input(repeatEvent)
        }
    }
}