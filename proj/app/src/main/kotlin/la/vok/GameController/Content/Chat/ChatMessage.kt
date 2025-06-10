package la.vok.GameController.Content.Chat

import la.vok.GameController.*
import processing.data.JSONObject

class ChatMessage(var gameController: GameController, var autor: String, var text: String, var otherData: JSONObject = JSONObject()) {
    var time = System.currentTimeMillis()
    
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
}