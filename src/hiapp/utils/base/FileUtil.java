/**
 * 
 */
package hiapp.utils.base;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author zhangguanghao
 *
 */
public class FileUtil {
	public static boolean copyFile(String srcFileName, String destFileName) throws IOException {
	    FileChannel in = null;  
	    FileChannel out = null;  
	    FileInputStream inStream = null;  
	    FileOutputStream outStream = null;  
	    Boolean returnValue = false;
	    try {  
	        inStream = new FileInputStream(srcFileName);  
	        outStream = new FileOutputStream(destFileName);  
	        in = inStream.getChannel();  
	        out = outStream.getChannel();  
	        in.transferTo(0, in.size(), out);  
	        returnValue = true;
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    } finally {  
	        inStream.close();  
	        in.close(); 
	        outStream.close();  
	        out.close();  
	    }  
	    
	    return returnValue;
	}
}
