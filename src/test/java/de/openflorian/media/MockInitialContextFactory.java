package de.openflorian.media;

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

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

/**
 * Initial Context Factory for JNDI Application Server Context Emulation
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class MockInitialContextFactory implements InitialContextFactory {

    private static final ThreadLocal<Context> currentContext = new ThreadLocal<Context>();

    @Override
    public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
        return currentContext.get();
    }

    public static void setCurrentContext(Context context) {
        currentContext.set(context);
    }

    public static void clearCurrentContext() {
        currentContext.remove();
    }

}