package com.search.tr;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class ViewMovieDetails extends AppCompatActivity {

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_movie_details);

        LinearLayout root = findViewById(R.id.magnets_layout);

        sp = getSharedPreferences("currentItem", 0);

        TextView title = findViewById(R.id.movie_title);
        ImageView thumbnail = findViewById(R.id.movie_thumbnail);
        TextView name = findViewById(R.id.movie_full_name);
        TextView infoMovieName = findViewById(R.id.info_movie_name);
        TextView infoMovieYear = findViewById(R.id.info_movie_year);
        TextView infoMovieLanguages = findViewById(R.id.info_movie_languages);
        TextView infoMovieQuality = findViewById(R.id.info_movie_quality);
        TextView infoMovieSize = findViewById(R.id.info_movie_size);

        title.setText(sp.getString("title",""));

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);

        Glide.with(this).load(sp.getString("thumbnailurl","")).apply(options).into(thumbnail);

        name.setText(sp.getString("name",""));

        infoMovieName.setText(sp.getString("title", ""));
        infoMovieYear.setText(sp.getString("year", ""));
        infoMovieLanguages.setText(sp.getString("languages", ""));
        infoMovieQuality.setText(sp.getString("quality", ""));
        infoMovieSize.setText(sp.getString("size", ""));

        for(int i = 0; i < sp.getInt("noofmagnets", 0); i++){


            LayoutInflater inflater = LayoutInflater.from(this);
            LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.button_layout, root, false);

            root.addView(layout);

            Button openmagnetButton = layout.findViewById(R.id.open_magnet_link_text);
            Button copymagnetButton = layout.findViewById(R.id.copy_magnet_link_text);
            openmagnetButton.setText(String.format("open magnet link %s", (i > 0) ? i : ""));
            openmagnetButton.setTag(i);
            copymagnetButton.setTag(i);

            openmagnetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String magnetLink = sp.getString("magnet"+ v.getTag(), "");

                    if(!magnetLink.equals("")){

                        Toast.makeText(getApplicationContext(), "Opening Magnet Link", Toast.LENGTH_SHORT).show();

                        Uri uri = Uri.parse(magnetLink);
                        startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Error Occurred, Try Again.", Toast.LENGTH_SHORT).show();
                }
            });

            copymagnetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("magnet", sp.getString("magnet"+ v.getTag(), ""));
                    assert clipboard != null;
                    clipboard.setPrimaryClip(clip);

                    Toast.makeText(getApplicationContext(), "Magnet Link Copied to Clipboard", Toast.LENGTH_SHORT).show();

                }
            });

        }

    }
}
