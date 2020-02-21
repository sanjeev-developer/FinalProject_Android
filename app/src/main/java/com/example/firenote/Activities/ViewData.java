package com.example.firenote.Activities;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.WindowManager;
import com.example.firenote.Adapter.ViewAdapter;
import com.example.firenote.R;
import com.example.firenote.database.AppDatabase;
import com.example.firenote.database.AppExecutors;
import com.example.firenote.model.UserData;

import java.util.List;

public class ViewData extends BaseActivity {

    ViewAdapter viewAdapter;
    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;
    private AppDatabase mDb;
    List<UserData> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewData.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_data);

        mDb = AppDatabase.getInstance(getApplicationContext());
        retrieveTasks();
    }

    public void setdata()
    {
        recyclerView = findViewById(R.id.my_recycler_viewdata);
        viewAdapter = new ViewAdapter(ViewData.this, user);
        layoutManager = new LinearLayoutManager(ViewData.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(viewAdapter);

    }

    private void retrieveTasks() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                user = mDb.noteDao().loadAllPersons();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       setdata();
                    }
                });
            }
        });
    }
}
