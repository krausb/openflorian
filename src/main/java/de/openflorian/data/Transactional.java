package de.openflorian.data;

import java.lang.annotation.*;

/**
 * Transactional annotation
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Transactional {

}
