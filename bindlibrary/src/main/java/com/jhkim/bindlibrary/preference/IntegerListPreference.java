package com.jhkim.bindlibrary.preference;

import android.content.Context;
import android.os.Build;
import android.preference.ListPreference;
import android.util.AttributeSet;
import androidx.annotation.RequiresApi;

public class IntegerListPreference extends ListPreference {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public IntegerListPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        verifyEntryValues(null);
    }

    public IntegerListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        verifyEntryValues(null);
    }

    @Override
    public void setEntryValues(CharSequence[] entryValues) {
        CharSequence[] oldValues = getEntryValues();
        super.setEntryValues(entryValues);
        verifyEntryValues(oldValues);
    }

    @Override
    public void setEntryValues(int entryValuesResId) {
        CharSequence[] oldValues = getEntryValues();
        super.setEntryValues(entryValuesResId);
        verifyEntryValues(oldValues);
    }

    @Override
    protected String getPersistedString(String defaultReturnValue) {
        // During initial query, there's no known default value
        int defaultIntegerValue = Integer.MIN_VALUE;
        if (defaultReturnValue != null) {
            defaultIntegerValue = Integer.parseInt(defaultReturnValue);
        }

        // When the list preference asks us to read a string, instead read an
        // integer.
        int value = getPersistedInt(defaultIntegerValue);
        return Integer.toString(value);
    }

    @Override
    protected boolean persistString(String value) {
        // When asked to save a string, instead save an integer
        return persistInt(Integer.parseInt(value));
    }

    private void verifyEntryValues(CharSequence[] oldValues) {
        CharSequence[] entryValues = getEntryValues();
        if (entryValues == null) {
            entryValues=new CharSequence[getEntries().length];
            for(int i=0;i<getEntries().length;++i){
                entryValues[i]=String.valueOf(i);
            }
            super.setEntryValues(entryValues);
            return;
        }

        for (CharSequence entryValue : entryValues) {
            try {
                Integer.parseInt(entryValue.toString());
            } catch (NumberFormatException nfe) {
                super.setEntryValues(oldValues);
                throw nfe;
            }
        }
    }
}
