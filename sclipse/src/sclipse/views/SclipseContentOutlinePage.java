package sclipse.views;

/**
 * @author Semeria Agostino
 * 
 */

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.StringTokenizer;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DefaultPositionUpdater;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IPositionUpdater;
import org.eclipse.jface.text.Position;
// import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

// import sclipse.SclipsePlugin;
import sclipse.editors.SclipseEditor;
import sclipse.stklos.elements.StklosElement;
import sclipse.stklos.elements.StklosFunction;

/**
 * A content outline page which always represents the content of the
 * connected Sclipse editor.  Currently, this implementation parses the entire
 * file at one go.  This only happens when you save or load the Sclipse module
 * file.
 *
 * Eventually this will be updated to do real-time parsing.
 */
public class SclipseContentOutlinePage extends ContentOutlinePage {

	/**
	 * Divides the editor's document into some number of segments and provides
	 * elements for them.  This provides a context-sensitive outline of the
	 * source files.
	 */
	protected class ContentProvider implements ITreeContentProvider {

//		private static final String GOO_DC_RE = "\\(\\s*dc\\s";
		private static final String STKLOS_DEFINE_RE = "\\(\\s*define\\s";
//		private static final String GOO_DG_RE = "\\(\\s*dg\\s";
//		private static final String GOO_DM_RE = "\\(\\s*dm\\s";
//		private static final String GOO_DP_RE = "\\(\\s*dp\\s";
//		private static final String GOO_USE_RE = "\\(\\s*use\\s";
		private static final String SEGMENTS = "__stklos_segments";

		private IPositionUpdater fPositionUpdater = new DefaultPositionUpdater(SEGMENTS);
		private List fContent = new ArrayList();
//		private GooClass gooClassVals = new GooClass();			// Classes
//		private GooUse gooUseVals = new GooUse();				// Module 'use' Declarations
		private StklosFunction stklosFunVals = new StklosFunction("functions");	// Functions
//		private GooMeth gooMethVals = new GooMeth("methods");		// Methods
//		private GooGen gooGenVals = new GooGen("generics");		// Generic Functions

		/**
		 * Returns the position of the closing bracket after startPosition.
		 * @return the location of the closing bracket.
		 * @param startPosition - the beginning position
		 * @param document - the document being searched
		 * @throws BadLocationException if position is invalid.
		 */
		 protected final int searchForClosing(final int startPosition, final IDocument document)
			throws BadLocationException {

			final char openBracket = '(';
			final char closeBracket = ')';
			int currPosition = startPosition;

			// Make sure we bypass any leading blanks on the line -- otherwise
			// we'll double-count the opening bracket and never find the end.
			while ((document.getChar(currPosition) == ' ')
					|| (document.getChar(currPosition) == '\t')) {
						++currPosition;
			}

			int stack = 1;
			int closePosition = currPosition + 1;
			int length = document.getLength();
			char nextChar;

			while (closePosition < length && stack > 0) {
				nextChar = document.getChar(closePosition);
				if (nextChar == openBracket && nextChar != closeBracket) {
					stack++;
				} else if (nextChar == closeBracket) {
					stack--;
				} else if (nextChar == ';') {
					// Must loop until end-of-line
					int line = document.getLineOfOffset(closePosition);
					++line;
					closePosition = document.getLineOffset(line);
					continue;	// Don't want to increment again in this case
				}
				closePosition++;
			}

			if (stack == 0) {
				return closePosition - 1;
			} else {
				return -1;
			}
		}

		/**
		 * Find the signature of the Goo function or method passed.
		 * @param dfltSig Default Signature to use
		 * @param dt Data to evaluate
		 * @return Signature string representation
		 */
		protected final String findSignature(final String dfltSig, final String dt) {
			// We are handed an s-expression of the form
			// (a) or (a| <blah>) or (a| <blah> => <blah2>)
			//
			// We want to strip off the leading stuff prior
			// to the bar, then take the rest (removing the
			// trailing ')'.  If we are left with nothing,
			// we'll just assume <any> => null
			//

			String data = dt.substring(0, dt.indexOf(')'));

			StringTokenizer st = new StringTokenizer(data, "|");
			if (st.countTokens() <= 1) {
				return dfltSig;
			}

			// If two or more, we want everything after the
			// first token
			data = data.substring(st.nextToken().length()).trim();
			return data.substring(data.charAt(0) == '|' ? 1 : 0);
		}

