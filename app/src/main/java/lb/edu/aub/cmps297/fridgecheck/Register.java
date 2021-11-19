package lb.edu.aub.cmps297.fridgecheck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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
            startActivity(new Intent(Register .this, Login.class));
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
                        Toast.makeText(Register.this, "User registered Successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Register.this, Login.class));
                    } else {
                        Toast.makeText(Register.this, "Registration Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}