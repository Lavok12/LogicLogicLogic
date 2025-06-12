package la.vok.GameController.Content.Chat

import la.vok.GameController.*
import la.vok.UI.Elements.LText
import processing.data.JSONObject
import java.awt.Color

class ChatMessage(var gameController: GameController, var autor: String, var text: String, var otherData: JSONObject = JSONObject()) {
    var time = System.currentTimeMillis()
    var number = 0

    var r = 255
    var g = 255
    var b = 255

    fun getFullText(): String {
        return "$autor : $text"
    }
    fun getRawData() : JSONObject {
        var json = JSONObject()
        json.put("autor", autor)
        json.put("text", text)
        json.put("finalText", getFullText())
        json.put("r", r)
        json.put("g", g)
        json.put("b", b)
        return json
    }

    fun updateElement() {
        textElement?.textColor = Color(r, g, b)
        textElement?.text = getFullText()
    }

    var textElement: LText? = null
}