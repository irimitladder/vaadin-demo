package irimitlad.contentdashboard.views.extra;

import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.html.Div;

import java.util.ArrayList;
import java.util.List;

public class Dashboard
     extends Div {

    private final DropTarget<Dashboard> dropTarget;
    private final List<DashboardBlock> blocks;
    public final List<Integer> blockLeftXs;
    public final List<Integer> blockRightXs;
    public final List<Integer> blockTopYs;
    public final List<Integer> blockBottomYs;
    private int lastBlockIndex;

    public Dashboard() {
        super();
        setId("content_dashboard");

        // Add an option to add the first block
        setClassName("content_empty_dashboard");
        dropTarget = DropTarget.create(this);
        dropTarget.addDropListener(droppingEvent -> {
            removeClassName("content_empty_dashboard");
            dropTarget.setActive(false);
            new DashboardBlock(this, 0, 0b01000000_00000000_00000000_00000000, 0, 0b01000000_00000000_00000000_00000000);
        });

        blocks = new ArrayList<>();
        blockLeftXs = new ArrayList<>();
        blockRightXs = new ArrayList<>();
        blockTopYs = new ArrayList<>();
        blockBottomYs = new ArrayList<>();
        lastBlockIndex = 0;
    }

    public int increaseBlockCount() {
        return ++lastBlockIndex;
    }

    public void lowlightBlocks() {
        addClassName("content_dashboard_lowlighted");
    }

    public void unlowlightBlocks() {
        removeClassName("content_dashboard_lowlighted");
    }

    public void addBlock(boolean blockAlone,
                         int blockIndex,
                         boolean blockHorizontal,
                         boolean blockBefore) {
        List<DashboardBlock> shrunkBlocks = new ArrayList<>();
        shrunkBlocks.add(blocks.get(blockIndex));
        List<Integer> blockCoordinates1;
        List<Integer> blockCoordinates2;
        List<Integer> blockCoordinates3;
        List<Integer> blockCoordinates4;
        if (blockHorizontal) {
            blockCoordinates1 = blockLeftXs;
            blockCoordinates2 = blockRightXs;
            blockCoordinates3 = blockTopYs;
            blockCoordinates4 = blockBottomYs;
        } else {
            blockCoordinates1 = blockTopYs;
            blockCoordinates2 = blockBottomYs;
            blockCoordinates3 = blockLeftXs;
            blockCoordinates4 = blockRightXs;
        }
        if (!blockAlone) {
            getBlocks(shrunkBlocks, blockCoordinates1, blockCoordinates2, blockCoordinates3, blockCoordinates4);
            getBlocks(shrunkBlocks, blockCoordinates1, blockCoordinates2, blockCoordinates4, blockCoordinates3);
        }
        int blockCoordinate1 = blockCoordinates1.get(blockIndex);
        int blockCoordinate2 = blockCoordinates2.get(blockIndex);
        int blockUpdatedCoordinate = (blockCoordinate1 + blockCoordinate2) / 2;
        if (blockBefore)
            blockCoordinate2 = blockUpdatedCoordinate;
        else {
            blockCoordinates1 = blockCoordinates2;
            blockCoordinate1 = blockUpdatedCoordinate;
        }
        int blockCoordinate3 = 0b01000000_00000000_00000000_00000000;
        int blockCoordinate4 = 0;
        for (DashboardBlock shrunkBlock : shrunkBlocks) {
            blockCoordinates1.set(shrunkBlock.index, blockUpdatedCoordinate);
            shrunkBlock.updateLayout();
            int shrunkBlockCoordinate = blockCoordinates3.get(shrunkBlock.index);
            if (blockCoordinate3 > shrunkBlockCoordinate)
                blockCoordinate3 = shrunkBlockCoordinate;
            shrunkBlockCoordinate = blockCoordinates4.get(shrunkBlock.index);
            if (blockCoordinate4 < shrunkBlockCoordinate)
                blockCoordinate4 = shrunkBlockCoordinate;
        }
        if (blockHorizontal)
            new DashboardBlock(this, blockCoordinate1, blockCoordinate2, blockCoordinate3, blockCoordinate4);
        else
            new DashboardBlock(this, blockCoordinate3, blockCoordinate4, blockCoordinate1, blockCoordinate2);
    }

    // Adds a new block
    public int addBlock(DashboardBlock block,
                        int blockLeftX,
                        int blockRightX,
                        int blockTopY,
                        int blockBottomY) {
        add(block);
        int blockIndex = blocks.size();
        blocks.add(block);
        blockLeftXs.add(blockLeftX);
        blockRightXs.add(blockRightX);
        blockTopYs.add(blockTopY);
        blockBottomYs.add(blockBottomY);
        return blockIndex;
    }

    // Finds nearby blocks
    private void getBlocks(List<DashboardBlock> blocks,
                           List<Integer> blockCoordinates1,
                           List<Integer> blockCoordinates2,
                           List<Integer> blockCoordinates3,
                           List<Integer> blockCoordinates4) {
        int blockIndex = blocks.get(0).index;
        int blockCount = blockCoordinates1.size();
        int blockCoordinate1 = blockCoordinates1.get(blockIndex);
        int blockCoordinate2 = blockCoordinates2.get(blockIndex);
        while (true) {
            DashboardBlock block = null;
            int blockCoordinate4 = blockCoordinates4.get(blockIndex);
            for (blockIndex = 0; blockIndex < blockCount; blockIndex++)
                if ((blockCoordinates1.get(blockIndex).compareTo(blockCoordinate1) == 0) &&
                    (blockCoordinates2.get(blockIndex).compareTo(blockCoordinate2) == 0) &&
                    (blockCoordinates3.get(blockIndex).compareTo(blockCoordinate4) == 0)) {
                    block = this.blocks.get(blockIndex);
                    break;
                }
            if (block == null)
                return;
            blocks.add(block);
        }
    }
}
