package vn.co.honda.hondacrm.ui.adapters.vehicles;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import org.jetbrains.annotations.NotNull;

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private ItemTouchListener mListener;

    public SimpleItemTouchHelperCallback(ItemTouchListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public int getMovementFlags(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return super.isLongPressDragEnabled();
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return super.isItemViewSwipeEnabled();
    }

    @Override
    public void onSwiped(@NotNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public boolean onMove(@NotNull RecyclerView recyclerView,
                          @NotNull RecyclerView.ViewHolder viewHolder,
                          @NotNull RecyclerView.ViewHolder target) {
        mListener.onMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return false;
    }
}
