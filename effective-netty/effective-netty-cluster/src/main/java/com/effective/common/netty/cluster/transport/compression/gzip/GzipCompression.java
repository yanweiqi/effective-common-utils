package com.effective.common.netty.cluster.transport.compression.gzip;


import com.effective.common.netty.cluster.transport.compression.Compression;
import com.effective.common.netty.cluster.transport.compression.Finishable;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static com.effective.common.netty.cluster.transport.compression.Compression.GZIP;

/**
 * Gzip Compression
 */
@Slf4j
//@Component
public class GzipCompression implements Compression {

    @Override
    public byte getTypeId() {
        return GZIP;
    }

    @Override
    public String getTypeName() {
        return "gzip";
    }

    @Override
    public OutputStream compress(final OutputStream out) throws IOException {
        return new MyGZIPOutputStream(out);
    }

    @Override
    public InputStream decompress(final InputStream input) throws IOException {
        return new GZIPInputStream(input);
    }

    /**
     * 压缩
     */
    protected static class MyGZIPOutputStream extends GZIPOutputStream implements Finishable {

        public MyGZIPOutputStream(final OutputStream out) throws IOException {
            super(out);
        }

    }
}
