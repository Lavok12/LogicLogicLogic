package la.vok.UI

import la.vok.LavokLibrary.Functions
import processing.data.JSONObject
import la.vok.GameController.GameController
import la.vok.Storages.Storage

class LScene(var tag: String, var paths: ArrayList<String> = ArrayList(), var gameController: GameController) {
    var elementTags: ArrayList<String> = ArrayList()
    var canvas: LCanvas
    var loaded = false;
    
    init {
        canvas = LCanvas(0f, 0f, Storage.disW, Storage.disH, 1f, 1f, gameController = gameController);
    }

    fun clearElements() {
        canvas = LCanvas(0f, 0f, Storage.disW, Storage.disH, 1f, 1f, gameController = gameController);
    }

    fun clear() {
        elementTags.clear()
    }
    
    fun addPath(path: String) {
        paths.add(path)
    }

    fun checkLoaded() {
        if (!loaded) {
            loadScene()
            loaded = true
        }
    }
    
    fun loadScene() {
        if (loaded) {
            clearElements();
            clear();
        }
        for (path in paths) {
            val json = Functions.loadJSONObject(path)
            for (key in json.keys()) {
                if (json.get(key as String) is String) {
                    val tag = json.getString(key)
                    if (tag != "") {
                        elementTags.add(tag)
                    }
                }
            }
            if (json.hasKey("add")) {
                val arr = json.getJSONArray("add")
                for (i in 0 until arr.size()) {
                    val tag = arr.getString(i)
                    if (tag != "") {
                        elementTags.add(tag)
                    }
                }
            } 
            if (json.hasKey("remove")) {
                val arr = json.getJSONArray("remove")
                for (i in 0 until arr.size()) {
                    val tag = arr.getString(i)
                    if (tag == "all") {
                        elementTags.clear()
                    } else if (tag != "") {
                        elementTags.remove(tag)
                    }
                }
            }
        }
    }
    
}