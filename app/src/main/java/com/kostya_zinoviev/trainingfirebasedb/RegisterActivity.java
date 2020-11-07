package com.kostya_zinoviev.trainingfirebasedb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText edEmail,edPassword;
    private Button registerBtn;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        registerBtn = findViewById(R.id.registerBtn);

        createProgressDialog();

        mAuth = FirebaseAuth.getInstance();


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email = edEmail.getText().toString().trim();
                String password = edPassword.getText().toString().trim();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    registrationUser(email, password);
                } else {
                    Toast.makeText(RegisterActivity.this, "Зполни пустые поля", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Загрузка");
        progressDialog.setMessage("Пожалуйста подождите...");
        progressDialog.setCancelable(false);
    }

    private void registrationUser(String email, String password) {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    user = mAuth.getCurrentUser();
                    progressDialog.dismiss();

                    Toast.makeText(RegisterActivity.this, "Ваш аккаунт был создан!", Toast.LENGTH_SHORT).show();

                    Intent loginIntent = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(loginIntent);

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Произошла ошибка: " + task.getException().toString(), Toast.LENGTH_SHORT).show();

                    user = null;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (user != null){
            Intent homeIntent = new Intent(RegisterActivity.this,HomeActivity.class);
            startActivity(homeIntent);
        }
    }
}
