package la.vok.Storages

object Settings {
    var isLocal: Boolean = false
    var isServer: Boolean = false
    var isClient: Boolean = true
    var language: String = "eng"
    var languagePath: String = "lang"
    var spritePath: String = "paths/sprites"
    var UIPath: String = "paths/ui"
    var scriptPath: String = "paths/scripts"
    var scenePath: String = "paths/scenes"

    var spriteUploadTime: Int = 100*120;
    var updateIntervalFrames = 10L
    var playersKickTime = 4000
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
}