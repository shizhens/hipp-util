/**
 * 
 */
package hiapp.utils.idfactory;

import java.util.Date;

/**
 * @author zhang
 *
 */
public class IdHeadData {
	private String idHead;
	private int batchSize;
	private Date seedDate;
	private int seed;

	/**
	 * @return the idHead
	 */
	public String getIdHead() {
		return idHead;
	}
	/**
	 * @param idHead the idHead to set
	 */
	public void setIdHead(String idHead) {
		this.idHead = idHead;
	}
	/**
	 * @return the batchSize
	 */
	public int getBatchSize() {
		return batchSize;
	}
	/**
	 * @param batchSize the batchSize to set
	 */
	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}
	/**
	 * @return the seedDate
	 */
	public Date getSeedDate() {
		return seedDate;
	}
	/**
	 * @param seedDate the seedDate to set
	 */
	public void setSeedDate(Date seedDate) {
		this.seedDate = seedDate;
	}
	/**
	 * @return the seed
	 */
	public int getSeed() {
		return seed;
	}
	/**
	 * @param seed the seed to set
	 */
	public void setSeed(int seed) {
		this.seed = seed;
	}
	
}
