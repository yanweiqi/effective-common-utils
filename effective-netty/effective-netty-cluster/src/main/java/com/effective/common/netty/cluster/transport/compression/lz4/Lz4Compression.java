package com.effective.common.netty.cluster.transport.compression.lz4;


import com.effective.common.netty.cluster.transport.compression.Compression;
import com.effective.common.netty.cluster.transport.compression.Finishable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.compressors.lz4.BlockLZ4CompressorInputStream;
import org.apache.commons.compress.compressors.lz4.BlockLZ4CompressorOutputStream;
import org.apache.commons.compress.compressors.lz77support.Parameters;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Lz4压缩算法
 */
@Slf4j
public class Lz4Compression implements Compression {

    @Override
    public byte getTypeId() {
        return LZ4;
    }

    @Override
    public OutputStream compress(final OutputStream out) throws IOException {
        return new MyBlockLZ4CompressorOutputStream(out);
    }

    @Override
    public InputStream decompress(final InputStream input) throws IOException {
        return new BlockLZ4CompressorInputStream(input);
    }

    @Override
    public String getTypeName() {
        return "lz4";
    }

    /**
     * 覆盖flush操作
     */
    protected static class MyBlockLZ4CompressorOutputStream extends BlockLZ4CompressorOutputStream implements Finishable {

        protected final OutputStream os;

        public MyBlockLZ4CompressorOutputStream(OutputStream os) throws IOException {
            super(os);
            this.os = os;
        }

        public MyBlockLZ4CompressorOutputStream(OutputStream os, Parameters params) throws IOException {
            super(os, params);
            this.os = os;
        }

        @Override
        public void flush() throws IOException {
            os.flush();
        }
    }
}