		/**
		 * Find the type of the property or variable passed
		 * @param dt String to review for the type information.
		 * @return Type description string.
		 */
		protected final String findType(final String dt) {
			// We are handed an s-expression of the form
			// (a) or (a| <blah>) or (a| <blah> => <blah2>)
			//
			// We want to strip off the leading stuff prior
			// to the bar, then take the rest (removing the
			// trailing ')'.  If we are left with nothing,
			// we'll just assume <any> => null
			//
			final String dfltType = "<any>";
			final String propRE = "=>";

			String data = dt.substring(0, dt.indexOf(')'));

			int answer = data.indexOf(propRE);

			if (answer == -1) {
				return dfltType;
			}

			return data.substring(answer + propRE.length()).trim();
		}

		/**
		 * Find the element's name on the declaring line.
		 * @param data String to evaluate for the name.
		 * @return The name
		 */
		protected final String findName(final String data) {

			StringTokenizer st = new StringTokenizer(data);

			// Use only the first word on the line.  Discard any
			// other tokens that might be present
			if (st.countTokens() == 0) {
				return null;
			}

			// Return the name (stripping off the closing bracket
			return st.nextToken().replace(')', '\0');
		}

		/**
		 * Determine the module names 'used' by this Goo module.
		 * @param data Data to include in the model.
		 * @param lineScan Matcher for the document.
		 * @param p Position in the document.
		 */
//		protected final void listUseModules(final String data, final Matcher lineScan,
//			final Position p) {
//
//			final String exportRE = "/[eE][xX][pP][oO][rR][tT]";
//
//			// Discard any export portion:
//			String importData = data.replaceAll(exportRE, data);
//
//			// Break the line up based on the 'use' token
//			String[] imports = importData.split(GOO_USE_RE);
//
//			for (int i = 0; i < imports.length; ++i) {
//				// Use the first word of each of the tokens
//				StringTokenizer st = new StringTokenizer(imports[i]);
//
//				// Use only the first word on the line.  Discard any
//				// other tokens that might be present
//				if (st.countTokens() == 0) {
//					continue;
//				}
//
//				GooUse val = new GooUse();
//				val.setName(st.nextToken());
//
//				// Strip off the closing bracket
//				val.setName(val.getName().replace(')', '\0'));
//				val.setPosition(p);
//				val.setParent(gooUseVals);
//
//				gooUseVals.getChildren().add(val);
//			}
//		}

		/**
		 * Scan a line of data, looking for the individual declarations and
		 * type annotations.
		 * @param document The document to be scanned for tokens.
		 * @param matcher The matcher for the document.
		 * @param parent The element parent
		 * @param data Data to be included in the model.
		 * @param line Line to be evaluated
		 * @return True if a token was found, False otherwise.
		 * @throws BadLocationException if the document position is invalid.
		 * @throws BadPositionCategoryException if the position category is invalid.
		 */
		protected final boolean scanForTokens(final IDocument document, final Matcher matcher,
			final StklosElement parent, final String data, final int line)
			throws BadLocationException, BadPositionCategoryException {

			matcher.reset(data);
			if (matcher.find()) {
				// Determine range of source this encloses
				int start = document.getLineOffset(line);	// Subtract to read last line
				int end = searchForClosing(start, document);
				int length = end - start;
				Position p = new Position(start, length);
				document.addPosition(SEGMENTS, p);

				// Must now find the top level form's name and type,
				// but we need to skip any modifiers.
				String search = data.substring(matcher.end()).trim();
				String Name = findName(search);

				StklosElement val = null;
				String dfltSig = "<any>";
//				if (parent instanceof GooClass) {
//					val = new GooClass();
//					((GooClass) parent).getClassChildren().put(Name, val);
//				} else {
					if (parent instanceof StklosFunction) {
						dfltSig = "<any> => <void>";
						val = new StklosFunction();
						
//						if (parent instanceof GooMeth) {
//							val = new GooMeth();
//						} else if (parent instanceof GooGen) {
//							val = new GooGen();
//						} else {
//							val = new GooFun();
//						}
//					}
					parent.getChildren().add(val);
				}

				// Populate element
				val.setPosition(p);
				val.setParent(parent);
				val.setName(Name);
				if (Name.length() >= search.length()) {
					val.setType(dfltSig);
				} else {
					val.setType(findSignature(dfltSig, search.substring(Name.length())));
				}
				return true;
			}
			return false;
		}

