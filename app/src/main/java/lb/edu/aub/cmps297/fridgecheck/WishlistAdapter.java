package lb.edu.aub.cmps297.fridgecheck;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.MyViewHolder> {

    Context context;
    ArrayList<Item> itemArrayList;
    private StorageReference mStorageRef;

    public WishlistAdapter(FragmentActivity context, ArrayList<Item> itemArrayList) {
        this.context = context;
        this.itemArrayList = itemArrayList;
    }

    @NonNull
    @Override
    public WishlistAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.wishlistcard, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistAdapter.MyViewHolder holder, int position) {
        Item item = itemArrayList.get(position);
        mStorageRef = FirebaseStorage.getInstance().getReference().child(item.category +"/"+ item.Image);
        try {
            final File localFile =  File.createTempFile(item.itemName, "png");
            mStorageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    holder.itemImage.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("Error is: " + e.getMessage());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        holder.itemTitle.setText(item.itemName);
        holder.itemStockNumber.setText(item.stock);
        holder.itemPrice.setText(Integer.toString(item.price) + " L.L.");
        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, itemsPage.class);
                intent.putExtra("Image", item.Image);
                intent.putExtra("Type", item.Type);
                intent.putExtra("category", item.category);
                intent.putExtra("description", item.description);
                intent.putExtra("itemName", item.itemName);
                intent.putExtra("price", Integer.toString(item.price));
                intent.putExtra("stock", item.stock);
                intent.putExtra("uid", item.uid);
                context.startActivity(intent);
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DocumentReference docRef = FirebaseFirestore.getInstance().collection("Users").document(user.getUid());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            User userData = document.toObject(User.class);
                            ArrayList<String> wishlist = userData.getWishlist();
                            wishlist.remove(item.uid);
                            docRef.update("Wishlist", wishlist).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    itemArrayList.remove(position);
                                    WishlistAdapter.this.notifyDataSetChanged();
                                }
                            });
                        }else{
                            Log.e("Tagged Error", "An error occured");
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView itemImage;
        TextView itemTitle, itemStockNumber, itemPrice;
        ImageButton deleteButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemTitle = itemView.findViewById(R.id.itemTitle);
            itemStockNumber = itemView.findViewById(R.id.itemStockNumber);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
