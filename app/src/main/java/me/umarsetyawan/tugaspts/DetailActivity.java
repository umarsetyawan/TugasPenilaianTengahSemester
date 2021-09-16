package me.umarsetyawan.tugaspts;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import io.realm.Realm;

public class DetailActivity extends AppCompatActivity {
    TextView email, name;
    ImageView avatar;
    Button favbtn;
    ActionBar actionBar;
    List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        email = findViewById(R.id.EmailView);
        name = findViewById(R.id.NameView);
        avatar = findViewById(R.id.ImageView);
        favbtn = findViewById(R.id.fav_add);

        actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle bundle = getIntent().getExtras();

        String emailtxt = bundle.getString("email");
        String nametxt = bundle.getString("name");
        String avatarurl = bundle.getString("Avatar_url");

        byte[] mBytes = bundle.getByteArray("avatar");
        Bitmap bitmap = BitmapFactory.decodeByteArray(mBytes, 0, mBytes.length);

        email.setText(emailtxt);
        name.setText(nametxt);
        avatar.setImageBitmap(bitmap);
        String[] namefull = nametxt.split(" ");

        favbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User userFind = Realm.getDefaultInstance().where(User.class).equalTo("email", emailtxt).findFirst();
                if(userFind == null)
                {
                    Toast.makeText(DetailActivity.this, "Profile have been successfully added", Toast.LENGTH_SHORT).show();
                    Realm.getDefaultInstance().executeTransactionAsync(realm -> {
                        realm.copyToRealm(new User(emailtxt, namefull[0], namefull[1], avatarurl));
                    });
                }else {
                    Realm.getDefaultInstance().executeTransactionAsync(realm -> {
                        Realm.getDefaultInstance().where(User.class).equalTo("email", emailtxt).findFirst().deleteFromRealm();
                    });
                }
            }
        });

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}