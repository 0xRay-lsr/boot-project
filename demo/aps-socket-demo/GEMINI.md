# Gemini Project Analysis: aps-socket-demo

## Project Overview

This project is a Java-based demonstration of a custom, thread-safe socket connection pooling library. It provides a robust implementation for managing and reusing socket connections to a specific host and port, which is critical for performance in applications that frequently communicate over TCP sockets.

The core logic is centered around the `SocketConnectionPool` class, which handles connection creation, validation, leasing, and lifecycle management (including idle timeouts and leak detection). A `SocketConnectionPoolManager` is used to manage multiple named connection pools.

The project is built using **Java 8** and **Maven**, with **SLF4J** and **Logback** for logging. It includes a clear entry point (`pool/Main.java`) that starts a mock server and demonstrates the pool's functionality.

Two different implementations of the connection pool are present:
- `src/main/java/cn/aps/boot/socket/demo/pool/`: The primary, more polished implementation used by the main demonstration class.
- `src/main/java/cn/aps/boot/socket/demo/nconn/`: An alternative or possibly earlier implementation with a slightly different structure and a more detailed statistics class.

## Building and Running

This is a standard Maven project. The following commands can be used to build and run the application.

### Build

To compile the source code and package it into a JAR file, run:

```bash
mvn package
```
This will create a JAR file with all dependencies included in the `target/` directory (e.g., `aps-socket-demo-1.0-SNAPSHOT-jar-with-dependencies.jar`).

### Run

To run the main demonstration, which starts a mock server and performs a borrow/release operation on the connection pool, execute the packaged JAR:

```bash
java -jar target/aps-socket-demo-1.0-SNAPSHOT-jar-with-dependencies.jar
```
Alternatively, you can run the main class directly using the Maven exec plugin:

```bash
mvn exec:java -Dexec.mainClass="cn.aps.boot.socket.demo.pool.Main"
```

### Test

To run the unit tests for the project, use:

```bash
mvn test
```

## Development Conventions

*   **Structure:** The project follows a standard Maven directory layout (`src/main/java`, `src/test/java`).
*   **Configuration:** The primary connection pool (`pool` package) is configured programmatically using a `PoolConfig.Builder` pattern, which provides a clean and readable way to set up pool parameters.
*   **Concurrency:** The connection pool is designed to be thread-safe, utilizing `BlockingQueue`, `ConcurrentHashMap`, `AtomicInteger`, and `ReentrantLock` to manage concurrent access.
*   **Resource Management:** The pool manages the lifecycle of socket connections, including background threads for monitoring idle connections and detecting leaks.
*   **Logging:** The project uses SLF4J for logging, allowing for flexible logging configuration via `src/main/resources/logback.xml`.
