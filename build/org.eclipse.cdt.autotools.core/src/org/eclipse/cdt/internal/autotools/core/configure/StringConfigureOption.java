/*******************************************************************************
 * Copyright (c) 2009, 2015 Red Hat Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.cdt.internal.autotools.core.configure;


public class StringConfigureOption extends AbstractConfigurationOption {

	private String value;
	
	public StringConfigureOption(String name, AutotoolsConfiguration cfg) {
		super(name, cfg);
		this.value = ""; // $NON-NLS-1$
	}
	
	public StringConfigureOption(String name, String msgName, AutotoolsConfiguration cfg) {
		super(name, msgName, cfg);
		this.value = ""; // $NON-NLS-1$
	}
	
	private StringConfigureOption(String name, AutotoolsConfiguration cfg,
			String value) {
		super(name, cfg);
		this.value = value;
	}
	
	@Override
	public String getValue() {
		return value;
	}
	
	@Override
	public void setValue(String newValue) {
		if (!newValue.equals(value)) {
			cfg.setDirty(true);
			value = newValue;
		}
	}

	@Override
	public boolean isParmSet() {
		return value.length() > 0;
	}
	
	@Override
	public String getParameter() {
		if (isParmSet())
			return getParameterName() + "=" + getValue(); // $NON-NLS-1$
		return "";
	}
	
	@Override
	public IConfigureOption copy(AutotoolsConfiguration config) {
		return new StringConfigureOption(name, config, value);
	}

	@Override
	public int getType() {
		return STRING;
	}
}
