(
    cd java_server
    mvn -DskipTests package exec:java -Dexec.mainClass=com.example.grpc.App
)