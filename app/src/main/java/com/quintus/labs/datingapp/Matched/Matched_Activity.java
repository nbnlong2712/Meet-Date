package com.quintus.labs.datingapp.Matched;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.CalculateAge;
import com.quintus.labs.datingapp.Utils.PreferenceUtils;
import com.quintus.labs.datingapp.Utils.TopNavigationViewHelper;
import com.quintus.labs.datingapp.Utils.User;

import java.util.ArrayList;
import java.util.List;
public class Matched_Activity extends AppCompatActivity {

    private static final String TAG = "Matched_Activity";
    private static final int ACTIVITY_NUM = 2;
    List<Users> matchList = new ArrayList<>();
    List<User> copyList = new ArrayList<>();
    private Context mContext = Matched_Activity.this;
    private String userId, userSex, lookforSex;
    private double latitude = 37.349642;
    private double longtitude = -121.938987;
    private EditText search;
    private List<Users> usersList = new ArrayList<>();
    private RecyclerView recyclerView, mRecyclerView;
    private ActiveUserAdapter adapter;
    private MatchUserAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matched);

        setupTopNavigationView();
        searchFunc();


        recyclerView = findViewById(R.id.active_recycler_view);
        mRecyclerView = findViewById(R.id.matche_recycler_view);

        adapter = new ActiveUserAdapter(usersList, getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        prepareActiveData();

        mAdapter = new MatchUserAdapter(matchList, getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager1);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        prepareMatchData();


    }

    private void prepareActiveData() {

        usersList.addAll(loadUsersMatched());

        adapter.notifyDataSetChanged();
    }

    private void prepareMatchData() {

        matchList.addAll(loadUsersMatched());

        mAdapter.notifyDataSetChanged();
    }

    public ArrayList<Users> loadUsersMatched() {
        ArrayList<Users> listMatched = new ArrayList<>();
        ArrayList<String> listEmail = new ArrayList<>();
        String mail = PreferenceUtils.getEmail(mContext);
        SQLiteDatabase db = openOrCreateDatabase("Matched.db", Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("select * from Matched where email_1 = ?" + "OR email_2=?", new String[]{mail, mail});
        while (cursor.moveToNext()) {
            String email_1 = cursor.getString(0);
            String email_2 = cursor.getString(1);
            if (!email_1.equals(mail) && !listEmail.contains(email_1)) {
                listEmail.add(email_1);
            }
            if (!email_2.equals(mail) && !listEmail.contains(email_2)) {
                listEmail.add(email_2);
            }
        }

        SQLiteDatabase db1 = openOrCreateDatabase("Account.db", Context.MODE_PRIVATE, null);
        SQLiteDatabase db2 = openOrCreateDatabase("Profile.db", Context.MODE_PRIVATE, null);
        for (int i = 0; i < listEmail.size(); i++) {
            Cursor cursor1 = db1.rawQuery("select * from Account where email = ?", new String[]{listEmail.get(i)});
            Cursor cursor2 = db2.rawQuery("select * from Profile where email_p = ?", new String[]{listEmail.get(i)});

            cursor1.moveToFirst();
            cursor2.moveToFirst();
            String age = cursor1.getString(3);
            String name = cursor1.getString(1);
            String decribe = cursor2.getString(7);
            String path=cursor2.getString(1);
            String interest=cursor2.getString(11);
            CalculateAge cal = new CalculateAge(age);
            int age_ = cal.getAge();
            String gender_ = cursor1.getString(5);
            Users users = new Users(listEmail.get(i), name, age_, path, decribe, interest, 200);
            listMatched.add(users);
        }
        return listMatched;
    }

    private void searchFunc() {
       /* search = findViewById(R.id.searchBar);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchText();
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchText();
            }
        });*/
    }

    /* private void searchText() {
         String text = search.getText().toString().toLowerCase(Locale.getDefault());
         if (text.length() != 0) {
             if (matchList.size() != 0) {
                 matchList.clear();
                 for (User user : copyList) {
                     if (user.getUsername().toLowerCase(Locale.getDefault()).contains(text)) {
                         matchList.add(user);
                     }
                 }
             }
         } else {
             matchList.clear();
             matchList.addAll(copyList);
         }

         mAdapter.notifyDataSetChanged();
     }

     private boolean checkDup(User user) {
         if (matchList.size() != 0) {
             for (User u : matchList) {
                 if (u.getUsername() == user.getUsername()) {
                     return true;
                 }
             }
         }

         return false;
     }

     private void checkClickedItem(int position) {

         User user = matchList.get(position);
         //calculate distance
         Intent intent = new Intent(this, ProfileCheckinMatched.class);
         intent.putExtra("classUser", user);

         startActivity(intent);
     }
 */
    private void setupTopNavigationView() {
        Log.d(TAG, "setupTopNavigationView: setting up TopNavigationView");
        BottomNavigationViewEx tvEx = findViewById(R.id.topNavViewBar);
        TopNavigationViewHelper.setupTopNavigationView(tvEx);
        TopNavigationViewHelper.enableNavigation(mContext, tvEx);
        Menu menu = tvEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }


}
