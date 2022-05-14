package com.teradata.openapi.httpserver.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.teradata.openapi.httpserver.core.server.RpcServer;
import com.teradata.openapi.httpserver.core.thread.NamedThreadFactory;
import com.teradata.openapi.httpserver.netty.server.handler.CommonRpcHttpServerHander;

/**
 * 
 * httpServer netty封装. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年3月30日 上午11:43:55
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
public class CommonRpcHttpServer implements RpcServer {

	private static final Log LOGGER = LogFactory.getLog(CommonRpcHttpServer.class);

	private static final int PROCESSORS = Runtime.getRuntime().availableProcessors();

	private EventLoopGroup bossGroup;

	private NioEventLoopGroup workerGroup;

	public CommonRpcHttpServer() {

	}

	private static class SingletonHolder {

		static final CommonRpcHttpServer instance = new CommonRpcHttpServer();
	}

	public static CommonRpcHttpServer getInstance() {
		return SingletonHolder.instance;
	}

	@Override
	public void stop() throws Exception {
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
	}

	/*
	 * (non-Javadoc)
	 * @see com.cross.plateform.common.rpc.core.server.RpcServer#start(int, int)
	 */
	@Override
	public void start(int port, final int timeout) throws Exception {
		ThreadFactory serverBossTF = new NamedThreadFactory("NETTYSERVER-BOSS-");
		ThreadFactory serverWorkerTF = new NamedThreadFactory("NETTYSERVER-WORKER-");
		bossGroup = new NioEventLoopGroup(PROCESSORS, serverBossTF);
		workerGroup = new NioEventLoopGroup(PROCESSORS * 2, serverWorkerTF);
		workerGroup.setIoRatio(50);
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout)
		        .option(ChannelOption.SO_BACKLOG, 1024).option(ChannelOption.SO_REUSEADDR, true).option(ChannelOption.SO_KEEPALIVE, true)
		        .option(ChannelOption.SO_SNDBUF, 65535).option(ChannelOption.SO_RCVBUF, 65535).childOption(ChannelOption.TCP_NODELAY, true)
		        .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
		bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel channel) throws Exception {
				ChannelPipeline pipeline = channel.pipeline();
				pipeline.addLast("codec", new HttpServerCodec());
				pipeline.addLast("aggegator", new HttpObjectAggregator(512 * 1024));
				pipeline.addLast("timeout", new IdleStateHandler(0, 0, 120));
				pipeline.addLast("biz", new CommonRpcHttpServerHander());
			}

		});
		LOGGER.info("-----------------开始启动--------------------------");
		bootstrap.bind(new InetSocketAddress(port)).sync();
		LOGGER.info("端口号：" + port + "的服务端已经启动");
		LOGGER.info("-----------------启动结束--------------------------");
	}

}
