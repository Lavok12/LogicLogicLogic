package la.vok.GameController.Server
import la.vok.Storages.*
import la.vok.GameController.Content.*
import la.vok.GameController.*

class ConnectsContainer(var serverController: ServerController) {
    var connects = HashMap<String, PlayerConnect>()
    
    fun pingAll() {
        for (i in connects.keys) {
            serverController.serverFunctions.ping(i)
        }
    }
    fun addConnect(id: String) {
        if (!connects.containsKey(id)) {
            connects[id] = PlayerConnect(serverController, id)
        }
    }
    fun contains(id: String) : Boolean {
        return connects.containsKey(id)
    }
    fun getConnect(id: String) : PlayerConnect {
        return connects[id]!!
    } 
    fun getPlayerData(id: String) : PlayerData {
        return getConnect(id).playerData
    }
    fun removeConnect(id: String) {
        if (contains(id)) {
            connects.remove(id)
        }
    }
    fun removeOldConnections() {
        for (i in connects.keys) {
            if (connects[i]!!.lastUpdate + Settings.playersKickTime < Storage.main.millis()) {
                serverController.serverFunctions.disconnectPlayer(i)
            }
        }
    }

    fun connectionsToPlayerContainer() : PlayersContainer {
        var p: PlayersContainer = PlayersContainer(serverController.gameController)
        for (i in connects.keys) {
            p.playersData[connects[i]!!.playerData.id] = connects[i]!!.playerData
        }
        return p
    }
}