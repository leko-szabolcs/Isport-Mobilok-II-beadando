package beadando.isports_app.fragments.main;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import beadando.isports_app.R;
import beadando.isports_app.databinding.ItemEventBinding;
import beadando.isports_app.domain.Event;
import beadando.isports_app.util.DateUtils;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
    private final List<Event> eventList = new ArrayList<>();
    private OnItemClickListener  listener;

    public interface OnItemClickListener {
        void onItemClick(Event event);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setEvents(List<Event> newEvents) {
        eventList.clear();
        eventList.addAll(newEvents);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MainAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemEventBinding binding = ItemEventBinding.inflate(inflater, parent, false);
        return new MainViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.MainViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.bind(event);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(event);
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    static class MainViewHolder extends RecyclerView.ViewHolder {
        private final ItemEventBinding binding;

        public MainViewHolder(ItemEventBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Event event) {
            binding.textEventTitle.setText(event.getTitle());
            binding.textEventLocation.setText(event.getLocation());
            binding.textParticipants.setText(binding.getRoot().getContext().getString(
                            R.string.event_participants,
                            event.getParticipantsList().size(),
                            event.getParticipants()
                    ));
            binding.textEventDate.setText(DateUtils.formatFirebaseTimestampForDisplay(event.getDate()));
            binding.imageEventType.setImageResource(R.drawable.ic_sport_logo);
        }
    }
}
