/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM - Initial API and implementation
 * Anton Leherbauer (Wind River Systems)
 * Markus Schorn (Wind River Systems)
 *******************************************************************************/
package org.eclipse.cdt.ui.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.cdt.refactoring.tests.RenameRegressionTests;
import org.eclipse.cdt.ui.tests.callhierarchy.CallHierarchyTestSuite;
import org.eclipse.cdt.ui.tests.includebrowser.IncludeBrowserTestSuite;
import org.eclipse.cdt.ui.tests.text.TextTestSuite;
import org.eclipse.cdt.ui.tests.text.contentassist.ContentAssistTestSuite;
import org.eclipse.cdt.ui.tests.text.contentassist2.ContentAssist2TestSuite;
import org.eclipse.cdt.ui.tests.text.selection.SelectionTestSuite;
import org.eclipse.cdt.ui.tests.typehierarchy.TypeHierarchyTestSuite;
import org.eclipse.cdt.ui.tests.viewsupport.ViewSupportTestSuite;

/**
 * Test all areas of the UI.
 */
public class AutomatedSuite extends TestSuite {

	/**
	 * Returns the suite.  This is required to
	 * use the JUnit Launcher.
	 */
	public static Test suite() {
		return new AutomatedSuite();
	}

	/**
	 * Construct the test suite.
	 */
	public AutomatedSuite() {
		
		// tests from package org.eclipse.cdt.ui.tests.text
		addTest(TextTestSuite.suite());

		// tests for package org.eclipse.cdt.ui.tests.viewsupport
		addTest(ViewSupportTestSuite.suite());

		// tests for package org.eclipse.cdt.ui.tests.callhierarchy
		addTest(CallHierarchyTestSuite.suite());

		// tests for package org.eclipse.cdt.ui.tests.typehierarchy
		addTest(TypeHierarchyTestSuite.suite());

		// tests for package org.eclipse.cdt.ui.tests.includebrowser
		addTest(IncludeBrowserTestSuite.suite());

		// tests from package org.eclipse.cdt.ui.tests.text.contentAssist
		addTest(ContentAssistTestSuite.suite());

		// tests from package org.eclipse.cdt.ui.tests.text.contentAssist2
		addTest(ContentAssist2TestSuite.suite());

		// tests from the refactoring plugin
		addTest(RenameRegressionTests.suite());
		
		// tests from package org.eclipse.cdt.ui.tests.text.selection
		addTest(SelectionTestSuite.suite());
	}
	
}
