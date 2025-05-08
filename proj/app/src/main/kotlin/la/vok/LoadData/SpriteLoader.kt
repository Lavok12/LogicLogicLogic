package la.vok.LoadData

import la.vok.LavokLibrary.Functions
import la.vok.Storages.Storage
import la.vok.Storages.Settings
import processing.core.PApplet
import processing.core.PImage

class SpriteLoader {
    var SpritesData: SpritesData
    var sprites: HashMap<String, LSprite> = HashMap()

    init {
        SpritesData = SpritesData()
        SpritesData.loadData()
    }

    fun getPatch(key: String): String {
        return SpritesData.getString(key)
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
            if (currentTime + Storage.spriteUploadTime < sprites[key]?.lastCalled!!) {
                sprites[key]?.removeSprite()
            }
        }
    }
}

class LSprite(var key: String, var SpriteLoader: SpriteLoader) {
    var img: PImage? = null
    get() {
        if (img == null) {
            loadSprite()
        }
        return img
    }
    
    var lastCalled: Int = 0;

    fun loadSprite() {
        var patch = SpriteLoader.getPatch(key)
        img = Storage.main.loadImage(patch)
    }
    fun removeSprite() {
        lastCalled = Storage.main.millis();
        if (img != null) {
            img = null
        }
    }
}

class SpritesData() : JsonDataLoader() {
    override fun loadDataFromFolder(patch: String) {
        data.clear()
        super.loadDataFromFolder(patch)
    }

    fun loadData() {
        var spritePatch = Settings.spritePatch;
        val patch = Functions.resourceDir("$spritePatch/")
        loadDataFromFolder(patch)
    }
}
