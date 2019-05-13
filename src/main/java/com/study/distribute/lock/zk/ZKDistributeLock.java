package com.study.distribute.lock.zk;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;

import com.study.distribute.lock.sample.MyZkSerializer;

public class ZKDistributeLock implements Lock {

	private String lockPath;

	private ZkClient client;

	public ZKDistributeLock(String lockPath) {
		super();
		this.lockPath = lockPath;

		client = new ZkClient("localhost:2181");
		client.setZkSerializer(new MyZkSerializer());
	}

	// No block.
	@Override
	public boolean tryLock() { 
		
		// Lock by creating a temp node.
		try {
			client.createEphemeral(lockPath);
		} catch (ZkNodeExistsException e) {
			return false;
		}
		return true;
	}

	@Override
	public void unlock() {
		client.delete(lockPath);
	}

	// Block to wait if failing to get the lock.
	@Override
	public void lock() { 
		if (!tryLock()) {
			
			// Block self if failing to get the lock
			waitForLock();
			
			// Try again.
			lock();
		}
	}

	private void waitForLock() {
		
		CountDownLatch cdl = new CountDownLatch(1);
		IZkDataListener listener = new IZkDataListener() {
			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				System.out.println("----Node is deleted-------------" + dataPath);
				cdl.countDown(); // cdl - 1
			}
			@Override
			public void handleDataChange(String dataPath, Object data)
					throws Exception {
				System.out.println("----Node is changed-------------" + dataPath);
			}
		};
		client.subscribeDataChanges(lockPath, listener); // register the watcher

		// Block self.
		if (this.client.exists(lockPath)) {
			try {
				cdl.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// Un-scribe here?
		client.unsubscribeDataChanges(lockPath, listener);
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean tryLock(long time, TimeUnit unit)
			throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Condition newCondition() {
		// TODO Auto-generated method stub
		return null;
	}

}
