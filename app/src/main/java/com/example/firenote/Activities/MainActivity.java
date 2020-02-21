package com.example.firenote.Activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.firenote.Adapter.HomeAdapter;
import com.example.firenote.Adapter.NavigationAdapter;
import com.example.firenote.R;
import com.example.firenote.database.AppDatabase;
import com.example.firenote.database.AppExecutors;
import com.example.firenote.model.UserData;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

import static com.example.firenote.Adapter.NavigationAdapter.recposition;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    HomeAdapter homeAdapter;
    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;
    DrawerLayout drawer;
    NavigationView navigationView;
    NavigationAdapter navigationAdapter;
    Button addcategory;
    Dialog dialog;
    private AppDatabase mDb;
    List<UserData> user;
    EditText categorytext;
    UserData userData;
    private Boolean exit = false;

    @BindView(R.id.ll_emptycat)
    LinearLayout ll_emptycat;

    @BindView(R.id.ll_emptynote)
    LinearLayout ll_emptynote;

    @BindView(R.id.info_text)
    TextView info_text;

    @BindView(R.id.edt_searchnote)
    EditText edt_searchnote;

    @BindView(R.id.my_recycler_view)
    RecyclerView my_recycler_view;

    static int cattitle = 0;

    ArrayList<UserData> notesarray ;
    ArrayList<UserData> categoryarray ;
    ArrayList<String> catcompare ;
    ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ButterKnife.bind(this);
        userData = new UserData();
        mDb = AppDatabase.getInstance(getApplicationContext());
        getcurrentdate();

        edt_searchnote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());
            }
        });


        notesarray = new ArrayList<UserData>();
        catcompare = new ArrayList<String>();
        categoryarray = new ArrayList<UserData>();
        FabSpeedDial fabSpeedDial = (FabSpeedDial) findViewById(R.id.fab_add);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem)
            {
                switch (menuItem.getItemId()) {
                    case R.id.faboptions_addnote:

                        boolean move = false;

                        try {
                            String cat = user.get(cattitle).getCategory();
                            move = true;
                        }
                        catch (Exception e)
                        {
                            move = false;
                        }

                        if(!move)
                        {
                            displayAlert(MainActivity.this, "Please add a category first!");
                        }
                        else
                        {
                            intent = new Intent(MainActivity.this, Writenote.class);
                            intent.putExtra("category", user.get(cattitle).getCategory());
                            intent.putExtra("categoryposition", cattitle);
                            startActivity(intent);
                        }
                        return true;


                    case R.id.faboptions_deletedata:
                        //Toast.makeText(MainActivity.this, "Clean Database", Toast.LENGTH_LONG).show();
                        deletedata();
                        break;

                    case R.id.faboptions_viewdatabase:
                       // Toast.makeText(MainActivity.this, "View Database", Toast.LENGTH_LONG).show();
                        intent = new Intent(MainActivity.this, ViewData.class);
                        startActivity(intent);
                        break;

                    case R.id.faboptions_setting:
                        Toast.makeText(MainActivity.this, "setting note", Toast.LENGTH_LONG).show();
                        break;
                }
                return false;
            }
        });

        retrieveTasks();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_doctor);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view_doctor);
        addcategory = navigationView.findViewById(R.id.but_add_cat);
        recyclerView = navigationView.findViewById(R.id.rec_category);
        addcategory.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.but_add_cat:
                showdialog();
                break;
        }
    }

    public void showdialog()
    {
        dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MainActivity.this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.addcat_layout);
        dialog.setCancelable(false);

        categorytext =dialog.findViewById(R.id.edt_cat);
        Button  cancel= dialog.findViewById(R.id.but_cancel);
        Button  add= dialog.findViewById(R.id.but_add_cat);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                hideKeyboard(MainActivity.this);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addcategory();
                dialog.dismiss();
                hideKeyboard(MainActivity.this);
            }
        });

        dialog.show();
    }

    public void deletedata()
    {
        dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MainActivity.this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.delete_data_layout);
        dialog.setCancelable(true);

        Button  deldata= dialog.findViewById(R.id.but_deldata);

        deldata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        user = mDb.noteDao().loadAllPersons();

                        for(int i = 0 ; i<user.size(); i++)
                        {
                            mDb.noteDao().delete(user.get(i));
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "total size = "+user.size(), Toast.LENGTH_LONG).show();
                            }
                        });

                        System.out.println( "total size = "+user.size());
                    }
                });
            }
        });

        dialog.show();
    }

    private void retrieveTasks()
    {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                user = mDb.noteDao().loadAllPersons();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        notesarray = new ArrayList<UserData>();
                        catcompare = new ArrayList<String>();
                        categoryarray = new ArrayList<UserData>();

                        if(user.size()>0)
                        {
                            ll_emptycat.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                            for (int y = 0; y<user.size(); y++)
                            {
                                if(!catcompare.contains(user.get(y).getCategory()))
                                {
                                    categoryarray.add(user.get(y));
                                    catcompare.add(user.get(y).getCategory());
                                }
                            }

                            attachswipelistener();

                            info_text.setText(user.get(cattitle).getCategory());

                            for (int i = 0; i<user.size(); i++)
                            {
                                if(user.get(cattitle).getCategory().equals(user.get(i).getCategory()))
                                {
                                    if(user.get(i).getTitle()==null || user.get(i).getTitle().isEmpty() || user.get(i).getTitle().equals(""))
                                    {

                                    }
                                    else
                                    {
                                        notesarray.add(user.get(i));
                                    }
                                }
                            }

                            if(notesarray.isEmpty())
                            {
                                ll_emptynote.setVisibility(View.VISIBLE);
                                my_recycler_view.setVisibility(View.GONE);
                            }
                            else
                            {
                                ll_emptynote.setVisibility(View.GONE);
                                my_recycler_view.setVisibility(View.VISIBLE);
                                homeAdapter = new HomeAdapter(MainActivity.this, cattitle, notesarray);
                                layoutManager = new LinearLayoutManager(MainActivity.this);
                                my_recycler_view.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                                my_recycler_view.setHasFixedSize(true);
                                my_recycler_view.setItemAnimator(new DefaultItemAnimator());
                                my_recycler_view.setAdapter(homeAdapter);
                            }
                        }
                        else
                        {
                            ll_emptycat.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);

                            ll_emptynote.setVisibility(View.VISIBLE);
                            my_recycler_view.setVisibility(View.GONE);

                            attachswipelistener();
                        }
                    }
                });
            }
        });
    }

    public void addcategory()
    {
        catcompare = new ArrayList<String>();
        categoryarray = new ArrayList<UserData>();
        for (int y = 0; y<user.size(); y++)
        {
            if(!catcompare.contains(user.get(y).getCategory()))
            {
                categoryarray.add(user.get(y));
                catcompare.add(user.get(y).getCategory());
            }
        }


        if(categorytext.getText().toString().isEmpty() || categorytext.getText().toString().equals(""))
        {
            displayAlert(MainActivity.this, "Please enter the category first.");
        }
        else
        {
            if(catcompare.contains(categorytext.getText().toString()))
            {
                displayAlert(MainActivity.this, "Category already exist !");
            }
            else
            {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {

                        userData.setCategory(categorytext.getText().toString());
                        mDb.noteDao().insertPerson(userData);

                        retrieveTasks();
                    }
                });
            }
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void setreccolor(int position, String s) {
        drawer.closeDrawers();
        notesarray = new ArrayList<UserData>();
        cattitle = position;
        recposition(position);
        navigationAdapter.notifyDataSetChanged();
        info_text.setText(user.get(cattitle).getCategory());


        for (int i = 0; i<user.size(); i++)
        {
            if(user.get(i).getCategory().equals(s))
            {
                if(user.get(i).getTitle()==null || user.get(i).getTitle().isEmpty() || user.get(i).getTitle().equals(""))
                {

                }
                else
                {
                    notesarray.add(user.get(i));
                }
            }
        }


        if(notesarray.isEmpty())
        {
            ll_emptynote.setVisibility(View.VISIBLE);
            my_recycler_view.setVisibility(View.GONE);
        }
        else
        {
            ll_emptynote.setVisibility(View.GONE);
            my_recycler_view.setVisibility(View.VISIBLE);
            homeAdapter = new HomeAdapter(MainActivity.this, cattitle, notesarray);
            layoutManager = new LinearLayoutManager(MainActivity.this);
            my_recycler_view.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
            my_recycler_view.setHasFixedSize(true);
            my_recycler_view.setItemAnimator(new DefaultItemAnimator());
            my_recycler_view.setAdapter(homeAdapter);
        }
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            finishAffinity();
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }

    public void deletenote(final UserData userData, final boolean b) {

        dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MainActivity.this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.delete_note_layout);
        dialog.setCancelable(false);

        Button deldata= dialog.findViewById(R.id.but_deldata);
        Button cancel= dialog.findViewById(R.id.but_canceldata);

        deldata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {

                            if(b)
                            {
                                for(int i = 0; i<user.size(); i++)
                                {
                                    String cat = userData.getCategory();
                                    if(cat.equals(user.get(i).getCategory()))
                                    {
                                        mDb.noteDao().delete(user.get(i));
                                    }
                                }
                            }
                            else
                            {
                                mDb.noteDao().delete(userData);
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // homeAdapter.notifyDataSetChanged();
                                    dialog.cancel();
                                    retrieveTasks();
                                }
                            });
                        }
                    });
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.cancel();
            }
        });

        dialog.show();

    }

    public void attachswipelistener()
    {
        navigationAdapter = new NavigationAdapter(MainActivity.this, categoryarray, user);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(navigationAdapter);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(MainActivity.this, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Toast.makeText(MainActivity.this, "on Swiped ", Toast.LENGTH_SHORT).show();
                //Remove swiped item from list and notify the RecyclerView
                int position = viewHolder.getAdapterPosition();
                // arrayList.remove(position);
                // Toast.makeText(MainActivity.this, "on Move", Toast.LENGTH_SHORT).show();
                // navigationAdapter.notifyDataSetChanged();
                deletenote(user.get(viewHolder.getAdapterPosition()), true);
                navigationAdapter.notifyDataSetChanged();
            }
        };

        itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void getcurrentdate()
    {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        String formattedDate = df.format(c);
        System.out.println(">>>>>>"+formattedDate);
    }

    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<UserData> filterdata = new ArrayList<>();

        //looping through existing elements
        for (int i=0; i<notesarray.size(); i++) {
            //if the existing elements contains the search input
            if (notesarray.get(i).getSubtitle().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdata.add(notesarray.get(i));
            }
        }

        //calling a method of the adapter class and passing the filtered list
        homeAdapter = new HomeAdapter(MainActivity.this, cattitle, filterdata);
      //  homeAdapter.notifyDataSetChanged();
      //  layoutManager = new LinearLayoutManager(MainActivity.this);
//        my_recycler_view.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
//        my_recycler_view.setHasFixedSize(true);
//        my_recycler_view.setItemAnimator(new DefaultItemAnimator());
          my_recycler_view.setAdapter(homeAdapter);
    }
}
