package lb.edu.aub.cmps297.fridgecheck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class addItem extends AppCompatActivity {

    private ImageButton yes;
    private ImageButton no;
    private TextInputEditText Name;
    private TextInputEditText Price;
    private Spinner Quantity;
    private Spinner Type;
    private Spinner Category;
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
        no = findViewById(R.id.noButton);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(addItem.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //Create the dropdown list Quantity
        Quantity = (Spinner) findViewById(R.id.Quantity);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Quantity, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Create the dropdown list type
        Type = (Spinner) findViewById(R.id.typeSpinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.type, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Create the dropdown list type
        Category = (Spinner) findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.category, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Quantity.setAdapter(adapter);
        Type.setAdapter(adapter2);
        Category.setAdapter(adapter3);

        Name = findViewById(R.id.Name);
        Price = findViewById(R.id.Price);
        Price.setText("0");
        Desc = findViewById(R.id.Description);
    }
    public void createItem(){
        String name = Name.getText().toString();
        int price = Integer.parseInt(Price.getText().toString());
        String quantity = Quantity.getSelectedItem().toString();
        String type = Type.getSelectedItem().toString();
        String category = Category.getSelectedItem().toString();
        String desc = Desc.getText().toString();

        if(name != null && Quantity != null && desc != null && price != 0){
            Map<String, Object> item = new HashMap<>();
            item.put("itemName", name);
            item.put("price", price);
            item.put("stock", quantity);
            item.put("description", desc);
            item.put("Type", type);
            item.put("category", category);

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
        }else{
            Toast.makeText(this, "You should fill all fields", Toast.LENGTH_LONG).show();
        }
    }
}