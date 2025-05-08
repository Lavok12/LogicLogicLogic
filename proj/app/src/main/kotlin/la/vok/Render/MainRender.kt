package la.vok.Render

import la.vok.Storages.Storage
import la.vok.LavokLibrary.LGraphics
import la.volk.Render.Elements.LButton
import java.awt.Button
import java.awt.Color

class MainRender {
    lateinit var lg: LGraphics
    var mainCanvas: LCanvas

    init {
        mainCanvas = LCanvas(0f, 0f, Storage.disW, Storage.disH, 1f, 1f);
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
        var buttonM = LButton(parentCanvas = mainCanvas, x = -0f, width = 1200f, height = 900f, buttonColor = Color(20,20,20),
        textPosAlignX = 0.8f, textPosAlignY = 0.6f, fontSize = 50f, text = "BigPanel"
        )
        var button = LButton(parentCanvas = buttonM.elementCanvas, alignX = -0.3f, width = 800f, height = 800f,
        textPosAlignX = 0.1f, textPosAlignY = 0.1f, fontSize = 50f, text = "Panel"
        )
        var button2 = LButton(parentCanvas = button.elementCanvas, alignX = -1.0f, alignY = -1.0f, percentWidth = 0.1f, percentHeight = 0.1f, text = "Button2",
        buttonColor = Color(50,20,20), offsetByWidth = 0.5f, offsetByHeight = 0.5f, x = 30f, y = 30f
        )
        var button3 = LButton(parentCanvas = button.elementCanvas, alignX = -1.0f, alignY = -1.0f, percentWidth = 0.1f, percentHeight = 0.1f, text = "Button2",
        buttonColor = Color(50,20,20), offsetByWidth = 0.5f, offsetByHeight = 1.5f, x = 30f, y = 60f
        )
        buttonM.render(this)
        button.render(this)
        button2.render(this);
        button3.render(this);
        
        updateDistaplay()
        endRender()
    }
}