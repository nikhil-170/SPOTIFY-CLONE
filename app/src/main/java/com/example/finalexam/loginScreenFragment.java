package com.example.finalexam;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginScreenFragment extends Fragment {

    private FirebaseAuth mAuth;
    Button loginButton;
    EditText emailEditText, passwordEditText;
    TextView createNewAccountTV;
    public loginScreenFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login_screen, container, false);
        loginButton = view.findViewById(R.id.loginButton);
        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        createNewAccountTV = view.findViewById(R.id.createAccountTVId);

        getActivity().setTitle("Login");
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = String.valueOf(emailEditText.getText());
                String password = String.valueOf(passwordEditText.getText());

                if(email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@[a-zA-Z0-9]+\\.[a-zA-Z]+$")){
                    AlertUtils.showOKDialog(getContext(), getResources().getString(R.string.error), getResources().getString(R.string.email_hint));
                }else if(password.isEmpty()){
                    AlertUtils.showOKDialog(getContext(), getResources().getString(R.string.error), getResources().getString(R.string.password_hint));
                }else {

                    mAuth = FirebaseAuth.getInstance();
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getContext(), "Login Successfull", Toast.LENGTH_SHORT).show();
                                mlistener.callMainactivity();
                            }else {
                                AlertUtils.showOKDialog(getContext(),getResources().getString(R.string.error), task.getException().getMessage());
                            }
                        }
                    });
                }
            }
        });

        createNewAccountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.callnewAccountFragment();
            }
        });
        return view;
    }

    loginInterface mlistener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mlistener = (loginScreenFragment.loginInterface) context;
    }


    public interface loginInterface{
        void callnewAccountFragment();
        void callMainactivity();
    }
}