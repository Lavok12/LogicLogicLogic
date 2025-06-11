package la.vok.LoadData

import la.vok.LavokLibrary.*
import la.vok.Storages.Storage
import la.vok.Storages.Settings
import processing.core.PApplet
import processing.core.PImage
import processing.data.JSONObject
import la.vok.UI.Canvas.*
import la.vok.UI.Scenes.*
import la.vok.GameController.GameController

class UILoader(var gameController: GameController) {
    var UIData: UIData

    init {
        UIData = UIData()
    }
    fun loadData() {
        UIData.loadData()
    }

    fun getJson(key: String): JSONObject {
        if (hasKey(key)) {
            return UIData.UIData[key]?.loadJson() ?: JSONObject()
        }
        return JSONObject()
    }
    fun hasKey(key: String): Boolean {
        return UIData.UIData.containsKey(key);
    }
}

class UIData() : JsonDataLoader() {
    var UIData = HashMap<String, UIDataElement>()

    override fun loadDataFromFolder(path: String) {
        val filesList = Functions.scanDirRecursive(path)
        
        for (file in filesList) {
            val json = Functions.loadJSONObject("$file")
            println("file $file")
            for (key in json.keys()) {
                val ljson = json.getJSONObject(key as String);
                val path1 = ljson.getString("path")
                val jsonArray = ljson.getJSONArray("addData")
                val addData = ArrayList<String>()
                if (jsonArray != null) {
                    for (i in 0 until jsonArray.size()) {
                        addData.add(jsonArray.getString(i))
                    }
                }
                println("   key $key addData $addData path $path1")
                UIData[key as String] = UIDataElement(path1, addData)
            }
        }
    }

    fun loadData() {
        UIData.clear()
        var UIPath = Settings.UIPath;
        val path = Functions.resourceDir("$UIPath/")
        loadDataFromFolder(path)
    }
}

class UIDataElement(var path: String, var addData: ArrayList<String> = ArrayList()) {
    fun loadJson(): JSONObject {
        val json = Functions.loadJSONObject(path)
        for (i in addData) {
            json.add(Functions.loadJSONObject(i))
        }
        return json
    }
}