package ch.hsr.apparch.purchaselist.api;

import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

class LoggingOutputStream extends OutputStream {

    private Logger logger;
    private StringBuilder writer = new StringBuilder();

    private LoggingOutputStream(Logger logger) {
        this.logger = logger;
    }

    static PrintStream createLoggingPrintStream(Logger logger) {
        return new PrintStream(new LoggingOutputStream(logger), true);
    }

    @Override
    public void write(int b) {
        writer.append((char) b);
    }

    @Override
    public void flush() {
        logger.debug(writer.toString());
        writer.delete(0, writer.length() - 1);
    }

    @Override
    public void close() throws IOException {
        super.close();
    }
}
