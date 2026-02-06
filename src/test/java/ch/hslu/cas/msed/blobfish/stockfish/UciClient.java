package ch.hslu.cas.msed.blobfish.stockfish;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

class UciClient implements AutoCloseable {

    private final String host;
    private final int port;

    public UciClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void init() {
        try (Socket socket = new Socket(host, port);
             var in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {

            send(out, "uci");
            in.lines().forEach(System.out::println);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void send(BufferedWriter out, String cmd) throws IOException {
        out.write(cmd);
        out.write("\n");
        out.flush();
    }

    @Override
    public void close() throws Exception {

    }
}
