package com.futech.our_school.activities.register;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.futech.our_school.R;
import com.futech.our_school.dialog.ProgressDialog;
import com.futech.our_school.request.school.SchoolClassData;
import com.futech.our_school.request.school.SchoolData;
import com.futech.our_school.request.school.SchoolHelper;
import com.futech.our_school.utils.SchoolToast;
import com.futech.our_school.utils.country.CityData;
import com.futech.our_school.utils.country.CountryHelper;
import com.futech.our_school.utils.country.StateData;
import com.futech.our_school.utils.request.listener.SelectListener;

public class SelectSchoolHelper implements AdapterView.OnItemSelectedListener {

    private Spinner statesSpinner, citySpinner, schoolSpinner, classSpinner;
    private Context context;
    private Button doneButton;
    private int classSelected;

    public SelectSchoolHelper(View view) {

        context = view.getContext();

        statesSpinner = view.findViewById(R.id.states_spinner);
        citySpinner = view.findViewById(R.id.city_spinner);
        schoolSpinner = view.findViewById(R.id.school_spinner);
        classSpinner = view.findViewById(R.id.class_spinner);
        doneButton = view.findViewById(R.id.done_button);

        citySpinner.setEnabled(false);
        schoolSpinner.setEnabled(false);
        classSpinner.setEnabled(false);
        doneButton.setEnabled(false);
        loadStates();

        statesSpinner.setOnItemSelectedListener(this);
        citySpinner.setOnItemSelectedListener(this);
        schoolSpinner.setOnItemSelectedListener(this);
        classSpinner.setOnItemSelectedListener(this);
    }

    private void loadStates() {
        CountryHelper helper = new CountryHelper(context);
        citySpinner.setEnabled(false);
        schoolSpinner.setEnabled(false);
        classSpinner.setEnabled(false);
        doneButton.setEnabled(false);
        helper.getStates(new SelectListener<StateData[]>() {
            @Override
            public void onSelect(StateData[] data, boolean isOnline) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                        android.R.layout.simple_list_item_1);
                for (StateData stateData : data) {
                    adapter.add(stateData.getName());
                }
                statesSpinner.setAdapter(adapter);
            }

            @Override
            public void onError(String msg) {
                SchoolToast.makeFail(context, msg, SchoolToast.LENGTH_SHORT);
            }
        });
    }

    private void loadCities(String state) {
        CountryHelper helper = new CountryHelper(context);
        schoolSpinner.setEnabled(false);
        classSpinner.setEnabled(false);
        doneButton.setEnabled(false);
        helper.getCities(new SelectListener<CityData[]>() {
            @Override
            public void onSelect(CityData[] data, boolean isOnline) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                        android.R.layout.simple_list_item_1);
                for (CityData cityData : data) {
                    adapter.add(cityData.getName());
                }
                citySpinner.setAdapter(adapter);
                citySpinner.setEnabled(true);
            }

            @Override
            public void onError(String msg) {
                SchoolToast.makeFail(context, msg, SchoolToast.LENGTH_SHORT).show();
            }
        }, state);
    }

    private void loadSchools(String city) {
        classSpinner.setEnabled(false);
        doneButton.setEnabled(false);
        SchoolHelper helper = new SchoolHelper(context);
        helper.getSchoolInCity(city, new SelectListener<SchoolData[]>() {
            @Override
            public void onSelect(SchoolData[] dataList, boolean isOnline) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                        android.R.layout.simple_list_item_1);
                if (dataList.length > 0) {
                    for (SchoolData data : dataList) {
                        adapter.add(data.getName());
                    }
                }else {
                    adapter.add(context.getString(R.string.not_found_school));
                }
                schoolSpinner.setAdapter(adapter);
                schoolSpinner.setEnabled(true);
            }

            @Override
            public void onError(String msg) {
                SchoolToast.makeFail(context, msg, SchoolToast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner sp = (Spinner) parent;
        int spId = sp.getId();
        if (spId == statesSpinner.getId()) {
            loadCities(statesSpinner.getSelectedItem().toString());
        }else if (spId == citySpinner.getId()) {
            loadSchools(citySpinner.getSelectedItem().toString());
        }else if (spId == schoolSpinner.getId()) {
            loadClasses(schoolSpinner.getSelectedItem().toString());
        }else if (spId == classSpinner.getId()) {
            Object selectedItem = classSpinner.getSelectedItem();
            setClassSelected(-1);
            if (selectedItem instanceof SchoolClassData) {
                int data = ((SchoolClassData) selectedItem).getId();
                setClassSelected(data);
                doneButton.setEnabled(true);
            }
        }
    }

    private void loadClasses(String schoolName) {
        doneButton.setEnabled(false);
        SchoolHelper helper = new SchoolHelper(context);
        helper.getSchoolClasses(schoolName, new SelectListener<SchoolClassData[]>() {
            @Override
            public void onSelect(SchoolClassData[] data, boolean isOnline) {
                if (data.length > 0) {
                    ArrayAdapter<SchoolClassData> adapter = new ArrayAdapter<>(context,
                            android.R.layout.simple_list_item_1);
                    adapter.addAll(data);
                    classSpinner.setAdapter(adapter);
                }else {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                            android.R.layout.simple_list_item_1);
                    adapter.add(context.getString(R.string.not_found_classes));
                    classSpinner.setAdapter(adapter);
                }
                classSpinner.setEnabled(true);
            }

            @Override
            public void onError(String msg) {
                SchoolToast.makeFail(context, msg, SchoolToast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public int getClassSelected() {
        return classSelected;
    }

    private void setClassSelected(int classSelected) {
        this.classSelected = classSelected;
    }
}
