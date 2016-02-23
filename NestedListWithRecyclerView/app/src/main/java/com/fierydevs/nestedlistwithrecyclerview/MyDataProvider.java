package com.fierydevs.nestedlistwithrecyclerview;

import android.support.v4.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Pranit on 23-02-2016.
 */
public class MyDataProvider extends AbstractExpandableDataProvider {
    String jsonString = "{\"Ord_NOV15/000182\":{\"OrderCode\":\"Ord_NOV15/000182\",\"Product\":[{\"ProdId\":\"23\",\"sub_pro_name\":\"Silver Pomfret\",\"image_path\":\"Silver-Pomfret.png\",\"OrderQty\":\"1\"},{\"ProdId\":\"30\",\"sub_pro_name\":\"Black Pomfret (Halwa)\",\"image_path\":\"Black-Pomfret-Halwa.png\",\"OrderQty\":\"1\"}],\"OrderDate\":\"Nov  3 2015 11:23:20:967AM\",\"DeliveryDate\":\"\"},\"Ord_OCT15/000162\":{\"OrderCode\":\"Ord_OCT15/000162\",\"Product\":[{\"ProdId\":\"1\",\"sub_pro_name\":\"Prawns\",\"image_path\":\"Prawns.png\",\"OrderQty\":\"1\"},{\"ProdId\":\"4\",\"sub_pro_name\":\"Tiger Prawns\",\"image_path\":\"Tiger-Prawns.png\",\"OrderQty\":\"2\"},{\"ProdId\":\"1\",\"sub_pro_name\":\"Prawns\",\"image_path\":\"Prawns.png\",\"OrderQty\":\"1\"},{\"ProdId\":\"4\",\"sub_pro_name\":\"Tiger Prawns\",\"image_path\":\"Tiger-Prawns.png\",\"OrderQty\":\"2\"}],\"OrderDate\":\"Oct 30 2015 01:53:16:347PM\",\"DeliveryDate\":\"\"},\"Ord_OCT15/000161\":{\"OrderCode\":\"Ord_OCT15/000161\",\"Product\":[{\"ProdId\":\"1\",\"sub_pro_name\":\"Prawns\",\"image_path\":\"Prawns.png\",\"OrderQty\":\"1\"},{\"ProdId\":\"4\",\"sub_pro_name\":\"Tiger Prawns\",\"image_path\":\"Tiger-Prawns.png\",\"OrderQty\":\"2\"}],\"OrderDate\":\"Oct 30 2015 01:51:24:597PM\",\"DeliveryDate\":\"\"},\"Ord_OCT15/000160\":{\"OrderCode\":\"Ord_OCT15/000160\",\"Product\":[{\"ProdId\":\"1\",\"sub_pro_name\":\"Prawns\",\"image_path\":\"Prawns.png\",\"OrderQty\":\"1\"},{\"ProdId\":\"4\",\"sub_pro_name\":\"Tiger Prawns\",\"image_path\":\"Tiger-Prawns.png\",\"OrderQty\":\"2\"},{\"ProdId\":\"1\",\"sub_pro_name\":\"Prawns\",\"image_path\":\"Prawns.png\",\"OrderQty\":\"1\"},{\"ProdId\":\"4\",\"sub_pro_name\":\"Tiger Prawns\",\"image_path\":\"Tiger-Prawns.png\",\"OrderQty\":\"2\"}],\"OrderDate\":\"Oct 30 2015 01:51:21:873PM\",\"DeliveryDate\":\"\"},\"Ord_OCT15/000137\":{\"OrderCode\":\"Ord_OCT15/000137\",\"Product\":[{\"ProdId\":\"4\",\"sub_pro_name\":\"Tiger Prawns\",\"image_path\":\"Tiger-Prawns.png\",\"OrderQty\":\"2\"}],\"OrderDate\":\"Oct 21 2015 09:06:28:383AM\",\"DeliveryDate\":\"\"}}";
    private List<Pair<GroupData, List<ChildData>>> mData;

    // for undo group item
    private Pair<GroupData, List<ChildData>> mLastRemovedGroup;
    private int mLastRemovedGroupPosition = -1;

    // for undo child item
    private ChildData mLastRemovedChild;
    private long mLastRemovedChildParentGroupId = -1;
    private int mLastRemovedChildPosition = -1;

