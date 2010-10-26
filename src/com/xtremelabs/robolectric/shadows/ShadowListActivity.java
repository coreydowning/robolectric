package com.xtremelabs.robolectric.shadows;

import android.app.ListActivity;
import android.view.View;
import android.widget.ListView;
import com.xtremelabs.robolectric.ProxyDelegatingHandler;
import com.xtremelabs.robolectric.util.Implementation;
import com.xtremelabs.robolectric.util.Implements;
import com.xtremelabs.robolectric.util.SheepWrangler;

@SuppressWarnings({"UnusedDeclaration"})
@Implements(ListActivity.class)
public class ShadowListActivity extends ShadowActivity {
    @SheepWrangler private ProxyDelegatingHandler proxyDelegatingHandler;
    private ListView listView;

    public ShadowListActivity(ListActivity realActivity) {
        super(realActivity);
    }

    @Implementation
    public ListView getListView() {
        if (listView == null) {
            if ((listView = findListView(contentView)) == null) {
                throw new RuntimeException("No ListView found under content view");
            }
        }
        return listView;
    }

    private ListView findListView(View parent) {
        if (parent instanceof ListView) {
            return (ListView) parent;
        }
        ShadowViewGroup shadowViewGroup = (ShadowViewGroup) proxyDelegatingHandler.shadowOf(parent);
        for (int i = 0; i < shadowViewGroup.getChildCount(); i++) {
            ListView listView = findListView(shadowViewGroup.getChildAt(i));
            if (listView != null) {
                return listView;
            }
        }
        return null;
    }
}
