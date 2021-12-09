package lb.edu.aub.cmps297.fridgecheck;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
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

public class SearchPage extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Item> arrayList;
    ItemAdapter itemAdapter;
    ProgressDialog progressDialog;
    FirebaseFirestore db;

    Button search;
    TextInputEditText input;
    Editable querykey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.search_page);
        search = findViewById(R.id.searchButton);
        input = findViewById(R.id.input);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList.clear();
                querykey = input.getText();
                progressDialog = new ProgressDialog(SearchPage.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Fetching Data ...");
                progressDialog.show();
                EventChangeListener();
            }
        });

        recyclerView = findViewById(R.id.searchRecycler);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        db = FirebaseFirestore.getInstance();
        arrayList = new ArrayList<Item>();

        itemAdapter = new ItemAdapter(SearchPage.this ,arrayList);

        recyclerView.setAdapter(itemAdapter);
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
                                    String itemName = (String) dc.getDocument().get("itemName");
                                    itemName = itemName.toLowerCase();
                                    String query = querykey.toString();
                                    query = query.toLowerCase();
                                    if(itemName.contains(query)){
                                        System.out.println("Item found");
                                        Item snap = dc.getDocument().toObject(Item.class);
                                        String uid = dc.getDocument().getId();
                                        snap.setUid(uid);
                                        if(wishlist.contains(uid))
                                            snap.setFav(true);
                                        arrayList.add(snap);
                                    }else{
                                        System.out.println("Item not found");
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