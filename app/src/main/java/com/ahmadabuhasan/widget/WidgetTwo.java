package com.ahmadabuhasan.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */

/**
 * Created by Ahmad Abu Hasan on 1/10/2020.
 */

public class WidgetTwo extends AppWidgetProvider {

    private WidgetTwo mWidget = null;
    private boolean mStop = true;
    public static String MAIN_ACTIVITY_UPDATE_UI = "main_activity_update_ui";
    public static String KEY_MAIN_ACTIVITY_UI_BTN = "main_activity_ui_btn_key";
    public static String KEY_MAIN_ACTIVITY_UI_TEXT = "main_activity_ui_text_key";
    public static final int VAL_UPDATE_UI_PLAY = 1, VAL_UPDATE_UI_PAUSE =2;

    public WidgetTwo getInstance() {
        if (mWidget == null) {
            mWidget = new WidgetTwo();
        }
        return mWidget;
    }

    private PendingIntent getPendingIntent(Context context, int buttonId) {
        Intent intent = new Intent();
        intent.setClass(context, WidgetTwo.class);
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        intent.setData(Uri.parse("Muli:" + buttonId));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        return pendingIntent;
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        pushUpdate(context,appWidgetManager,"",false);
    }

    private void pushUpdate(Context context, AppWidgetManager appWidgetManager, String songName, Boolean play_pause) {

        RemoteViews remoteView = new RemoteViews(context.getPackageName(),R.layout.widget_two);
        remoteView.setOnClickPendingIntent(R.id.play_pause,getPendingIntent(context, R.id.play_pause));
        remoteView.setOnClickPendingIntent(R.id.prev_song, getPendingIntent(context, R.id.prev_song));
        remoteView.setOnClickPendingIntent(R.id.next_song, getPendingIntent(context, R.id.next_song));

        if (!songName.equals("")) {
            remoteView.setTextViewText(R.id.song_name, songName);
        }

        if (play_pause) {
            remoteView.setImageViewResource(R.id.play_pause, R.drawable.music_pause);
        }else {
            remoteView.setImageViewResource(R.id.play_pause, R.drawable.music_play);
        }

        ComponentName componentName = new ComponentName(context, WidgetTwo.class);
        appWidgetManager.updateAppWidget(componentName, remoteView);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (intent.hasCategory(Intent.CATEGORY_ALTERNATIVE)) {
            Uri data = intent.getData();
            int buttonId = Integer.parseInt(data.getSchemeSpecificPart());
            switch (buttonId) {
                case R.id.play_pause:
                    pushAction(context, MusicService.ACTION_PLAY_PAUSE);
                    if(mStop){
                        Intent startIntent = new Intent(context,MusicService.class);
                        context.startService(startIntent);
                        mStop = false;
                    }
                    break;
                case R.id.prev_song:
                    pushAction(context, MusicService.ACTION_PRE);
                    break;
                case R.id.next_song:
                    pushAction(context, MusicService.ACTION_NEXT);
                    break;
            }
        }else if (MAIN_ACTIVITY_UPDATE_UI.equals(action)){
            int play_pause =  intent.getIntExtra(KEY_MAIN_ACTIVITY_UI_BTN, -1);
            int song_id = intent.getIntExtra(KEY_MAIN_ACTIVITY_UI_TEXT, -1);
            switch (play_pause) {
                case VAL_UPDATE_UI_PLAY:
                    pushUpdate(context, AppWidgetManager.getInstance(context), "song_0"+song_id,true);
                    break;
                case VAL_UPDATE_UI_PAUSE:
                    pushUpdate(context, AppWidgetManager.getInstance(context), "song_0"+song_id,false);
                    break;
                default:
                    break;
            }

        }

        super.onReceive(context, intent);
    }

    private void pushAction(Context context, int ACTION) {
        Intent actionIntent = new Intent(MusicService.ACTION);
        actionIntent.putExtra(MusicService.KEY_USR_ACTION, ACTION);
        context.sendBroadcast(actionIntent);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {

        super.onDeleted(context, appWidgetIds);
    }
}