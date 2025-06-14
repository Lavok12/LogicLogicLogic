package la.vok.UI.Elements

import la.vok.UI.MainRender
import la.vok.Storages.Storage
import java.awt.Color
import la.vok.UI.Canvas.*
import la.vok.UI.Scenes.*
import processing.data.JSONObject
import la.vok.LavokLibrary.*
import la.vok.GameController.GameController
import la.vok.UI.*
import la.vok.InputController.MouseController
import la.vok.UI.Canvas.*
import la.vok.UI.Scenes.*


class LTextField(
    gameController: GameController,
    x: Float = 0f,
    y: Float = 0f,
    width: Float = 200f,
    height: Float = 100f,
    alignX: Float = 0f,
    alignY: Float = 0f,
    parentCanvas: LCanvas = Storage.gameController.getCanvas(),
    var text: String = "Text",
    var inputString: String = "",
    var multiline: Boolean = false,
    var textTranslate: Boolean = true,
    var fontSize: Float = 30f,
    var textColor: Color = Color(180, 180, 180),
    var inputTextColor: Color = Color(255, 255, 255),
    var textDeltaX: Float = 0f,
    var textDeltaY: Float = 0f,
    var textPosAlignX: Float = 0f,
    var textPosAlignY: Float = 0f,
    var panelColor: Color = Color(100, 100, 100, 255),
    var borderRadius: Float = 0f,
    tag: String = ""
) : LElement(
    gameController, x, y, width, height, alignX, alignY, parentCanvas,
    -1f, -1f, 0f, 0f, 0f, 0f, 0f, 0f, tag
) {
    private var TPX: Float = 0f
    private var TPY: Float = 0f
    private var textSize: Float = 0f

    var isEditing = false

    var startEditing : Boolean = false
    var stopEditing : Boolean = false
    var tickEditing : Boolean = false
    var newChar : Boolean = true

    var cursorPosition: Int = 0
    set(value) {
        field = value.coerceIn(0, inputString.length) // защита от выхода за пределы
    }

    companion object {
        fun JSONToElement(json: JSONObject, parentCanvas: LCanvas, gameController: GameController): LTextField {
            var ret = LTextField(
                gameController = gameController,
                x = json.LgetFloat("x", 0f),
                y = json.LgetFloat("y", 0f),
                width = json.LgetFloat("width", 200f),
                height = json.LgetFloat("height", 100f),
                alignX = json.LgetFloat("alignX", 0f),
                alignY = json.LgetFloat("alignY", 0f),
                parentCanvas = parentCanvas,
                text = json.LgetString("text", "Text"),
                multiline = json.LgetBoolean("text", false),
                textTranslate = json.LgetBoolean("textTranslate", true),
                fontSize = json.LgetFloat("fontSize", 30f),
                textColor = Functions.getColorFromJSON(json, "textColor", Color(180, 180, 180)),
                inputTextColor = Functions.getColorFromJSON(json, "textColor", Color(255, 255, 255)),
                textDeltaX = json.LgetFloat("textDeltaX", 0f),
                textDeltaY = json.LgetFloat("textDeltaY", 0f),
                textPosAlignX = json.LgetFloat("textPosAlignX", 0f),
                textPosAlignY = json.LgetFloat("textPosAlignY", 0f),
                panelColor = Functions.getColorFromJSON(json, "panelColor", Color(100, 100, 100, 255)),
                borderRadius = json.LgetFloat("borderRadius", 0f),
                tag = json.LgetString("tag", "")
            )
            ret.gameController = gameController
            ret.checkChilds(json)
            ret.setEvents(json)
            return ret
        }
    }

    override fun setEvents(objs: JSONObject) {
        super.setEvents(objs)
        if (objs.hasKey("startEditing")) {
            var localJson = objs.LgetJSONObject("startEditing")
            startEditing = localJson.LgetBoolean("isActive", false)
        }
        if (objs.hasKey("stopEditing")) {
            var localJson = objs.LgetJSONObject("stopEditing")
            stopEditing = localJson.LgetBoolean("isActive", false)
        }
        if (objs.hasKey("tickEditing")) {
            var localJson = objs.LgetJSONObject("tickEditing")
            tickEditing = localJson.LgetBoolean("isActive", false)
        }
        if (objs.hasKey("newChar")) {
            var localJson = objs.LgetJSONObject("newChar")
            newChar = localJson.LgetBoolean("isActive", false)
        }
    }

    override fun otherTick() {
        if (isEditing) {
            handleTickEditing()
        }
    } 

    fun handleStartEditing() {
        if (startEditing) {
            startEditing()
        }
    }
    fun handleStopEditing() {
        if (stopEditing) {
            stopEditing()
        }
    }
    fun handleTickEditing() {
        if (tickEditing) {
            tickEditing()
        }
    }
    fun handleNewChar(char: Char) {
        if (newChar) {
            newChar(char)
        }
    }

    fun startEditing() {}
    fun stopEditing() {}
    fun tickEditing() {}
    fun newChar(char: Char) {}

    fun standartVisuals() {
        TPX = PX + textDeltaX * parentCanvas.scale.x + SX * textPosAlignX / 2 - width / 2 + textSize
        TPY = PY + textDeltaY * parentCanvas.scale.y + SY * textPosAlignY / 2 + height / 2 - textSize
        textSize = parentCanvas.applyCanvasTextSize(fontSize * parentCanvas.textScale)
    }
    override fun updateGridVisuals(nx: Float, ny: Float) {
        super.updateGridVisuals(nx, ny)
        standartVisuals()
    }
    override fun updateVisuals() {
        super.updateVisuals()
        standartVisuals()
    }
    override fun handleMouseDown(mouseController: MouseController) {
        super.handleMouseDown(mouseController)

        if (gameController.textFieldController.isEditing
        && gameController.textFieldController.lTextField == this) {
            gameController.textFieldController.stopEditing()
        } else {
            cursorPosition = 999999999
            gameController.textFieldController.startEditing(this)
        }
    }
    var selectionStart: Int? = null

    fun hasSelection(): Boolean = selectionStart != null && selectionStart != cursorPosition

    fun getSelectedRange(): IntRange? {
        return if (hasSelection()) {
            val start = selectionStart!!
            val end = cursorPosition
            if (start < end) start until end else end until start
        } else null
    }
    
    fun insertCharAtCursor(c: Char) {
        if (hasSelection()) {
            deleteSelection()
        }
        if ((c == '\n' && multiline == false) == false) {
            inputString = inputString.substring(0, cursorPosition) + c + inputString.substring(cursorPosition)
        }
        cursorPosition++
        selectionStart = null
    }
    
    fun deleteCharBeforeCursor() {
        if (hasSelection()) {
            deleteSelection()
            return
        }
        if (cursorPosition > 0) {
            inputString = inputString.removeRange(cursorPosition - 1, cursorPosition)
            cursorPosition--
        }
    }
    
    fun deleteSelection() {
        val range = getSelectedRange() ?: return
        inputString = inputString.removeRange(range)
        cursorPosition = range.first
        selectionStart = null
    }
    
    override fun renderElement(mainRender: MainRender) {
        val lg = mainRender.lg
    
        // Отрисовка фона
        RenderElements.renderBlock(
            posX = PX, posY = PY,
            width = SX, height = SY,
            borderRadius = borderRadius,
            mainRender = mainRender,
            clr = panelColor
        )
    
        // Подготовка текста
        val displayText = if (inputString.isEmpty())
            if (textTranslate) gameController.languageController.getText(text) else text
        else inputString
    
        // Вычисления
        val lineHeight = textSize * 1.3f
        val lines = displayText.split('\n')
    
        // Выделение
        renderSelection(mainRender, lines, lineHeight)
    
        // Текст
        
        lg.setTextAlign(-1, 1)
        lg.pg.fill(textColor.red.toFloat(), textColor.green.toFloat(), textColor.blue.toFloat(), textColor.alpha.toFloat())
        lg.setText(displayText, TPX, TPY, textSize)
    
        // Курсор
        if (isEditing) {
            renderCursor(mainRender, lines, lineHeight)
        }
    }
    
    private fun renderSelection(mainRender: MainRender, lines: List<String>, lineHeight: Float) {
        getSelectedRange()?.let { range ->
            var lineStart = 0
            val start = range.first
            val end = range.last + 1
            val cursorY = TPY + textSize * 0.2f
    
            for ((i, line) in lines.withIndex()) {
                val lineEnd = lineStart + line.length
                if (lineEnd < start) {
                    lineStart += line.length + 1
                    continue
                }
                if (lineStart > end) break
    
                val selStart = (start - lineStart).coerceIn(0, line.length)
                val selEnd = (end - lineStart).coerceIn(0, line.length)
                val x1 = TPX + mainRender.lg.getTextWidth(line.substring(0, selStart), textSize)
                val x2 = TPX + mainRender.lg.getTextWidth(line.substring(0, selEnd), textSize)
    
                val y = cursorY - i * lineHeight * 1.2f
                mainRender.lg.fill(100f, 150f, 255f, 120f)
                mainRender.lg.setBlock(
                    x1 + (x2 - x1) / 2f,
                    y,
                    x2 - x1,
                    lineHeight
                )
    
                lineStart += line.length + 1
            }
        }
    }
    
    private fun renderCursor(mainRender: MainRender, lines: List<String>, lineHeight: Float) {
        var remaining = cursorPosition
        var lineIndex = 0
        var charIndexInLine = 0
    
        for ((i, line) in lines.withIndex()) {
            if (remaining <= line.length) {
                charIndexInLine = remaining
                lineIndex = i
                break
            } else {
                remaining -= line.length + 1
            }
        }
    
        val cursorX = TPX + mainRender.lg.getTextWidth(lines[lineIndex].substring(0, charIndexInLine), textSize)
        val cursorY = TPY + textSize * 0.2f - (lineIndex * lineHeight) * 1.2f
    
        mainRender.lg.fill(50f, 150f, 220f, 160f)
        mainRender.lg.setBlock(
            cursorX,
            cursorY,
            textSize / 6f,
            lineHeight
        )
    }
    

    fun getCursorPositionAt(mouseX: Float, mouseY: Float): Int {
        val lines = inputString.split('\n')
        var yOffset = TPY + textSize * 0.2f
        val lineHeight = textSize * 1.3f
        var currentPos = 0
    
        for (i in lines.indices) {
            val line = lines[i]
            val lineY = yOffset - i * lineHeight * 1.24f
            if (mouseY > lineY - lineHeight / 2 && mouseY < lineY + lineHeight / 2) {
                // Текущая строка
                for (j in 0..line.length) {
                    val sub = line.substring(0, j)
                    val width = gameController.mainRender.lg.getTextWidth(sub, textSize)
                    val charX = TPX + width
                    if (mouseX < charX) {
                        return currentPos + j
                    }
                }
                return currentPos + line.length
            }
            currentPos += line.length + 1
        }
        return inputString.length
    }
    
    fun clearInput() {
        if (isEditing) {
            gameController.textFieldController.stopEditing()
        }
        inputString = ""
    }
}
