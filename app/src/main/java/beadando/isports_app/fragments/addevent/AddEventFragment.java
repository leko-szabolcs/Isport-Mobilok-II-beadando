package beadando.isports_app.fragments.addevent;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import beadando.isports_app.MainActivity;
import beadando.isports_app.R;
import beadando.isports_app.databinding.FragmentAddEventBinding;
import beadando.isports_app.domain.Event;
import beadando.isports_app.domain.User;
import beadando.isports_app.fragments.SharedViewModel;
import beadando.isports_app.util.SessionManager;
import beadando.isports_app.util.validation.EventValidator;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddEventFragment extends Fragment {
    @Inject
    SessionManager sessionManager;

    private AddEventViewModel addEventViewModel;
    private SharedViewModel sharedViewModel;
    private FragmentAddEventBinding binding;
    private Calendar calendar;
    private EventValidator eventValidator;
    private ArrayAdapter<String> sportTypeAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddEventBinding.inflate(inflater, container, false);
        addEventViewModel = new ViewModelProvider(this).get(AddEventViewModel.class);
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        eventValidator = new EventValidator();
        setupSportTypesSpinner();

        sharedViewModel.sportTypes.observe(getViewLifecycleOwner(), this::handleSportTypes);
        addEventViewModel.isLoading.observe(getViewLifecycleOwner(), this::handleLoadingState);
        addEventViewModel.saveSuccess.observe(getViewLifecycleOwner(), this::handleSaveSuccess);
        addEventViewModel.errorMessage.observe(getViewLifecycleOwner(), this::handleError);

        sharedViewModel.loadSportTypes();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendar = Calendar.getInstance();
        setupDatePicker();

        binding.btnUpload.setOnClickListener(v -> {
            handleCreateEvent();
        });
    }

    private void setupDatePicker() {
        binding.etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        showTimePickerDialog();
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        updateDateTimeField();
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );

        timePickerDialog.show();
    }

    private void updateDateTimeField() {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy. MM. dd. HH:mm", Locale.getDefault());
        binding.etDate.setText(dateTimeFormat.format(calendar.getTime()));
    }

    private void updateDateField() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy. MM. dd.", Locale.getDefault());
        binding.etDate.setText(dateFormat.format(calendar.getTime()));
    }

    private void handleCreateEvent() {
        String title = binding.etTitle.getText().toString();
        String location = binding.etLocation.getText().toString();
        String type = binding.spinnerType.getSelectedItem().toString();
        Timestamp date = new Timestamp(calendar.getTime());
        String participantsStr = binding.etParticipants.getText().toString();
        String description = binding.etDescription.getText().toString();
        Boolean fee = binding.radioPaid.isChecked();

        try {
            eventValidator.isValidEvent(requireContext(), title, type, location, date, participantsStr, description);

            int participants = Integer.parseInt(participantsStr);
            User user = sessionManager.getUser();

            Event event = new Event(title, type, location, date, fee, participants, description, user.getUid(), new ArrayList<>());
            addEventViewModel.saveEvent(event);
        } catch (EventValidator.ValidationException e) {
            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void clearForm() {
        binding.etTitle.setText("");
        binding.etLocation.setText("");
        binding.etDate.setText("");
        binding.etParticipants.setText("");
        binding.etDescription.setText("");
        binding.radioPaid.setChecked(false);
        binding.spinnerType.setSelection(0);
        calendar = Calendar.getInstance();
    }

    private void handleLoadingState(Boolean isLoading) {
        binding.btnUpload.setEnabled(!isLoading);
        ((MainActivity) requireActivity()).showLoadingOverlay(isLoading);
    }

    private void handleSaveSuccess(int resId) {
        showMessage(resId);
        clearForm();
    }

    private void handleError(int resId) {
        showMessage(resId);
    }

    private void showMessage(int resId) {
        if (resId != 0){
            Toast.makeText(requireContext(), getString(resId), Toast.LENGTH_LONG).show();
        }
    }

    private void handleSportTypes(List<String> types) {
        sportTypeAdapter.clear();
        sportTypeAdapter.addAll(types);
        sportTypeAdapter.notifyDataSetChanged();
    }

    private void setupSportTypesSpinner(){
        sportTypeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new ArrayList<>());
        sportTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerType.setAdapter(sportTypeAdapter);
    }
}
