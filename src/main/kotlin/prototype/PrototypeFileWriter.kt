package prototype

import java.io.File

class PrototypeFileWriter {

    fun writeFile(filePath: String, text: String) {
        val file = File(filePath)
        if (!file.exists()) file.createNewFile()
        file.writeText(text)
    }

    fun readFile(filePath: String): String? {
        val file = File(filePath)
        return if (file.exists()) file.readText() else null
    }
}