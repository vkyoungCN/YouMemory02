package com.vkyoungcn.learningtools;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private RecyclerView allMissionListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        allMissionListView = (RecyclerView) findViewById(R.id.all_missions_rv);
/*下面要建LayoutManager、Adapter两个内容*/

    }
}
