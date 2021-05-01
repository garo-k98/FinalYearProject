package mobi.graff.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.regex.Pattern;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private TextView Brand2, registerUser, alreadyReg;
    private EditText editTextFullName, editTextAge, editTextEmail, editTextPassword, editTextPhone, editTextAddress;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        Brand2 = (TextView) findViewById(R.id.tvBrand2);
        Brand2.setOnClickListener(this);

        alreadyReg = (TextView) findViewById(R.id.alreadyReg);
        alreadyReg.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.btnRegisterUser);
        registerUser.setOnClickListener(this);

        editTextFullName = (EditText) findViewById(R.id.etFullName);
        editTextAge = (EditText) findViewById(R.id.etAge);
        editTextEmail = (EditText) findViewById(R.id.etEmail2);
        editTextPassword = (EditText) findViewById(R.id.etPassword2);
        editTextPhone = (EditText) findViewById(R.id.etPhone2);
        editTextAddress = (EditText) findViewById(R.id.etAddress2);

        progressBar = (ProgressBar) findViewById(R.id.progressBar2);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBrand2:
            case R.id.alreadyReg:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.btnRegisterUser:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String age = editTextAge.getText().toString().trim();
        String fullName = editTextFullName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();

        if(fullName.isEmpty()) {
            editTextFullName.setError("Full name is required!");
            editTextFullName.requestFocus();
            return;
        }
        if(age.isEmpty()) {
            editTextAge.setError("Age is required!");
            editTextAge.requestFocus();
            return;
        }
        if(phone.isEmpty()) {
            editTextPhone.setError("Phone Number is required!");
            editTextPhone.requestFocus();
            return;
        }
        if(address.isEmpty()) {
            editTextAddress.setError("Address is required!");
            editTextAddress.requestFocus();
            return;
        }
        if(email.isEmpty()) {
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please provide valid Email!");
            editTextEmail.requestFocus();
            return;
        }
        if(password.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }
        if(password.length() < 6) {
            editTextPassword.setError("Password should be 6 characters at least!");
            editTextPassword.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {
                    User user = new User(fullName, age, email, phone, address);

                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(RegisterUser.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }else {
                                Toast.makeText(RegisterUser.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }else {
                    Toast.makeText(RegisterUser.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }
}