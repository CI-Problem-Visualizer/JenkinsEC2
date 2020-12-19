import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.stream.Collectors

object TestDataFiles {
    fun contentOf(testDataFileName: String): String {
        val bufferedReader = BufferedReader(
            InputStreamReader(FileInputStream(File("src/test/resources/test-data-files/$testDataFileName")))
        )
        return bufferedReader.lines().collect(Collectors.joining("\n"))
    }
}