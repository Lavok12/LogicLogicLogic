package la.vok.LoadData

import la.vok.LavokLibrary.Functions
import la.vok.LoadData.LanguageData
import la.vok.Storages.Settings
import processing.data.JSONObject
import la.vok.GameController.GameController;

class LanguageController(var lang: String = "eng", var gameController: GameController) {
    var langData = HashMap<String, LanguageData>(); 

    fun loadData() {
        addLang(lang);
    }

    fun changeLang(lang: String) {
        if (!langData.containsKey(lang)) {
            addLang(lang);
        }
        this.lang = lang;
    }

    fun getText(key: String): String {
        if (!langData.containsKey(lang)) {
            return key;
        }
        return langData[lang]?.getString(key) ?: key
    }


    fun addLang(lang: String) {
        if (langData.containsKey(lang)) {
            return;
        }
        langData[lang] = LanguageData(lang);
        langData[lang]?.loadData();
    }

    fun removeLang(lang: String) {
        langData.remove(lang);
    }
}


class LanguageData(var lang: String) : JsonDataLoader() {
    override fun loadDataFromFolder(path: String) {
        data.clear()
        super.loadDataFromFolder(path)
    }

    fun loadData() {
        var langPath = Settings.languagePath;
        val path = Functions.resourceDir("$langPath/$lang")
        loadDataFromFolder(path)
    }
}
