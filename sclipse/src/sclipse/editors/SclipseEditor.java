package sclipse.editors;

import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorInput;

import sclipse.views.SclipseContentOutlinePage;

public class SclipseEditor extends TextEditor {

	private ColorManager colorManager;
	/** The outline page */
	private SclipseContentOutlinePage fOutlinePage;

	public SclipseEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new XMLConfiguration(colorManager));
		setDocumentProvider(new XMLDocumentProvider());
	}
	
	/** The <code>GooEditor</code> implementation of this
	 * <code>AbstractTextEditor</code> method  gets
	 * the Goo content outline page if request is for an
	 * outline page.
	 *
	 * @param required Gets the Goo Outline widget.
	 * @return The Goo Outline widget.
	 */
	
	/** The <code>GooEditor</code> implementation of this
	 * <code>AbstractTextEditor</code> method performs any extra
	 * disposal actions required by the Goo editor.
	 */
	public final void dispose() {
		
		if (fOutlinePage != null) {
			fOutlinePage.setInput(null);
		}

		super.dispose();
	}

	/** The <code>GooEditor</code> implementation of this
	 * <code>AbstractTextEditor</code> method performs any extra
	 * revert behavior required by the Goo editor.
	 */
	public final void doRevertToSaved() {
		
		super.doRevertToSaved();
		if (fOutlinePage != null) {
			fOutlinePage.update();
		}
	}

	/** The <code>GooEditor</code> implementation of this
	 * <code>AbstractTextEditor</code> method performs any extra
	 * save behavior required by the Goo editor.
	 *
	 * @param monitor The progress monitor
	 */
	public final void doSave(final IProgressMonitor monitor) {
		
		super.doSave(monitor);
		if (fOutlinePage != null) {
			fOutlinePage.update();
		}
	}

	/** The <code>GooEditor</code> implementation of this
	 * <code>AbstractTextEditor</code> method performs any extra
	 * save as behavior required by the Goo editor.
	 */
	public final void doSaveAs() {
		super.doSaveAs();
		if (fOutlinePage != null) {
			fOutlinePage.update();
		}
	}

	/** The <code>GooEditor</code> implementation of this
	 * <code>AbstractTextEditor</code> method performs sets the
	 * input of the outline page after AbstractTextEditor has set input.
	 *
	 * @param input The input for the output page to review.
	 * @throws CoreException if fOutlinePage throws
	 */
	public final void doSetInput(final IEditorInput input) throws CoreException {
		super.doSetInput(input);
		if (fOutlinePage != null) {
			fOutlinePage.setInput(input);
		}
	}
	
	public final Object getAdapter(final Class required) {
		
		if (IContentOutlinePage.class.equals(required)) {
			if (fOutlinePage == null) {
				fOutlinePage = new SclipseContentOutlinePage(getDocumentProvider(), this);
				if (getEditorInput() != null) {
					fOutlinePage.setInput(getEditorInput());
				}
			}
			return fOutlinePage;
		}
		return super.getAdapter(required);
	}

}
