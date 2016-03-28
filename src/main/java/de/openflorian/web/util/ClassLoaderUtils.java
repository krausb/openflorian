package de.openflorian.web.util;

/*
 * This file is part of Openflorian.
 * 
 * Copyright (C) 2015  Bastian Kraus
 * 
 * Openflorian is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version)
 *     
 * Openflorian is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *     
 * You should have received a copy of the GNU General Public License
 * along with Openflorian.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * ClassLoader Utils<br/>
 * <br/>
 * This little helper serves classes and classpath resources.
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
@SuppressWarnings("rawtypes")
public class ClassLoaderUtils {

	public static Class loadClass(String name, Class caller) throws ClassNotFoundException {
		return loadClass(name, caller.getClassLoader());
	}

	/**
	 * Loads a class from caller
	 * 
	 * @param name class name
	 * @param callingLoader callingClass classloader
	 * @return {@link Class}
	 * @throws ClassNotFoundException
	 */
	public static Class loadClass(String name, ClassLoader callingLoader) throws ClassNotFoundException {
		try {
			return Thread.currentThread().getContextClassLoader().loadClass(name);
		} catch (ClassNotFoundException e) {
			try {
				return Class.forName(name);
			} catch (ClassNotFoundException ex) {
				try {
					return ClassLoaderUtils.class.getClassLoader().loadClass(name);
				} catch (ClassNotFoundException exc) {
					return callingLoader.loadClass(name);
				}
			}
		}
	}

	/**
	 * Loads a given resource
	 * 
	 * @param resourceName
	 * @param callingClass
	 * @return
	 * 		{@link URL} instance to given resource
	 */
	public static URL getResource(String resourceName, Class callingClass) {
		URL url = null;
		url = Thread.currentThread().getContextClassLoader().getResource(resourceName);

		if (url == null)
			url = ClassLoaderUtils.class.getClassLoader().getResource(resourceName);
		if (url == null)
			url = callingClass.getClassLoader().getResource(resourceName);
		
		return url;
	}

	/**
	 * Loads a resource bundle by {@code locale} from class loader of {@code callingClass}
	 * 
	 * @param resourceName
	 * @param locale
	 * @param callingClass
	 * @return
	 */
	public static ResourceBundle getBundle(String resourceName, Locale locale, Class callingClass) {
		ResourceBundle bundle = null;
		bundle = ResourceBundle.getBundle(resourceName, locale, Thread.currentThread().getContextClassLoader());

		if (bundle == null)
			bundle = ResourceBundle.getBundle(resourceName, locale, ClassLoaderUtils.class.getClassLoader());
		if (bundle == null)
			bundle = ResourceBundle.getBundle(resourceName, locale, callingClass.getClassLoader());

		return bundle;
	}

	/**
	 * Loads a enum of found resources by {@code resourceName} from classloader of {@code callingClass}
	 * 
	 * @param resourceName
	 * @param callingClass
	 * @return
	 * @throws IOException
	 */
	public static Enumeration getResources(String resourceName, Class callingClass) throws IOException {
		Enumeration urls = Thread.currentThread().getContextClassLoader().getResources(resourceName);
		if (urls == null) {
			urls = ClassLoaderUtils.class.getClassLoader().getResources(resourceName);
			if (urls == null)
				urls = callingClass.getClassLoader().getResources(resourceName);
		}
		return urls;
	}

	/**
	 * This is a convenience method to load a resource as a stream.
	 *
	 * The algorithm used to find the resource is given in getResource()
	 *
	 * @param resourceName
	 *            Name of resource to load
	 * @param callingClass
	 *            Class of the calling object
	 */
	public static InputStream getResourceAsStream(String resourceName, Class callingClass) {
		URL url = getResource(resourceName, callingClass);
		try {
			return url != null ? url.openStream() : null;
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Prints the current classloader hierarchy - useful for debugging.
	 */
	public static void printClassLoader() {
		System.out.println("ClassLoaderUtils.printClassLoader");
		printClassLoader(Thread.currentThread().getContextClassLoader());
	}

	/**
	 * Prints the classloader hierarchy from a given classloader - useful for
	 * debugging.
	 */
	public static void printClassLoader(ClassLoader cl) {
		System.out.println("ClassLoaderUtils.printClassLoader(cl = " + cl + ")");
		if (cl != null) 
			printClassLoader(cl.getParent());
	}
}
