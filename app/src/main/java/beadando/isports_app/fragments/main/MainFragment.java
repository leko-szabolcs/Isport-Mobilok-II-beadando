package beadando.isports_app.fragments.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import beadando.isports_app.MainActivity;
import beadando.isports_app.R;
import beadando.isports_app.databinding.FragmentMainBinding;
import beadando.isports_app.domain.Event;
import beadando.isports_app.fragments.SharedViewModel;
import beadando.isports_app.fragments.event.EventViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainFragment extends Fragment {

    private MainAdapter adapter;
    private MainViewModel mainViewModel;
    private FragmentMainBinding binding;

    private SharedViewModel sharedViewModel;
    private ArrayAdapter<String> sportTypeAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        setupSportTypesSpinner();
        sharedViewModel.sportTypes.observe(getViewLifecycleOwner(), this::handleSportTypes);

        return binding.getRoot();
    }

    private void setupSportTypesSpinner() {
        sportTypeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new ArrayList<>());
        sportTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerEventType.setAdapter(sportTypeAdapter);
    }

    private void handleSportTypes(List<String> types) {
        sportTypeAdapter.clear();
        sportTypeAdapter.addAll(types);
        sportTypeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel.loadSportTypes();

        adapter = new MainAdapter();
        binding.recyclerViewEvents.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewEvents.setAdapter(adapter);

        mainViewModel.getEvents().observe(getViewLifecycleOwner(), this::handleEvents);
        mainViewModel.isLoading.observe(getViewLifecycleOwner(), this::handleLoading);
        mainViewModel.errorMessage.observe(getViewLifecycleOwner(), this::handleError);

        setupOnItemClickListener();
        setupScrollListener();
        mainViewModel.loadEvents();
    }

    private void setupScrollListener() {
        binding.recyclerViewEvents.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (lm != null
                        && lm.findLastVisibleItemPosition() >= adapter.getItemCount() - 5
                        && !Boolean.TRUE.equals(mainViewModel.isLoading.getValue())) {
                    mainViewModel.loadEvents();
                }
            }
        });
    }

    private void setupOnItemClickListener() {
        adapter.setOnItemClickListener(event -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("event", event);
            NavHostFragment.findNavController(this).navigate(R.id.action_mainFragment_to_eventFragment, bundle);
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
