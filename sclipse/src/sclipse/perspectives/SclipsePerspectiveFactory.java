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

        layout.setEditorAreaVisible(true);

        IFolderLayout top =
            layout.createFolder("top", IPageLayout.TOP, 1f, editorArea);  //$NON-NLS-1$
      //  top.addView(IPageLayout.ID_EDITOR_AREA);
       
        IFolderLayout bottom = 
            layout.createFolder("bottom", IPageLayout.BOTTOM, 0.3f, IPageLayout.ID_EDITOR_AREA);  //$NON-NLS-1$
        bottom.addView(IDebugUIConstants.ID_CONSOLE_VIEW);

        IFolderLayout left =
            layout.createFolder("left", IPageLayout.LEFT, 0.7f, IPageLayout.ID_OUTLINE);    //$NON-NLS-1$
        left.addView(IPageLayout.ID_NAVIGATE_ACTION_SET);
        left.addView(IPageLayout.ID_OUTLINE);
        

                
    }

}
