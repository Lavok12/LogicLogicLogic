package la.vok.LavokLibrary

import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import processing.core.*
import processing.data.JSONObject
import la.vok.App
import la.vok.Storages.Storage

fun String.compress(): ByteArray? {
    return try {
        val byteStream = ByteArrayOutputStream()
        GZIPOutputStream(byteStream).use { it.write(this.toByteArray()) }
        byteStream.toByteArray()
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

fun ByteArray.decompress(): String? {
    return try {
        val byteStream = ByteArrayInputStream(this)
        GZIPInputStream(byteStream).use { gzip ->
            BufferedReader(InputStreamReader(gzip)).use { reader ->
                reader.readText()
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

object Functions {
    lateinit var parent: App
    fun r(num: Float): Int {
        return PApplet.round(num)
    }

    fun rand(randMin: Int, randMax: Int): Int {
        return parent.random(randMin.toFloat(), (randMax + 1).toFloat()).toInt()
    }

    object Functions {

        fun xrot(x: Float, a: Float, r: Float): Float {
            return x + PApplet.sin(a) * r
        }
    
        fun yrot(y: Float, a: Float, r: Float): Float {
            return y + PApplet.cos(a) * r
        }
    
        fun tap(xPos: Float, yPos: Float, xSize: Float, ySize: Float): Boolean {
            return tapPos(xPos, yPos, xSize, ySize, Storage.moux, Storage.mouy)
        }
    
        fun tapPos(xPos: Float, yPos: Float, xSize: Float, ySize: Float, mx: Float, my: Float): Boolean {
            return (mx > xPos - xSize / 2 && mx < xPos + xSize / 2) &&
                   (my > yPos - ySize / 2 && my < yPos + ySize / 2)
        }
    
        fun leng(X1: Float, Y1: Float): Float {
            return PApplet.sqrt(PApplet.pow(X1, 2f) + PApplet.pow(Y1, 2f))
        }
    
        fun leng(X1: Float, Y1: Float, Z1: Float): Float {
            return PApplet.sqrt(PApplet.sq(X1) + PApplet.sq(Y1) + PApplet.sq(Z1))
        }
    }
    
}