package com.codepath.nytimessearch.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
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
public class FilterDialogFragment extends DialogFragment {
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
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
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
        if (cbFashion.isChecked()) newsTypes.add("news_desk:Fashion");
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
        DatePickerFragment newFragment = new DatePickerFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        cal.setTime(filter.getBeginDate());
        args.putInt("year", cal.get(Calendar.YEAR));
        args.putInt("month", cal.get(Calendar.MONTH));
        args.putInt("day", cal.get(Calendar.DAY_OF_MONTH));
        newFragment.setArguments(args);
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
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

    public void updateBeginDate(int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, monthOfYear, dayOfMonth);
        filter.setBeginDate(cal.getTime());
        etBeginDate.setText(filter.getBeginDateToShow());
    }
}
