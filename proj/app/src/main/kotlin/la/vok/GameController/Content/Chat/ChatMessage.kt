package la.vok.GameController.Content.Chat

import la.vok.GameController.*
import processing.data.JSONObject

class ChatMessage(var gameController: GameController, var autor: String, var text: String, var otherData: JSONObject = JSONObject()) {
    var time = System.currentTimeMillis()
    
    fun getFullText(): String {
        return "$autor : $text"
    }
    fun getRawData() : JSONObject {
        var json = JSONObject()
        json.put("autor", autor)
        json.put("text", text)
        json.put("finalText", getFullText())
        return json
    }
}