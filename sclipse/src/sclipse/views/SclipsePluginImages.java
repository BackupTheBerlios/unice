package sclipse.views;

/**
 * @author Semeria Agostino
 * sclipse
 * 
 */

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.swt.graphics.Image;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;

import sclipse.SclipsePlugin;

/**
 * Bundle of most images used by the Stklos plugin.
 */
public final class SclipsePluginImages {

	private static final String NAME_PREFIX = "sclipse.";
	private static final int NAME_PREFIX_LENGTH = NAME_PREFIX.length();

	private static URL fgIconBaseURL = null;

	// Determine display depth. If depth > 4 then we use high color images. Otherwise low color
	// images are used
	static {
		String pathSuffix = "icons/full/";
		try {
			fgIconBaseURL =
				new URL(
					SclipsePlugin
						.getDefault()
						.getDescriptor()
						.getInstallURL(),
					pathSuffix);
		} catch (MalformedURLException e) {
			// SclipsePlugin.log(e);
		}
	}

	// The plugin registry
	private static final ImageRegistry IMAGE_REGISTRY = new ImageRegistry();

	/*
	 * Available cached Images in the Goo plugin image registry.
	 */
//	public static final String IMG_CLASS = NAME_PREFIX + "class_obj.gif";
	public static final String IMG_FUN = NAME_PREFIX + "fun_obj.gif";
//	public static final String IMG_USE = NAME_PREFIX + "use_obj.gif";
//	public static final String IMG_METH = NAME_PREFIX + "methdef_obj.gif";
//	public static final String IMG_GEN = NAME_PREFIX + "generic_obj.gif";
//	public static final String IMG_PROP = NAME_PREFIX + "prop_obj.gif";
//	public static final String IMG_INFERIOR = NAME_PREFIX + "run_exc.gif";
//	public static final String IMG_OBJS_CFILE = NAME_PREFIX + "classf_obj.gif";
	/*
	 * Set of predefined Image Descriptors.
	 */
	private static final String T_OBJ = "obj16";
	private static final String T_OVR = "ovr16";

	public static final ImageDescriptor DESC_FUN =
		createManaged(T_OBJ, IMG_FUN);
//	public static final ImageDescriptor DESC_GEN =
//		createManaged(T_OBJ, IMG_GEN);
//	public static final ImageDescriptor DESC_CLASS =
//		createManaged(T_OBJ, IMG_CLASS);
//	public static final ImageDescriptor DESC_METH =
//		createManaged(T_OBJ, IMG_METH);
//	public static final ImageDescriptor DESC_USE =
//		createManaged(T_OBJ, IMG_USE);
//	public static final ImageDescriptor DESC_PROP =
//		createManaged(T_OBJ, IMG_PROP);
//	public static final ImageDescriptor DESC_INFERIOR =
//		createManaged(T_OBJ, IMG_INFERIOR);
//	public static final ImageDescriptor DESC_OBJS_CFILE =
//		createManaged(T_OBJ, IMG_OBJS_CFILE);
//
//	public static final ImageDescriptor DESC_OVR_STATIC =
//		create(T_OVR, "static_co.gif");
//	public static final ImageDescriptor DESC_OVR_FINAL =
//		create(T_OVR, "final_co.gif");
//	public static final ImageDescriptor DESC_OVR_ABSTRACT =
//		create(T_OVR, "abstract_co.gif");
//	public static final ImageDescriptor DESC_OVR_SYNCH =
//		create(T_OVR, "synch_co.gif");
//	public static final ImageDescriptor DESC_OVR_RUN =
//		create(T_OVR, "run_co.gif");
//	public static final ImageDescriptor DESC_OVR_WARNING =
//		create(T_OVR, "warning_co.gif");
//	public static final ImageDescriptor DESC_OVR_ERROR =
//		create(T_OVR, "error_co.gif");
//	public static final ImageDescriptor DESC_OVR_OVERRIDES =
//		create(T_OVR, "over_co.gif");
//	public static final ImageDescriptor DESC_OVR_IMPLEMENTS =
//		create(T_OVR, "implm_co.gif");

