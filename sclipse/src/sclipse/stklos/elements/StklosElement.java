package sclipse.stklos.elements;

/**
 * @author SemeriAgostino
 * 
 */

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.Position;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * This class represents a generic Stklos element's outline data.  It is the
 * basis for all of the other more-specific data types in the outline view.
 */
public abstract class StklosElement {

	private String name; // The top-level name
	private String type; // The form type
	private Position position; // Position in the document
	//public List modifiers = new ArrayList();  // These don't exist (yet)
	private List children = new ArrayList();
	private StklosElement parent = null;
	private ImageDescriptor imageDesc = null;

	/**
	 * Abstract class representing a Stklos element.
	 *
	 */
	public StklosElement () {
	}

	/**
	 * Standard Children accessor.
	 * @return the List of children.
	 */
	public final List getChildren () {
		
		return children;
	}

	/**
	 * Standard Parent accessor.
	 * @return the parent StklosElement
	 */
	public final StklosElement getParent () {
		
		return parent;
	}

	/**
	 * Standard Parent accessor.
	 * @param p the parent StklosElement
	 */
	public final void setParent (final StklosElement p) {
		
		parent = p;
	}

	/**
	 * Standard Position accessor.
	 * @return the name
	 */
	public final Position getPosition () {
		
		return position;
	}

	/**
	 * Standard Position accessor.
	 * @param p The position to assign.
	 */
	public final void setPosition (final Position p) {
		
		position = p;
	}

	/**
	 * Standard Name accessor.
	 * @return the name
	 */
	public final String getName () {
		
		return name;
	}

	/**
	 * Standard name mutator.
	 * @param nm The name of the Element.
	 */
	public final void setName (final String nm) {
		
		name = nm;
	}

	/**
	 * Standard Type accessor.
	 * @return the name
	 */
	public final String getType () {
		
		return type;
	}

	/**
	 * Standard Type mutator.
	 * @param typ The name of the Element.
	 */
	public final void setType (final String typ) {
		
		type = typ;
	}

	/**
	 * Predicate indicating whether child elements are present
	 * @return true if no children, false otherwise.
	 */
	public final boolean noChildren () {
		
		return children.isEmpty();
	}

	/**
	 * Return string representation of the Stklos Element.
	 * @return The string representation of the Element.
	 */
	public String toString () {
		
		// return name + "|" + type;
		return name;
	}

	/**
	 * Return the image descriptor for the Stklos Element.
	 * @return the image descriptor for the Stklos Element.
	 */
	public final ImageDescriptor getImage () {
		
		return imageDesc;
	}

	/**
	 * Set the image descriptor for the Stklos Element.
	 * @param desc the image descriptor for the Stklos Element.
	 */
	protected final void setImage (final ImageDescriptor desc) {
		
		imageDesc = desc;
	}
}
