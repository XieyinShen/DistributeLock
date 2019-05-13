package com.js.lock.sample;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

public class ZkWatcherDemo {

	public static void main(String[] args) {

		// Create a ZK client
		ZkClient client = new ZkClient("localhost:2181");
		client.setZkSerializer(new MyZkSerializer());

		client.subscribeDataChanges("/Jovi", new IZkDataListener() {

			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				System.out.println("----Node is deleted-------------");
			}

			@Override
			public void handleDataChange(String dataPath, Object data)
					throws Exception {
				System.out.println("----Node is changed: " + data + "-------------");
			}
		});

		try {
			Thread.sleep(1000 * 60 * 2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
