/*******************************************************************************
 * Copyright (c) 2007, 2010 Intel Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Intel Corporation - Initial API and implementation
 *******************************************************************************/
package org.eclipse.cdt.managedbuilder.core;


/**
 * @noextend This class is not intended to be subclassed by clients.
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface IBuildPropertiesRestriction {
	String[] getSupportedTypeIds();
	
	boolean supportsType(String id);
	
	String[] getSupportedValueIds(String typeId);
	
	boolean supportsValue(String typeId, String valueId);
	
	boolean requiresType(String typeId);

	String[] getRequiredTypeIds();
}
