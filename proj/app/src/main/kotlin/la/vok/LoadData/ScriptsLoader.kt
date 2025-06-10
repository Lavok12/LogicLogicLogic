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
        data.clear()
        super.loadDataFromFolder(path)
    }

    fun loadData() {
        var scriptPath = Settings.scriptPath;
        val path = Functions.resourceDir("$scriptPath")
        loadDataFromFolder(path)
    }
}
