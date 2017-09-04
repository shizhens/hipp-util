/**
 * 
 */
package hiapp.utils.serviceresult;

import java.util.TreeSet;

/**
 * @author zhangguanghao
 *
 */
public class TreeDataResult extends ServiceResult {
	private TreeSet<TreeNode> data = new TreeSet<TreeNode>();
	
	/**
	 * @return the solutionTree
	 */
	public TreeSet<TreeNode> getData() {
		return data;
	}
	
}
