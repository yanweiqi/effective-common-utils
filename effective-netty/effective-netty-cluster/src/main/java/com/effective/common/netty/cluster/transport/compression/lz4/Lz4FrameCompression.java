package com.effective.common.netty.cluster.transport.compression.lz4;


import com.effective.common.netty.cluster.transport.compression.Compression;
import com.effective.common.netty.cluster.transport.compression.Finishable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.compressors.lz4.FramedLZ4CompressorInputStream;
import org.apache.commons.compress.compressors.lz4.FramedLZ4CompressorOutputStream;
//import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Lz4压缩算法
 */
@Slf4j

public class Lz4FrameCompression implements Compression {

    @Override
    public byte getTypeId() {
        return LZ4_FRAME;
    }

    @Override
    public OutputStream compress(final OutputStream out) throws IOException {
        return new MyFramedLZ4CompressorOutputStream(out);
    }

    @Override
    public InputStream decompress(final InputStream input) throws IOException {
        return new FramedLZ4CompressorInputStream(input);
    }

    @Override
    public String getTypeName() {
        return "lz4f";
    }

    /**
     * 覆盖flush操作
     */
    protected static class MyFramedLZ4CompressorOutputStream extends FramedLZ4CompressorOutputStream implements Finishable {

        protected OutputStream out;

        public MyFramedLZ4CompressorOutputStream(OutputStream out) throws IOException {
            super(out, new Parameters(BlockSize.K64, false, false, false));
            this.out = out;
        }

        public MyFramedLZ4CompressorOutputStream(OutputStream out, Parameters params) throws IOException {
            super(out, params);
            this.out = out;
        }

        @Override
        public void flush() throws IOException {
            out.flush();
        }
    }
}
