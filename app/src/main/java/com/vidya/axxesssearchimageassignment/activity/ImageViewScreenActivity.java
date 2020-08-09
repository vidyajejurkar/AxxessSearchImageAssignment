package com.vidya.axxesssearchimageassignment.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vidya.axxesssearchimageassignment.Database.DatabaseHelper;
import com.vidya.axxesssearchimageassignment.R;

import java.util.ArrayList;

public class ImageViewScreenActivity extends AppCompatActivity {
     ImageView imageView2,avatarImageView;
     TextView usernameTextView;
     EditText etComment;
     Button btnSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view_screen);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        usernameTextView = (TextView) findViewById(R.id.usernameTextView);
        etComment = (EditText) findViewById(R.id.etComment);
        btnSubmit =(Button)findViewById(R.id.btnSubmit);
        final DatabaseHelper helper = new DatabaseHelper(this);
        final ArrayList array_list = helper.getAllComment();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Intent i = getIntent();
            String image = i.getStringExtra("img_url");
            Picasso.with(getApplicationContext()).load(image).into(imageView2);

            String name = bundle.getString("name");
            usernameTextView.setText(name);
        }

        avatarImageView = (ImageView) findViewById(R.id.avatarImageView);
        avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ImageViewScreenActivity.this,SearchImageActivity.class);
                startActivity(intent);
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etComment.getText().toString().isEmpty()) {
                    if (helper.insert(etComment.getText().toString())) {
                        Toast.makeText(ImageViewScreenActivity.this, "Inserted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ImageViewScreenActivity.this, "NOT Inserted", Toast.LENGTH_LONG).show();
                    }
                } else {
                    etComment.setError("Enter Comment");
                }

                etComment.getText().clear();
            }
        });
    }
}