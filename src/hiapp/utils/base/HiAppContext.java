/**
 * 
 */
package hiapp.utils.base;

import java.io.File;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import hiapp.utils.tenant.Tenant;

/**
 * @author zhang
 *
 */
@Service("appContext")
public class HiAppContext implements ApplicationContextAware {
	@Autowired
	private Tenant tenant;
	
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

	/**
	 * @return the tenant
	 */
	public Tenant getTenant() {
		return tenant;
	}

	/*
	 * 讲站点相对路径转换为本地存储的绝对路径。
	 */
	public String convertToRealPath(String fileName) {
		String websiteRealPath = this.getServletContext().getRealPath("/");
		String tenantRoot = this.getServletContext().getContextPath();
		
		String realPath = "";
		if (fileName.startsWith(tenantRoot))
			realPath = String.format("%s%s", websiteRealPath, fileName.substring(tenantRoot.length()));
		else
			realPath = String.format("%s%s", websiteRealPath, fileName);

		realPath = realPath.replace("/", File.separator);
		realPath = realPath.replace("\\", File.separator);
		realPath = realPath.replace(File.separator + File.separator, File.separator);
		
		return realPath;
	}
	
	/*
	 * 将本地存储的绝对路径转换为站点相对路径。
	 */
	public String convertFromRealPath(String realPath) {
		String websiteRealPath = this.getServletContext().getRealPath("/");
		
		String relativePath = "";
		relativePath = realPath.replace(websiteRealPath, "");
		relativePath = relativePath.replace("\\", "/");
		if (!relativePath.startsWith("/")) {
			relativePath = "/" + relativePath;
		}
		return relativePath;
	}
}
