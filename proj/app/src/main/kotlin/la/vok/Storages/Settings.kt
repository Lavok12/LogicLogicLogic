package la.vok.Storages

import la.vok.LavokLibrary.copy
import java.awt.Color

object Settings {
    var isLocal: Boolean = false
    var isServer: Boolean = true
    var isClient: Boolean = true
    var language: String = "eng"
    var languagesPath: String = "lang"
    var spritesPath: String = "paths/sprites"
    var UIPath: String = "paths/ui"
    var scriptsPath: String = "paths/scripts"
    var commandsPath: String = "commands"
    var scenesPath: String = "paths/scenes"

    var spriteUploadTime: Int = 100*120;
    var updateIntervalFrames = 10L
    var playersKickTime = 15000
    var playerDisconnectTime = 4000
    var pingInterval = 200L
    var keyRepeatDelay = 25 
    var keyRepeatRate = 1 

    var canvasRenderLayers = 10
    var address = "ws://26.229.52.4:8810"
    var port = 8810

    var serverChatHistory = 100
    var clientChatHistory = 100
    var clientChatHistoryTime = 20000

    var errorMessage = Color(230, 50, 50)
        get() {
            return field.copy()
        }
    var standartMessage = Color(255, 255, 255)
        get() {
            return field.copy()
        }
    var systemMessage = Color(180, 230, 220)
        get() {
            return field.copy()
        }
    var systemMessage2 = Color(120,230,120)
        get() {
            return field.copy()
        }
    var localMessage = Color(150,150,150)
        get() {
            return field.copy()
        }
}