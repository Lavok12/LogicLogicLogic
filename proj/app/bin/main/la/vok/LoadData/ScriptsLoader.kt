package la.vok.LoadData

import la.vok.LavokLibrary.Functions
import la.vok.Storages.Settings
import la.vok.GameController.GameController;


class ScriptsLoader(var gameController: GameController) {
    var luaData = LuaData()
    var ktsData = KtsData()

    fun loadData() {
        luaData.loadData()
        ktsData.loadData()
    }
    fun getLuaPath(key: String): String {
        return luaData.getString(key)
    }
    fun getKtsPath(key: String): String {
        return ktsData.getString(key)
    }
}


class LuaData() : JsonDataLoader() {
    override fun loadDataFromFolder(path: String) {
        super.loadDataFromFolder(path)
    }

    fun loadData() {
        data.clear()
        var scriptPath = Settings.scriptsPath;
        var path = Functions.resourceDir("$scriptPath/lua")
        loadDataFromFolder(path)
    }
}

class KtsData() : JsonDataLoader() {
    override fun loadDataFromFolder(path: String) {
        super.loadDataFromFolder(path)
    }

    fun loadData() {
        data.clear()
        var scriptPath = Settings.scriptsPath;
        var path = Functions.resourceDir("$scriptPath/kts")
        loadDataFromFolder(path)
    }
}
