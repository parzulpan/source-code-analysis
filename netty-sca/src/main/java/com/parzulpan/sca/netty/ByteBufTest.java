package com.parzulpan.sca.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.CompositeByteBuf;
import io.netty.util.internal.StringUtil;

/**
 * @author panpan
 * @since 2024/07
 */
public class ByteBufTest {
    public static void main(String[] args) {
//        byteBufCapacityExpansion();

//        bufferReader();

//        sliceZeroCopy();

        compositeZeroCopy();

    }

    /**
     * 测试Netty-ByteBuf自动扩容机制
     */
    private static void byteBufCapacityExpansion() {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(16);
        System.out.println(buffer);

        buffer.writeBytes("6".repeat(17).getBytes());
        printBuffer(buffer);
    }

    /**
     * 测试ByteBuf的read、get、mark功能
     */
    private static void bufferReader() {
        // 分配一个初始容量为10的缓冲区
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(10);

        // 向缓冲区中写入10个字符（占位十个字节）
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(i);
        }
        buffer.writeBytes(sb.toString().getBytes());

        // 使用read方法读取前5个字节数据
        printBuffer(buffer);
        buffer.readBytes(5);
        printBuffer(buffer);

        // 再使用get方法读取后五个字节数据
        buffer.getByte(5);
        printBuffer(buffer);
    }

    /**
     * 测试Netty-ByteBuf的slice零拷贝方法
     */
    private static void sliceZeroCopy() {
        // 分配一个初始容量为10的缓冲区
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(10);

        // 写入0~9十个字节数据
        byte[] numData = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        buffer.writeBytes(numData);
        printBuffer(buffer);

        // 从下标0开始，向后截取五个字节，拆分成一个新ByteBuf对象
        ByteBuf b1 = buffer.slice(0, 5);
        printBuffer(b1);
        // 从下标5开始，向后截取五个字节，拆分成一个新ByteBuf对象
        ByteBuf b2 = buffer.slice(5, 5);
        printBuffer(b2);

        // 证明切割出的两个ByteBuf对象，是共享第一个ByteBuf对象数据的
        // 这里修改截取后的b1对象，然后查看最初的buffer对象
        b1.setByte(0, 'a');

    }

    /**
     * 测试Netty-ByteBuf的composite零拷贝方法
     */
    private static void compositeZeroCopy() {
        // 创建两个小的ByteBuf缓冲区，并往两个缓冲区中插入数据
        ByteBuf b1 = ByteBufAllocator.DEFAULT.buffer(5);
        ByteBuf b2 = ByteBufAllocator.DEFAULT.buffer(5);
        byte[] data1 = {'a', 'b', 'c', 'd', 'e'};
        byte[] data2 = {'n', 'm', 'x', 'y', 'z'};
        b1.writeBytes(data1);
        b2.writeBytes(data2);

        // 创建一个合并缓冲区的CompositeByteBuf对象
        CompositeByteBuf buffer = ByteBufAllocator.DEFAULT.compositeBuffer();
        // 将前面两个小的缓冲区，合并成一个大的缓冲区
        buffer.addComponents(true, b1, b2);
        printBuffer(buffer);
    }

    /**
     * 打印ByteBuf中数据
     * <p>
     * 基于Netty自身提供的Dump方法实现
     */
    private static void printBuffer(ByteBuf buffer) {
        int bytes = buffer.readableBytes();
        int rows = bytes / 16 + (bytes % 15 == 0 ? 0 : 1) + 4;
        StringBuilder sb = new StringBuilder(rows * 80 * 2);

        // 获取缓冲区的容量、读/写指针信息放入StringBuilder
        sb.append("ByteBuf缓冲区信息：{");
        sb.append("读取指针=").append(buffer.readerIndex()).append(", ");
        sb.append("写入指针=").append(buffer.writerIndex()).append(", ");
        sb.append("容量大小=").append(buffer.capacity()).append("}");

        // 利用Netty框架自带的格式化方法、Dump方法输出缓冲区数据
        sb.append(StringUtil.NEWLINE);
        ByteBufUtil.appendPrettyHexDump(sb, buffer);
        System.out.println(sb);
    }
}
