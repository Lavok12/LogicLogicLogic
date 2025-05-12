package la.vok.Render

import la.vok.Storages.Storage
import la.vok.LavokLibrary.LGraphics
import la.volk.Render.Elements.*
import java.awt.Button
import java.awt.Color

class MainRender {
    lateinit var lg: LGraphics
    var mainCanvas: LCanvas
    
    init {
        mainCanvas = LCanvas(0f, 0f, Storage.disW, Storage.disH, 1f, 1f);
        mainCanvas.addChild("ui2.JSON")
    }
    fun startRender() {
        Storage.lg.beginDraw();
    }
    fun endRender() {
        Storage.lg.endDraw();
    }
    fun clearLg() {
        Storage.lg.pg.background(0);
        lg.pg.noStroke();
        Storage.lg = Storage.lg;
    }
    fun clearDisplay() {
        Storage.main.background(0);
    }
    fun updateDistaplay() {
        clearDisplay();
        Storage.main.image(Storage.lg.pg, 0f, 0f, Storage.main.width.toFloat(), Storage.main.height.toFloat());
    }
    fun render() {
        lg = Storage.lg;
        startRender();
        clearLg();
        mainCanvas.width = Storage.lg.disW
        mainCanvas.height = Storage.lg.disH
        mainCanvas.renderElements();
        updateDistaplay()
        endRender()
    }
}