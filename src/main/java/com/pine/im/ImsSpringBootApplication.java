package com.pine.im;

import com.pine.im.netty.TCPNettyServer;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ImsSpringBootApplication implements CommandLineRunner {

    @Value("${netty.port}")
    private int port;

    @Value("${netty.url}")
    private String url;

    @Autowired
    private TCPNettyServer tcpNettyServer;

    public static void main(String[] args) {
        SpringApplication.run(ImsSpringBootApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ChannelFuture future = tcpNettyServer.startServer(url,port);
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                tcpNettyServer.destroyServer();
            }
        });
        //服务端管道关闭的监听器并同步阻塞,直到channel关闭,线程才会往下执行,结束进程
        future.channel().closeFuture().syncUninterruptibly();
    }

}
