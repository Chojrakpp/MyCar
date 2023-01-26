package be.ehb.mycar.fragments;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import be.ehb.mycar.R;
import be.ehb.mycar.databinding.FragmentCreateAccountBinding;
import be.ehb.mycar.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    EditText editTextEmail, editTextpassword;
    Button btnLogin;
    ProgressBar progressBar;
    TextView btnToRegister;

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        editTextEmail = binding.etEmail;
        editTextpassword = binding.etPassword;
        btnLogin = binding.btnLogin;
        progressBar = binding.progressBar;
        btnToRegister = binding.btnToRegister;

        btnLogin.setOnClickListener(v-> loginUser());
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnToRegister.setOnClickListener(
                (View v) -> {
                    NavHostFragment.findNavController(LoginFragment.this)
                            .navigate(R.id.action_login_to_createAccount);
                }
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    void loginUser(){
        String email = editTextEmail.getText().toString();
        String password = editTextpassword.getText().toString();

        boolean isValidated = validateData(email,password);
        if(!isValidated){
            return;
        }

        LoginAccountInFirebase(email, password);
    }

    void LoginAccountInFirebase(String email, String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if(task.isSuccessful()){
                    // login is success
                        //if (firebaseAuth.getCurrentUser().isEmailVerified()){
                    NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.action_login_to_home);
                        //}
                } else {
                    // login failed
                }
            }
        });
    }

    //loading bar showing after login btn click
    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
        }
    }

    // check if data is good from input
    boolean validateData(String email, String password){

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Email is invalid");
            return false;
        }
        if (password.length()<6){
            editTextpassword.setError("Password too short");
            return false;
        }
        return true;
    }
}
