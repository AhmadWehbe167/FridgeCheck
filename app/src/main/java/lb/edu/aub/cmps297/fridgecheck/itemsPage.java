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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    private String type;
    private String uid;

    private ImageView itemImage;
    private TextView itemTitle;
    private TextView itemQuantity;
    private TextView itemDesc;
    private TextView itemPrice;
    private ArrayList<String> wishlist;
    private ImageView delete;
//    private Bitmap imageBitmap;

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
        back = findViewById(R.id.imageButton4);
        wish = findViewById(R.id.imageButton5);
        edit = findViewById(R.id.edit);
        delete = findViewById(R.id.deletebutton);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("Users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        User userData = document.toObject(User.class);
                        if (!userData.getIsAdmin()){
                            edit.setVisibility(View.INVISIBLE);
                            delete.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEdit();
            }
        });
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
            price = extras.getString("price");
            description = extras.getString("description");
            category = extras.getString("category");
            type = extras.getString("Type");
            uid = extras.getString("uid");

            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child(category +"/"+ Image);
            final File localFile;
            try {
                localFile = File.createTempFile(itemName, "png");
                mStorageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap imageBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        itemImage.setImageBitmap(imageBitmap);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            itemTitle.setText(itemName);
            itemQuantity.setText(stock);
            itemDesc.setText(description);
            String priceLB = price + " L.L.";
            itemPrice.setText(priceLB);
        }
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance().collection("Items").document(uid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(itemsPage.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(itemsPage.this, "Item Deleted successfully!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


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
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DocumentReference docRef = db.collection("Users").document(user.getUid());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                User userData = document.toObject(User.class);
                                ArrayList<String> wishlist = userData.getWishlist();
                                for(DocumentChange dc : value.getDocumentChanges()){
                                    if(dc.getDocument().get("Type") == null){
                                        System.out.println("itemName is: " + dc.getDocument().get("itemName"));
                                    }else{
                                        if(dc.getType() == DocumentChange.Type.ADDED  && dc.getDocument().get("category").equals(category) && !dc.getDocument().get("itemName").equals(itemName) && ItemArrayList.size() < 5){
                                            Item snap = dc.getDocument().toObject(Item.class);
                                            String uid = dc.getDocument().getId();
                                            snap.setUid(uid);
                                            if(wishlist.contains(uid))
                                                snap.setFav(true);
                                            ItemArrayList.add(snap);
                                        }
                                        ItemAdapter.notifyDataSetChanged();
                                    }
                                    if(progressDialog.isShowing())
                                        progressDialog.dismiss();
                                }
                            } else {
                                Log.e("","No such document");
                            }
                        } else {
                            Log.e("","get failed with ", task.getException());
                        }
                    }
                });
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
        intent.putExtra("Image",Image);
        intent.putExtra("itemName",itemName);
        intent.putExtra("price",price);
        intent.putExtra("stock",stock);
        intent.putExtra("description",description);
        intent.putExtra("category",category);
        intent.putExtra("Type",type);
        intent.putExtra("uid",uid);
        startActivity(intent);
    }
}