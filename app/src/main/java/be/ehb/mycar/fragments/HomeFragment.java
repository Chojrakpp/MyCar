package be.ehb.mycar.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import be.ehb.mycar.R;
import be.ehb.mycar.adapters.NoteAdapter;
import be.ehb.mycar.databinding.FragmentHomeBinding;
import be.ehb.mycar.models.Note;

public class HomeFragment extends Fragment {

    FloatingActionButton btnNewNote;
    RecyclerView recyclerView;
    ImageButton btnMenu;
    NoteAdapter noteAdapter;

    private FragmentHomeBinding binding;
    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        btnNewNote = binding.btnNewNote;
        recyclerView = binding.recyclerView;
        btnMenu = binding.btnMenu;

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnNewNote.setOnClickListener(
                (View v) -> {
                    NavHostFragment.findNavController(HomeFragment.this)
                            .navigate(R.id.action_home_to_noteDetails);
                }
        );

        btnMenu.setOnClickListener(
                (View v) -> {
                    showMenu();
                }
        );

        setupRecyclerView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    void showMenu(){

    }

    void setupRecyclerView(){
        Query query = getCollectionReferenceForNotes().orderBy("timestamp", Query.Direction.DESCENDING); // created database with timestamp
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>() // converted query to options
                .setQuery(query,Note.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        noteAdapter = new NoteAdapter(options, getActivity()); // options passed in noteadapter
        recyclerView.setAdapter(noteAdapter);
    }

    static CollectionReference getCollectionReferenceForNotes() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("notes")
                .document(currentUser.getUid()).collection("my_notes");
    }

    @Override
    public void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
    }
}

