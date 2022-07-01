package irimitlad.contentdashboard.views.extra;

import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.html.Div;

public class DashboardBlock
     extends Div {

    public final int index;
    private final Dashboard parent;
    private final Div mainSegment;

    public DashboardBlock(Dashboard parent,
                          int leftX,
                          int rightX,
                          int topY,
                          int bottomY) {
        super();
        setClassName("content_block");
        this.parent = parent;
        index = parent.addBlock(this, leftX, rightX, topY, bottomY);

        // Set upper segments
        Div bigContainer = setSegment(this);
        setCornerSegment(bigContainer, "content_block_left_segment", "content_block_top_segment");
        Div smallContainer = setSegment(bigContainer);
        DropTarget.create(setSegment(smallContainer, "content_block_outer_segment", "content_block_top_segment")).addDropListener(segmentDroppingEvent -> this.parent.addBlock(false, index, false, true));
        DropTarget.create(setSegment(smallContainer, "content_block_inner_segment", "content_block_top_segment")).addDropListener(segmentDroppingEvent -> this.parent.addBlock(true, index, false, true));
        setCornerSegment(bigContainer, "content_block_right_segment", "content_block_top_segment");

        // Set central segments
        bigContainer = setSegment(this);
        DropTarget.create(setSegment(bigContainer, "content_block_outer_segment", "content_block_left_segment")).addDropListener(segmentDroppingEvent -> this.parent.addBlock(false, index, true, true));
        DropTarget.create(setSegment(bigContainer, "content_block_inner_segment", "content_block_left_segment")).addDropListener(segmentDroppingEvent -> this.parent.addBlock(true, index, true, true));
        Div mainSegment = setSegment(bigContainer, "content_block_central_segment");
        DropTarget<Div> mainSegmentDropTarget = DropTarget.create(mainSegment);

        // No need to replace a block with another one, just change its name
        mainSegmentDropTarget.addDropListener(mainSegmentDroppingEvent -> resetIndex());
        this.mainSegment = mainSegment;
        DropTarget.create(setSegment(bigContainer, "content_block_inner_segment", "content_block_right_segment")).addDropListener(segmentDroppingEvent -> this.parent.addBlock(true, index, true, false));
        DropTarget.create(setSegment(bigContainer, "content_block_outer_segment", "content_block_right_segment")).addDropListener(segmentDroppingEvent -> this.parent.addBlock(false, index, true, false));

        // Set lower segments
        bigContainer = setSegment(this);
        setCornerSegment(bigContainer, "content_block_left_segment", "content_block_bottom_segment");
        smallContainer = setSegment(bigContainer);
        DropTarget.create(setSegment(smallContainer, "content_block_inner_segment", "content_block_bottom_segment")).addDropListener(segmentDroppingEvent -> this.parent.addBlock(true, index, false, false));
        DropTarget.create(setSegment(smallContainer, "content_block_outer_segment", "content_block_bottom_segment")).addDropListener(segmentDroppingEvent -> this.parent.addBlock(false, index, false, false));
        setCornerSegment(bigContainer, "content_block_right_segment", "content_block_bottom_segment");

        // Set name
        resetIndex();

        // Set position and size
        updateLayout();
    }

    private static void setCornerSegment(Div container,
                                         String segmentXClass,
                                         String segmentYClass) {
        setSegment(container, "content_block_outer_segment", "content_block_inner_segment", segmentXClass, segmentYClass);
    }

    private static Div setSegment(Div container,
                                  String... segmentClasses) {
        Div segment = new Div();
        for (String segmentClass : segmentClasses)
            segment.addClassName(segmentClass);
        container.add(segment);
        return segment;
    }

    private static double toPercentage(int dividend) {
        return (double) (dividend) / 10_737_418.24;
    }

    public void resetIndex() {
        mainSegment.setText("Content #" + parent.increaseBlockCount());
    }

    public void updateLayout() {
        int leftX = parent.blockLeftXs.get(index);
        int topY = parent.blockTopYs.get(index);
        getElement().setAttribute("style", "left: " + toPercentage(leftX) + "%; top: " + toPercentage(topY) + "%; width: " + toPercentage(parent.blockRightXs.get(index) - leftX) + "%; height: " + toPercentage(parent.blockBottomYs.get(index) - topY) + "%");
    }
}