		/**
		 * Parse the document, building an outline for the functions, methods,
		 * classes, properties, etc., declared in the module file.
		 * @param document The document to be outlined.
		 */
		protected final void parse(final IDocument document) {

//			final Pattern usePatt = Pattern.compile(GOO_USE_RE, Pattern.CASE_INSENSITIVE);
//			final Pattern dcPatt = Pattern.compile(GOO_DC_RE, Pattern.CASE_INSENSITIVE);
			final Pattern definePatt = Pattern.compile(STKLOS_DEFINE_RE, Pattern.CASE_INSENSITIVE);
//			final Pattern dmPatt = Pattern.compile(GOO_DM_RE, Pattern.CASE_INSENSITIVE);
//			final Pattern dgPatt = Pattern.compile(GOO_DG_RE, Pattern.CASE_INSENSITIVE);
//			final Pattern dpPatt = Pattern.compile(GOO_DP_RE, Pattern.CASE_INSENSITIVE);

			int lines = document.getNumberOfLines();
			int spanStart = 0;

//			Matcher lineUseScan = null;
//			Matcher lineDCScan = null;
			Matcher lineDEFINEScan = null;
//			Matcher lineDGScan = null;
//			Matcher lineDMScan = null;
//			Matcher lineDPScan = null;

//			boolean firstUse = true;
//			boolean firstDC = true;
			boolean firstDEFINE = true;
//			boolean firstDM = true;
//			boolean firstDG = true;

			String data = null;
			int offset = 0;
			int len = 0;

			try {
				offset = document.getLineOffset(0);
				len = document.getLineLength(0);
				data = document.get(offset, len);

				// Build the original matchers
//				lineUseScan = usePatt.matcher(data);
//				lineDCScan = dcPatt.matcher(data);
				lineDEFINEScan = definePatt.matcher(data);
//				lineDGScan = dgPatt.matcher(data);
//				lineDMScan = dmPatt.matcher(data);
//				lineDPScan = dpPatt.matcher(data);

				for (int line = 0; line < lines; ++line) {
					offset = document.getLineOffset(line);
					len = document.getLineLength(line);

					data = document.get(offset, len); //.trim();

					// If the line is a comment, don't parse it at all
					if (data.length() == 0) {
						continue;
					} else if (data.charAt(0) == ';') {
						continue;
					}

//					lineUseScan.reset(data);
//					if (lineUseScan.find()) {
//						// Add an entry to the document for this line of
//						// 'use' data:
//						int start = document.getLineOffset(line);
//
//						Position p = new Position(start, 1);
//						document.addPosition(SEGMENTS, p);
//
//						// Have one or more 'use' statements.  Need to add an
//						// entry for each one to the useDirectives array.
//						listUseModules(data, lineUseScan, p);
//
//						if (firstUse) {
//							fContent.add(gooUseVals);
//							firstUse = false;
//						}
//						continue;
//					}

					// Does this line contain a function definition?
					if (scanForTokens(document, lineDEFINEScan, stklosFunVals, data, line)) {
						if (firstDEFINE) {
							fContent.add(stklosFunVals);
							firstDEFINE = false;
						}
						continue;
					}

					// Does this line contain a method definition?
//					if (scanForTokens(document, lineDMScan, gooMethVals, data, line)) {
//						if (firstDM) {
//							fContent.add(gooMethVals);
//							firstDM = false;
//						}
//						continue;
//					}

					// Does this line contain a generic function definition?
//					if (scanForTokens(document, lineDGScan, gooGenVals, data, line)) {
//						if (firstDG) {
//							fContent.add(gooGenVals);
//							firstDG = false;
//						}
//						continue;
//					}

					// Does this line contain a class definition?
//					if (scanForTokens(document, lineDCScan, gooClassVals, data, line)) {
//						if (firstDC) {
//							fContent.add(gooClassVals);
//							firstDC = false;
//						}
//						continue;
//					}

					// Check to see if this is a property of a Goo class
//					lineDPScan.reset(data);
//					if (lineDPScan.find()) {
//						// Determine scope of definition
//						int start = document.getLineOffset(line);	// Subtract to read last line
//						int end = searchForClosing(start, document);
//						int length = end - start;
//
//						Position p = new Position(start, length);
//						document.addPosition(SEGMENTS, p);
//
//						// Determine the property name
//						String work = data.substring(lineDPScan.end()).trim();
//
//						// Populate the new Property
//						GooProp val = new GooProp(findName(work));
//						val.setType(findType(work.substring(val.getName().length())));
//						val.setPosition(p);
//
//						// Class name will be the first token following the next
//						// open bracket:
//						work = work.substring(val.getName().length()).trim();
//						if (work.charAt(0) == '(') {
//							work = work.substring(1).trim();
//						}
//
//						String className = findName(work);
//
//						// Need to find the class this property belongs to
//						GooClass gooClass = (GooClass) gooClassVals.getClassChildren().get(className);
//						if (gooClass == null) {
//							// Property declared before class!
//							gooClass = new GooClass();
//							gooClass.setName(className);
//							gooClassVals.getClassChildren().put(className, gooClass);
//						}
//
//						val.setParent(gooClass);
//						gooClass.getClassChildren().put(val.getName(), val);
//					}
//					// Otherwise, parsing for children
//					// [TO BE IMPLEMENTED]
				}
			} catch (BadPositionCategoryException x) {
				// GooEditorPlugin.log(x);
			} catch (BadLocationException x) {
				// GooEditorPlugin.log(x);
			}
		}

