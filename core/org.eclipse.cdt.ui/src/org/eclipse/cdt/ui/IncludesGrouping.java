/*******************************************************************************
 * Copyright (c) 2000, 2012 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     QNX Software Systems - Initial API and implementation
 *******************************************************************************/
package org.eclipse.cdt.ui;

import org.eclipse.cdt.core.model.CModelException;
import org.eclipse.cdt.core.model.ICElement;
import org.eclipse.cdt.core.model.ITranslationUnit;

/**
 * IncludesGrouping
 */
public class IncludesGrouping extends CElementGrouping {
	ITranslationUnit tu;

	public IncludesGrouping(ITranslationUnit unit) {
		super(CElementGrouping.INCLUDES_GROUPING);
		tu = unit;
	}

	@Override
	public Object[] getChildren(Object object) {
		try {
			return tu.getChildrenOfType(ICElement.C_INCLUDE).toArray();
		} catch (CModelException e) {
		}
		return super.getChildren(object);
	}

	@Override
	public Object getParent(Object object) {
		return tu;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof IncludesGrouping) {
			return tu.equals(((IncludesGrouping) obj).tu) ;
		}
		return false;
	}
}
