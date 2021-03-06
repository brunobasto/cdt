/*******************************************************************************
 * Copyright (c) 2009, 2015 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Incorporated - initial API and implementation
 *******************************************************************************/
package org.eclipse.cdt.internal.autotools.ui.actions;

import org.eclipse.cdt.internal.autotools.core.AutotoolsNewMakeGenerator;
import org.eclipse.cdt.managedbuilder.core.IManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.cdt.ui.CUIPlugin;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Jeff Johnston
 *
 */
public class ReconfigureHandler extends AbstractAutotoolsHandler {

	@Override
	public Object execute(ExecutionEvent event) {
		return execute1(event);
	}

	@Override
	public void run(Shell activeShell) {
		IContainer container = getSelectedContainer();
		if (container == null)
			return;

		// We need to use a workspace root scheduling rule because adding
		// MakeTargets
		// may end up saving the project description which runs under a
		// workspace root rule.
		final ISchedulingRule rule = ResourcesPlugin.getWorkspace().getRoot();

		Job backgroundJob = new Job("Reconfigure Action") { //$NON-NLS-1$
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					ResourcesPlugin.getWorkspace().run((IWorkspaceRunnable) monitor1 -> {
						IProject project = getSelectedContainer().getProject();
						AutotoolsNewMakeGenerator m = new AutotoolsNewMakeGenerator();
						IManagedBuildInfo info = ManagedBuildManager.getBuildInfo(project);
						CUIPlugin.getDefault().startGlobalConsole();
						m.initialize(project, info, monitor1);
						try {
							m.reconfigure();
						} catch (CoreException e) {
							// do nothing for now
						}
					}, rule, IWorkspace.AVOID_UPDATE, monitor);
				} catch (CoreException e) {
					return e.getStatus();
				}
				IStatus returnStatus = Status.OK_STATUS;
				return returnStatus;
			}
		};

		backgroundJob.setRule(rule);
		backgroundJob.schedule();
	}

}
