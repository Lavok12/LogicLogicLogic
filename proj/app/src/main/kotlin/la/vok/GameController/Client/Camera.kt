package la.vok.GameController.Client

import la.vok.GameController.Content.Map.LogicMap
import la.vok.gameController.Content.Logic.LogicWire

class Camera(var PX: Float, var PY: Float, var zoom: Float) {

    fun camX(x: Float): Float {
        return (x - PX) * zoom
    }

    fun camY(y: Float): Float {
        return (y - PY) * zoom
    }

    fun camZ(size: Float): Float {
        return size * zoom
    }

    fun worldX(x: Float): Float {
        return x / zoom + PX
    }

    fun worldY(y: Float): Float {
        return y / zoom + PY
    }

    fun worldZ(size: Float): Float {
        return size / zoom
    }

    fun setCameraPosition(PX: Float, PY: Float) {
        this.PX = PX
        this.PY = PY
    }

    fun setCameraPosition(PX: Float, PY: Float, zoom: Float) {
        this.PX = PX
        this.PY = PY
        this.zoom = zoom
    }

    fun setCameraPosition(zoom: Float) {
        this.zoom = zoom
    }

    fun moveCamera(targetPX: Float, targetPY: Float, targetZoom: Float, percentage: Float) {
        PX += (targetPX - PX) * percentage
        PY += (targetPY - PY) * percentage
        zoom += (targetZoom - zoom) * percentage
    }

    fun moveCamera(targetPX: Float, targetPY: Float, percentage: Float) {
        PX += (targetPX - PX) * percentage
        PY += (targetPY - PY) * percentage
    }

    fun moveCamera(targetZoom: Float, percentage: Float) {
        zoom += (targetZoom - zoom) * percentage
    }

    fun getScreenX(worldX: Float): Float {
        return (worldX - PX) * zoom
    }

    fun getScreenY(worldY: Float): Float {
        return (worldY - PY) * zoom
    }

    fun getScreenSize(worldSize: Float): Float {
        return worldSize * zoom
    }

    fun getWorldX(screenX: Float): Float {
        return screenX / zoom + PX
    }

    fun getWorldY(screenY: Float): Float {
        return screenY / zoom + PY
    }

    fun getWorldSize(screenSize: Float): Float {
        return screenSize / zoom
    }

    fun RenderLogicElements(clientController: ClientController) {
        for (element in clientController.logicMap.list()) {
            element.render(this);
        }
    }
    fun RenderWires(clientController: ClientController) {
        
    }
}
