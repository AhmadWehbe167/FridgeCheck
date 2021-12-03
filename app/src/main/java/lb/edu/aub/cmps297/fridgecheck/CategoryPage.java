package lb.edu.aub.cmps297.fridgecheck;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CategoryPage extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Item> arrayList;
    ItemAdapter itemAdapter;
    ProgressDialog progressDialog;
    String category;
    int numberOfColumns = 2;
    FirebaseFirestore db;
    ImageButton back;
    private ArrayList<String> wishlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_category_page);
        back=findViewById(R.id.categoryPageback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryPage.this, MainActivity.class);
                startActivity(intent);
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            category = extras.getString("category");
            //The key argument here must match that used in the other activity
        }
        TextView textView = findViewById(R.id.categoryTitle);
        textView.setText(category);

        progressDialog = new ProgressDialog(CategoryPage.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data ...");
        progressDialog.show();

        recyclerView = findViewById(R.id.categoryRecycler);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        db = FirebaseFirestore.getInstance();
        arrayList = new ArrayList<Item>();

        itemAdapter = new ItemAdapter(CategoryPage.this ,arrayList);

        recyclerView.setAdapter(itemAdapter);

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
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()){
                                User userData = document.toObject(User.class);
                                ArrayList<String> wishlist = userData.getWishlist();
                                for(DocumentChange dc : value.getDocumentChanges()){
                                    if(dc.getDocument().get("category").equals(category)){
                                        Item snap = dc.getDocument().toObject(Item.class);
                                        String uid = dc.getDocument().getId();
                                        snap.setUid(uid);
                                        if(wishlist.contains(uid))
                                            snap.setFav(true);
                                        arrayList.add(snap);
                                    }
                                    itemAdapter.notifyDataSetChanged();
                                    if(progressDialog.isShowing())
                                        progressDialog.dismiss();
                                }

                            }else{
                                Log.e("","No such document");
                            }
                        }else{
                            Log.e("","get failed with ", task.getException());
                        }
                    }
                });
            }
        });
    }
}