package com.fierydevs.nestedlistwithrecyclerview;

/**
 * Created by Pranit on 23-02-2016.
 */
public abstract class AbstractExpandableDataProvider {
    public static abstract class BaseData {

        public abstract String getText();

        public abstract void setPinned(boolean pinned);

        public abstract boolean isPinned();
    }

    public static abstract class GroupData extends BaseData {
        public abstract boolean isSectionHeader();
        public abstract long getGroupId();
        public abstract String getOrderDate();
        public abstract String getDeliveryDate();
    }

    public static abstract class ChildData extends BaseData {
        public abstract long getChildId();
        public abstract String getOrderQty();
        public abstract String getImagePath();
    }

    public abstract int getGroupCount();
    public abstract int getChildCount(int groupPosition);

    public abstract GroupData getGroupItem(int groupPosition);
    public abstract ChildData getChildItem(int groupPosition, int childPosition);

    public abstract void moveGroupItem(int fromGroupPosition, int toGroupPosition);
    public abstract void moveChildItem(int fromGroupPosition, int fromChildPosition, int toGroupPosition, int toChildPosition);

    public abstract void removeGroupItem(int groupPosition);
    public abstract void removeChildItem(int groupPosition, int childPosition);

    public abstract long undoLastRemoval();
}
