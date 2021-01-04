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

        shellScript {
            content = """
                cd JavaAnalyser
                ./gradlew build
            """.trimIndent()
        }

        shellScript {
            content = """
                pwd
                cd ../Deployment
                pwd
            """.trimIndent()
        }
    }
}