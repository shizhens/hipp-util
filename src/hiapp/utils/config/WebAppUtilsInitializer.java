/**
 * 
 */
package hiapp.utils.config;

import hiapp.utils.spring.HiAppWebAnnotationConfigDispatcherServletInitializer;

/**
 * @author zhang
 *
 */
public class WebAppUtilsInitializer extends HiAppWebAnnotationConfigDispatcherServletInitializer {

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer#getRootConfigClasses()
	 */
	@Override
	protected Class<?>[] getRootConfigClasses() {
		// TODO Auto-generated method stub
		return new Class<?>[] { UtilsRootConfig.class };
	}

}
