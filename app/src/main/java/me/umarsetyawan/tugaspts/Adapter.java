package me.umarsetyawan.tugaspts;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.BitmapRequestListener;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmQuery;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private final List<User> users;
    private final Activity activity;

    public Adapter(List<User> users, Activity activity) {
        this.users = users;
        this.activity = activity;
        for (User user: users)
        {
            System.out.println(user.getEmail());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.email.setText(users.get(position).getEmail());
        User userFind = Realm.getDefaultInstance().where(User.class).equalTo("email", users.get(position).getEmail()).findFirst();
        if (userFind == null) holder.fav.setImageResource(R.drawable.hollowstar);
        else holder.fav.setImageResource(R.drawable.star);
        holder.fav.setOnClickListener(view -> {
            User userAdded = Realm.getDefaultInstance().where(User.class).equalTo("email", users.get(position).getEmail()).findFirst();
            if (userAdded == null) {
                User user = users.get(position);
                Realm.getDefaultInstance().executeTransactionAsync(realm -> {
                    realm.copyToRealm(user);
                }, () -> {
                    holder.fav.setImageResource(R.drawable.star);
                }, error -> {
                    holder.fav.setImageResource(R.drawable.hollowstar);
                });
            }else {
                User user = users.get(position);
                Realm.getDefaultInstance().executeTransactionAsync(realm -> {
                    Objects.requireNonNull(realm.where(User.class).equalTo("email", users.get(position).getEmail()).findFirst()).deleteFromRealm();
                }, () -> {
                    holder.fav.setImageResource(R.drawable.hollowstar);
                }, error -> {
                    holder.fav.setImageResource(R.drawable.star);
                });
            }

        });
        holder.name.setText(users.get(position).getFirstName() + " " + users.get(position).getLastName());
        AndroidNetworking.get(users.get(position)
                .getAvatar())
                .setTag("imageRequestTag")
                .setPriority(Priority.MEDIUM)
                .setBitmapMaxHeight(400)
                .setBitmapMaxWidth(400)
                .setBitmapConfig(Bitmap.Config.ARGB_8888)
                .build()
                .getAsBitmap(new BitmapRequestListener() {
                    @Override
                    public void onResponse(Bitmap response) {
                        holder.avatar.setImageBitmap(response);
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return (users == null) ? 0 : users.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView email, name;
        private final ImageView avatar;
        private final ImageView fav;
        ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            email = itemView.findViewById(R.id.email);
            name = itemView.findViewById(R.id.name);
            avatar = itemView.findViewById(R.id.avatar);
            fav = itemView.findViewById(R.id.fav_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) avatar.getDrawable();

            Bitmap bitmap = bitmapDrawable.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bytes = stream.toByteArray();
            Bundle bundle = new Bundle();
            bundle.putString("email", email.getText().toString());
            bundle.putString("name", name.getText().toString());
            bundle.putByteArray("avatar", bytes);
            Intent intent = new Intent(activity, DetailActivity.class);
            intent.putExtras(bundle);
            activity.startActivity(intent);
        }
    }
}
