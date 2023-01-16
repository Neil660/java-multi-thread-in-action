/*
授权声明：
本源码系《Java多线程编程实战指南（核心篇）》一书（ISBN：978-7-121-31065-2，以下称之为“原书”）的配套源码，
欲了解本代码的更多细节，请参考原书。
本代码仅为原书的配套说明之用，并不附带任何承诺（如质量保证和收益）。
以任何形式将本代码之部分或者全部用于营利性用途需经版权人书面同意。
将本代码之部分或者全部用于非营利性用途需要在代码中保留本声明。
任何对本代码的修改需在代码中以注释的形式注明修改人、修改时间以及修改内容。
本代码可以从以下网址下载：
https://github.com/Viscent/javamtia
http://www.broadview.com.cn/31065
*/
package com.mtia.ch4.case01;

import com.mtia.util.Debug;
import com.mtia.util.Tools;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.atomic.AtomicLong;

public class Storage implements Closeable, AutoCloseable {
    // 可自由访问文件任意位置
    private final RandomAccessFile storeFile;
    private final FileChannel storeChannel;
    protected final AtomicLong totalWrites = new AtomicLong(0);

    public Storage(long fileSize, String fileShortName) throws IOException {
        String fullFileName = System.getProperty("java.io.tmpdir") + "/"
                + fileShortName;
        String localFileName;
        // 创建用来存储的文件
        localFileName = createStoreFile(fileSize, fullFileName);
        storeFile = new RandomAccessFile(localFileName, "rw");
        storeChannel = storeFile.getChannel();
    }

    /**
     * 将data中指定的数据写入文件
     *
     * @param offset  写入数据在整个文件中的起始偏移位置
     * @param byteBuf byteBuf必须在该方法调用前执行byteBuf.flip()
     * @return 写入文件的数据长度
     * @throws IOException
     */
    public int store(long offset, ByteBuffer byteBuf)
            throws IOException {
        int length;
        storeChannel.write(byteBuf, offset);
        length = byteBuf.limit();
        totalWrites.addAndGet(length);
        return length;
    }

    public long getTotalWrites() {
        return totalWrites.get();
    }

    private String createStoreFile(final long fileSize, String fullFileName)
            throws IOException {
        File file = new File(fullFileName);
        Debug.info("create local file:%s", fullFileName);
        RandomAccessFile raf;
        // “r” 以只读方式来打开指定文件夹。如果试图对该RandomAccessFile执行写入方法，都将抛出IOException异常。
        // “rw” 以读，写方式打开指定文件。如果该文件尚不存在，则试图创建该文件。
        // “rws” 以读，写方式打开指定文件。相对于”rw” 模式，还要求对文件内容或元数据的每个更新都同步写入到底层设备。
        // “rwd” 以读，写方式打开指定文件。相对于”rw” 模式，还要求对文件内容每个更新都同步写入到底层设备。
        raf = new RandomAccessFile(file, "rw");
        try {
            raf.setLength(fileSize);
        }
        finally {
            try {
                raf.close();
            }
            catch (Exception ignored) {
            }
        }
        return fullFileName;
    }

    @Override
    public synchronized void close() throws IOException {
        if (storeChannel.isOpen()) {
            Tools.silentClose(storeChannel, storeFile);
        }
    }
}