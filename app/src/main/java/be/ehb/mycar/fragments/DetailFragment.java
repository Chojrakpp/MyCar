package be.ehb.mycar.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import be.ehb.mycar.R;
import be.ehb.mycar.databinding.FragmentDetailBinding;
import be.ehb.mycar.models.Note;

public class DetailFragment extends Fragment {

    Button btnDeleteNote;
    TextView btnDetailToHome;

    private FragmentDetailBinding binding;
    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(inflater, container, false);

        btnDeleteNote = binding.btnDeleteNote;
        btnDetailToHome = binding.btnDetailToHome;


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Note passedNote = (getArguments() != null)? (Note) getArguments().getSerializable("note") : null;// argumenten bestaan check
        if (passedNote != null) {
            TextView titleTV = view.findViewById(R.id.details_tv_title);
            TextView contextTV = view.findViewById(R.id.details_tv_context);
            TextView fuelEconomyTV = view.findViewById(R.id.details_tv_fuel_economy);
            TextView createdTV = view.findViewById(R.id.details_tv_created);

            titleTV.setText(passedNote.getTitle());
            contextTV.setText(passedNote.getContent());
            fuelEconomyTV.setText(passedNote.getFuelEconomy());
            createdTV.setText(timestampToString(passedNote.getTimestamp()));

            btnDeleteNote.setOnClickListener(
                    (View v) -> {
                        deleteNoteFromFirebase(passedNote);
                    }
            );

            btnDetailToHome.setOnClickListener(
                    (View v) -> {
                        NavHostFragment.findNavController(DetailFragment.this)
                                .navigate(R.id.action_detail_to_home);
                    }
            );
        }
    }

    void deleteNoteFromFirebase(Note note){
        String docId = note.getDocId();
        DocumentReference documentReference;
        documentReference = getCollectionReferenceForNotes().document(docId);

        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    // note is deleted
                    Toast.makeText(getActivity(), "Note deleted successfully", Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(DetailFragment.this)
                            .navigate(R.id.action_detail_to_home);
                } else {
                    //note delete failed
                    Toast.makeText(getActivity(), "Failed while adding note", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    static CollectionReference getCollectionReferenceForNotes() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("notes")
                .document(currentUser.getUid()).collection("my_notes");
    }

    static String timestampToString(Timestamp ts){
        return new SimpleDateFormat("dd/MM/yyyy, HH:mm").format(ts.toDate());
    }
}
