package lb.edu.aub.cmps297.fridgecheck;

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

import com.google.firebase.firestore.DocumentChange;
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
                for(DocumentChange dc : value.getDocumentChanges()){
                    if(dc.getDocument().get("category").equals(category)){
                        arrayList.add(dc.getDocument().toObject(Item.class));
                    }
                    itemAdapter.notifyDataSetChanged();
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }
        });
    }
}