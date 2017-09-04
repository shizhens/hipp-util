/**
 * 
 */
package hiapp.utils.serviceresult;

import java.util.TreeSet;

/**
 * @author zhangguanghao
 *
 */
public class TreeBranch extends TreeNode {
	private String state;
	private TreeSet<TreeNode> children = new TreeSet<TreeNode>();
	
	public TreeBranch() {
		this.setBranch(true);
		this.setState("closed");
	}
	
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the children
	 */
	public TreeSet<TreeNode> getChildren() {
		return children;
	}
}
