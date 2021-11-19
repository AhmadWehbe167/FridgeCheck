package lb.edu.aub.cmps297.fridgecheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Settings extends AppCompatActivity {

    private Button logout;
    private ImageView back;
    private ImageView profile;
    private ImageView call;
    private ImageView about;
    private ImageView wishlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_settings);

        back = findViewById(R.id.imageView12);
        profile = findViewById(R.id.imageView3);
        call = findViewById(R.id.imageView7);
        wishlist = findViewById(R.id.imageView5);
        about = findViewById(R.id.imageView9);

        logout = findViewById(R.id.register);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLogout();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBack();
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfile();
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCall();
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAbout();
            }
        });
        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWishList();
            }
        });
    }
    public void openLogout(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void openBack(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void openProfile(){
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }
    public void openCall(){
        Intent intent = new Intent(this, PopCall.class);
        startActivity(intent);
    }
    public void openAbout(){
        Intent intent = new Intent(this, About.class);
        startActivity(intent);
    }
    public void openWishList(){
        Intent intent = new Intent(this, wishList.class);
        startActivity(intent);
    }
}