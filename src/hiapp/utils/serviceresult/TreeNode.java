package hiapp.utils.serviceresult;

@SuppressWarnings("rawtypes")
public class TreeNode implements java.lang.Comparable{
	private String id;
	private boolean isBranch;
	private String text;
	private String iconCls;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the isFolder
	 */
	public boolean isBranch() {
		return isBranch;
	}
	/**
	 * @param isFolder the isFolder to set
	 */
	protected void setBranch(boolean isBranch) {
		this.isBranch = isBranch;
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	public int compareTo(Object node) {
		// TODO Auto-generated method stub
		return compareTo((TreeNode)node);
	}
	private int compareTo(TreeNode node) {
		// TODO Auto-generated method stub
		return this.getId().compareTo(node.getId());
	}
	/**
	 * @return the iconCls
	 */
	public String getIconCls() {
		return iconCls;
	}
	/**
	 * @param iconCls the iconCls to set
	 */
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	} 
}
