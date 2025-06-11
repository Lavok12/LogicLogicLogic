package la.vok.LoadData

import la.vok.LavokLibrary.Functions
import la.vok.LoadData.LanguageData
import la.vok.Storages.Settings
import processing.data.JSONObject
import la.vok.GameController.GameController;
import la.vok.UI.Canvas.*
import la.vok.UI.Scenes.*


class ScriptsLoader(var gameController: GameController) {
    var scriptsData = ScriptsData();

    fun loadData() {
        scriptsData.loadData()
    }
    fun getPath(key: String): String {
        return scriptsData.getString(key)
    }
}


class ScriptsData() : JsonDataLoader() {
    override fun loadDataFromFolder(path: String) {
        super.loadDataFromFolder(path)
    }

    fun loadData() {
        data.clear()
        var scriptPath = Settings.scriptPath;
        var path = Functions.resourceDir("$scriptPath/lua")
        loadDataFromFolder(path)
        path = Functions.resourceDir("$scriptPath/kts")
        loadDataFromFolder(path)
    }
}
