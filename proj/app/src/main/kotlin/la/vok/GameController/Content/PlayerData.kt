package la.vok.GameController.Content

import la.vok.Storages.Storage
import la.vok.UI.LCanvas
import processing.data.JSONObject
import la.vok.GameController.GameController
import la.vok.GameController.Client.Camera
import la.volk.UI.Elements.LText

class PlayerData(var id: String, var name: String, var gameController: GameController) {
    var lastUpdate: Int = 0;
    var canvas: LCanvas = LCanvas(0f, 0f, 100f, 100f, 1f, 1f, 1f, 1f, 1f, gameController)

    init {
        updateTime()
        canvas.addChild("playerCanvas", "text")
    }

    var PX: Float = 0f
    var PY: Float = 0f

    var VX: Float = 0f
    var VY: Float = 0f
    var VZ: Float = 0f
    
    var SEND_PX = 0f
    var SEND_PY = 0f
    
    var DELETE_FLAG = true

    fun updateTime() {
        lastUpdate = Storage.main.millis()
    }

    fun updateOtherPlayer() {
        updateTime()
        PX = SEND_PX * 0.1f + PX * 0.9f
        PY = SEND_PY * 0.1f + PY * 0.9f
    }
    fun update() {
        updateTime()
    }

    fun updateVisual(camera: Camera) {
        VX = camera.camX(PX)
        VY = camera.camY(PY)
        VZ = camera.camZ(1f)

        canvas.posX = VX
        canvas.posY = VY - 60
        (canvas.getElementByTag("text") as LText).text = name
    }

    fun updateCoord(PX: Float, PY: Float) {
        this.PX = PX
        this.PY = PY
    }

    fun toJsonObject(): JSONObject {
        var json = JSONObject()
        json.put("id", id)
        json.put("name", name)
        json.put("PX", PX)
        json.put("PY", PY)
        json.put("lastUpdate", lastUpdate)
        return json
    }

    companion object {
        fun fromJsonObject(JSONObject: JSONObject, gameController: GameController) : PlayerData {
            val id = JSONObject.getString("id", "")
            val name = JSONObject.getString("name", "")
            val playerData = PlayerData(id, name, gameController)
            
            playerData.PX = JSONObject.getFloat("PX")
            playerData.PY = JSONObject.getFloat("PY")
            playerData.lastUpdate = JSONObject.getInt("lastUpdate")
            
            return playerData
        }
    }
}