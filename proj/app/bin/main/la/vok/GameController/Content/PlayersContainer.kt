package la.vok.GameController.Content

import la.vok.GameController.Server.*
import la.vok.GameController.*
import la.vok.Storages.Storage
import processing.data.JSONObject
import la.vok.GameController.Client.Rendering.*

class PlayersContainer(var gameController: GameController) {
    var playersData = HashMap<String, PlayerData>()

    fun updateCanvas() {
        for (i in getAllPlayers()) {
            i.updateCanvas()
        }
    }
    fun getData(id: String): PlayerData {
        return playersData[id]!!
    }
    fun addData(id: String, name: String) {
        playersData.put(id, PlayerData(id, name, gameController))
    }
    fun checkData(id: String): Boolean {
        return playersData.containsKey(id)
    }
    fun removeData(id: String) {
        playersData[id]?.destroy()
        playersData.remove(id)
    }
    fun getAllPlayers(): Set<PlayerData> {
        return playersData.values.toSet()
    }
    fun getAllPlayersWithout(id: String): Set<PlayerData> {
        var ret = playersData.values.toSet()
        if (playersData.containsKey(id)) {
            ret -= playersData[id]!!
        }
        return ret
    }
    fun toJsonObject(): JSONObject {
        var json: JSONObject = JSONObject()
        for (i in playersData.keys) {
            var obj = playersData[i]
            json.put(i, obj!!.toJsonObject())
        }
        return json
    }
    fun setDeleteFlags() {
        for (i in getAllPlayers()) {
            i.DELETE_FLAG = true
        }
    }
    fun renderUpdate(renderBuffer: RenderBuffer) {
        for (i in getAllPlayers()) {
            var iRender: IRender = i
            iRender.renderUpdate(renderBuffer)
        }
    }
    fun deleteWithFlags() {
        for (i in playersData.keys) {
            var obj = playersData[i]
            if (obj!!.DELETE_FLAG) {
                removeData(i)
            }
        }
    }
    fun clear() {
        for (i in playersData.keys) {
            removeData(i)
        }
    }
    companion object {
        fun fromJsonObject(JSONObject: JSONObject, gameController: GameController) : PlayersContainer {
            var container = PlayersContainer(gameController)
            val keys = JSONObject.keys().iterator()
            while (keys.hasNext()) {
                val key = keys.next()
                val playerJson = JSONObject.getJSONObject(key as String)
                container.playersData[key] = PlayerData.fromJsonObject(playerJson, gameController)
            }
            return container
        }
    }
}