package la.vok.GameController.Server 

import la.vok.GameController.Content.PlayerData
import la.vok.LavokLibrary.Vectors.Vec2
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
        playerData.pos = Vec2(0f, 0f)
    }
}