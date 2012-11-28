package io.netty.buffer;

public class ReadWriteUTF8ByteBufTest
{

    public static void main(String[] args) throws Exception {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeUTF8("abc");
        byteBuf.writeUTF8("def");
        
        System.out.println(byteBuf.readUTF8()); // print abc
        System.out.println(byteBuf.readUTF8()); // print def
    }
    
}
