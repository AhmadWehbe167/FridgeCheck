package lb.edu.aub.cmps297.fridgecheck;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    RecyclerView newestRecyclerView;
    RecyclerView bestRecyclerView;
    RecyclerView importRecyclerView;

    ArrayList<Item> newItemArrayList;
    ArrayList<Item> bestItemArrayList;
    ArrayList<Item> importItemArrayList;

    ItemAdapter newItemAdapter;
    ItemAdapter bestItemAdapter;
    ItemAdapter importitemAdapter;

    FirebaseFirestore db;
    ProgressDialog progressDialog;
    Button inputfield;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    private FloatingActionButton add;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data ...");
        progressDialog.show();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        inputfield = (Button) view.findViewById(R.id.searchBar);
        LinearLayout breakfastCategoryItem = (LinearLayout) view.findViewById(R.id.breakfastCard);
        LinearLayout snacksCategoryItem = (LinearLayout) view.findViewById(R.id.SnacksCard);
        LinearLayout beveragesfastCategoryItem = (LinearLayout) view.findViewById(R.id.BeveragesCard);
        LinearLayout dairyCategoryItem = (LinearLayout) view.findViewById(R.id.DairyCard);
        LinearLayout fishCategoryItem = (LinearLayout) view.findViewById(R.id.FishCard);
        LinearLayout cakeCategoryItem = (LinearLayout) view.findViewById(R.id.CakeCard);
        LinearLayout biscuitsCategoryItem = (LinearLayout) view.findViewById(R.id.BiscuitsCard);
        add = (FloatingActionButton) view.findViewById(R.id.addButton);

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
                            add.hide();
                        }
                    }
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), addItem.class);
                startActivity(intent);
            }
        });
        breakfastCategoryItem.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getActivity(), CategoryPage.class);
                intent.putExtra("category","Breakfast");
                startActivity(intent);
                ((Activity) getActivity()).overridePendingTransition(0, 0);
            }
        }
        );
        snacksCategoryItem.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Intent intent = new Intent(getActivity(), CategoryPage.class);
                        intent.putExtra("category","Snacks");
                        startActivity(intent);
                        ((Activity) getActivity()).overridePendingTransition(0, 0);
                    }
                }
        );
        beveragesfastCategoryItem.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Intent intent = new Intent(getActivity(), CategoryPage.class);
                        intent.putExtra("category","Beverages");
                        startActivity(intent);
                        ((Activity) getActivity()).overridePendingTransition(0, 0);
                    }
                }
        );
        dairyCategoryItem.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Intent intent = new Intent(getActivity(), CategoryPage.class);
                        intent.putExtra("category","Dairy");
                        startActivity(intent);
                        ((Activity) getActivity()).overridePendingTransition(0, 0);
                    }
                }
        );
        fishCategoryItem.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Intent intent = new Intent(getActivity(), CategoryPage.class);
                        intent.putExtra("category","Fish");
                        startActivity(intent);
                        ((Activity) getActivity()).overridePendingTransition(0, 0);
                    }
                }
        );
        cakeCategoryItem.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Intent intent = new Intent(getActivity(), CategoryPage.class);
                        intent.putExtra("category","Cake");
                        startActivity(intent);
                        ((Activity) getActivity()).overridePendingTransition(0, 0);
                    }
                }
        );
        biscuitsCategoryItem.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Intent intent = new Intent(getActivity(), CategoryPage.class);
                        intent.putExtra("category","Biscuits");
                        startActivity(intent);
                        ((Activity) getActivity()).overridePendingTransition(0, 0);
                    }
                }
        );
        inputfield.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchPage.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newestRecyclerView = getView().findViewById(R.id.recyclerview);
        bestRecyclerView = getView().findViewById(R.id.bestrecyclerview);
        importRecyclerView = getView().findViewById(R.id.importrecyclerview);

        newestRecyclerView.setHasFixedSize(true);
        bestRecyclerView.setHasFixedSize(true);
        importRecyclerView.setHasFixedSize(true);

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager horizontalLayoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager horizontalLayoutManager3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        newestRecyclerView.setLayoutManager(horizontalLayoutManager);
        bestRecyclerView.setLayoutManager(horizontalLayoutManager2);
        importRecyclerView.setLayoutManager(horizontalLayoutManager3);

        db = FirebaseFirestore.getInstance();
        newItemArrayList = new ArrayList<Item>();
        bestItemArrayList = new ArrayList<Item>();
        importItemArrayList = new ArrayList<Item>();

        newItemAdapter = new ItemAdapter(getActivity(),newItemArrayList);
        bestItemAdapter = new ItemAdapter(getActivity(),bestItemArrayList);
        importitemAdapter = new ItemAdapter(getActivity(),importItemArrayList);

        newestRecyclerView.setAdapter(newItemAdapter);
        bestRecyclerView.setAdapter(bestItemAdapter);
        importRecyclerView.setAdapter(importitemAdapter);

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
                                        if(dc.getType() == DocumentChange.Type.ADDED  && dc.getDocument().get("Type").equals("Newest") && newItemArrayList.size() < 5){
                                            Item snap = dc.getDocument().toObject(Item.class);
                                            String uid = dc.getDocument().getId();
                                            snap.setUid(uid);
                                            if(wishlist.contains(uid))
                                                snap.setFav(true);
                                            newItemArrayList.add(snap);
                                        }else if(dc.getType() == DocumentChange.Type.ADDED && dc.getDocument().get("Type").equals("BestSelling") && bestItemArrayList.size() < 5){
                                            Item snap = dc.getDocument().toObject(Item.class);
                                            String uid = dc.getDocument().getId();
                                            snap.setUid(uid);
                                            if(wishlist.contains(uid))
                                                snap.setFav(true);
                                            bestItemArrayList.add(snap);
                                        }else if(dc.getType() == DocumentChange.Type.ADDED && dc.getDocument().get("Type").equals("Imported") && importItemArrayList.size() < 5){
                                            Item snap = dc.getDocument().toObject(Item.class);
                                            String uid = dc.getDocument().getId();
                                            snap.setUid(uid);
                                            if(wishlist.contains(uid))
                                                snap.setFav(true);
                                            importItemArrayList.add(snap);
                                        }
                                        newItemAdapter.notifyDataSetChanged();
                                        bestItemAdapter.notifyDataSetChanged();
                                        importitemAdapter.notifyDataSetChanged();
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
}