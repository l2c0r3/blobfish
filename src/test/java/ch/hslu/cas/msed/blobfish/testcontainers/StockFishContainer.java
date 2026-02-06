package ch.hslu.cas.msed.blobfish.testcontainers;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class StockFishContainer {

    private static final int UCI_PORT = 5555;
    private static final GenericContainer<?> STOCK_FISH_CONTAINER = new GenericContainer<>(
            new ImageFromDockerfile()
                    .withFileFromClasspath("Dockerfile", "stockfish/Dockerfile"))
            .withExposedPorts(UCI_PORT);

    private String host;
    private int port;


    public void start() {
        STOCK_FISH_CONTAINER.start();
        host = STOCK_FISH_CONTAINER.getHost();
        port = STOCK_FISH_CONTAINER.getMappedPort(UCI_PORT);

        try (Socket socket = new Socket(host, port);
             var in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {

            send(out, "uci");
            in.lines().forEach(System.out::println);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public void stop() {
        STOCK_FISH_CONTAINER.stop();
    }


    private void send(BufferedWriter out, String cmd) throws IOException {
        out.write(cmd);
        out.write("\n");
        out.flush();
    }




}
