package me.umarsetyawan.tugaspts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class FavActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);
        recyclerView = findViewById(R.id.rvdata);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<User> users = Realm.getDefaultInstance().where(User.class).findAll();
        adapter = new Adapter(users,FavActivity.this);
        recyclerView.setAdapter(adapter);

    }
}