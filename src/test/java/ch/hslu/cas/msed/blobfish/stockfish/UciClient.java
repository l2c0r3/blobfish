package ch.hslu.cas.msed.blobfish.stockfish;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.Socket;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.awaitility.Awaitility.await;

class UciClient implements AutoCloseable {

    private final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final UciResponseParser uciResponseParser = new UciResponseParser();

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

    /**
     * Output the N best lines (principal variations, PVs) when searching.
     */
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
        System.out.println("go depth " + depth);
        send("go depth " + depth);
        var response = readTillReceivePrefix("bestmove", Duration.ofSeconds(10));
        var result = uciResponseParser.parseBestMoves(response.toArray(new String[0]));
        System.out.println("best moves: " + result);
        return result;
    }

    /**
     * This is sent to the engine when the next search (started with position and go) will be from a different game.
     * This can be a new game the engine should play or a new game it should analyze but also the next position from a
     * test suite with positions only.
     */
    public void newGame() {
        System.out.println("starts new game");
        send("ucinewgame");
        send("isready");
        waitTillReceive("readyok", Duration.ofSeconds(10));
    }

    /**
     * The number of CPU threads used for searching a position. For best performance, set this equal to the number of
     * CPU cores available.
     */
    public void setNrOfThread(int value) {
        System.out.println("set Threads value" + value);
        sendAndWaitForAck("setoption name Threads value " + value);
    }

    public void clearHash() {
        System.out.println("clear hash");
        sendAndWaitForAck("setoption name Clear Hash");
    }

    private void sendAndWaitForAck(String command) {
        send(command);
        waitTillReceive(command, Duration.ofMillis(500));
    }

    private List<String> readTillReceivePrefix(String message, Duration timeout) {
        var response = new ArrayList<String>();
        await().atMost(timeout)
                .until(() -> {
                    if (in.ready()) {
                        String line = in.readLine();
                        if (StringUtils.isBlank(line)) {
                            return false;
                        }
                        else if (line.startsWith(message)) {
                            response.add(line);
                            return true;
                        } else {
                            response.add(line);
                            return false;
                        }
                    }
                    return false;
                });
        return response;
    }

    private void waitTillReceive(String message, Duration duration) {
        await().atMost(duration)
                .until(() -> {
                    if (in.ready()) {
                        String line = in.readLine();
                        if (StringUtils.isBlank(line)) {
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
