package lb.edu.aub.cmps297.fridgecheck;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class itemsPage extends AppCompatActivity {

    private RecyclerView RecyclerView;
    private ArrayList<Item> ItemArrayList;
    private ItemAdapter ItemAdapter;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;

    private ImageView back;
    private ImageView wish;
    private ImageView edit;

    private String itemName;
    private String Image;
    private String price;
    private String stock;
    private String description;
    private String category;

    private ImageView itemImage;
    private TextView itemTitle;
    private TextView itemQuantity;
    private TextView itemDesc;
    private TextView itemPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_items_page);
        itemImage = findViewById(R.id.itemImage);
        itemTitle = findViewById(R.id.itemName);
        itemQuantity = findViewById(R.id.itemQuantity);
        itemDesc = findViewById(R.id.itemDesc);
        itemPrice = findViewById(R.id.itemPrice);

        edit = findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEdit();
            }
        });
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
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            itemName = extras.getString("itemName");
            Image = extras.getString("Image");
            stock = extras.getString("stock");
            price = extras.getString("price") + " L.L.";
            description = extras.getString("description");
            category = extras.getString("category");
            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child(category +"/"+ Image);
            final File localFile;
            try {
                localFile = File.createTempFile(itemName, "png");
                mStorageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        itemImage.setImageBitmap(bitmap);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            itemTitle.setText(itemName);
            itemQuantity.setText(stock);
            itemDesc.setText(description);
            itemPrice.setText(price);
        }

        progressDialog = new ProgressDialog(itemsPage.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data ...");
        progressDialog.show();
        RecyclerView = findViewById(R.id.relatedRecycler);
        RecyclerView.setHasFixedSize(true);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.setLayoutManager(horizontalLayoutManager);
        db = FirebaseFirestore.getInstance();
        ItemArrayList = new ArrayList<Item>();
        ItemAdapter = new ItemAdapter(itemsPage.this,ItemArrayList);
        RecyclerView.setAdapter(ItemAdapter);
        EventChangeListener();
    }

    private void EventChangeListener() {
        db.collection("Items").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                    Log.e("Firestore error", error.getMessage());
                    return;
                }
                for(DocumentChange dc : value.getDocumentChanges()){
                    if(dc.getDocument().get("Type") == null){
                        System.out.println("itemName is: " + dc.getDocument().get("itemName"));
                    }else{
                        if(dc.getType() == DocumentChange.Type.ADDED  && dc.getDocument().get("category").equals(category) && !dc.getDocument().get("itemName").equals(itemName) && ItemArrayList.size() < 5){
                            ItemArrayList.add(dc.getDocument().toObject(Item.class));
                        }
                        ItemAdapter.notifyDataSetChanged();
                    }
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                }
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
    private void openEdit() {
        Intent intent = new Intent(this, editItems.class);
        startActivity(intent);
    }
}