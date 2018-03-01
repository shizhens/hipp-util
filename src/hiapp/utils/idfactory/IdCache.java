/**
 * 
 */
package hiapp.utils.idfactory;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhang
 *
 */
public class IdCache implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7322502387974872890L;

	private Date date;
	private int max;
	private int seed;
	
	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	/**
	 * @return the max
	 */
	public int getMax() {
		return max;
	}
	/**
	 * @param max the max to set
	 */
	public void setMax(int max) {
		this.max = max;
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
