package de.openflorian.data;

import java.lang.reflect.Method;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.openflorian.data.jpa.GenericDao;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;

/**
 * Factory for wrapping {@link Transactional} around DAO Service Methods
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class TransactionalProxyFactory {

	private static final Logger log = LoggerFactory.getLogger(TransactionalProxyFactory.class);

	/**
	 * Factory Method for creating a proxied object
	 * 
	 * @param object
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T proxy(final T object) {

		// check if the type or a supertype has {@link Transactional}
		boolean isTransactional = false;
		Class<?> parent = object.getClass();
		while (!isTransactional && parent != null) {
			if (parent.getAnnotation(Transactional.class) != null) {
				isTransactional = true;
			}

			for (Class<?> ifclass : parent.getInterfaces()) {
				Class<?> ifparent = ifclass;
				while (!isTransactional && ifparent != null) {
					if (ifparent.getAnnotation(Transactional.class) != null) {
						isTransactional = true;
					}

					ifparent = ifparent.getSuperclass();
				}
			}

			parent = parent.getSuperclass();
		}

		// create proxy class from concrete class
		final boolean transactional = isTransactional;
		return (T) Enhancer.create(object.getClass(), new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

				if (transactional || method.getAnnotation(Transactional.class) != null) {
					EntityManager em = GenericDao.getLocalEntityManager();
					try {
						em.getTransaction().begin();

						// invoke method and return its value if provided
						Object o = method.invoke(object, args);

						return o;
					} catch (Exception e) {
						log.error(e.getMessage(), e);
						if (em.getTransaction().isActive())
							em.getTransaction().rollback();
						throw new TransactionalException(e.getMessage(), e);
					} finally {
						// commit transaction
						em.getTransaction().commit();
					}
				}

				// not transactional, so invoke as normal
				else {
					return method.invoke(object, args);
				}
			}
		});
	}

}
