/**
 * 
 */
package hiapp.utils.spring;

import org.springframework.util.ObjectUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * @author zhang
 *
 */
public abstract class HiAppWebAnnotationConfigDispatcherServletInitializer 
		extends AbstractAnnotationConfigDispatcherServletInitializer {
	protected static AnnotationConfigWebApplicationContext rootAppContext = new AnnotationConfigWebApplicationContext();
	
	/**
	 * {@inheritDoc}
	 * <p>This implementation creates an {@link AnnotationConfigWebApplicationContext},
	 * providing it the annotated classes returned by {@link #getRootConfigClasses()}.
	 * Returns {@code null} if {@link #getRootConfigClasses()} returns {@code null}.
	 */
	@Override
	protected WebApplicationContext createRootApplicationContext() {
		registerRootConfigClasses();
		return getRootAppContent();
	}
	
	protected void registerRootConfigClasses() {
		Class<?>[] configClasses = getRootConfigClasses();
		if (!ObjectUtils.isEmpty(configClasses)) {
			AnnotationConfigWebApplicationContext rootAppContext = getRootAppContent();
			rootAppContext.register(configClasses);
		}
	}
	
	protected static AnnotationConfigWebApplicationContext getRootAppContent() {
		return HiAppWebAnnotationConfigDispatcherServletInitializer.rootAppContext;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer#getServletConfigClasses()
	 */
	@Override
	protected Class<?>[] getServletConfigClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.support.AbstractDispatcherServletInitializer#getServletMappings()
	 */
	@Override
	protected String[] getServletMappings() {
		// TODO Auto-generated method stub
		return null;
	}
}
