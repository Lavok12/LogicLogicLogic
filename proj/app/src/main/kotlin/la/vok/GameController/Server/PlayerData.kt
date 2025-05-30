package la.vok.GameController.Server

import la.vok.Storages.Storage

class PlayerData(var id: String, var name: String) {
    var lastUpdate: Int = 0;

    init {
        updateTime()
    }

    var PX: Float = 0f
    var PY: Float = 0f

    fun updateTime() {
        lastUpdate = Storage.main.millis()
    }

    fun updateCoord(PX: Float, PY: Float) {
        this.PX = PX
        this.PY = PY
    }
}