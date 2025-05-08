package la.vok.LoadData

import la.vok.LavokLibrary.Functions

open class JsonDataLoader {
    protected val data = HashMap<String, String>()

    open fun loadDataFromFolder(patch: String) {
        val filesList = Functions.scanDir(patch)
        println("Загружаю JSON файлы из '$patch'")
        for (file in filesList) {
            try {
                val json = Functions.loadJSONObject("$patch/$file")
                for (key in json.keys()) {
                    data[key as String] = json.getString(key)
                    println("key: $key, value: ${json.getString(key)}, file: $file")
                }
            } catch (e: Exception) {
                println("Ошибка при загрузке JSON файла '$file' из '$patch': ${e.message}")
            }
        }
    }
    
    fun getString(key: String): String {
        return data[key] ?: key
    }

    fun setString(key: String, value: String) {
        data[key] = value
    }
}