package beadando.isports_app.fragments.event;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


import beadando.isports_app.databinding.ItemParticipantBinding;
import beadando.isports_app.domain.User;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private final List<User> eventParticipants = new ArrayList<>();

    EventAdapter(List<User> eventParticipants) {
        this.eventParticipants.addAll(eventParticipants);
    }

    @NonNull
    @Override
    public EventAdapter.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemParticipantBinding binding = ItemParticipantBinding.inflate(inflater, parent, false);
        return new EventViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.EventViewHolder holder, int position) {
        User user = eventParticipants.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return eventParticipants.size();
    }

    public void addParticipants(List<User> users) {
        int oldSize = eventParticipants.size();
        eventParticipants.addAll(users);
        notifyItemRangeInserted(oldSize, users.size());
    }

    public class EventViewHolder  extends RecyclerView.ViewHolder{
        private final ItemParticipantBinding binding;

        public EventViewHolder(ItemParticipantBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(User user) {
            binding.textParticipantsUsername.setText(user.getUsername());
        }
    }
}
