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

    fun filesInDirectory(directoryPath: String): Collection<String> {
        val files = ArrayList<String>()
        File(directoryPath).walk().forEach { if (!it.isDirectory) files.add(it.name) }
        return files
    }
}