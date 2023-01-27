package be.ehb.mycar.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import be.ehb.mycar.R;
import be.ehb.mycar.databinding.FragmentCreateAccountBinding;

public class CreateAccountFragment extends Fragment {

    private FragmentCreateAccountBinding binding;
    EditText editTextEmail, editTextpassword, editTextConfirmPassword;
    Button btnSingUp;
    ProgressBar progressBar;
    TextView btnToLogin;

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        binding = FragmentCreateAccountBinding.inflate(inflater, container, false);

        editTextEmail = binding.etEmail;
        editTextpassword = binding.etPassword;
        editTextConfirmPassword = binding.etConfirmPassword;
        btnSingUp = binding.btnSingUp;
        progressBar = binding.progressBar;
        btnToLogin = binding.btnToLogin;

        btnSingUp.setOnClickListener(v-> createAccount());
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnToLogin.setOnClickListener(
                (View v) -> {
                    NavHostFragment.findNavController(CreateAccountFragment.this)
                            .navigate(R.id.action_createAccount_to_login);
                }
        );
    }

    void createAccount(){
        String email = editTextEmail.getText().toString();
        String password = editTextpassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();

        boolean isValidated = validateData(email,password,confirmPassword);
        if(!isValidated){
            return;
        }

        CreateAccountInFirebase(email, password);
    }

    void CreateAccountInFirebase(String email,String password){
        changeInProgress(true);

        FirebaseAuth fireBaseAuth = FirebaseAuth.getInstance();
        fireBaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(),
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false); // take it too false to let the user make an other try to register and push button
                        if(task.isSuccessful()){
                            // Account created
                            Toast.makeText(getActivity(), "Succesfully create account, Check email to verify", Toast.LENGTH_SHORT).show();
                            //fireBaseAuth.getCurrentUser().sendEmailVerification();
                            fireBaseAuth.signOut(); // sign out after verification can login instantly
                            NavHostFragment.findNavController(CreateAccountFragment.this).navigate(R.id.action_createAccount_to_login);
                        } else {
                            // Register failed
                            Toast.makeText(getActivity(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //loading bar showing after register btn click
    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            btnSingUp.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            btnSingUp.setVisibility(View.VISIBLE);
        }
    }

    // check if data is good from input
    boolean validateData(String email, String password, String confirmPassword){

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Email is invalid");
            return false;
        }
        if (password.length()<6){
            editTextpassword.setError("Password too short");
            return false;
        }
        if (!password.equals(confirmPassword)){
            editTextConfirmPassword.setError("Passwords are not the same");
            return false;
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}