package lb.edu.aub.cmps297.fridgecheck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    TextView login;
    FirebaseAuth mAuth;
    TextInputEditText email, name, password, username, confirmpassword;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        email= findViewById(R.id.email);
        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirmPassword);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        register.setOnClickListener(view-> {
            createUser();
        });
        login.setOnClickListener(view -> {
            startActivity(new Intent(Register .this, MainActivity.class));
        });
    }

    private void createUser() {
        String Email = email.getText().toString();
        String Password = password.getText().toString();
        String Name = name.getText().toString();
        String ConfirmPassword = confirmpassword.getText().toString();
        String Username = username.getText().toString();
        if(TextUtils.isEmpty(Email)){
            email.setError("Email cannot be empty.");
            email.requestFocus();
        }else if(Password.isEmpty()){
            password.setError("Password cannot be empty.");
            password.requestFocus();
        }else if(Username.isEmpty()){
            username.setError("Username cannot be empty.");
            username.requestFocus();
        }
        else if(Name.isEmpty()){
            name.setError("Name cannot be empty.");
            name.requestFocus();
        }
        else if(ConfirmPassword.isEmpty()){
            confirmpassword.setError("Password Confirmation cannot be empty.");
            confirmpassword.requestFocus();
        }else{
            mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // message to user
                        startActivity(new Intent(Register.this, MainActivity.class));
                        Toast.makeText(Register.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                        // get database and user id
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                        //create user to write to db
                        ArrayList<String> empty = new ArrayList<String>();
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("Email", userEmail);
                        userMap.put("Name", Name);
                        userMap.put("Wishlist", empty);
                        userMap.put("isAdmin", false );
                        db.collection("Users")
                                .add(userMap)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        System.out.println("DocumentSnapshot added with ID: "+ documentReference.getId());
//                                        startActivity(new Intent(Register.this, HomeFragment.class));
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        System.out.println("Error adding document"+ e);
                                    }
                                });

                    } else {
                        Toast.makeText(Register.this, "Registration Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}