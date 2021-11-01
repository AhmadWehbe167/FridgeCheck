package lb.edu.aub.cmps297.fridgecheck;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class CategoryPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_category_page);
    }
}