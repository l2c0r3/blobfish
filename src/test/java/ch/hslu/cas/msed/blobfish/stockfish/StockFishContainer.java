package ch.hslu.cas.msed.blobfish.stockfish;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.io.IOException;
import java.util.List;

class StockFishContainer implements AutoCloseable {

    private static final int UCI_PORT = 5555;
    private GenericContainer<?> stockFishContainer;


    private String host;
    private int port;
    private boolean startedOwnContainer = false;

    public void init() {
        try {
            var docker = DockerClientFactory.instance().client();

            var containerIds = getRunningContainerWithLabel(docker, "stockfish");
            if (!containerIds.isEmpty()) {
                System.out.println("Recognized already running stockfish");
                this.host = DockerClientFactory.instance().dockerHostIpAddress();
                this.port = 5555;
            }

        } catch (IOException e) {
            // then just run own container
            startContainer();
        }
    }

    private void startContainer() {
        System.out.println("Starting own container");
        stockFishContainer = new GenericContainer<>(
                new ImageFromDockerfile()
                        .withFileFromClasspath("Dockerfile", "stockfish/Dockerfile"))
                .withExposedPorts(UCI_PORT);

        stockFishContainer.start();
        startedOwnContainer = true;
        this.host = stockFishContainer.getHost();
        this.port = stockFishContainer.getMappedPort(UCI_PORT);
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    @Override
    public void close() {
        if (startedOwnContainer) {
            System.out.println("Stopping own container");
            stockFishContainer.stop();
        }
    }

    private List<String> getRunningContainerWithLabel(DockerClient docker, String label) throws IOException {
        return docker.listContainersCmd()
                .withShowAll(false)
                .withLabelFilter(List.of(label))
                .exec().stream()
                .map(Container::getId).toList();
    }
}
