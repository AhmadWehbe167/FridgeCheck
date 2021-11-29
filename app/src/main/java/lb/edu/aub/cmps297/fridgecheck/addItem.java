package lb.edu.aub.cmps297.fridgecheck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class addItem extends AppCompatActivity {

    private ImageButton yes;
    private ImageButton no;
    private TextInputEditText Name;
    private TextInputEditText Price;
    private TextInputEditText Quantity;
    private TextInputEditText Desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_item);
        yes = findViewById(R.id.submit);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createItem();
            }
        });
//        no = findViewById(R.id.imageButton8);
//        no.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openNo();
//            }
//        });
    }
    public void createItem(){
        String name = Name.getText().toString();
        String price = Price.getText().toString();
        String quantity = Quantity.getText().toString();
        String desc = Desc.getText().toString();

        Map<String, Object> item = new HashMap<>();
        item.put("itemName", name);
        item.put("price", price);
        item.put("stock", quantity);
        item.put("description", desc);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        System.out.println(name);
        db.collection("Items").document().set(item)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Inserted Succefully", Toast.LENGTH_LONG).show();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Could not insert", Toast.LENGTH_LONG).show();
            }
        });
    }
//    public void openNo(){
//        Intent intent = new Intent(this, WishlistFragment.class);
//        startActivity(intent);
//    }
}