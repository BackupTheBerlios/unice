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

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.ui.DebugUITools;


/**
 *   Start an inferior process to run a Goo console.  Wrap it up as an Eclipse
 *   process and pass it on to the Workbench manager to display.
 *
 */
public class SchemeToplevel extends PlatformObject {

	private String entryName = null;
	private ILaunchConfigurationType fInferior;

	/**
	 * Constructor
	 *
	 * @param fileName The file to launch (may be null)
	 * @throws CoreException If the plugin can't run the launch.
	 * @throws IOException If the supplied file can't be located.
	 */
	public SchemeToplevel(final String fileName) throws CoreException, IOException {
		ILaunchConfigurationType launchConfigurationType =
			DebugPlugin.getDefault().getLaunchManager()
				.getLaunchConfigurationType("sclipse.toplevel.SchemeToplevel");
		List candidateConfigs = Collections.EMPTY_LIST;

		ILaunchConfiguration[] configs =
			DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurations(launchConfigurationType);

		ILaunchConfigurationWorkingCopy launchConfig =
			launchConfigurationType.newInstance(null, "Scheme Toplevel"); 
					
		DebugUITools.launch(launchConfig, ILaunchManager.RUN_MODE);

		
		
	}

	/**
	 * Terminate the running Goo Console.
	 *
	 */
	public final void terminate() {
		// Kill toplevel process
		IProcess proc = DebugUITools.getCurrentProcess();
		if (proc != null) {
			ILaunch launch = proc.getLaunch();
			try {
				launch.terminate();
				proc.terminate();
				proc = null;
			} catch (DebugException e) {
				e.printStackTrace();
			}
		}
	}
}