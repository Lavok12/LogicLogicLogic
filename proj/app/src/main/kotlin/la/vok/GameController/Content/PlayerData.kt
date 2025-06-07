package la.vok.GameController.Content

import la.vok.Storages.Storage
import la.vok.UI.*
import processing.data.JSONObject
import la.vok.GameController.GameController
import la.vok.GameController.Client.Camera
import la.vok.UI.Elements.LText
import la.vok.GameController.Client.Rendering.*

class PlayerData(var id: String, var name: String, var gameController: GameController) : IRender {
    override var renderLayersData: RenderLayersData = RenderLayersData(
        RenderLayer(Layers.B3, 5, this::render)
    )
    override val updateVisualF: (MainRender) -> Unit by lazy { this::updateVisual }
    override var isVisible = true

    var canvas: LCanvas = LCanvas(0f, 0f, 100f, 100f, 1f, 1f, 1f, 1f, 1f, 2, gameController)

    init {
        canvas.addChild("playerCanvas", "text")
    }

    var PX: Float = 0f
    var PY: Float = 0f

    var VX: Float = 0f
    var VY: Float = 0f
    var VZ: Float = 0f
    
    var DELETE_FLAG = true
    
    fun updateCanvas() {
        if (isVisible) {
            canvas.posX = VX
            canvas.posY = VY - 60
            (canvas.getElementByTag("text") as LText).text = name
            gameController.lCanvasController.add(canvas)
        } else {
            gameController.lCanvasController.remove(canvas)
        }
    }

    fun toJsonObject(): JSONObject {
        var json = JSONObject()
        json.put("id", id)
        json.put("name", name)
        json.put("PX", PX)
        json.put("PY", PY)
        return json
    }

    companion object {
        fun fromJsonObject(JSONObject: JSONObject, gameController: GameController) : PlayerData {
            val id = JSONObject.getString("id", "")
            val name = JSONObject.getString("name", "")
            val playerData = PlayerData(id, name, gameController)
            
            playerData.PX = JSONObject.getFloat("PX")
            playerData.PY = JSONObject.getFloat("PY")
            
            return playerData
        }
    }

    fun updateVisual(mainRender: MainRender) {
        VX = mainRender.camera.camX(PX)
        VY = mainRender.camera.camY(PY)
        VZ = mainRender.camera.camZ(1f)
    }

    fun destroy() {
        gameController.lCanvasController.remove(canvas)
    }

    fun render(mainRender: MainRender) {
        mainRender.lg.fill(255f)
        mainRender.lg.setEps(VX, VY, VZ * 40f, VZ * 80f)
    }
}