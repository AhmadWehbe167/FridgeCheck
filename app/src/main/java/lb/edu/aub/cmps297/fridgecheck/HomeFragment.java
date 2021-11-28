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
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        LinearLayout breakfastCategoryItem = (LinearLayout) view.findViewById(R.id.breakfastCard);
        LinearLayout snacksCategoryItem = (LinearLayout) view.findViewById(R.id.SnacksCard);
        LinearLayout beveragesfastCategoryItem = (LinearLayout) view.findViewById(R.id.BeveragesCard);
        LinearLayout dairyCategoryItem = (LinearLayout) view.findViewById(R.id.DairyCard);
        LinearLayout fishCategoryItem = (LinearLayout) view.findViewById(R.id.FishCard);
        LinearLayout cakeCategoryItem = (LinearLayout) view.findViewById(R.id.CakeCard);
        LinearLayout biscuitsCategoryItem = (LinearLayout) view.findViewById(R.id.BiscuitsCard);

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
                for(DocumentChange dc : value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED  && dc.getDocument().get("Type").equals("Newest") && newItemArrayList.size() < 5){
                        newItemArrayList.add(dc.getDocument().toObject(Item.class));
                    }else if(dc.getType() == DocumentChange.Type.ADDED && dc.getDocument().get("Type").equals("BestSelling") && bestItemArrayList.size() < 5){
                        bestItemArrayList.add(dc.getDocument().toObject(Item.class));
                    }else if(dc.getType() == DocumentChange.Type.ADDED && dc.getDocument().get("Type").equals("Imported") && importItemArrayList.size() < 5){
                        importItemArrayList.add(dc.getDocument().toObject(Item.class));
                    }
                    newItemAdapter.notifyDataSetChanged();
                    bestItemAdapter.notifyDataSetChanged();
                    importitemAdapter.notifyDataSetChanged();
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }
        });
    }
}