package la.vok.LoadData

import la.vok.LavokLibrary.Functions
import la.vok.Storages.Storage
import la.vok.Storages.Settings
import processing.core.PImage
import la.vok.GameController.GameController;

class SpriteLoader(var gameController: GameController) {
    var spritesData: SpritesData
    var sprites: HashMap<String, LSprite> = HashMap()

    init {
        spritesData = SpritesData()
    }
    fun loadData() {
        spritesData.loadData()
    }

    fun getPath(key: String): String {
        return spritesData.getString(key)
    }

    fun getSprite(key: String): LSprite? {
        if (sprites.containsKey(key)) {
        } else {
            var sprite = LSprite(key, this)
            sprites[key] = sprite
        }
        return sprites[key]
    }

    fun dynamicUnload() {
        var currentTime = Storage.main.millis()
        for (key in sprites.keys) {
            if (currentTime + Settings.spriteUploadTime < sprites[key]?.lastCalled!!) {
                sprites[key]?.removeSprite()
            }
        }
    }
}

class LSprite(var key: String, var SpriteLoader: SpriteLoader) {
    var img: PImage? = null
    get() {
        if (field == null) {
            loadSprite()
        }
        return field
    }
    
    var lastCalled: Int = 0;

    fun loadSprite() {
        var path = SpriteLoader.getPath(key)
        img = Storage.main.loadImage(path)
    }
    fun removeSprite() {
        lastCalled = Storage.main.millis();
        if (img != null) {
            img = null
        }
    }
}

class SpritesData() : JsonDataLoader() {
    override fun loadDataFromFolder(path: String) {
        super.loadDataFromFolder(path)
    }

    fun loadData() {
        data.clear()
        var spritePath = Settings.spritesPath;
        val path = Functions.resourceDir("$spritePath/")
        loadDataFromFolder(path)
    }
}