		/**
		 * @see IContentProvider#inputChanged(Viewer, Object, Object)
		 */
		public final void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
			if (oldInput != null) {
				IDocument document = fDocumentProvider.getDocument(oldInput);
				if (document != null) {
					try {
						document.removePositionCategory(SEGMENTS);
					} catch (BadPositionCategoryException x) {
						// GooEditorPlugin.log(x);
					}
					document.removePositionUpdater(fPositionUpdater);
				}
			}

//			gooClassVals.getClassChildren().clear();
//			gooUseVals.getChildren().clear();
			stklosFunVals.getChildren().clear();
//			gooMethVals.getChildren().clear();
//			gooGenVals.getChildren().clear();
			fContent.clear();

			if (newInput != null) {
				IDocument document = fDocumentProvider.getDocument(newInput);
				if (document != null) {
					document.addPositionCategory(SEGMENTS);
					document.addPositionUpdater(fPositionUpdater);

					parse(document);
				}
			}
		}

		/**
		 * @see IContentProvider#dispose
		 */
		public final void dispose() {
			if (fContent != null) {
				fContent.clear();
				fContent = null;
			}
		}

		/**
		 * @see IContentProvider#isDeleted(Object)
		 */
		public final boolean isDeleted(final Object element) {
			return false;
		}

		/**
		 * @see IStructuredContentProvider#getElements(Object)
		 */
		public final Object[] getElements(final Object element) {
//			if (element instanceof GooClass) {
//				return ((GooClass) element).getClassChildren().values().toArray();
//			}
			if (element instanceof StklosElement) {
				return ((StklosElement) element).getChildren().toArray();
			}
			return fContent.toArray();
		}

