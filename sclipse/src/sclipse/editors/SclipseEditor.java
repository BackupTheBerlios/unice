package sclipse.editors;

import org.eclipse.ui.editors.text.TextEditor;

public class SclipseEditor extends TextEditor {

	private ColorManager colorManager;

	public SclipseEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new XMLConfiguration(colorManager));
		setDocumentProvider(new XMLDocumentProvider());
	}
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

}
