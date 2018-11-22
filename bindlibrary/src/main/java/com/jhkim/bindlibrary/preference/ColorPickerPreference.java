package com.jhkim.bindlibrary.preference;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.jhkim.bindlibrary.R;
import com.jhkim.bindlibrary.preference.color.ColorPickerDialog;
import com.jhkim.bindlibrary.preference.color.SimpleColorDialogFragment;

@SuppressWarnings("WeakerAccess, unused, FieldCanBeLocal")
public class ColorPickerPreference extends Preference implements ColorPickerDialog.OnColorChangedListener {
    private final int colorValuesResId;
    private final int applyResId;
    private final int moreResId;

    private View mPreviewView;
    private int mValue = 0;

    public ColorPickerPreference(Context context) {
        this(context, null);
    }

    public ColorPickerPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorPickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWidgetLayoutResource(R.layout.preference_widget_color);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColorPreference, defStyleAttr, 0);
        colorValuesResId = a.getResourceId(R.styleable.ColorPreference_android_entryValues, R.array.default_color_choice_values);
        applyResId = a.getResourceId(R.styleable.ColorPreference_applyString, R.string.dialog_color_picker);
        moreResId = a.getResourceId(R.styleable.ColorPreference_moreString, R.string.more);
        a.recycle();
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        final TextView titleView = view.findViewById(android.R.id.title);
        if(titleView!=null) {
            titleView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        }
        mPreviewView = view.findViewById(R.id.calendar_color_view);
        setColorViewValue(mPreviewView, mValue);
    }
/*
    public void onBindViewHolder(PreferenceViewHolder holder) {
        //super.onBindViewHolder(holder);
        final TextView titleView = (TextView) holder.findViewById(android.R.id.title);
        if(titleView!=null) {
            titleView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        }
        mPreviewView = holder.findViewById(R.id.calendar_color_view);
        setColorViewValue(mPreviewView, mValue);
    }*/

    public void setValue(int value) {
        if (callChangeListener(value)) {
            mValue = value;
            persistInt(value);
            notifyChanged();
        }
    }

    @Override
    protected void onClick() {
        super.onClick();

        SimpleColorDialogFragment colorCalendar = SimpleColorDialogFragment.create(colorValuesResId, mValue, moreResId);
        colorCalendar.setOnColorChangedListener(this);
        colorCalendar.show(((AppCompatActivity)getActivity()).getSupportFragmentManager(), null);
    }

    public Activity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }

    @Override
    public void onColorChanged(int color) {
        setValue(color);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        setValue(restoreValue ? getPersistedInt(0xFF000000) : (Integer) defaultValue);
    }

    public int getValue() {
        return mValue;
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        int colorInt;
        String mHexDefaultValue = a.getString(index);
        if (mHexDefaultValue != null && mHexDefaultValue.startsWith("#")) {
            colorInt = Color.parseColor(mHexDefaultValue);
            return colorInt;

        } else {
            return a.getColor(index, Color.BLACK);
        }
        //return a.getInteger(index, 0xFF000000);
    }

    private static void setColorViewValue(View view, int color) {
        if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            Resources res = imageView.getContext().getResources();

            Drawable currentDrawable = imageView.getDrawable();
            GradientDrawable colorChoiceDrawable;
            if (currentDrawable != null && currentDrawable instanceof GradientDrawable) {
                // Reuse drawable
                colorChoiceDrawable = (GradientDrawable) currentDrawable;
            } else {
                colorChoiceDrawable = new GradientDrawable();
                colorChoiceDrawable.setShape(GradientDrawable.OVAL);
            }

            // Set stroke to dark version of color
            int darkenedColor = Color.rgb(
                    Color.red(color) * 192 / 256,
                    Color.green(color) * 192 / 256,
                    Color.blue(color) * 192 / 256);

            colorChoiceDrawable.setColor(color);
            colorChoiceDrawable.setStroke((int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 1, res.getDisplayMetrics()), darkenedColor);
            imageView.setImageDrawable(colorChoiceDrawable);

        } else if (view instanceof TextView) {
            ((TextView) view).setTextColor(color);
        }
    }
}
