package edu.coursera.distributed;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * A basic and very limited implementation of a file server that responds to GET
 * requests from HTTP clients.
 */
@SuppressWarnings("WeakerAccess")
public final class FileServer {

    private static final String GET = "GET";
    private static final String PROTOCOL_1_0 = "HTTP/1.0";
    private static final String PROTOCOL_1_1 = "HTTP/1.1";
    private static final String READ_FILE_PREFIX = GET + " ";
    private static final String READ_FILE_SUFFIX = " " + PROTOCOL_1_1;
    private static final String OK_CODE = "200";
    private static final String NOT_FOUND_CODE = "404";
    private static final String OK_MESSAGE = "OK";
    private static final String NOT_FOUND_MESSAGE = "Not Found";
    private static final String SERVER_INFORMATION = "Server: FileServer";
    private static final String LINE_FEED = "\r\n";

    /**
     * Main entry point for the basic file server.
     *
     * @param serverSocket Provided socket to accept connections on.
     * @param filesystem A proxy filesystem to serve files from. See the PCDPFilesystem
     *           class for more detailed documentation of its usage.
     * @throws IOException If an I/O error is detected on the server. This
     *                     should be a fatal error, your file server
     *                     implementation is not expected to ever throw
     *                     IOExceptions during normal operation.
     */
    public void run(final ServerSocket serverSocket, final PCDPFilesystem filesystem) throws IOException {
        /*
         * Enter a spin loop for handling client requests to the provided
         * ServerSocket object.
         */
        while (!serverSocket.isClosed()) {
            try(
                    Socket socket = obtainSocket(serverSocket);
                    BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(socket.getInputStream(), Charset.defaultCharset())
                    );
                    BufferedWriter bufferedWriter = new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream(), Charset.defaultCharset())
                    )
            ) {
                String line = bufferedReader.readLine();
                writeFile(filesystem, obtainFile(line), bufferedWriter);
            }
        }
    }

    /**
     * 1) Use socket.accept to get a Socket object
     */
    private Socket obtainSocket(ServerSocket serverSocket) throws IOException {
        return serverSocket.accept();
    }

    /**
     * 2) Using Socket.getInputStream(), parse the received HTTP
     * packet. In particular, we are interested in confirming this
     * message is a GET and parsing out the path to the file we are
     * GETing. Recall that for GET HTTP packets, the first line of the
     * received packet will look something like:
     *
     *     GET /path/to/file HTTP/1.1
     */
    private PCDPPath obtainFile(String line) {
        if(line.startsWith(READ_FILE_PREFIX) && line.endsWith(READ_FILE_SUFFIX)) {
            String filePath = line.substring(READ_FILE_PREFIX.length(), line.length() - READ_FILE_SUFFIX.length());
            return new PCDPPath(filePath);
        } else {
            return null;
        }
    }

    /**
     * 3) Using the parsed path to the target file, construct an
     * HTTP reply and write it to Socket.getOutputStream(). If the file
     * exists, the HTTP reply should be formatted as follows:
     *
     *   HTTP/1.0 200 OK\r\n
     *   Server: FileServer\r\n
     *   \r\n
     *   FILE CONTENTS HERE\r\n
     *
     * If the specified file does not exist, you should return a reply
     * with an error code 404 Not Found. This reply should be formatted
     * as:
     *
     *   HTTP/1.0 404 Not Found\r\n
     *   Server: FileServer\r\n
     *   \r\n
     *
     * Don't forget to close the output stream.
     */
    private void writeFile(PCDPFilesystem filesystem, PCDPPath file, BufferedWriter bufferedWriter) throws IOException {
        if(file != null) {
            String fileContent = filesystem.readFile(file);
            if (fileContent != null) {
                bufferedWriter.write(PROTOCOL_1_0 + " " + OK_CODE + " " + OK_MESSAGE + LINE_FEED);
                bufferedWriter.write(SERVER_INFORMATION + LINE_FEED);
                bufferedWriter.write(LINE_FEED);
                bufferedWriter.write(fileContent);
                bufferedWriter.write(LINE_FEED);
            } else {
                bufferedWriter.write(PROTOCOL_1_0 + " " + NOT_FOUND_CODE + " " + NOT_FOUND_MESSAGE + LINE_FEED);
                bufferedWriter.write(SERVER_INFORMATION + LINE_FEED);
                bufferedWriter.write(LINE_FEED);
            }
            bufferedWriter.flush();
        }
    }
}
