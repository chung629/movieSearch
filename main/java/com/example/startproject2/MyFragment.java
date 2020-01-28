package com.example.startproject2;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment extends Fragment {

    EditText name;
    EditText birthday;
    EditText email;
    EditText password;
    RadioGroup radioGroup;
    private DatePickerDialog.OnDateSetListener callback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my, container, false);

        //VIEW id mapping
        name = rootView.findViewById(R.id.editText);
        birthday = rootView.findViewById(R.id.editText2);
        email = rootView.findViewById(R.id.editText3);
        password = rootView.findViewById(R.id.editText4);
        radioGroup = rootView.findViewById(R.id.radioGroup);

        // 클릭시 달력 출력
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), callback, 2019,
                        11, 15);
                datePickerDialog.show();
            }
        });
        callback = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                birthday.setText(year + "년" + (month+1) + "월" + day + "일");
            }
        };

        //상태 불러오기
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("save", MODE_PRIVATE);
        name.setText(sharedPreferences.getString("name", ""));
        birthday.setText(sharedPreferences.getString("birthday", ""));
        email.setText(sharedPreferences.getString("email", ""));
        password.setText(sharedPreferences.getString("password", ""));
        radioGroup.check(sharedPreferences.getInt("radiogroup", 0));
        return rootView;
    }
    //상태 저장
    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("save", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("name", name.getText().toString());
        editor.putString("birthday", birthday.getText().toString());
        editor.putString("email", email.getText().toString());
        editor.putString("password", password.getText().toString());
        editor.putInt("radiogroup", radioGroup.getCheckedRadioButtonId());

        editor.commit();
    }
}