package sclipse;

import org.eclipse.ui.plugin.*;
import org.eclipse.core.runtime.*;
import java.util.*;
import sclipse.toplevel.*;

/**
 * The main plugin class to be used in the desktop.
 */
public class SclipsePlugin extends AbstractUIPlugin {
	//The shared instance.
	private static SclipsePlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	//Default Toplevel
	private SchemeToplevel toplevel = null;
	
	/**
	 * The constructor.
	 */
	public SclipsePlugin(IPluginDescriptor descriptor) {
		super(descriptor);
		plugin = this;
		try {
			resourceBundle   = ResourceBundle.getBundle("sclipse.SclipsePluginResources");
			
		}
		catch (MissingResourceException x) {
			resourceBundle = null;
		}
	
		
	}

	/**
	 * Returns the shared instance.
	 */
	public static SclipsePlugin getDefault() {
		return plugin;
	}
	
	/**
	 * Returns the shared instance.
	 */
	public SchemeToplevel getSchemeToplevel() {
		return toplevel;
	}
	

	/**
	 * Returns the shared instance.
	 */
	public void setSchemeToplevel(SchemeToplevel st) {
		toplevel = st;
	}
		

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = SclipsePlugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}
}
