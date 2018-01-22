/**
 * 
 */
package hiapp.utils.database.delegator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author zhang
 *
 */
public class Delegator<T> implements InvocationHandler {
	protected T realObject;
	protected T proxyObject;
	
	@SuppressWarnings("unchecked")
	public Delegator(T realObject, Class<?>[] interfaces) {
        this.realObject = realObject;
        this.proxyObject = (T) Proxy.newProxyInstance(realObject.getClass().getClassLoader(), interfaces, this); //委托
    }
	
	public T getProxyObject() {
		return this.proxyObject;
	}
	
	protected T getRealObject() {
		return this.realObject;
	}

    protected Object invokeSuper(Method method, Object[] args) throws Throwable {
        return method.invoke(this.realObject, args);
    }

	/* (non-Javadoc)
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// TODO Auto-generated method stub
		switch (method.getName()) {
		case "toString":
			return this.invokeSuper(method, args) + "$Proxy";
			
		default:
			return this.invokeSuper(method, args);
		}
	}

}
