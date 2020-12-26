package com.example.finalexam;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class newAccountFragment extends Fragment {

    EditText emailEditText,passwordEditText, nameEditText;
    TextView cancelTextView;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public newAccountFragment() {
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
        View view = inflater.inflate(R.layout.fragment_new_account, container, false);
        mAuth = FirebaseAuth.getInstance();
        getActivity().setTitle(R.string.CreateNewAccount);
        emailEditText =view.findViewById(R.id.emailEditTextRegister);
        passwordEditText = view.findViewById(R.id.passwordEditTextRegister);
        nameEditText = view.findViewById(R.id.nameEditTextRegister);
        cancelTextView = view.findViewById(R.id.cancelTextView);

        view.findViewById(R.id.submitButton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = nameEditText.getText().toString();
                final String email = emailEditText.getText().toString();
                final String password = passwordEditText.getText().toString();

                if (email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@[a-zA-Z0-9]+\\.[a-zA-Z]+$")) {
                    AlertUtils.showOKDialog(getContext(), getResources().getString(R.string.error), getResources().getString(R.string.email_hint));
                } else if (password.isEmpty() && (password.length() < 8)) {
                    AlertUtils.showOKDialog(getContext(), getResources().getString(R.string.error), getResources().getString(R.string.password_hint));
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                mListener.callMainActivity();
                                createUserName(name);
                            }else {
                                AlertUtils.showOKDialog(getContext(), getResources().getString(R.string.error),task.getException().getMessage());
                            }
                        }
                    });
                }
            }
        });

        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return view;
    }

    newAccountInterface mListener;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (newAccountInterface) context;
    }

    public interface newAccountInterface{
        void callMainActivity();
    }


    public void createUserName(String name){
        HashMap<String, String> map = new HashMap<>();
        map.put("username", name);
        db.collection("users").document(mAuth.getCurrentUser().getUid()).set(map);

    }
}