import java.io.File
import java.nio.file.Paths
import java.util.concurrent.TimeUnit

object RemoteSocket {
    fun get(): String {
        return jenkinsIp() + ":9000"
    }

    private fun jenkinsIp(): String {
        val deploymentDirectory = Paths.get("..", "Deployment").toFile()
        return "sh jenkins_ip.sh"
                .runCommand(deploymentDirectory)
    }
}

private fun String.runCommand(workingDir: File): String {
    val parts = this.split("\\s".toRegex())
    val proc = ProcessBuilder(*parts.toTypedArray())
            .directory(workingDir)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()
    proc.waitFor(5, TimeUnit.SECONDS)
    return proc.inputStream.bufferedReader().readText().trim()
}