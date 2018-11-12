package org.zenbot.szolnok.timetable.batch.batch;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.net.SocketTimeoutException;

@Slf4j
public class HtmlDocumentReaderReader implements ResourceAwareItemReaderItemStream<Document> {
    public static final int RETRY_COUNT = 4;
    private Resource resource;
    private int count = -1;
    private boolean readNext;

    @Override
    public void setResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    public Document read() throws Exception {
        count++;
        if (readNext) {
            return null;
        }
        log.info("Reading html file [{}]", resource.getURL().toString());
        return readWithRetry();
    }

    private Document readWithRetry() throws IOException {
        Document result = null;
        int i = 0;
        while (i <= RETRY_COUNT) {
            try  {
                result = Jsoup.connect(resource.getURL().toString()).get();
                break;
            } catch (SocketTimeoutException e) {
                log.debug("Read timed out [{}]", resource.getURL().toString());
                log.debug("Retry last operation for the [{}] time", i + 1);
            }
            i++;
        }
        if (result == null) {
            throw new IllegalStateException(String.format("Cannot fetch documet=[%s]", resource.getURL().toString()));
        }
        return result;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        readNext = false;
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        int resouceIndex = (Integer) executionContext.get("MultiResourceItemReader.resourceIndex");
        if (resouceIndex <= count) {
            readNext = true;
        }
    }

    @Override
    public void close() throws ItemStreamException {
        readNext = true;
    }
}
