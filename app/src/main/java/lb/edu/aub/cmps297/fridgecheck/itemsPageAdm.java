package lb.edu.aub.cmps297.fridgecheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class itemsPageAdm extends AppCompatActivity {

    private ImageButton back;
    private ImageButton edit;
    private ImageButton delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_items_page_adm);
        back = findViewById(R.id.imageButton4);
        edit = findViewById(R.id.imageButton);
        delete = findViewById(R.id.imageButton2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEdit();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast();
            }
        });
    }
    public void openBack(){
        Intent intent = new Intent(this, itemsPageAdm.class);
        startActivity(intent);
    }
    public void openEdit(){
        Intent intent = new Intent(this, editItems.class);
        startActivity(intent);
    }
    public void showToast(){
        Context context = getApplicationContext();
        CharSequence text = "Item Deleted!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}