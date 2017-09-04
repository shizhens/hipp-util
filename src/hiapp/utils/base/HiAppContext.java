/**
 * 
 */
package hiapp.utils.base;

import java.io.File;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author zhang
 *
 */
@Service("appContext")
public class HiAppContext implements ApplicationContextAware {
	private WebApplicationContext webApplicationContext;
	private ServletContext servletContext; 

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		webApplicationContext = (WebApplicationContext)applicationContext;
		servletContext = webApplicationContext.getServletContext();
	}

	public WebApplicationContext getWebApplicationContext() {
		return webApplicationContext;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public String convertToRealPath(String fileName) {
		System.out.println(fileName); 

		String websiteRealPath = this.getServletContext().getRealPath("/");
		String tenantRoot = this.getServletContext().getContextPath();
		
		System.out.println(websiteRealPath);
		System.out.println(tenantRoot);
		
		String realPath = "";
		if (fileName.startsWith(tenantRoot))
			realPath = String.format("%s%s", websiteRealPath, fileName.substring(tenantRoot.length()));
		else
			realPath = String.format("%s%s", websiteRealPath, fileName);
			
		System.out.println("P3: " + realPath);
		realPath = realPath.replace("/", File.separator);
		realPath = realPath.replace("\\", File.separator);
		realPath = realPath.replace(File.separator + File.separator, File.separator);
		
		return realPath;
	}
}
