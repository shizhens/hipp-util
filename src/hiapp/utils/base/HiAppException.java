package hiapp.utils.base;

public class HiAppException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int exceptionCode;
	
	public HiAppException(String message, int exceptionCode) {
		super(message);
		this.exceptionCode = exceptionCode;
	}

	public int getExceptionCode() {
		return exceptionCode;
	}

}
