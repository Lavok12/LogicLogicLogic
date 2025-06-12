package la.vok.GameController.Server 

import la.vok.GameController.Content.PlayerData
import la.vok.Storages.*

class PlayerConnect(var serverController: ServerController, var clientId: String) {
    lateinit var playerData: PlayerData
    var lastUpdate: Int
    var ping: Int = 0
    
    init {
        lastUpdate = Storage.main.millis()
    }
    fun updateLastTime() {
        lastUpdate = Storage.main.millis()
    }
    fun initPlayer(id: String, name: String) {
        playerData = PlayerData(id, name, serverController.gameController)
        playerData.PX = 500f
        playerData.PY = 100f
    }
}