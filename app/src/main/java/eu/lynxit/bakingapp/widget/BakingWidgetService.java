package eu.lynxit.bakingapp.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by lynx on 24/02/18.
 */

public class BakingWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new BakingListProvider(this.getApplicationContext(),
                intent));
    }
}