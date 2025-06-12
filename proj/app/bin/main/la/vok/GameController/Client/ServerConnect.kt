package la.vok.GameController.Client

import la.vok.Storages.*
import la.vok.GameController.Content.*
import la.vok.GameController.Server.*

class ServerConnect(clientController: ClientController) {
    var lastUpdate: Int
    var ping: Int = 0
    
    init {
        lastUpdate = Storage.main.millis()
    }
    fun updateLastTime() {
        lastUpdate = Storage.main.millis()
    }
}