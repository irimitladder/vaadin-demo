package irimitlad.contentdashboard.views.extra;

import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.html.Div;

public class DashboardDraggableBlock
     extends Div
  implements DragSource<DashboardDraggableBlock> {

    public DashboardDraggableBlock(Panel parent,
                                   Dashboard dashboard) {
        super();
        setId("content_block");
        addDragStartListener(draggingStartingEvent -> dashboard.lowlightBlocks());
        addDragEndListener(draggingEndingEvent -> dashboard.unlowlightBlocks());
        parent.add(this);
    }
}
