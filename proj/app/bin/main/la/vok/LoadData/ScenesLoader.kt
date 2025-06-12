package la.vok.LoadData

import la.vok.LavokLibrary.Functions
import la.vok.LoadData.LanguageData
import la.vok.Storages.Settings
import processing.data.JSONObject
import la.vok.GameController.GameController;
import la.vok.UI.Canvas.*
import la.vok.UI.Scenes.*


class ScenesLoader(var gameController: GameController) {
    var scenesData = ScenesData(gameController);

    fun loadData() {
        scenesData.loadData()
    }
    fun getPath(key: String): String {
        return scenesData.getString(key)
    }

    fun newScene(name: String, key: String): LScene {
        println("Creating new scene with name '$name' and key '$key'")
        return scenesData.newScene(name, key)
    }
}


class ScenesData(var gameController: GameController) : JsonDataLoader() {
    var keys = HashMap<String, ArrayList<String>>()

    override fun loadDataFromFolder(path: String) {
        val filesList = Functions.scanDirRecursive(path)
        for (file in filesList) {
            println("file $file")
            try {
                val json = Functions.loadJSONObject("$file")
                for (key in json.keys()) {
                    if (!keys.containsKey(key as String)) {
                        keys[key] = ArrayList<String>()
                    }
                    var jpath = json.getString(key)
                    keys[key]!! += jpath
                    
                    println("   key: $key, value: ${jpath}")
                }
            } catch (e: Exception) {
                println("Error JSON '$file' -- '$path': ${e.message}")
            }
        }
    }

    fun newScene(name: String, key: String): LScene {
        val newScene = LScene(key, name, gameController = gameController)
        if (keys.containsKey(key)) {
            for (path in keys[key]!!) {
                newScene.addPath(path)
            }
        } else {
            println("No paths found for scene '$key'")
        }
        return newScene
    }

    fun loadData() {
        data.clear()
        var scenePath = Settings.scenePath;
        val path = Functions.resourceDir("$scenePath")
        loadDataFromFolder(path)
    }
}
