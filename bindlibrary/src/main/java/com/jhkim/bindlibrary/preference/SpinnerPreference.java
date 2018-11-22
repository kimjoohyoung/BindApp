package com.jhkim.bindlibrary.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.preference.Preference;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.jhkim.bindlibrary.R;

public class SpinnerPreference extends Preference {
    private final LayoutInflater mLayoutInflater;
    private String[] mEntries = new String[0];
    private String[] mEntryValues = new String[0];
    private int mSelection = 0;

    public SpinnerPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpinnerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWidgetLayoutResource(R.layout.preference_spinner);
        mLayoutInflater = LayoutInflater.from(getContext());
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SpinnerPreference);
        int entriesResId = ta.getResourceId(R.styleable.SpinnerPreference_entries, 0);
        if (entriesResId != 0) {
            mEntries = context.getResources().getStringArray(entriesResId);
        }
        int valuesResId = ta.getResourceId(R.styleable.SpinnerPreference_entryValues, 0);
        if (valuesResId != 0) {
            mEntryValues = context.getResources().getStringArray(valuesResId);
        }else{
            mEntryValues=new String[mEntries.length];
            for(int i=0;i<mEntries.length;++i){
                mEntryValues[i]=String.valueOf(i);
            }
        }
        ta.recycle();
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        super.onSetInitialValue(restorePersistedValue, defaultValue);
        String value = restorePersistedValue ? getPersistedString(null) : (String) defaultValue;
        for (int i = 0; i < mEntryValues.length; i++) {
            if (TextUtils.equals(mEntryValues[i], value)) {
                mSelection = i;
                break;
            }
        }
    }

    @Override
    public void onBindView(View view) {
        super.onBindView(view);
        final Spinner spinner = view.findViewById(R.id.spinner);
        view.setOnClickListener(v -> spinner.performClick());
        spinner.setAdapter(new SpinnerAdapter() {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = createDropDownView(position, parent);
                }
                bindDropDownView(position, convertView);
                return convertView;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {
                // no op
            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {
                // no op
            }

            @Override
            public int getCount() {
                return mEntries.length;
            }

            @Override
            public Object getItem(int position) {
                return null; // not applicable
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return getDropDownView(position, convertView, parent);
            }

            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        });
        spinner.setSelection(mSelection);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelection = position;
                persistString(mEntryValues[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // no op
            }
        });
    }

    /**
     * Create dropdown view for item at given position
     * @param position    item position
     * @param parent      parent view
     * @return  created view
     */
    private View createDropDownView(@SuppressWarnings("UnusedParameters")int position, ViewGroup parent){
        return mLayoutInflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
    }

    /**
     * Customize dropdown view for given spinner item
     * @param position  item position
     * @param view      item view
     */
    private void bindDropDownView(int position, View view){
        TextView textView = view.findViewById(android.R.id.text1);
        textView.setText(mEntries[position]);
    }
}
