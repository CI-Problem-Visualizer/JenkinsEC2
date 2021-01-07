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
                
                # Next step requires getting the secret SSH keys for the
                # AWS EC2 node.
                # We don't store the secret in Space itself, because it's larger
                # than 1000 bytes. So we could store it in another tool that can
                # do secret management, like AWS KMS (and access it via the AWS
                # CLI, using credentials stored in Space).
                # ./analysis_server_bounce.sh
            """.trimIndent()
        }
    }
}