package la.vok.LoadData

import la.vok.LavokLibrary.Functions
import la.vok.LoadData.LanguageData
import la.vok.Storages.Settings
import processing.data.JSONObject
import la.vok.GameController.GameController;
import la.vok.UI.LScene

class ScenesLoader(var gameController: GameController) {
    var sceneData = SceneData(gameController);

    fun loadData() {
        sceneData.loadData()
    }
    fun getPath(key: String): String {
        return sceneData.getString(key)
    }

    fun getScene(key: String): LScene {
        return sceneData.getScene(key)
    }
}


class SceneData(var gameController: GameController) : JsonDataLoader() {
    var scenes = HashMap<String, LScene>()

    override fun loadDataFromFolder(path: String) {
        val filesList = Functions.scanDirRecursive(path)
        for (file in filesList) {
            println("file $file")
            try {
                val json = Functions.loadJSONObject("$file")
                for (key in json.keys()) {
                    if (!scenes.containsKey(key as String)) {
                        scenes[key] = LScene(key, gameController = gameController)
                    }
                    var jpath = json.getString(key)
                    scenes[key]!!.addPath(jpath)
                    println("   key: $key, value: ${jpath}")
                }
            } catch (e: Exception) {
                println("Error JSON '$file' -- '$path': ${e.message}")
            }
        }
        
    }

    fun loadData() {
        var scenePath = Settings.scenePath;
        val path = Functions.resourceDir("$scenePath")
        loadDataFromFolder(path)
    }

    fun getScene(key: String): LScene {
        if (!scenes.containsKey(key)) { 
            if (!scenes.containsKey("")) {
                scenes[""] = LScene("", gameController = gameController)
                println("Scene with key '$key' not found, using default scene." +
                        " Please check your scene paths and keys.")
            }
            return scenes[""]!!
        }
        return scenes[key]!!
    } 
}
