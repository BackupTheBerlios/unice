package sclipse.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.debug.ui.IDebugUIConstants;

//import sclipse.views.SampleView;


/**
 * @author Simeon Kostov
 */
public class SclipsePerspectiveFactory implements IPerspectiveFactory {

    /**
     * Constructor for JDependPerspectiveFactory.
     */
    public SclipsePerspectiveFactory() {
        super();
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
     */
    public void createInitialLayout(IPageLayout layout) {
        String editorArea = layout.getEditorArea();
       	IFolderLayout topLeft = layout.createFolder("topLeft", IPageLayout.LEFT, 0.25f,
					editorArea);
		topLeft.addView(IPageLayout.ID_RES_NAV);
		
		IFolderLayout bottomLeft = layout.createFolder("bottomLeft", IPageLayout.BOTTOM, 0.50f,
					"topLeft");

		bottomLeft.addView(IPageLayout.ID_OUTLINE);
					

     	layout.addView(IDebugUIConstants.ID_CONSOLE_VIEW, IPageLayout.BOTTOM, 0.66f, editorArea);

        

                
    }

}
