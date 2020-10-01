package com.ahmadabuhasan.widget;

import java.util.ArrayList;
import java.util.List;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

/**
 * Created by Ahmad Abu Hasan on 1/10/2020.
 */

public class StackService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final int mCount = 10;
    private List<WidgetItem> mWidgetItems = new ArrayList<WidgetItem>();
    private Context mContext;
    private int mAppWidgetId;

    public StackRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    public void onCreate() {
        for (int i = 0; i < mCount; i++) {
            mWidgetItems.add(new WidgetItem(i + "!"));
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        mWidgetItems.clear();
    }

    public int getCount() {
        return mCount;
    }

    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(),
                R.layout.widget_item);
        if (position % 8 == 0) {
            rv.setImageViewResource(R.id.widget_item, R.drawable.sukarno);
        } else if (position % 7 == 0) {
            rv.setImageViewResource(R.id.widget_item, R.drawable.suharto);
        } else if (position % 6 == 0) {
            rv.setImageViewResource(R.id.widget_item, R.drawable.habibie);
        } else if (position % 5 == 0) {
            rv.setImageViewResource(R.id.widget_item, R.drawable.gusdur);
        } else if (position % 4 == 0) {
            rv.setImageViewResource(R.id.widget_item, R.drawable.megawati);
        } else if (position % 3 == 0) {
            rv.setImageViewResource(R.id.widget_item, R.drawable.megawati);
        } else if (position % 2 == 0) {
            rv.setImageViewResource(R.id.widget_item, R.drawable.sby);
        } else {
            rv.setImageViewResource(R.id.widget_item, R.drawable.jokowi);
        }

        Bundle extras = new Bundle();
        extras.putInt(WidgetOne.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.widget_item, fillInIntent);

        try {
            System.out.println("Loading view " + position);
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return rv;
    }

    public RemoteViews getLoadingView() {
        return null;
    }

    public int getViewTypeCount() {
        return 1;
    }

    public long getItemId(int position) {
        return position;
    }

    public boolean hasStableIds() {
        return true;
    }

    public void onDataSetChanged() {
    }
}
