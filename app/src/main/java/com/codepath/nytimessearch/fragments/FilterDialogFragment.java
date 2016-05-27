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
        filter = Parcels.unwrap(getArguments().getParcelable("filter"));
        spSortOrderAdapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.sort_order_array, android.R.layout.simple_spinner_item);
        spSortOrderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSortOrder.setAdapter(spSortOrderAdapter);
        etBeginDate.setText(filter.getBeginDate());
        setNewsType(filter.getNewsType());
        for(int i = 0; i < spSortOrderAdapter.getCount(); i++) {
            if(spSortOrderAdapter.getItem(i).toString().equalsIgnoreCase(filter.getSortOrder())) {
                spSortOrder.setSelection(i);
            }
        }

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
        filter.setBeginDate(etBeginDate.getText().toString());
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

}
