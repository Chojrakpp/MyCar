package be.ehb.mycar.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firestore.v1.DocumentTransform;

import be.ehb.mycar.R;
import be.ehb.mycar.databinding.FragmentNoteDetailsBinding;
import be.ehb.mycar.models.Note;

public class NoteDetailsFragment extends Fragment {

    private FragmentNoteDetailsBinding binding;

    EditText editTextTitle, editTextContent;
    ImageButton btnSaveNote;
    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        binding = FragmentNoteDetailsBinding.inflate(inflater, container, false);

        editTextTitle = binding.etNoteTitle;
        editTextContent = binding.etNoteContent;
        btnSaveNote = binding.btnSaveNote;

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnSaveNote.setOnClickListener(
                (View v) -> {
                    saveNote();
                }
        );
    }

    void saveNote(){
        String noteTitle = editTextTitle.getText().toString();
        String noteContent = editTextContent.getText().toString();
        if (noteTitle==null || noteTitle.isEmpty()) {
            editTextTitle.setError("Title is required");
            return;
        }

        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        saveNoteToFirebase(note);
    }

    void saveNoteToFirebase(Note note){
        DocumentReference documentReference;
        documentReference = getCollectionReferenceForNotes().document();

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    // note is added
                    Toast.makeText(getActivity(), "Note added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    //note failed
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
