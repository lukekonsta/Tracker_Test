package com.example.tracker_test;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PointF;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

public class SeeProgress extends WearableActivity {

    int backgroundMinimum, backgroundMaximum;
    protected boolean mUpdateListeners = true;

    SharedPreferences preferences;

    long allTimeProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_progress);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        backgroundMaximum = preferences.getInt("key_goal_daily", 10000);
        System.out.println(backgroundMaximum);
        backgroundMinimum = 0;



        DecoView arcView = (DecoView)findViewById(R.id.dynamicArcView);

// Create background track
        arcView.addSeries(new SeriesItem.Builder(Color.argb(255, 218, 218, 218))
                .setRange(backgroundMinimum, backgroundMaximum, 100)
                .setInitialVisibility(false)
                .setLineWidth(5f)
                .build());

//Create data series track
        SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 64, 196, 0))
                .setRange(backgroundMinimum, backgroundMaximum, 0)
                .setLineWidth(10f)
                .build();

        int series1Index = arcView.addSeries(seriesItem1);

        //Create data series track
        SeriesItem seriesItem2 = new SeriesItem.Builder(Color.argb(155, 0, 9, 90))
                .setRange(backgroundMinimum, backgroundMaximum, 0)
                .setLineWidth(10f)
                .setInset(new PointF(20f, 20f))
                .build();

        int series2Index = arcView.addSeries(seriesItem2);


        arcView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
                .setDelay(1000)
                .setDuration(2000)
                .build());



        arcView.addEvent(new DecoEvent.Builder(5000).setIndex(series1Index).setDelay(1000).build());
        arcView.addEvent(new DecoEvent.Builder(9000).setIndex(series2Index).setDelay(1000).build());



        final TextView textPercent = (TextView) findViewById(R.id.textPercentage);
        if (textPercent != null) {
            textPercent.setText("");
            addProgressListener(seriesItem1, textPercent, "%.0f%%");
        }

        final TextView textToGo = (TextView) findViewById(R.id.textRemaining);
        textToGo.setText("");
        addProgressRemainingListener(seriesItem1, textToGo, "%.0f min to goal", backgroundMaximum);


        allTimeProgress += System.currentTimeMillis();

    }//end of onCreate








    //Two methods for adjusting the graph text


    protected void addProgressListener(@NonNull final SeriesItem seriesItem, @NonNull final TextView view, @NonNull final String format) {
        if (format.length() <= 0) {
            throw new IllegalArgumentException("String formatter can not be empty");
        }

        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                if (mUpdateListeners) {
                    if (format.contains("%%")) {
                        // We found a percentage so we insert a percentage
                        float percentFilled = (currentPosition - seriesItem.getMinValue()) / (seriesItem.getMaxValue() - seriesItem.getMinValue());
                        view.setText(String.format(format, percentFilled * 100f));
                    } else {
                        view.setText(String.format(format, currentPosition));
                    }
                }
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });
    }



    protected void addProgressRemainingListener(@NonNull final SeriesItem seriesItem, @NonNull final TextView view, @NonNull final String format, final float maxValue) {
        if (format.length() <= 0) {
            throw new IllegalArgumentException("String formatter can not be empty");
        }

        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {

            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                if (mUpdateListeners) {
                    if (format.contains("%%")) {
                        // We found a percentage so we insert a percentage
                        view.setText(String.format(format, (1.0f - (currentPosition / seriesItem.getMaxValue())) * 100f));
                    } else {
                        view.setText(String.format(format, maxValue - currentPosition));
                    }
                }
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });
    }






}
