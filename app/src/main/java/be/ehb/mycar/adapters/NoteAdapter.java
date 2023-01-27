package be.ehb.mycar.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import be.ehb.mycar.R;
import be.ehb.mycar.models.Note;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder> {
    Context context;

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteAdapter.NoteViewHolder holder, int position, @NonNull Note note) {
        holder.textViewTitle.setText(note.getTitle()); // setting data to textview
        holder.textViewContent.setText(note.getContent());
        holder.textViewTimeStamp.setText(timestampToString(note.getTimestamp()));
        holder.textViewFuelEconomy.setText(note.getFuelEconomy());
        holder.itemView.setOnClickListener(view -> {
            Bundle dataToPass = new Bundle();
            String docId = this.getSnapshots().getSnapshot(position).getId();
            note.setDocId(docId);
            dataToPass.putSerializable("note", note);
            Navigation.findNavController(holder.itemView).navigate(R.id.action_home_to_detail, dataToPass);});
    }

    @NonNull
    @Override
    public NoteAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item,parent,false);
        return new NoteViewHolder(view);
    }


class NoteViewHolder extends RecyclerView.ViewHolder{

        TextView textViewTitle, textViewContent, textViewFuelEconomy,textViewTimeStamp;
        final LinearLayout cardView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tv_title_note);
            textViewContent = itemView.findViewById(R.id.tv_content_note);
            textViewTimeStamp = itemView.findViewById(R.id.tv_timestamp_note);
            textViewFuelEconomy = itemView.findViewById(R.id.tv_fuel_economy);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }

    static String timestampToString(Timestamp ts){
        return new SimpleDateFormat("dd/MM/yyyy, HH:mm").format(ts.toDate());
    }
}
