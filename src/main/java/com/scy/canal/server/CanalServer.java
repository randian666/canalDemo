package com.scy.canal.server;

import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.otter.canal.deployer.CanalController;

/**
 * canal服务端代码
 * @author liuxun
 *
 */
public class CanalServer {
	private static final Log logger = LogFactory.getLog(CanalServer.class);

	private static final String CLASSPATH_URL_PREFIX = "classpath:";

	private CanalController controller;

	private List<String> configs;

	public void startup() {
		logger.debug("CanalServer startup 准备启动canal服务端...");
		try {
			Properties properties = new Properties();
			for (String conf : configs) {
				if (conf.startsWith(CLASSPATH_URL_PREFIX)) {
					conf = StringUtils.substringAfter(conf, CLASSPATH_URL_PREFIX);
					properties.load(CanalServer.class.getClassLoader().getResourceAsStream(conf));
				} else {
					properties.load(new FileInputStream(conf));
				}
			}
			logger.debug("CanalController创建开始...");
			controller = new CanalController(properties);
			logger.debug("CanalController创建结束...");
			controller.start();
			logger.debug("CanalServer startup 启动canal服务端成功！");
			Runtime.getRuntime().addShutdownHook(new Thread() {

				public void run() {
					try {
						logger.info("## stop the canal server");
						controller.stop();
					} catch (Throwable e) {
						logger.warn("##something goes wrong when stopping canal Server:\n{}", e);
					} finally {
						logger.info("## canal server is down.");
					}
				}

			});
		} catch (Throwable e) {
			logger.error("CanalServer startup 启动canal服务端失败，", e);
			System.exit(0);
		}
	}

	public void shutdown() {
		try {
			controller.stop();
		} catch (Throwable e) {
			logger.error("CanalServer shutdown canal服务端异常，", e);
		}
	}

	public void setConfigs(List<String> configs) {
		this.configs = configs;
	}
}
