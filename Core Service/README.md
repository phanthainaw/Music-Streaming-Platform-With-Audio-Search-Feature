# Music Streaming Platform With Audio Search Feature

## Configuration

### Application Port Configuration

By default, the application runs on port 8080. You can change this in the following ways:

1. **Using application.properties**:
   
   Open the `src/main/resources/application.properties` file and modify the `server.port` property:
   
   ```properties
   # Configure the server port (default is 8080)
   server.port=8080
   ```
   
   Change `8080` to your desired port number.

2. **Using command-line arguments**:
   
   You can override the port when starting the application:
   
   ```bash
   java -jar your-application.jar --server.port=9090
   ```

3. **Using environment variables**:
   
   Set the `SERVER_PORT` environment variable:
   
   ```bash
   # For Windows
   set SERVER_PORT=9090
   
   # For Linux/Mac
   export SERVER_PORT=9090
   ```

Remember that the port must be available on your system. If another application is already using the specified port, the application will fail to start.