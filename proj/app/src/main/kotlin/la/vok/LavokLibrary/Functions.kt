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
import java.net.URL
import java.awt.Color
import processing.core.*
import processing.data.JSONObject
import la.vok.App
import la.vok.Storages.Storage
import la.vok.LavokLibrary.*
import la.vok.LavokLibrary.Vectors.Vec2
import java.net.NetworkInterface
import java.util.*


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
    val parent: App
        get() = Storage.main

    fun r(num: Float): Int {
        return PApplet.round(num)
    }

    fun rand(randMin: Int, randMax: Int): Int {
        return parent.random(randMin.toFloat(), (randMax + 1).toFloat()).toInt()
    }
    fun xrot(x: Float, a: Float, r: Float): Float {
        return x + PApplet.sin(a) * r
    }
    
    fun yrot(y: Float, a: Float, r: Float): Float {
        return y + PApplet.cos(a) * r
    }
    
    fun tap(xPos: Float, yPos: Float, xSize: Float, ySize: Float, mx: Float, my: Float): Boolean {
        return (mx > xPos - xSize / 2 && mx < xPos + xSize / 2) &&
        (my > yPos - ySize / 2 && my < yPos + ySize / 2)
    }
    fun tap(position: Vec2, size: Vec2, mouse: Vec2): Boolean {
        return (mouse.x > position.x - size.x / 2 && mouse.x < position.x + size.x / 2) &&
                (mouse.y > position.y - size.y / 2 && mouse.y < position.y + size.y / 2)
    }
    fun tap(position: Vec2, size: Vec2, mx: Float, my: Float): Boolean {
        return (mx > position.x - size.x / 2 && mx < position.x + size.x / 2) &&
                (my > position.y - size.y / 2 && my < position.y + size.y / 2)
    }

    fun leng(X1: Float, Y1: Float): Float {
        return PApplet.sqrt(PApplet.pow(X1, 2f) + PApplet.pow(Y1, 2f))
    }
    
    fun leng(X1: Float, Y1: Float, Z1: Float): Float {
        return PApplet.sqrt(PApplet.sq(X1) + PApplet.sq(Y1) + PApplet.sq(Z1))
    }

    fun scanDir(directoryPath: String): List<String> {
        val directory = File(directoryPath)
        if (!directory.exists() || !directory.isDirectory) {
            println("Указанная директория не существует или не является директорией.")
            return emptyList()
        }
        
        return directory.listFiles()
            ?.filter { it.isFile } // Фильтруем, оставляя только файлы
            ?.map { it.name }    // Получаем имена файлов
            ?: emptyList()       // Возвращаем пустой список, если listFiles вернул null
    }

    
    fun scanDirRecursive(directoryPath: String): List<String> {
        val directory = File(directoryPath)
        if (!directory.exists() || !directory.isDirectory) {
            println("null")
            return emptyList()
        }

        return scanDirectory(directory)
    }

    private fun scanDirectory(dir: File): List<String> {
        val result = mutableListOf<String>()
        val files = dir.listFiles() ?: return result

        for (file in files) {
            if (file.isFile) {
                result.add(file.absolutePath) // можно заменить на file.name если нужен только файл без пути
            } else if (file.isDirectory) {
                result.addAll(scanDirectory(file)) // рекурсивный вызов для поддиректорий
            }
        }

        return result
    }

    fun getNameFromPath(path: String): String {
        return path.substringAfterLast(File.separator)
    }

    fun resourceDir(resourcePath: String): String {
        return parent.dataPath(resourcePath);
    }

    fun loadJSONObject(filePath: String): JSONObject {
        val json = parent.loadJSONObject(filePath)
        return json;
    }

    fun getColorFromJSON(json: JSONObject, key: String, default: Color): Color {
        return if (json.hasKey(key)) {
            val arr = json.getJSONArray(key)
            val r = arr.LgetInt(0, 0)
            val g = arr.LgetInt(1, 0)
            val b = arr.LgetInt(2, 0)
            val a = if (arr.size() > 3) arr.LgetInt(3, 255) else 255
            Color(r, g, b, a)
        } else {
            default
        }
    }

    fun loadFile(filePath: String): String {
        var path = parent.dataPath(filePath);
        val file = File(path)
        if (!file.exists()) {
            println("File not found: $filePath")
            return ""
        }
        return file.readText()
    }

    fun getUniqueDeviceId(): String {
        val interfaces = NetworkInterface.getNetworkInterfaces()
        for (iface in interfaces) {
            if (!iface.isLoopback && iface.hardwareAddress != null) {
                return iface.hardwareAddress.joinToString(":") { "%02X".format(it) }
            }
        }
        return UUID.randomUUID().toString() // fallback
    }    
}