	/**
	 * Private constructor (Helper Class)
	 *
	 */
	private SclipsePluginImages () {
	}

	/**
	 * Returns the image managed under the given key in this registry.
	 *
	 * @param key the image's key
	 * @return the image managed under the given key
	 */
	public static final Image get(final String key) {
		return IMAGE_REGISTRY.get(key);
	}

	/**
	 * Sets the three image descriptors for enabled, disabled, and hovered to an action. The actions
	 * are retrieved from the *tool16 folders.
	 * @param action The action to handle.
	 * @param iconName The icon to associate with the action.
	 */
	public static final void setToolImageDescriptors(
		final IAction action,
		final String iconName) {
		setImageDescriptors(action, "tool16", iconName);
	}

	/**
	 * Sets the three image descriptors for enabled, disabled, and hovered to an action. The actions
	 * are retrieved from the *lcl16 folders.
	 * @param action The action to handle.
	 * @param iconName The icon to associate with the action.
	 */
	public static final void setLocalImageDescriptors(
		final IAction action,
		final String iconName) {
		setImageDescriptors(action, "lcl16", iconName);
	}

	/**
	 * Helper method to access the image registry from the JavaPlugin class.
	 * @return The ImageRegistry for the current workspace.
	 */
	public static ImageRegistry getImageRegistry() {
		return IMAGE_REGISTRY;
	}

	//---- Helper methods to access icons on the file system --------------------------------------
	/**
	 * Create image descriptors for a particular action.
	 * @param action The action to handle.
	 * @param type The image type
	 * @param relPath The relative path to the icon.
	 */
	private static final void setImageDescriptors(
		final IAction action,
		final String type,
		final String relPath) {
		try {
			ImageDescriptor id =
				ImageDescriptor.createFromURL(
					makeIconFileURL("d" + type, relPath));
			if (id != null) {
				action.setDisabledImageDescriptor(id);
			}
		} catch (MalformedURLException e) {
			//SclipsePlugin.log(e);
		}
		try {
			ImageDescriptor id =
				ImageDescriptor.createFromURL(
					makeIconFileURL("c" + type, relPath));
			if (id != null) {
				action.setHoverImageDescriptor(id);
			}
		} catch (MalformedURLException e) {
			//SclipsePlugin.log(e);
		}
		action.setImageDescriptor(create("e" + type, relPath));
	}

	/**
	 * Create a managed image descriptor
	 * @param prefix Prefix to use
	 * @param name Filename
	 * @return The managed ImageDescriptor
	 */
	private static final ImageDescriptor createManaged(
		final String prefix,
		final String name) {
		try {
			ImageDescriptor result =
				ImageDescriptor.createFromURL(
					makeIconFileURL(
						prefix,
						name.substring(NAME_PREFIX_LENGTH)));
			IMAGE_REGISTRY.put(name, result);
			return result;
		} catch (MalformedURLException e) {
			return ImageDescriptor.getMissingImageDescriptor();
		}
	}

	/**
	 * Create an image descriptor from a prefix and a filename.
	 * @param prefix Prefix to use
	 * @param name Filename
	 * @return An image descriptor
	 */
	private static ImageDescriptor create(
		final String prefix,
		final String name) {
		try {
			return ImageDescriptor.createFromURL(makeIconFileURL(prefix, name));
		} catch (MalformedURLException e) {
			return ImageDescriptor.getMissingImageDescriptor();
		}
	}

	/**
	 * Creates a URL for an icon file.
	 * @param prefix The prefix to use
	 * @param name File name
	 * @return The URL
	 * @throws MalformedURLException if URL can't be generated for this file.
	 */
	private static final URL makeIconFileURL(
		final String prefix,
		final String name)
		throws MalformedURLException {
		if (fgIconBaseURL == null) {
			throw new MalformedURLException();
		}

		StringBuffer buffer = new StringBuffer(prefix);
		buffer.append('/');
		buffer.append(name);
		return new URL(fgIconBaseURL, buffer.toString());
	}

}
