package lb.edu.aub.cmps297.fridgecheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class itemsPage extends AppCompatActivity {

    private ImageView back;
    private ImageView wish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_items_page);
        back = findViewById(R.id.imageButton4);
        wish = findViewById(R.id.imageButton5);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBack();
            }
        });
        wish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWish();
            }
        });
    }
    public void openBack(){
        Intent intent = new Intent(this, itemsPageAdm.class);
        startActivity(intent);
    }
    public void openWish(){
        Intent intent = new Intent(this, WishlistFragment.class);
        startActivity(intent);
    }
}