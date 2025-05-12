package la.vok.LoadData

import la.vok.LavokLibrary.Functions
import la.vok.Storages.Storage
import la.vok.Storages.Settings
import processing.core.PApplet
import processing.core.PImage
import la.vok.Render.LCanvas
import la.vok.GameController.GameController

class UILoader(var gameController: GameController) {
    var UIData: UIData

    init {
        UIData = UIData()
        UIData.loadData()
    }

    fun getPatch(key: String): String {
        if (hasKey(key)) {
            return UIData.getString(key)
        }
        return ""
    }
    fun hasKey(key: String): Boolean {
        return UIData.data.containsKey(key);
    }
}

class UIData() : JsonDataLoader() {
    override fun loadDataFromFolder(patch: String) {
        data.clear()
        val filesList = Functions.scanDirRecursive(patch)
        
        for (file in filesList) {
            println(Functions.getNameFromPath(file) + " " + file);
            data[Functions.getNameFromPath(file)] = file
        }
    }

    fun loadData() {
        var UIPatch = Settings.UIPatch;
        val patch = Functions.resourceDir("$UIPatch/")
        loadDataFromFolder(patch)
    }
}
