package com.example.tracker_test;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends WearableActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        mTextView = (TextView) findViewById(R.id.text);

        // Enables Always-on
        setAmbientEnabled();



        // Create a list of words
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("Set Daily Goal"));
        //words.add(new Word("Set Micro Goal"));
        words.add(new Word("See Progress"));

        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        WordAdapter adapter = new WordAdapter(this, words);
        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // word_list.xml layout file.
        ListView listView = (ListView) findViewById(R.id.list);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                // Get the {@link Word} object at the given position the user clicked on
                Word word = words.get(position);
                String nameWord = word.getDefaultTranslation();
                //Toast.makeText(MainActivity.this, nameWord, Toast.LENGTH_LONG).show();

                if(nameWord.equals("Set Daily Goal")){

                    Intent intent = new Intent(MainActivity.this, SetDaily.class);
                    startActivity(intent);

                }/*else if(nameWord.equals("Set Micro Goal")){

                    Intent intent = new Intent(MainActivity.this, SetMicro.class);
                    startActivity(intent);

                }*/else if(nameWord.equals("See Progress")){

                    Intent intent = new Intent(MainActivity.this, SeeProgress.class);
                    startActivity(intent);

                }


            }
        });




    }
}
