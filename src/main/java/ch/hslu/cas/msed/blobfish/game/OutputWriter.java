package ch.hslu.cas.msed.blobfish.game;

import org.jline.terminal.Terminal;

public class OutputWriter {
    Terminal terminal;

    public OutputWriter(Terminal terminal) {
        this.terminal = terminal;
    }

    public void println(String x) {
        terminal.writer().println(x);
    }

    public void print(String x) {
        terminal.writer().print(x);
    }

    public void printlnAndFlush(String x) {
        println(x);
        flush();

    }

    public void printAndFlush(String x) {
        print(x);
        flush();
    }

    public void flush() {
        terminal.flush();
    }
}
