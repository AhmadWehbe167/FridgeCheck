package lb.edu.aub.cmps297.fridgecheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class CategoryPage extends AppCompatActivity {
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_category_page);
        back = findViewById(R.id.backcategory);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHome();
            }
        });
    }

    private void openHome() {
        Intent intent = new Intent(this, HomeFragment.class);
        startActivity(intent);
    }
}