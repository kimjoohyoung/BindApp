package com.jhkim.bindlibrary.preference.color;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import androidx.fragment.app.DialogFragment;
import com.jhkim.bindlibrary.R;

public class SimpleColorDialogFragment extends DialogFragment {
    private final static String TITLE="TITLE";
    private final static String COLOR="COLOR";
    private final static String COLOR_VALUES_RES_ID="COLOR_VALUES_RES_ID";
    private final static String MORE_RES_ID="MORE_RES_ID";
    GridView mGridView;
    Button mButtonMore;

    int color;
    String title="색상 선택";

    int selectedColorIndex=-1;

    private Integer[] mColors;
    private ColorPickerDialog.OnColorChangedListener mListener;
    private ArrayAdapter<Integer> adapter;

    private DialogInterface.OnDismissListener onDismissListener;
    private int colorValuesId;

    public static SimpleColorDialogFragment create(int colorValuesResId, int color, int moreResId){
        Bundle b=new Bundle();
        b.putInt(COLOR, color);
        b.putInt(COLOR_VALUES_RES_ID, colorValuesResId);
        b.putInt(MORE_RES_ID, moreResId);
        SimpleColorDialogFragment f=new SimpleColorDialogFragment();
        f.setArguments(b);
        return f;
    }

    public SimpleColorDialogFragment(){
        //setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_color_dialog, container, false);
        mGridView= v.findViewById(R.id.gridView);
        mButtonMore= v.findViewById(R.id.buttonMore);
        Bundle b=getArguments();
        if(b!=null){
            title=b.getString(TITLE, "색상선택");
            color=b.getInt(COLOR, 0xff000000);
            colorValuesId = b.getInt(COLOR_VALUES_RES_ID, R.array.default_color_choice_values);
            int moreResId = b.getInt(MORE_RES_ID, R.string.more);
            mButtonMore.setText(moreResId);
        }
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onAfterView();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if(onDismissListener!=null)
            onDismissListener.onDismiss(dialog);
        super.onDismiss(dialog);
    }

    void onAfterView(){
        getView().setBackgroundColor(0xC0444444);
        getDialog().setTitle(title);
        //String[] colors = getResources().getStringArray(R.array.default_color_choice_values);
        TypedArray colors= getResources().obtainTypedArray(colorValuesId);
        mColors=new Integer[colors.length()];
        for(int i=0;i<colors.length();++i){
            mColors[i]=colors.getColor(i,0);
            if(mColors[i]==color){
                selectedColorIndex=i;
            }
        }
        colors.recycle();

        adapter=new ArrayAdapter<Integer>(getActivity(), 0, mColors){
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                CircleButton btn= (CircleButton) convertView;
                if(convertView==null){
                    btn=new CircleButton(getContext());
                    //convertView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    btn.setLayoutParams(new ViewGroup.LayoutParams(mGridView.getColumnWidth(), mGridView.getColumnWidth()));
                    btn.setClickable(false);
                    btn.setFocusable(false);
                    convertView=btn;
                }
                if(selectedColorIndex==position)
                    btn.setImageResource(R.drawable.ic_action_tick);
                else
                    btn.setImageResource(0);
                btn.setColor(getItem(position));
                //convertView.setBackgroundDrawable(new ColorStateDrawable(new Drawable[]{convertView.getBackground()}, getItem(position)));
                return btn;
            }
        };

        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null)
                    mListener.onColorChanged(adapter.getItem(position));
                SimpleColorDialogFragment.this.dismiss();
            }
        });
        if(selectedColorIndex>=0)
            mGridView.setSelection(selectedColorIndex);
        mButtonMore.setOnClickListener(v1 -> {
            final ColorPickerDialog dialog = new ColorPickerDialog(SimpleColorDialogFragment.this.getActivity(), color);
            dialog.setOnColorChangedListener(color -> {
                if (mListener != null)
                    mListener.onColorChanged(color);
                dialog.dismiss();
                SimpleColorDialogFragment.this.dismiss();
            });
            dialog.show();
        });
    }

    public ColorPickerDialog.OnColorChangedListener getOnColorChangedListener() {
        return mListener;
    }

    public void setOnColorChangedListener(ColorPickerDialog.OnColorChangedListener listener) {
        this.mListener = listener;
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }
}
