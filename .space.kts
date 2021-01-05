/**
 * cd JavaAnalyser
 * ./gradlew build
 * cd ../Deployment
 * if (node_ip.sh != empty):
 *   ./analysis_server_bounce.sh
 * else:
 *   Bring up the node
 * */
job("Analysis Server") {
    container("openjdk:11") {
        resources {
            memory = 2048
            cpu = 2048
        }

        // Build Analyser
        shellScript {
            content = """
                cd JavaAnalyser
                ./gradlew build
            """.trimIndent()
        }

        // Deploy it to the cloud
        shellScript {
            content = """
                cd Deployment
                # ./analysis_server_bounce.sh # We give Space the secret file.
            """.trimIndent()
        }
    }
}