package com.js.lock.zk;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;

import com.js.lock.sample.MyZkSerializer;


public class ZKDistributeImproveLock implements Lock {

	/*
	 * Use createEphemeralSequential to implement distributed lock.
	 * Lock: like wait in queue to get the number (createEphemeralSequential), check if the current one if min, if yes
	 *       get the lock, if no register a watcher to the previous node and block.
	 * Unlock: remote the created temp sequential node.
	 */
	private String LockPath;

	private ZkClient client;

	private String currentPath;

	private String beforePath;

	public ZKDistributeImproveLock(String lockPath) {
		super();
		LockPath = lockPath;
		client = new ZkClient("localhost:2181");
		client.setZkSerializer(new MyZkSerializer());
		if (!this.client.exists(LockPath)) {
			try {
				this.client.createPersistent(LockPath); // create root node first...
			} catch (ZkNodeExistsException e) {

			}
		}
	}

	@Override
	public boolean tryLock() {
		if (this.currentPath == null) { // do not create the dupe index...
			currentPath = this.client.createEphemeralSequential(LockPath + "/", "aaa");
		}
		// Get all the children node.
		List<String> children = this.client.getChildren(LockPath);

		// Sort.
		Collections.sort(children);

		// Check if the current one is min, starting from root path.
		if (currentPath.equals(LockPath + "/" + children.get(0))) {
			return true;
		} else {
			// Get the previous one index.
			int curIndex = children.indexOf(currentPath.substring(LockPath.length() + 1));
			beforePath = LockPath + "/" + children.get(curIndex - 1);
		}
		return false;
	}

	@Override
	public void lock() {
		if (!tryLock()) {		
			waitForLock();
			lock();
		}
	}

	private void waitForLock() {

		CountDownLatch cdl = new CountDownLatch(1);

		// Register watcher.
		IZkDataListener listener = new IZkDataListener() {

			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				System.out.println("-----Node is deleted");
				cdl.countDown();
			}

			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {

			}
		};

		client.subscribeDataChanges(this.beforePath, listener);

		// Block here!
		if (this.client.exists(this.beforePath)) {
			try {
				cdl.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// Wake up and unregister the watcher.
		client.unsubscribeDataChanges(this.beforePath, listener);
	}

	@Override
	public void unlock() {
		this.client.delete(this.currentPath);
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Condition newCondition() {
		// TODO Auto-generated method stub
		return null;
	}
}
