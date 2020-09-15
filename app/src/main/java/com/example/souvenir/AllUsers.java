package com.example.souvenir;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import com.example.souvenir.R;
import com.example.souvenir.Adapter.MyListAdapter;
import com.example.souvenir.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllUsers extends AppCompatActivity {
    public static final String Name = "";
    public static final String Email = "";
    String Email2;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    List<String> LISTusers = new ArrayList<>();
    ArrayAdapter<User> LISTad;
    FirebaseUser firebaseUser;
    private EditText searchusers;
    User u;
    DatabaseReference reference;
    RecyclerView recyclerView;
    FirebaseAuth auth;
    String user;
    static List<User> artistList;

//ListView Listt;

    Query dbArtists, Q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Listt = findViewById(R.id.list2);
        searchusers = findViewById(R.id.searchusers);
        searchusers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchusersmethod(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        artistList = new ArrayList<>();

        u = new User();
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);

        recyclerView.setHasFixedSize(true);
        // LISTad=new ArrayAdapter<user2>(this , android.R.layout.simple_list_item_1  , artistList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //1. SELECT * FROM Artists

        dbArtists = FirebaseDatabase.getInstance().getReference("Users");

        dbArtists.addListenerForSingleValueEvent(valueEventListener);

        //2. SELECT * FROM Artists WHERE id = "-LAJ7xKNj4UdBjaYr8Ju"

     /*   Query query = FirebaseDatabase.getInstance().getReference("Artists")

                .orderByChild("id")

                .equalTo("-LAJ7xKNj4UdBjaYr8Ju");
*/


        //3. SELECT * FROM Artists WHERE country = "India"

      /*  Query query3 = FirebaseDatabase.getInstance().getReference("Artists")

                .orderByChild("country")

                .equalTo("India");*/


        //4. SELECT * FROM Artists LIMIT 2

        /*     Query query4 = FirebaseDatabase.getInstance().getReference("Artists").limitToFirst(2);*/


        //5. SELECT * FROM Artists WHERE age < 30

     /*   Query query5 = FirebaseDatabase.getInstance().getReference("Artists")

                .orderByChild("age")

                .endAt(29);

*/


        //6. SELECT * FROM Artists WHERE name = "A%"

      /*  Query query6 = FirebaseDatabase.getInstance().getReference("Artists")

                .orderByChild("name")

                .startAt("A")

                .endAt("A\uf8ff");

*/

        ;

        /*

         * You just need to attach the value event listener to read the values

         * for example

         * query6.addListenerForSingleValueEvent(valueEventListener)

         * */








     /*   Listt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the selected artist

               // String artist = artistList.get(i);
                final user2 user1=artistList.get(i);


                Intent intent = new Intent(getApplicationContext(), Specific.class);
                intent.putExtra("userid",user1.getId());
                startActivity(intent);




                //creating an intent







            }
        });

*/


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


    }

    /* private void searchusersmethod(String s){


         final FirebaseUser firebaseUser =FirebaseAuth.getInstance().getCurrentUser();
         Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("username")
                 .startAt(s).endAt(s+"\uf8ff");
         query.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                 artistList.clear();
                 for (DataSnapshot ds : dataSnapshot.getChildren()) {
                     User user = ds.getValue(User.class);

                     assert user!=null;
                     assert firebaseUser!=null;

                     if (!user.getId().equals(firebaseUser.getUid())) {
                         artistList.add(user);
                     }
                 }
                 MyListAdapter adapter = new MyListAdapter( getApplicationContext() ,artistList);
                 recyclerView.setAdapter(adapter);
             }
             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });

     }*/
    private void searchusersmethod(String s) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("search")
                .startAt(s).endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                artistList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User user = ds.getValue(User.class);

                    assert user != null;
                    assert firebaseUser != null;

                    if (!user.getId().equals(firebaseUser.getUid())) {
                        artistList.add(user);
                    }
                }
                MyListAdapter adapter = new MyListAdapter(getApplicationContext(), artistList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    ValueEventListener valueEventListener = new ValueEventListener() {

        @Override

        public void onDataChange(DataSnapshot dataSnapshot) {

            artistList.clear();

            if (dataSnapshot.exists()) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    u = snapshot.getValue(User.class);

                    if (!u.getId().equals(firebaseUser.getUid()) ) {
                        artistList.add(u);
                    }

                    MyListAdapter adapter = new MyListAdapter(getApplicationContext(), artistList);
                    recyclerView.setAdapter(adapter);
                    /*

                      MyListAdapter adapter = new MyListAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

                     */


                    // artistList.add("Email :" +artist.getEmail());
                    //Toast.makeText(getApplicationContext(),""+artist.getEmail()+"" , Toast.LENGTH_LONG).show();
                }

              /*  for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                  //  String Name = snapshot.getValue(user2.class).getName();
                   String Email = snapshot.getValue(user2.class).getEmail();
//Email2 = Email;
                   // artistList.add(" Name :" + "  "+Name +"\n"+ " Email :" + "  "+Email +" ");
                    artistList.add(Email);
                   // user2  u = snapshot.getValue(user2.class);

                   //0 artistList.add(u);
                   // Toast.makeText(getApplicationContext(),"Datasnapshot"+Name + "  " +Email  , Toast.LENGTH_LONG).show();


                }*/

                // adapter.notifyDataSetChanged();
                // Listt.setAdapter(LISTad);
            }
            // Listt.setAdapter(LISTad);
        }


        @Override

        public void onCancelled(DatabaseError databaseError) {


        }

    };


}