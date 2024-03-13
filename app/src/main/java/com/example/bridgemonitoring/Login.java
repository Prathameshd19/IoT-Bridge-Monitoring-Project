package com.example.bridgemonitoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bridgemonitoring.Pojo.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    EditText etcontact,etpassword;
    Button btn_login;
    TextView tv_register;
    String Pattern = "[0-9]{10}";
    DatabaseReference databaseReference;
    int flg = 0;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tv_register=findViewById(R.id.tv_reg);
        etcontact=findViewById(R.id.edit_Contact);
        etpassword=findViewById(R.id.edit_pass);
        btn_login=findViewById(R.id.btn_login);

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etcontact.getText().toString().isEmpty()) {
                    etcontact.setError("Enter Contact Number");
                    return;
                }
                if (etpassword.getText().toString().isEmpty()) {
                    etpassword.setError("Enter Password");
                    return;
                }

                databaseReference= FirebaseDatabase.getInstance().getReference(AppConstant.BaseURL+ "/BridgeM_User");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            flg = 0;
                            UserInfo userInfo = data.getValue(UserInfo.class);

                            assert userInfo != null;
                            String contact = userInfo.getContact();
                            String password = userInfo.getPassword();

                            if (contact.equals(etcontact.getText().toString()) && etcontact.getText().toString().trim().matches(Pattern)) {
                                if (password.equals(etpassword.getText().toString())) {

                                    Intent intent = new Intent(Login.this, Dashboard.class);
                                    startActivity(intent);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}