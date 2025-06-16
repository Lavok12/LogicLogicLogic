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
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.UI.Canvas.*
import la.vok.UI.Scenes.*


class LTextField(
    gameController: GameController,
    pos: Vec2 = Vec2(0f, 0f),
    wh: Vec2 = Vec2(200f, 100f),
    align: Vec2 = Vec2(0f, 0f),
    parentCanvas: LCanvas = Storage.gameController.getCanvas(),
    var text: String = "Text",
    var inputString: String = "",
    var multiline: Boolean = false,
    var textTranslate: Boolean = true,
    var fontSize: Float = 30f,
    var textColor: Color = Color(180, 180, 180),
    var inputTextColor: Color = Color(255, 255, 255),
    var textDelta: Vec2 = Vec2(0f, 0f),
    var textPosAlign: Vec2 = Vec2(0f, 0f),
    var panelColor: Color = Color(100, 100, 100, 255),
    var borderRadius: Float = 0f,
    tag: String = ""
) : LElement(
    gameController,
    pos, wh,
    align,
    parentCanvas,
    -1f, -1f, Vec2(0f),
    tag
) {
    private var textVisualPos: Vec2 = Vec2(0f)
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
                pos = json.getVec2("pos", Vec2(0f)),
                wh = json.getVec2("wh", Vec2(0f)),
                align = json.getVec2("align", Vec2(0f)),
                parentCanvas = parentCanvas,
                text = json.LgetString("text", "Text"),
                inputString = json.LgetString("inputString", ""),
                multiline = json.LgetBoolean("text", false),
                textTranslate = json.LgetBoolean("textTranslate", true),
                fontSize = json.LgetFloat("fontSize", 30f),
                textColor = Functions.getColorFromJSON(json, "textColor", Color(180, 180, 180)),
                inputTextColor = Functions.getColorFromJSON(json, "textColor", Color(255, 255, 255)),
                textDelta = json.getVec2("textDelta", Vec2(0f)),
                textPosAlign = json.getVec2("textPosAlign", Vec2(0f)),
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
        textVisualPos = visualPos + textDelta * parentCanvas.scale  * parentCanvas.localScale
        textVisualPos.x -= wh.x / 2f
        textVisualPos.x += 15f
        textVisualPos.y -= fontSize / 4f
        textSize = parentCanvas.applyCanvasTextSize(fontSize * parentCanvas.textScale)
    }
    override fun updateGridVisuals(newPos: Vec2) {
        super.updateGridVisuals(newPos)
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
            pos = visualPos,
            wh = visualSize,
            borderRadius = borderRadius,
            mainRender = mainRender,
            clr = panelColor
        )
    
        // Подготовка текста
        val displayText = if (inputString.isEmpty())
            if (textTranslate) gameController.loaders.language.getText(text) else text
        else inputString
    
        // Вычисления
        val lineHeight = textSize * 1.3f
        val lines = displayText.split('\n')
    
        // Выделение
        renderSelection(mainRender, lines, lineHeight)
    
        // Текст
        
        lg.setTextAlign(-1, -1)
        lg.pg.fill(textColor.red.toFloat(), textColor.green.toFloat(), textColor.blue.toFloat(), textColor.alpha.toFloat())
        lg.setText(displayText, textVisualPos, textSize)
    
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
            val cursorY = textVisualPos.y + textSize * 0.2f
    
            for ((i, line) in lines.withIndex()) {
                val lineEnd = lineStart + line.length
                if (lineEnd < start) {
                    lineStart += line.length + 1
                    continue
                }
                if (lineStart > end) break
    
                val selStart = (start - lineStart).coerceIn(0, line.length)
                val selEnd = (end - lineStart).coerceIn(0, line.length)
                val x1 = textVisualPos.x + mainRender.lg.getTextWidth(line.substring(0, selStart), textSize)
                val x2 = textVisualPos.x + mainRender.lg.getTextWidth(line.substring(0, selEnd), textSize)
    
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
    
        val cursorX = textVisualPos.x + mainRender.lg.getTextWidth(lines[lineIndex].substring(0, charIndexInLine), textSize)
        val cursorY = textVisualPos.y + textSize * 0.2f - (lineIndex * lineHeight) * 1.2f
    
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
        var yOffset = textVisualPos.y + textSize * 0.2f
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
                    val charX = textVisualPos.y + width
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
