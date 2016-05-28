package com.codepath.nytimessearch.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.models.Filter;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by charlie_zhou on 5/26/16.
 */
public class FilterDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private Filter filter = new Filter();

    @BindView(R.id.etBeginDate)
    EditText etBeginDate;

    @BindView(R.id.spSortOrder)
    Spinner spSortOrder;
    ArrayAdapter<CharSequence> spSortOrderAdapter;

    @BindView(R.id.cbArts)
    CheckBox cbArts;

    @BindView(R.id.cbFashion)
    CheckBox cbFashion;

    @BindView(R.id.cbSports)
    CheckBox cbSports;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // populate spinner
        spSortOrderAdapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.sort_order_array, android.R.layout.simple_spinner_item);
        spSortOrderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSortOrder.setAdapter(spSortOrderAdapter);

        // load filter
        filter = Parcels.unwrap(getArguments().getParcelable("filter"));
        bind();
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();


        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((int) (size.x * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        // Call super onResume after sizing
        super.onResume();
    }

    public static FilterDialogFragment newInstance(Filter filter) {
        Bundle args = new Bundle();
        args.putParcelable("filter", Parcels.wrap(filter));
        FilterDialogFragment fragment = new FilterDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public interface FilterDialogListener {
        void onSaveFilterDialog(Filter filter);
    }

    @OnClick(R.id.btnSave)
    public void save() {
        filter.setSortOrder(spSortOrderAdapter.getItem(
                spSortOrder.getSelectedItemPosition()).toString());
        filter.setNewsType(getNewsType());
        FilterDialogListener listener = (FilterDialogListener) getActivity();
        listener.onSaveFilterDialog(filter);
        dismiss();
    }

    public String getNewsType() {
        List<String> newsTypes = new ArrayList<String>();
        if (cbArts.isChecked()) newsTypes.add("news_desk:Arts");
        if (cbFashion.isChecked()) newsTypes.add("news_desk:Fashion & Style");
        if (cbSports.isChecked()) newsTypes.add("news_desk:Sports");
        return TextUtils.join(" OR ", newsTypes);
    }

    public void setNewsType(String newsType) {
        cbArts.setChecked(newsType.contains("Arts"));
        cbFashion.setChecked(newsType.contains("Fashion"));
        cbSports.setChecked(newsType.contains("Sports"));
    }

    @OnClick(R.id.etBeginDate)
    public void showDatePickerDialog(View v) {
        FragmentManager fm = getFragmentManager();
        DatePickerFragment newFragment = new DatePickerFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        cal.setTime(filter.getBeginDate());
        args.putInt("year", cal.get(Calendar.YEAR));
        args.putInt("month", cal.get(Calendar.MONTH));
        args.putInt("day", cal.get(Calendar.DAY_OF_MONTH));
        newFragment.setArguments(args);
        newFragment.setTargetFragment(FilterDialogFragment.this, 300);
        newFragment.show(fm, "datePicker");
    }

    public void bind() {
        etBeginDate.setText(filter.getBeginDateToShow());
        setNewsType(filter.getNewsType());
        for(int i = 0; i < spSortOrderAdapter.getCount(); i++) {
            if(spSortOrderAdapter.getItem(i).toString().equalsIgnoreCase(filter.getSortOrder())) {
                spSortOrder.setSelection(i);
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, monthOfYear, dayOfMonth);
        filter.setBeginDate(cal.getTime());
        etBeginDate.setText(filter.getBeginDateToShow());
    }
}
