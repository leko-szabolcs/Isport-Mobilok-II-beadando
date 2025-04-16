package beadando.isports_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import beadando.isports_app.MainActivity;
import beadando.isports_app.R;
import beadando.isports_app.data.repostiory.EventRepository;
import beadando.isports_app.databinding.FragmentMainBinding;
import beadando.isports_app.domain.Event;

public class MainFragment extends Fragment {

    private MainAdapter adapter;
    private MainViewModel viewModel;
    private FragmentMainBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainViewModelFactory factory = new MainViewModelFactory();
        viewModel = new ViewModelProvider(this, factory).get(MainViewModel.class);

        adapter = new MainAdapter();
        binding.recyclerViewEvents.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewEvents.setAdapter(adapter);

        viewModel.getEvents().observe(getViewLifecycleOwner(), this::handleEvents);
        viewModel.isLoading.observe(getViewLifecycleOwner(), this::handleLoading);
        viewModel.errorMessage.observe(getViewLifecycleOwner(), this::handleError);

        setupScrollListener();
        viewModel.loadEvents();
    }

    private void setupScrollListener() {
        binding.recyclerViewEvents.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (lm != null
                        && lm.findLastVisibleItemPosition() >= adapter.getItemCount() - 5
                        && !Boolean.TRUE.equals(viewModel.isLoading.getValue())) {
                    viewModel.loadEvents();
                }
            }
        });
    }

    private void handleEvents(List<Event> events) {
        adapter.addEvents(events);
    }

    private void handleLoading(Boolean loading) {
        ((MainActivity) requireActivity()).showLoadingOverlay(loading);
    }

    private void handleError(String error) {
        if (error != null) {
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        }
    }
}
