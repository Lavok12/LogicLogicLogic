package la.vok.GameController.Content.Chat

import la.vok.GameController.*
import la.vok.LavokLibrary.copy
import la.vok.UI.Elements.LText
import processing.data.JSONObject
import java.awt.Color

class ChatMessage(var gameController: GameController, var autor: String, var text: String, var otherData: JSONObject = JSONObject()) {
    var time = System.currentTimeMillis()
    var number = 0

    var color = Color(255,255,255)

    fun getFullText(): String {
        if (autor != "") {
            return "$autor : $text"
        } else {
            return "$text"
        }
    }
    fun getRawData() : JSONObject {
        var json = JSONObject()
        json.put("autor", autor)
        json.put("text", text)
        json.put("finalText", getFullText())
        json.put("r", color.red)
        json.put("g", color.green)
        json.put("b", color.blue)
        return json
    }

    fun updateElement() {
        textElement!!.textColor = color.copy()
        textElement!!.text = getFullText()
    }

    fun updateColor(color: Color) {
        textElement!!.textColor = color.copy()
        textElement!!.text = getFullText()
    }

    var textElement: LText? = null
}