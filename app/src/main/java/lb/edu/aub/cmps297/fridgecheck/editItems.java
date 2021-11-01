package lb.edu.aub.cmps297.fridgecheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class editItems extends AppCompatActivity {

    private ImageButton yes;
    private ImageButton no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_edit_items);
        yes = findViewById(R.id.imageButton9);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openYes();
            }
        });
        no = findViewById(R.id.imageButton8);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNo();
            }
        });
    }
    public void openYes(){
        Intent intent = new Intent(this, itemsPageAdm.class);
        startActivity(intent);
    }
    public void openNo(){
        Intent intent = new Intent(this, itemsPageAdm.class);
        startActivity(intent);
    }
}