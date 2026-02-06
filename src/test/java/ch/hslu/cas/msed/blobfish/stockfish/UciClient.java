package ch.hslu.cas.msed.blobfish.stockfish;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.Socket;
import java.time.Duration;

import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

class UciClient implements AutoCloseable {

    private final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out;

    public UciClient(String host, int port) throws IOException {
        if (StringUtils.isBlank(host)) {
            throw new IllegalArgumentException("host can't be blank");
        }
        this.socket = new Socket(host, port);
        this.socket.setKeepAlive(true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        send("uci");
        waitTillReceive("uciok", Duration.ofSeconds(10));
        System.out.println("sucessfull initated uci connection");
    }

    public void setMultiPV(int value) {
        System.out.println("setMultiPV to " + value);
        send("setoption name MultiPV value " + value);
        waitTillReceiveStartsWith("setoption name MultiPV", Duration.ofMillis(300));
    }

    private void waitTillReceive(String message, Duration duration) {
        await().atMost(duration)
                .until(() -> {
                    if (in.ready()) {
                        String line = in.readLine();
                        if (line == null) {
                            return false;
                        }
                        return message.equals(line);
                    }
                    return false;
                });
    }

    private void waitTillReceiveStartsWith(String prefix, Duration duration) {
        await().atMost(duration)
                .until(() -> {
                    if (in.ready()) {
                        String line = in.readLine();
                        if (line == null) {
                            return false;
                        }
                        return line.startsWith(prefix);
                    }
                    return false;
                });
    }

    private void send(String cmd) {
        try {
            out.write(cmd);
            out.write("\n");
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        System.out.println("Closes UciClient");
        this.out.close();
        this.in.close();
        this.socket.close();
    }
}
