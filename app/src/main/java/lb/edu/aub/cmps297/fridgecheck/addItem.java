package lb.edu.aub.cmps297.fridgecheck;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class addItem extends AppCompatActivity {

    private ImageButton yes;
    private ImageButton no;
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
        setContentView(R.layout.activity_add_item);
        storageReference = FirebaseStorage.getInstance().getReference();
        image = findViewById(R.id.uploadImage);
        imageCard = findViewById(R.id.uploadImageCard);
        imageCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });
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

        Name = findViewById(R.id.nameitem);
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
            String image = uploadPicture(category, name);
            if(image != null){
                Map<String, Object> item = new HashMap<>();
                item.put("itemName", name);
                item.put("price", price);
                item.put("stock", quantity);
                item.put("description", desc);
                item.put("Type", type);
                item.put("category", category);
                item.put("Image", image);

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                System.out.println(name);
                db.collection("Items").document().set(item)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(),"Inserted Succefully", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(addItem.this, MainActivity.class);
                                startActivity(intent);
                                try {
                                    TimeUnit.SECONDS.sleep(1);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                System.out.println("I am triggered");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Could not insert", Toast.LENGTH_LONG).show();
                            }
                        });
            }else{
                Toast.makeText(this, "An Error Occured", Toast.LENGTH_LONG).show();
            }

        }else{
            Toast.makeText(this, "You should fill all fields", Toast.LENGTH_LONG).show();
        }
    }

    public void choosePicture(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageURI = data.getData();
            image.setImageURI(imageURI);
        }
    }

    public String uploadPicture(String category, String itemName){
        final ProgressDialog pd = new ProgressDialog(this);
        final String randomKey = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child(category + "/" + itemName +  randomKey);
        final String[] res = new String[1];
        riversRef.putFile(imageURI)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Snackbar.make(findViewById(android.R.id.content), "Image Uploaded", Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed to upload", Toast.LENGTH_LONG).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progressPercent = (100.00 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                pd.setMessage("Progress: " + (int) progressPercent + "%");
            }
        });
        return itemName +  randomKey;
    }
}