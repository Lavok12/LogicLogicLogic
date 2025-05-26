package la.vok.LoadData

import la.vok.LavokLibrary.Functions
import la.vok.UI.LScene

class ScenesContainer {
    private val scenes = mutableMapOf<String, LScene>()

    fun addScene(scene: LScene) {
        scenes[scene.name] = scene
    }

    fun getScene(name: String): LScene? {
        return scenes[name]
    }

    fun removeScene(name: String) {
        scenes.remove(name)
    }
    
    fun removeScene(scene: LScene) {
        scenes.remove(scene.name)
    }

    fun clear() {
        scenes.clear()
    }

    fun listScenes(): List<String> {
        return scenes.keys.toList()
    }
}