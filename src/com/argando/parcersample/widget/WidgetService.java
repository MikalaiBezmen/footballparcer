package com.argando.parcersample.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.widget.RemoteViewsService;

@TargetApi(11)
public class WidgetService extends RemoteViewsService {
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteFactory(this.getApplicationContext(), intent);
    }
}
