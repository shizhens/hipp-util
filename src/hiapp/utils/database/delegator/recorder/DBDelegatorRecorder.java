/**
 * 
 */
package hiapp.utils.database.delegator.recorder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import hiapp.utils.database.delegator.Delegator;

/**
 * @author zhang
 *
 */
@Service
public class DBDelegatorRecorder extends Thread {
	private static List<DelegatorInfo> delegatorInfoList = Collections.synchronizedList(new ArrayList<DelegatorInfo>());

	public DBDelegatorRecorder() {
		super("DBDelegatorRecorder-Thread");
		this.start();
	}
	
	public static void addDelegator(Delegator<?> delegator) {
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		String invokerInfo = String.format("%s.%s", stackTraceElements[4].getClassName(), stackTraceElements[4].getMethodName());
		synchronized(delegatorInfoList) {
			delegatorInfoList.add(new DelegatorInfo(delegator, invokerInfo));
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Logger logger = LogManager.getLogger("dbdetegator");
		while (true) {
			try {
				Thread.sleep(1 * 60 * 1000);
				synchronized(delegatorInfoList) {
					logger.info("==================================================DelegatorInfo=======================================");
					for (int index = delegatorInfoList.size() - 1; index > 0; --index) {
						DelegatorInfo logInfo = delegatorInfoList.get(index);
						int closeFlag = logInfo.isClosed();
						logger.info(logInfo.getLogContent());
						if (closeFlag == 1) {
							delegatorInfoList.remove(index);
						}
					}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
