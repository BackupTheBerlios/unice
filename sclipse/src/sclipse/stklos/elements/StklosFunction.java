package sclipse.stklos.elements;

/**
 * @author Semeria Agostino
 * sclipse
 */

import sclipse.views.SclipsePluginImages; 

/**
 * This class represents a Stklos 'function' objects's outline data.
 */
public class StklosFunction extends StklosElement {

	/**
	 * Constructor
	 * @param fname The function name.
	 */
	public StklosFunction (final String fname) {
		
		setName(fname);
		setImage(SclipsePluginImages.DESC_FUN);
	}

	/**
	 * Constructor
	 *
	 */
	public StklosFunction () {
		
		setImage(SclipsePluginImages.DESC_FUN);
	}

	/**
	 * Standard string converter.
	 * @return string representation of the function.
	 */
//	public final String toString () {
//		
//		if (noChildren()) {
//			return getName();
//		} else {
//			return getName() + " | " + getType();
//		}
//	}
}
