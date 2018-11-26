package com.effective.common.zk;

import com.google.common.io.Closeables;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Monitor Thread
 * @author liuzhankun
 * @date 2013-7-18
 *
 */
public class Monitor extends Thread {

	private static final Logger log = LoggerFactory.getLogger(Monitor.class);

	private String zks;
	private String path;
	private CuratorFramework client = null;
	private PathChildrenCache cache = null;

	public Monitor(String zks, String path) {
		this.zks = zks;
		this.path = path;
	}

	public void run() {

		try {
			client = CuratorFrameworkFactory.newClient(zks, new ExponentialBackoffRetry(100, 50));
			client.start();

			cache = new PathChildrenCache(client, path, true);
			cache.start();

			addListener(cache);
			
			while(true) {
				Thread.sleep(Long.MAX_VALUE);
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				Closeables.close(cache, true);
				Closeables.close(client, true);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}

	}

	private static void addListener(final PathChildrenCache cache) {
		PathChildrenCacheListener listener = new PathChildrenCacheListener() {
			@Override
			public void childEvent(CuratorFramework client,
					PathChildrenCacheEvent event) throws Exception {
				switch (event.getType()) {
				case CHILD_ADDED: {
					log.info("Node added: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
					list(cache);
					break;
				}
				case CHILD_UPDATED: {
					log.info("Node changed: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
					list(cache);
					break;
				}
				case CHILD_REMOVED: {
					log.info("Node removed: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
					list(cache);
					break;
				}
				}
			}
		};
		cache.getListenable().addListener(listener);
	}

	private static void list(PathChildrenCache cache) {
		Map<String, String> info = new HashMap<>();
		if (cache.getCurrentData() == null || cache.getCurrentData().size() == 0) {
			log.info("zk nodes is empty");
		} else {
			for (ChildData child : cache.getCurrentData()) {
				String path = child.getPath();
				String data = "";
				if (child.getData() != null) {
					data = new String(child.getData());
				}
				
				String[] array = path.split("\\/");
				String lastPath = array[array.length-1];
				
				info.put(lastPath, data);
				
				log.info("push server in zk, path=" + lastPath + ", data=" + data);
			}
		}
		PushNodeManager.refresh(info);
	}
}
