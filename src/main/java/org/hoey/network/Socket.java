package org.hoey.network;

/**
 * TODO
 *
 * @author liuhuijun
 * @since 2024/5/6 22:39
 */
public record Socket(long socket64, int socket32) {

    public Socket(int address){
        this(address, address);
    }

    public Socket(long address){
        this(address, (int) address);
    }

}