		/**
		 * @see ITreeContentProvider#hasChildren(Object)
		 */
		public final boolean hasChildren(final Object element) {
//			if (element instanceof GooClass) {
//				return !((GooClass) element).getClassChildren().isEmpty();
//			}
			if (element instanceof StklosElement) {
				return !((StklosElement) element).getChildren().isEmpty();
			}
			return element == fInput;
		}

		/**
		 * @see ITreeContentProvider#getParent(Object)
		 */
		public final Object getParent(final Object element) {
//			if (element instanceof GooUse) {
//				return fInput;
//			}
			if (element instanceof StklosElement) {
				StklosElement e = (StklosElement) element;
				if (e.getParent() == null) {
					return fInput;
				} else {
					return e.getParent();
				}
			}
			return null;
		}

		/**
		 * @see ITreeContentProvider#getChildren(Object)
		 */
		public final Object[] getChildren(final Object element) {
			if (element == fInput) {
				return fContent.toArray();
			}
//			if (element instanceof GooClass) {
//				return ((GooClass) element).getClassChildren().values().toArray();
//			}
			if (element instanceof StklosElement) {
				return ((StklosElement) element).getChildren().toArray();
			}

			return new Object[0];
		}
	};

	private Object fInput;
	private IDocumentProvider fDocumentProvider;
	private ITextEditor fTextEditor;
	// private GooEditor fEditor;
	private SclipseEditor fEditor;
	private static String fModifiers = null;

	/**
	 * Creates a content outline page using the given provider and the given editor.
	 * @param provider The IDocumentProvider
	 * @param editor The Goo text editor.
	 */
	public SclipseContentOutlinePage(final IDocumentProvider provider, final ITextEditor editor) {
		super();
		fDocumentProvider = provider;
		fTextEditor = editor;
		if (editor instanceof SclipseEditor) {
			fEditor = (SclipseEditor) editor;
		}
	}

	/**
	 * @see ContentOutlinePage#createControl
	 */
	public final void createControl(Composite parent) {
		super.createControl(parent);

//		GooElementLabelProvider lprovider =
//			new GooElementLabelProvider();
			//GooElementLabelProvider.getLabelDecorators(true, new OverrideAdornmentProvider()));

		TreeViewer viewer = getTreeViewer();
		viewer.setContentProvider(new ContentProvider());
//		viewer.setLabelProvider(new DecoratingLabelProvider(lprovider,
//			SclipsePlugin.getActiveWorkbenchWindow().getWorkbench().getDecoratorManager().getLabelDecorator()));
		viewer.addSelectionChangedListener(this);

		if (fInput != null) {
			viewer.setInput(fInput);
		}
	}

	/**
	 * @see ContentOutlinePage#selectionChanged
	 */
	public final void selectionChanged(final SelectionChangedEvent event) {

		super.selectionChanged(event);

		ISelection selection = event.getSelection();
		if (selection.isEmpty()) {
			fTextEditor.resetHighlightRange();
		} else {
			StklosElement segment = (StklosElement) ((IStructuredSelection) selection).getFirstElement();

			try {
				int start = segment.getPosition().getOffset();
				int length = segment.getPosition().getLength();
				fTextEditor.setHighlightRange(start, length, true);
			} catch (NullPointerException x) {
				fTextEditor.resetHighlightRange();
			} catch (IllegalArgumentException x) {
				fTextEditor.resetHighlightRange();
			}
		}
	}

	/**
	 * Sets the input of the outline page
	 * @param input The Input for the outline page
	 */
	public final void setInput(final Object input) {
		fInput = input;
		update();
	}

	/**
	 * Updates the outline page.
	 */
	public final void update() {
		TreeViewer viewer = getTreeViewer();

		if (viewer != null) {
			Control control = viewer.getControl();
			if (control != null && !control.isDisposed()) {
				control.setRedraw(false);
				viewer.setInput(fInput);
				//viewer.expandAll();
				control.setRedraw(true);
			}
		}
	}
	
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		
		if (newInput != null) {
			setInput(newInput);
			update();
		}
	}
}
