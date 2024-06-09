package com.example.myapplication.boking_Podcast;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class booking extends AppCompatActivity {
    private Button btn_booking;
    private DatePickerDialog picker;
    private TimePickerDialog timePickerDialog;
    private EditText nama, nomorHp, waktu, tanggal, durasi;
    private ImageButton btn_tnggal,btn_waktu,btn_durasi, back;
    private Calendar selectedDate;

    private String selectedTime;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boking);
        btn_tnggal = findViewById(R.id.imageButton3);
        btn_waktu = findViewById(R.id.imageButton);
        back = findViewById(R.id.btn_bck);
        back.setOnClickListener(view -> onBackPressed());
        nama = findViewById(R.id.editTextNama);
        nomorHp = findViewById(R.id.editTextTelepon);
        waktu = findViewById(R.id.editTexttime);
        tanggal = findViewById(R.id.editTextTanggal);
        btn_booking = findViewById(R.id.btn_booking);
        durasi = findViewById(R.id.editTextjam);

        db = FirebaseFirestore.getInstance();

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "DefaultUsername");
        String email = sharedPreferences.getString("nohp", "DefaultEmail");

        nama.setText(username);
        nomorHp.setText( email);



        btn_tnggal.setOnClickListener(view -> showDatePickerDialog());

        btn_waktu.setOnClickListener(view -> showTimePickerDialog());

        btn_booking.setOnClickListener(view -> bookAppointment());
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void showDatePickerDialog() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        // Create a DatePickerDialog with the current date as the default date
        picker = new DatePickerDialog(booking.this,
                (DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) -> {
                    selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDay);

                    if (!selectedDate.before(cldr)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        tanggal.setText(sdf.format(selectedDate.getTime()));
                    } else {
                        // Display a message that yesterday's date cannot be selected
                        tanggal.setText("");
                        Toast.makeText(booking.this, "Cannot select yesterday's date", Toast.LENGTH_SHORT).show();
                    }
                }, year, month, day);

        // Set the minimum date to today
        picker.getDatePicker().setMinDate(cldr.getTimeInMillis());

        // Show the DatePickerDialog
        picker.show();
    }


    private void showTimePickerDialog() {
        if (selectedDate != null) {
            Calendar currentCalendar = Calendar.getInstance();

            // Check if the selected date is the same as the current date
            if (selectedDate.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
                    selectedDate.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH) &&
                    selectedDate.get(Calendar.DAY_OF_MONTH) == currentCalendar.get(Calendar.DAY_OF_MONTH)) {

                // Create a TimePickerDialog with the current time as the default time
                timePickerDialog = new TimePickerDialog(booking.this,
                        (TimePicker timePicker, int hourOfDay, int minute) -> {
                            handleTimeSelection(hourOfDay, minute, currentCalendar);
                        }, currentCalendar.get(Calendar.HOUR_OF_DAY), currentCalendar.get(Calendar.MINUTE), true);
            } else {
                // Create a TimePickerDialog without checking for current time
                timePickerDialog = new TimePickerDialog(booking.this,
                        (TimePicker timePicker, int hourOfDay, int minute) -> {
                            handleTimeSelection(hourOfDay, minute, null);
                        }, currentCalendar.get(Calendar.HOUR_OF_DAY), currentCalendar.get(Calendar.MINUTE), true);
            }

            // Show the TimePickerDialog
            timePickerDialog.show();
        } else {
            Toast.makeText(booking.this, "Please select a valid date first", Toast.LENGTH_SHORT).show();
        }
    }


    private void bookAppointment() {
        if (validateInputs()) {
            saveDataToFirestore();
        }
    }

    private boolean validateInputs() {
        // Validate your input fields here
        // Replace with your validation logic
        return true;
    }

    private void saveDataToFirestore() {
        Map<String, Object> data = new HashMap<>();
        data.put("nama", nama.getText().toString());
        data.put("nomor_hp", nomorHp.getText().toString());
        data.put("tanggal", tanggal.getText().toString());
        data.put("waktu", selectedTime);
        data.put("jam", durasi.getText().toString());

        db.collection("booking")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(booking.this, "Data berhasil disimpan.", Toast.LENGTH_SHORT).show();
                    clearInputFields();
                })
                .addOnFailureListener(e -> Toast.makeText(booking.this, "Data gagal disimpan.", Toast.LENGTH_SHORT).show());
    }

    private void clearInputFields() {
        nama.setText("");
        nomorHp.setText("");
        tanggal.setText("");
        waktu.setText("");
        durasi.setText("");
    }

    private void handleTimeSelection(int hourOfDay, int minute, Calendar currentCalendar) {
        Calendar selectedCalendar = Calendar.getInstance();
        selectedCalendar.setTime(selectedDate.getTime());
        selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        selectedCalendar.set(Calendar.MINUTE, minute);

        if (currentCalendar == null || !selectedCalendar.before(currentCalendar)) {
            String selectedTime = String.format("%02d:%02d", hourOfDay, minute);

            checkDateTimeInFirebase(selectedCalendar.getTime(), selectedTime);
        } else {
            Toast.makeText(booking.this, "Please select a valid time in the future", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkDateTimeInFirebase(final Date date, final String selectedTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        final String formattedDate = sdf.format(date);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("booking")
                .whereEqualTo("tanggal", formattedDate)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean isDateTimeAlreadySelected = checkForExistingDateTime(task.getResult(), selectedTime);
                        Log.d("DEBUG", "Is DateTime Already Selected: " + isDateTimeAlreadySelected);
                        handleDateTimeResult(isDateTimeAlreadySelected, selectedTime);
                    } else {
                        Log.e("ERROR", "Firestore query failed", task.getException());
                        Toast.makeText(booking.this, "Firestore query failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void handleDateTimeResult(boolean isDateTimeAlreadySelected, String selectedTime) {
        if (isDateTimeAlreadySelected) {
            Toast.makeText(booking.this, "Selected date and time already exist!", Toast.LENGTH_SHORT).show();
            waktu.setText(""); // Clear the time field
        } else {
            waktu.setText(selectedTime); // Set the selected time
        }
    }

    private boolean checkForExistingDateTime(QuerySnapshot querySnapshot, String selectedTime) {
        for (DocumentSnapshot document : querySnapshot) {
            String appointmentTime = document.getString("waktu");
            String appointmentDurationStr = document.getString("jam"); // Assuming 'jam' is the field for duration

            if (appointmentTime != null && appointmentDurationStr != null) {
                try {
                    int appointmentDuration = Integer.parseInt(appointmentDurationStr);

                    if (isTimeSlotOverlapping(appointmentTime, appointmentDuration, selectedTime)) {
                        return true;
                    }
                } catch (NumberFormatException e) {
                    Log.e("ERROR", "NumberFormatException", e);
                    e.printStackTrace();
                    // Handle the case where 'jam' is not a valid integer
                }
            }
        }
        return false;
    }

    private boolean isTimeSlotOverlapping(String existingTime, int existingDuration, String selectedTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date existingStartTime = sdf.parse(existingTime);
            Date existingEndTime = new Date(existingStartTime.getTime() + TimeUnit.HOURS.toMillis(existingDuration));

            Date selectedStartTime = sdf.parse(selectedTime);
            Date selectedEndTime = new Date(selectedStartTime.getTime() + TimeUnit.HOURS.toMillis(existingDuration));

            Log.d("DEBUG", "Existing Start Time: " + existingStartTime);
            Log.d("DEBUG", "Existing End Time: " + existingEndTime);
            Log.d("DEBUG", "Selected Start Time: " + selectedStartTime);
            Log.d("DEBUG", "Selected End Time: " + selectedEndTime);

            boolean isOverlapping = (selectedStartTime.before(existingEndTime) && selectedEndTime.after(existingStartTime));
            Log.d("DEBUG", "Is Overlapping: " + isOverlapping);

            return isOverlapping;
        } catch (ParseException e) {
            Log.e("ERROR", "ParseException", e);
            e.printStackTrace();
        }
        return false;
    }




}
