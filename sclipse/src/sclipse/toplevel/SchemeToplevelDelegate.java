package sclipse.toplevel;


/*
 * (c) Brent Fulgham <bfulgham@debian.org>, 2002
 * All Rights Reserved.
 *
 * This software is based on examples and the source for Eclipse, which
 * are all copyright IBM, 2000, 2001 under the terms of the "Common
 * Public License Version 0.5 (CPL)".
 *
 * This package of software is also released under the terms of the CPL.
 */

import java.text.MessageFormat;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;


import org.eclipse.debug.core.IDebugEventFilter;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.debug.core.DebugPlugin;
import sclipse.preferences.*;

import sclipse.*;


/**
 *   Under Eclipse, "a launch is the result of launching a debug session and/or
 *   one or more system processes."  The Goo Plugin uses the Launch for the
 *   latter purpose -- a system process.
 *
 *   The Eclipse platform requires lots of plumbing to support any external
 *   process by the various widgets, so this class is part of a rudimentary
 *   implementation that follows the strict requirements of the Goo API.
 *
 *   Unfortunately, this class could not simply Subclass from the standard
 *   Launch implementation because it required certain items in the constructor
 *   signature that were not available for the Goo inferior process.  So, this
 *   just cribs the standard implementation, with minor changes for the
 *   Goo-specific use.
 *
 * @see ILaunch
 * @see ILaunchManager
 */
public class SchemeToplevelDelegate implements ILaunchConfigurationDelegate {
	private ILaunchConfiguration fElement;
	private ILaunch fLaunch;
	private String fMode;
	private String fSchemeRuntime;
	private String fGooCommand;
	private Process fInferiorProcess;
	private IProcess fEclipseProcHandle;
	private IProgressMonitor fMonitor;

	/**
	 * Constructor
	 *
	 */
	public SchemeToplevelDelegate() {
		fSchemeRuntime = SclipsePlugin.getDefault().getPreferenceStore().getString(SclipsePreferencePage.P_INTERPRETER_LOCATION);
	}

	/**
	 *   All implementations of ILaunchConfigurationDelegate must support
	 *   the Launch command that accepts particular parameters that are of
	 *   no use for the Goo launch.
	 *
	 *   This method actually launches the Goo runtime shell.
	 *
	 * @param configuration the launch configuration.
	 * @param mode The launch mode.
	 * @param launch The launch event.
	 * @param monitor The progress monitor.
	 * @throws CoreException if the launch fails.
	 */
	public final void launch(final ILaunchConfiguration configuration, final String mode,
		final ILaunch launch, final IProgressMonitor monitor) throws CoreException {
		if (monitor == null) {
			fMonitor = new NullProgressMonitor();
		} else {
			fMonitor = monitor;
		}

		fElement = configuration;
		fMode = mode;
		fLaunch = launch;

		monitor.beginTask(MessageFormat.format("Launching {0}...",
			new String[] {configuration.getName()}), 3); //$NON-NLS-1$
		if (monitor.isCanceled()) {
			// Was the process canceled?
			return;
		}

		IDebugEventFilter filter = new SchemeToplevelEventFilter(launch);
		DebugPlugin.getDefault().addDebugEventFilter(filter);
		String command = "";

		fInferiorProcess = DebugPlugin.exec(new String[] {fSchemeRuntime, command}, null);
		fEclipseProcHandle = DebugPlugin.newProcess(launch, fInferiorProcess, "Scheme Toplevel");
		monitor.worked(1);
	}
}

