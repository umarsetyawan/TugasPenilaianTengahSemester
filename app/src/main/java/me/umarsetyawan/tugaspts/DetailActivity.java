package me.umarsetyawan.tugaspts;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    TextView email, name;
    ImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        email = findViewById(R.id.EmailView);
        name = findViewById(R.id.NameView);
        avatar = findViewById(R.id.ImageView);

        Bundle bundle = getIntent().getExtras();

        String emailtxt = bundle.getString("email");
        String nametxt = bundle.getString("name");

        byte[] mBytes = bundle.getByteArray("avatar");
        Bitmap bitmap = BitmapFactory.decodeByteArray(mBytes, 0, mBytes.length);

        email.setText(emailtxt);
        name.setText(nametxt);
        avatar.setImageBitmap(bitmap);

    }
}