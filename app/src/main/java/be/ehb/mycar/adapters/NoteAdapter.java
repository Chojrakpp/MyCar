package be.ehb.mycar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.security.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        holder.textViewTimeStamp.setText(note.getTimestamp().toString());
    }



    @NonNull
    @Override
    public NoteAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item,parent,false);
        return new NoteViewHolder(view);
    }


class NoteViewHolder extends RecyclerView.ViewHolder{

        TextView textViewTitle, textViewContent, textViewTimeStamp;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tv_title_note);
            textViewContent = itemView.findViewById(R.id.tv_content_note);
            textViewTimeStamp = itemView.findViewById(R.id.tv_timestamp_note);
        }
    }
}
