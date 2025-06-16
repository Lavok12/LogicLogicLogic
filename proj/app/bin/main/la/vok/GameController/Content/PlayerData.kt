package la.vok.GameController.Content

import la.vok.Storages.Storage
import la.vok.UI.*
import processing.data.JSONObject
import la.vok.GameController.GameController
import la.vok.GameController.Client.Camera
import la.vok.UI.Elements.LText
import la.vok.GameController.Client.Rendering.*
import la.vok.LavokLibrary.getVec2
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.Vec3
import la.vok.LavokLibrary.putVec
import la.vok.UI.Canvas.*
import la.vok.UI.Scenes.*

class PlayerData(var id: String, var name: String, var gameController: GameController) : IRender {
    override var renderLayersData: RenderLayersData = RenderLayersData(
        RenderLayer(Layers.B3, 5, this::render)
    )
    override val updateVisualF: (MainRender) -> Unit by lazy { this::updateVisual }
    override var isVisible = true

    var canvas: LCanvas = LCanvas(Vec2(0f), Vec2(100f), Vec2(1f), Vec2(1f), 1f, 2, false, gameController)

    init {
        canvas.addChild("playerCanvas", "text")
    }

    var pos = Vec2(0f)
    var visualPos = Vec3(0f)
    
    var DELETE_FLAG = true
    
    fun updateCanvas() {
        if (isVisible) {
            canvas.pos = visualPos.xy-Vec2(0f, 50f)
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
        json.putVec("pos", pos)
        return json
    }

    companion object {
        fun fromJsonObject(JSONObject: JSONObject, gameController: GameController) : PlayerData {
            val id = JSONObject.getString("id", "")
            val name = JSONObject.getString("name", "")
            val playerData = PlayerData(id, name, gameController)
            
            playerData.pos = JSONObject.getVec2("pos")
            
            return playerData
        }
    }

    fun updateVisual(mainRender: MainRender) {
        visualPos = Vec3(mainRender.camera.cam(pos), mainRender.camera.camZ(1f))
    }

    fun destroy() {
        gameController.lCanvasController.remove(canvas)
    }

    fun render(mainRender: MainRender) {
        mainRender.lg.fill(255f)
        mainRender.lg.setEps(visualPos.xy, Vec2(40f, 80f) * visualPos.z)
    }
}