    public MyDataProvider() {
        JSONObject jsonObject = null;
        mData = new LinkedList<>();
        try {
            jsonObject = new JSONObject(jsonString);
            Iterator<?> keys = jsonObject.keys();

            int i = 0;
            while (keys.hasNext()) {
                String key = (String) keys.next();
                if (jsonObject.get(key) instanceof JSONObject) {
                    final long groupId = i;
                    JSONObject j = (JSONObject) jsonObject.get(key);
                    String code = j.getString("OrderCode");
                    code = code.replace("\\", "");

                    String[] orderDate = j.getString("OrderDate").split("\\s+");
                    String od = orderDate[0] + "-" + orderDate[1] + "-" + orderDate[2];

                    String[] deliver = j.getString("DeliveryDate").split("\\s+");
                    String deliveryDate = "Not set";
                    if (j.getString("DeliveryDate").length() > 0)
                        deliveryDate = deliver[0] + "-" + deliver[1] + "-" + deliver[2];

                    final ConcreteGroupData group = new ConcreteGroupData(groupId, code, od, deliveryDate);
                    final List<ChildData> children = new ArrayList<>();

                    JSONArray res = j.getJSONArray("Product");
                    for (int k = 0; k < res.length(); k++) {
                        JSONObject x = res.getJSONObject(k);

                        final long childId = group.generateNewChildId();

                        final int productId = x.getInt("ProdId");
                        final String productName = x.getString("sub_pro_name");
                        final String orderQty = x.getString("OrderQty");
                        String imagePath = "http://mfapi.aeonappstore.com/product_images/" + x.getString("image_path").replaceAll("\\s", "%20");

                        children.add(new ConcreteChildData(childId, productId, productName, orderQty, imagePath));
                    }

                    mData.add(new Pair<GroupData, List<ChildData>>(group, children));

                    i++;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getGroupCount() {
        return mData.size();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return mData.get(groupPosition).second.size();
    }

    @Override
    public GroupData getGroupItem(int groupPosition) {
        if (groupPosition < 0 || groupPosition >= getGroupCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
        }

        return mData.get(groupPosition).first;
    }

    @Override
    public ChildData getChildItem(int groupPosition, int childPosition) {
        if (groupPosition < 0 || groupPosition >= getGroupCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
        }

        final List<ChildData> children = mData.get(groupPosition).second;

        if (childPosition < 0 || childPosition >= children.size()) {
            throw new IndexOutOfBoundsException("childPosition = " + childPosition);
        }

        return children.get(childPosition);
    }

    @Override
    public void moveGroupItem(int fromGroupPosition, int toGroupPosition) {

    }

    @Override
    public void moveChildItem(int fromGroupPosition, int fromChildPosition, int toGroupPosition, int toChildPosition) {

    }

    @Override
    public void removeGroupItem(int groupPosition) {

    }

    @Override
    public void removeChildItem(int groupPosition, int childPosition) {

    }

    @Override
    public long undoLastRemoval() {
        return 0;
    }

    public static final class ConcreteGroupData extends GroupData {
        private final long mId;
        private final String mText;
        private final String orderDate;
        private final String deliveryDate;
        private boolean mPinned;
        private long mNextChildId;

        ConcreteGroupData(long id, String text, String orderDate, String deliveryDate) {
            mId = id;
            mText = text;
            this.orderDate = orderDate;
            this.deliveryDate = deliveryDate;
            mNextChildId = 0;
        }

        @Override
        public boolean isSectionHeader() {
            return false;
        }

        @Override
        public long getGroupId() {
            return mId;
        }

        @Override
        public String getText() {
            return mText;
        }

        @Override
        public void setPinned(boolean pinnedToSwipeLeft) {
            mPinned = pinnedToSwipeLeft;
        }

        @Override
        public boolean isPinned() {
            return mPinned;
        }

        public long generateNewChildId() {
            final long id = mNextChildId;
            mNextChildId += 1;
            return id;
        }

        public String getOrderDate() {
            return orderDate;
        }

        public String getDeliveryDate() {
            return deliveryDate;
        }
    }

    public static final class ConcreteChildData extends ChildData {
        private long mId;
        private long productId;
        private final String mText;
        private final String orderQty;
        private final String imagePath;
        private boolean mPinned;

        public ConcreteChildData(long childId, long productId, String productName, String orderQty, String imagePath) {
            mId = childId;
            this.productId = productId;
            this.mText = productName;
            this.orderQty = orderQty;
            this.imagePath = imagePath;
        }

        @Override
        public long getChildId() {
            return mId;
        }

        @Override
        public String getText() {
            return mText;
        }

        @Override
        public void setPinned(boolean pinned) {
            mPinned = pinned;
        }

        @Override
        public boolean isPinned() {
            return mPinned;
        }

        public void setChildId(long id) {
            this.mId = id;
        }

        public String getOrderQty() {
            return orderQty;
        }

        public String getImagePath() {
            return imagePath;
        }

        public long getProductId() {
            return productId;
        }
    }
}
