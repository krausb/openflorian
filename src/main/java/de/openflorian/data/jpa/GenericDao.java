package de.openflorian.data.jpa;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.bind.ValidationException;

import org.hibernate.validator.InvalidStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generic data access object for JPA persistence layer<br/>
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 * 
 * @param <T> Concrete Entity DTO
 * @param <ID> Type of the Primary Key
 */
public abstract class GenericDao<T, ID extends Serializable> {
	
    protected static Logger static_log = LoggerFactory.getLogger(GenericDao.class);
    private Logger log = LoggerFactory.getLogger(getClass());
    
    private Class<T> entityType;
    @SuppressWarnings("unused")
	private Class<ID> primaryKeyType;

    @PersistenceContext(name="default")
    protected EntityManager em;
    
    private static ThreadLocal<EntityManager> localEm = null;
    private static EntityManagerFactory emf;
    private static String PERSISTENCE_CONTEXT_NAME = null;
    
    /**
     * Generic JPA Dao CTOR<br/>
     * <br/>
     * Sets the type of the generic params &lt;T&gt; and &lt;ID&gt; 
     */
    @SuppressWarnings( "unchecked" )
    public GenericDao() {
        this.entityType = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.primaryKeyType = (Class<ID>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    /**
     * Get EntityManager.
     * 
     * @return EntityManager
     * @see TransactionFilter
     */
    public static EntityManager getLocalEntityManager() {
    	if(PERSISTENCE_CONTEXT_NAME == null) {
	    	InitialContext ctx = null;
	    	
	    	try {
	    		ctx = new InitialContext();
	    	} catch (NamingException e) {
	    		static_log.error(e.getMessage(), e);
	    	}
	    	
	    	synchronized (ctx) {
	    		try {
	    			PERSISTENCE_CONTEXT_NAME = (String) ctx.lookup( "java:/comp/env/persistence.context.name" );
	    		} catch (NamingException e) {
	    			static_log.error(e.getMessage(), e);
	    			throw new RuntimeException("Persistence context name is missing in app env (java:/comp/env/persistence.context.name)");
	    		}
			}
    	}
    	
    	if (emf == null)
    		emf = Persistence.createEntityManagerFactory(PERSISTENCE_CONTEXT_NAME);

    	if (localEm == null)
            localEm = new ThreadLocal<EntityManager>();
    	
    	// Recyle entity manager in same thread, create new if em was closed by JpaFilter
    	if (localEm.get() == null || !localEm.get().isOpen())
    		localEm.set(emf.createEntityManager());
        
    	return localEm.get();
    }
    
    /**
     * Trys to load a local entity manager set by dependency injection. If local entity manager is not found,
     * we try to get the XML defined entity manager.
     * 
     * @return
     * 		{@link EntityManager}
     */
    public EntityManager getEntityManager() {
    	if(this.em == null) return GenericDao.getLocalEntityManager();
    	else return this.em;
    }

    /**
     * Find all in specific order. Please don't implement findAll() without order parameter. If you really need to please ask the
     * original author.
     * 
     * @param order
     * @return {@link List}<{@link T}> of beans
     */
    @SuppressWarnings( {"unchecked", "cast"} )
    public List<T> findAll( String order ) {
        return (List<T>)getEntityManager().createQuery("from " + entityType.getCanonicalName() + " order by " + order).getResultList();
    }

    /**
     * Find all in specific order paged by {@code pagesize} and {@code offset}
     * 
     * @param order
     * @param pagesize
     * @param offset
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<T> findAllByPage( String order, int pagesize, int offset ) {
    	return (List<T>)getEntityManager()
    			.createQuery("from " + entityType.getCanonicalName() + " order by " + order)
    			.setFirstResult(pagesize*offset)
    			.setMaxResults(pagesize)
    			.getResultList();
    }
    
    /**
     * Find all by given attribute and value
     * 
     * @param attribute
     * @param value
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<T> findAllByAttribute(String attribute, Object value) {
    	Query query = getEntityManager().createQuery("from " + entityType.getCanonicalName() + " o WHERE o." + attribute + " LIKE :value");
    	query.setParameter("value", getWildcardParameter(value.toString()));
    	return (List<T>)query.getResultList();
    }
    
    /**
     * Find all by given attribute and value
     * 
     * @param attribute
     * @param filter
     * @return
     */
    @SuppressWarnings("unchecked")
	public T findByAttribute(String attribute, Object filter) {
    	Query query = getEntityManager().createQuery("from " + entityType.getCanonicalName() + " o WHERE o." + attribute + " = :value");
    	query.setParameter("value", filter);
    	return (T)query.getSingleResult();
    }
    
    /**
     * Get row count for all records
     * @return
     */
    public long sizeOf() {
    	try {
    		return (Long)getEntityManager().createQuery("SELECT COUNT(e) FROM " + entityType.getCanonicalName() + " e")
    			.getSingleResult();
    	} catch (NoResultException e) {
    		return 0L;
    	}
    }
    
    /**
     * Find a single entity by ID
     * 
     * @param ID primary key value
     * @return {@link T} entity object or null
     */
    public T findById( ID id ) {
        EntityManager em = getEntityManager();
        T entity = em.find(entityType, id);
        if (entity == null) {
            throw new EntityNotFoundException();
        }
        return entity;
    }

    /**
     * @param id
     * @return the found entity instance.
     */
    public T findEntityProxyById( ID id ) {
        EntityManager em = getEntityManager();
        T entity;
        try {
            entity = em.getReference(entityType, id);
            if (entity == null) {
                throw new EntityNotFoundException();
            }
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
        return entity;
    }

    /**
     * Remove entity by ID.
     * 
     * @param id ID
     */
    public void remove( ID id ) {
        T bean = findById(id);
        getEntityManager().remove(bean);
        // want to get referenced exceptions at once
        getEntityManager().flush();
    }

    /**
     * Persist object. Persist means: The object is updated if it was loaded from the db, otherwise inserted. Should be _the_
     * method for saving objects.
     * 
     * @param object Bean
     * @throws ValidationException
     */
    public void persist( T object ) throws ValidationException {
    	getEntityManager().persist(object);
        // want to get validation exceptions at once
        try {
        	getEntityManager().flush();
        } catch (InvalidStateException e) {
        	getEntityManager().getTransaction().setRollbackOnly();
            throw new ValidationException(e);
        }
    }

    /**
     * Merge object. Merge means: The object is merged with the one from db. Should be used only in rare cases where objects are
     * evicted before.
     * 
     * @see BaseDAO#evict(Object)
     * @param object Bean
     * @throws ValidationException
     */
    public T merge( T object ) throws ValidationException {
        T entity = getEntityManager().merge(object);
        // want to get validation exceptions at once
        try {
        	getEntityManager().flush();
            
            return entity;
        } catch (InvalidStateException e) {
        	getEntityManager().getTransaction().setRollbackOnly();
            throw new ValidationException(e);
        }
    }


    /**
     * @param value User input
     * @return Extended value
     */
    protected String getWildcardParameter( String value ) {
        if (value == null || value.length() == 0) {
            // Search all
            return "%";
        } else if (value.contains("*")) {
            // Explicit wildcard search
            return value.replace('*', '%');
        } else {
            // Implicit search with trailing and ending wildcard
            return "%" + value + "%";
        }
    }
    
    /**
     * Dependency injection of the {@link EntityManager}<br/>
     * e.g. in UnitTest case.
     * 
     * @param {@link EntityManager} the entity manager
     */
    public static void setLocalEntityManager( EntityManager em ) {
        if (localEm == null) {
            localEm = new ThreadLocal<EntityManager>();
        }
        localEm.set(em);
    }
 
    /**
     * Dependency injection of the {@link EntityManager}<br/>
     * e.g. in UnitTest case.
     * 
     * @param {@link EntityManager} the entity manager
     */
    public void setEntityManager( EntityManager em ) {
    	this.em = em;
    }
    
}
