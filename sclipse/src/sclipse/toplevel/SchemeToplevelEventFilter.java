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

import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.IDebugEventFilter;

/**
 *   This class serves to prevent the Launch system from throwing
 *   a tantrum because the Goo process doesn't use a standard Eclipse
 *   style launch protocol.
 *
 *   We basically tell everything to not Show or Handle any events
 *   from this launch.
 */
public class SchemeToplevelEventFilter implements IDebugEventFilter {

	private ILaunch fLaunch;

	/**
	 * Constructor for the filter
	 * @param launch The launch event.
	 */
	public SchemeToplevelEventFilter(final ILaunch launch) {
		fLaunch = launch;
	}

	/**
	 * This routine instructs the overall Launch system to not
	 * attempt to open a new Debug window for this process at
	 * Launch time.
	 *
	 * @param launch The launch to be displayed.
	 * @return False, since we never show the process launch.
	 */
	public final boolean showLaunch(final ILaunch launch) {
		return false;	// We never want to show the Goo process launch
	}

	/**
	 * Filter debug events to return Goo-relevant information.
	 *
	 * @param events Debug events to be filtered.
	 * @return Null, since we don't have any debug events we understand.
	 */
	public final DebugEvent[] filterDebugEvents(final DebugEvent[] events) {
		return null;
	}

	/**
	 * This routine instructs the overall Launch system to not
	 * attempt to open a new Debug window for this process when
	 * a Debug event is encountered.
	 *
	 * @param event The event to be evaluated.
	 * @return False, since we never process debug events.
	 */
	public final boolean showDebugEvent(final DebugEvent event) {
		return false;	// We never want to debug the Goo process
	}
}
