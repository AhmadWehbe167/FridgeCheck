package lb.edu.aub.cmps297.fridgecheck;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class editItems extends AppCompatActivity {

    private ImageButton yes;
    private ImageButton no;

    private String itemName;
    private String Image;
    private String price;
    private String stock;
    private String description;
    private String category;
    private String type;
    private String uid;
    private Bitmap imageBitmap;

    private FirebaseFirestore db;

    private TextInputEditText Name;
    private TextInputEditText Price;
    private Spinner Quantity;
    private Spinner Type;
    private Spinner Category;
    private TextInputEditText Desc;
    private ImageView image;
    private CardView imageCard;
    private Uri imageURI;
    private FirebaseStorage storage;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_edit_items);

        db = FirebaseFirestore.getInstance();

        image = findViewById(R.id.itemImage);
        Name = findViewById(R.id.nameofitem);
        Quantity = findViewById(R.id.Quantity);
        Desc = findViewById(R.id.Description);
        Price = findViewById(R.id.Price);
        Type = findViewById(R.id.typeSpinner);
        Category = findViewById(R.id.categorySpinner);

        yes = findViewById(R.id.submit);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateItem();
            }
        });
        no = findViewById(R.id.noButton);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNo();
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

        Name = findViewById(R.id.nameofitem);
        Price = findViewById(R.id.Price);
        Price.setText("0");
        Desc = findViewById(R.id.Description);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            itemName = extras.getString("itemName");
            Image = extras.getString("Image");
            stock = extras.getString("stock");
            price = extras.getString("price");
            description = extras.getString("description");
            category = extras.getString("category");
            uid = extras.getString("uid");
            type = extras.getString("Type");
            imageBitmap = (Bitmap) extras.get("imageBitmap");

            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child(category +"/"+ Image);
            final File localFile;
            try {
                localFile = File.createTempFile(itemName, "png");
                mStorageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap imageBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        image.setImageBitmap(imageBitmap);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            Name.setText(itemName);
            Quantity.setSelection(( (ArrayAdapter) Quantity.getAdapter()).getPosition(stock));
            Desc.setText(description);
            Price.setText(price);
            Type.setSelection(( (ArrayAdapter) Type.getAdapter()).getPosition(type));
            Category.setSelection(( (ArrayAdapter) Category.getAdapter()).getPosition(category));
        }

    }

    public void updateItem() {
        String newname = Name.getText().toString();
        int newprice = Integer.parseInt(Price.getText().toString().split("L")[0]);
        String newquantity = Quantity.getSelectedItem().toString();
        String newtype = Type.getSelectedItem().toString();
        String newcategory = Category.getSelectedItem().toString();
        String newdesc = Desc.getText().toString();

        if(newname != null && newquantity != null && newdesc != null && newprice != 0){
            if(image != null){
                Map<String, Object> item = new HashMap<>();
                item.put("itemName", newname);
                item.put("price", newprice);
                item.put("stock", newquantity);
                item.put("description", newdesc);
                item.put("Type", newtype);
                item.put("category", newcategory);

                DocumentReference itemRef = db.collection("Items").document(uid);
                itemRef.update(item);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "An Error Occured", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "You should fill all fields", Toast.LENGTH_LONG).show();
        }
    }

    public void openNo() {
        Intent intent = new Intent(this, HomeFragment.class);
        startActivity(intent);
    }

}