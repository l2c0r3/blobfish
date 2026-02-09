package ch.hslu.cas.msed.blobfish.stockfish;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.Socket;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

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
        sendAndWaitForAck("setoption name MultiPV value " + value);
    }

    /**
     * Set up the position described in fenstring.
     * If the game was played from the start position the string startpos must be sent.
     */
    public void setPosition(String fen, String... moves) {
        System.out.printf("set position with fen [%s] and moves %s %n", fen, Arrays.toString(moves));

        StringBuilder commandBuilder = new StringBuilder();

        if ("startpos".equals(fen)) {
            commandBuilder.append("position startpos");
        } else {
            commandBuilder.append("position fen ").append(fen);
        }

        if (moves != null && moves.length != 0) {
            commandBuilder.append(" moves");
            for (var mov : moves) {
                commandBuilder.append(" ").append(mov);
            }
        }

        var command = commandBuilder.toString();
        sendAndWaitForAck(command);
    }

    public List<String> go(int depth) {
        send("go depth " + depth);
        in.lines().forEach(System.out::println);
        return List.of("");
    }

    private void sendAndWaitForAck(String command) {
        send(command);
        waitTillReceive(command, Duration.ofMillis(500));
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
