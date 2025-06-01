package la.vok.UI

import la.vok.LavokLibrary.Functions
import processing.data.JSONObject
import la.vok.GameController.GameController
import la.vok.Storages.Storage

class LScene(var tag: String, var name: String, var paths: ArrayList<String> = ArrayList(), var gameController: GameController) {
    var elementTags: ArrayList<String> = ArrayList()
    var canvas: LCanvas
    var loaded = false;
    
    init {
        canvas = LCanvas(0f, 0f, Storage.gameController.rendering.disW, Storage.gameController.rendering.disH, 1f, 1f, gameController = gameController);
    }

    fun clearCanvas() {
        canvas = LCanvas(0f, 0f, Storage.gameController.rendering.disW, Storage.gameController.rendering.disH, 1f, 1f, gameController = gameController);
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
            clearCanvas();
            clear();
        }
        println("Loading scene with tag: $tag")
        for (path in paths) {
            println("   Loading path: $path")
            val json = Functions.loadJSONObject(path)
            for (key in json.keys()) {
                println("      key: $key, value: ${json.get(key as String)}")
                if (json.get(key as String) is String) {
                    val tag = json.getString(key)
                    if (tag != "") {
                        elementTags.add(tag)
                    }
                }
            }
            if (json.hasKey("add")) {
                println("      Adding tags: ")
                val arr = json.getJSONArray("add")
                for (i in 0 until arr.size()) {
                    val tag = arr.getString(i)
                    println("         Adding tag: $tag")
                    if (tag != "") {
                        elementTags.add(tag)
                    }
                }
            } 
            if (json.hasKey("remove")) {
                println("      Removing tags: ")
                val arr = json.getJSONArray("remove")
                for (i in 0 until arr.size()) {
                    val tag = arr.getString(i)
                    println("         Removing tag: $tag")
                    if (tag == "all") {
                        elementTags.clear()
                    } else if (tag != "") {
                        elementTags.remove(tag)
                    }
                }
            }
        }
    }
    fun addTagsToCanvas() {
        clearCanvas();
        for (tag in elementTags) {
            canvas.addChild(tag, tag)
        }
    }
}