/**
 * 
 */
package hiapp.utils.idfactory;

import java.text.SimpleDateFormat;
import java.util.Date;

import hiapp.utils.idfactory.data.IdRepository;

/**
 * @author zhang
 *
 */
public class IdGenerator {
	private String idHead;
	private IdRepository repository;
	private Date date;
	private int currentId = -1;
	private int maxId;
	private SimpleDateFormat dataFmt;
	
	public IdGenerator(IdRepository repository, String idHead) {
		this.repository = repository;
		this.idHead = idHead;
		this.dataFmt = new SimpleDateFormat("yyyyMMdd");
		this.pullBatch();
	}
	
	public String getIdHead() {
		return idHead;
	}
	
	public synchronized String newId() {
		int id = -1;
		if (this.getCurrentId() >= this.getMaxId()) {
			if (!this.pullBatch()) {
				return null;
			}
		}
		
		id = this.getCurrentId();
		this.setCurrentId(id + 1);
		
		return String.format("%s_%s_%d", this.getIdHead(), this.dataFmt.format(this.getDate()), id);
	}
	
	public boolean pullBatch() {
		return this.pullBatch(-1);
	}
	
	public boolean pullBatch(int batchSize) {
		this.date = new Date();
		return this.repository.pullBatchIds(this, batchSize);
	}
	
	public Date getDate() {
		return date;
	}

	public int getCurrentId() {
		return currentId;
	}

	public void setCurrentId(int currentId) {
		this.currentId = currentId;
	}

	public int getMaxId() {
		return maxId;
	}

	public void setMaxId(int maxId) {
		this.maxId = maxId;
	}
}
