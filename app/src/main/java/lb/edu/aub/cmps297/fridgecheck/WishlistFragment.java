package lb.edu.aub.cmps297.fridgecheck;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WishlistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WishlistFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView RecyclerView;
    ArrayList<Item> ItemArrayList;
    WishlistAdapter wishlistAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    private ImageButton back;

    public WishlistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WishlistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WishlistFragment newInstance(String param1, String param2) {
        WishlistFragment fragment = new WishlistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }

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
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView = getView().findViewById(R.id.wishlistRecycler);
        RecyclerView.setHasFixedSize(true);
        db = FirebaseFirestore.getInstance();
        ItemArrayList = new ArrayList<Item>();
        wishlistAdapter = new WishlistAdapter(getActivity(), ItemArrayList);
        RecyclerView.setAdapter(wishlistAdapter);
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
                                    } else {
                                        String uid = dc.getDocument().getId();
                                        if(dc.getType() == DocumentChange.Type.ADDED && wishlist.contains(uid)){
                                            Item snap = dc.getDocument().toObject(Item.class);
                                            snap.setUid(uid);
                                            if(wishlist.contains(uid))
                                                snap.setFav(true);
                                            ItemArrayList.add(snap);
                                        }else if(dc.getType() == DocumentChange.Type.REMOVED){
                                            Item snap = dc.getDocument().toObject(Item.class);
                                            snap.setUid(uid);
                                            if(wishlist.contains(uid))
                                                snap.setFav(true);
                                            ItemArrayList.remove(snap);
                                        }
                                        wishlistAdapter.notifyDataSetChanged();
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