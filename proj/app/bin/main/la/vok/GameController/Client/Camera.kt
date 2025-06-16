package la.vok.GameController.Client

import la.vok.LavokLibrary.Vectors.Vec2

class Camera(var position: Vec2, var zoom: Float) {

    constructor(PX: Float, PY: Float, zoom: Float) : this(Vec2(PX, PY), zoom)

    // ===== Перевод координат по компонентам =====
    fun camX(x: Float): Float = (x - position.x) * zoom
    fun camY(y: Float): Float = (y - position.y) * zoom
    fun camZ(size: Float): Float = size * zoom

    fun worldX(x: Float): Float = x / zoom + position.x
    fun worldY(y: Float): Float = y / zoom + position.y
    fun worldZ(size: Float): Float = size / zoom

    // ===== Перевод векторов =====
    fun cam(vec: Vec2): Vec2 = (vec - position) * zoom
    fun world(vec: Vec2): Vec2 = vec / zoom + position

    fun setCameraPosition(pos: Vec2) {
        position = pos.copy()
    }

    fun setCameraPosition(pos: Vec2, zoom: Float) {
        position = pos.copy()
        this.zoom = zoom
    }

    fun setCameraPosition(zoom: Float) {
        this.zoom = zoom
    }

    fun moveCamera(target: Vec2, targetZoom: Float, percentage: Float) {
        position = position + (target - position) * percentage
        zoom += (targetZoom - zoom) * percentage
    }

    fun moveCamera(target: Vec2, percentage: Float) {
        position = position + (target - position) * percentage
    }

    fun moveCamera(targetZoom: Float, percentage: Float) {
        zoom += (targetZoom - zoom) * percentage
    }

    fun getScreenX(worldX: Float): Float = (worldX - position.x) * zoom
    fun getScreenY(worldY: Float): Float = (worldY - position.y) * zoom
    fun getScreenSize(worldSize: Float): Float = worldSize * zoom

    fun getWorldX(screenX: Float): Float = screenX / zoom + position.x
    fun getWorldY(screenY: Float): Float = screenY / zoom + position.y
    fun getWorldSize(screenSize: Float): Float = screenSize / zoom

    fun getScreenPos(worldPos: Vec2): Vec2 = cam(worldPos)
    fun getWorldPos(screenPos: Vec2): Vec2 = world(screenPos)
}
