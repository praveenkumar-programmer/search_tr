package com.search.tr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class ViewMovieDetails extends AppCompatActivity {

    private LinearLayout root;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_movie_details);

        root = findViewById(R.id.magnets_layout);

        sp = getSharedPreferences("currentItem", 0);

        TextView title = findViewById(R.id.movie_title);
        TextView name = findViewById(R.id.movie_full_name);
        ImageView thumbnail = findViewById(R.id.movie_thumbnail);

        title.setText(sp.getString("title",""));
        name.setText(sp.getString("name",""));

//        RequestOptions options = new RequestOptions()
//                .centerCrop()
//                .placeholder(R.mipmap.ic_launcher_round)
//                .error(R.mipmap.ic_launcher_round);
//
//        Glide.with(this).load(sp.getString("thumbnailurl","")).apply(options).into(thumbnail);

        for(int i = 0; i < sp.getInt("noofmagnets", 0); i++){



            LayoutInflater inflater = LayoutInflater.from(this);
            FrameLayout layout = (FrameLayout) inflater.inflate(R.layout.button_layout, root, false);

            root.addView(layout);

            Button button = layout.findViewById(R.id.magnet_link_text);
            button.setText("copy magnet link " + i);
            button.getTag(i);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("magnet", sp.getString("magnet"+ v.getTag(), ""));
                    clipboard.setPrimaryClip(clip);

                    Toast.makeText(getApplicationContext(), "Magnet Link Copied to Clipboard", Toast.LENGTH_SHORT).show();

                }
            });

        }

    }
}
