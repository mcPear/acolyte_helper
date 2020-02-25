package com.example.mass.ui.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import com.example.mass.R;
import android.content.Context;
import com.example.mass.ui.main.util.ObjectSerializer;
import com.example.mass.ui.main.util.SharedPrefsProvider;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChecklistFragment extends Fragment {

    private final static int FAB_MARGIN = 16;
    private final static String RESET_TEXT = "Zresetowano";
    private final static String CHECKS_PREFS_KEY = "CHECKS";
    private Context mContext;
    private List<CheckBox> checkBoxes;
    private List<View> dividers;
    private View fab;
    private LinearLayout linearLayout;
    private CoordinatorLayout coordinatorLayout;
    private SharedPreferences mPrefs;
    private int index;

    public static ChecklistFragment newInstance(Context mContext, String[] steps, int index) {
        ChecklistFragment fragment = new ChecklistFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        fragment.mContext = mContext;
        fragment.checkBoxes = createCheckBoxes(mContext, steps);
        fragment.dividers = createDividers(mContext, steps.length);
        fragment.index = index;
        fragment.fab = createFab(mContext);
        return fragment;
    }

    private static List<CheckBox> createCheckBoxes(Context mContext, String[] steps) {
        List<CheckBox> checkBoxes = new ArrayList<>();
        for (String step : steps) {
            CheckBox checkBox = new CheckBox(mContext);
            checkBox.setText(step);
            checkBoxes.add(checkBox);
        }
        return checkBoxes;
    }

    private static List<View> createDividers(Context mContext, int count) {
        List<View> dividers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            LayoutInflater li = LayoutInflater.from(mContext);
            View divider = li.inflate(R.layout.divider, null);
            dividers.add(divider);
        }
        return dividers;
    }

    private static View createFab(Context mContext) {
        LayoutInflater li = LayoutInflater.from(mContext);
        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(
                CoordinatorLayout.LayoutParams.WRAP_CONTENT,
                CoordinatorLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.END;
        params.setMargins(FAB_MARGIN, FAB_MARGIN, FAB_MARGIN, FAB_MARGIN);

        View fab = li.inflate(R.layout.fab, null);
        fab.setLayoutParams(params);
        return fab;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        handleDynamicLayoutOnCreateView(root);
        return root;
    }

    private void handleDynamicLayoutOnCreateView(View root) {
        removeDynamicViews();
        setDynamicLayouts(root);
        addCheckBoxesToDynamicLayout();
        addFabToDynamicLayout();
    }

    private void removeDynamicViews() {
        if (linearLayout != null) {
            linearLayout.removeAllViews();
        }
        if (coordinatorLayout != null) {
            coordinatorLayout.removeView(fab);
        }
    }

    private void setDynamicLayouts(View root) {
        linearLayout = root.findViewById(R.id.linearLayout);
        coordinatorLayout = root.findViewById(R.id.coordinatorLayout);
    }

    private void addCheckBoxesToDynamicLayout() {
        for (int i = 0; i < checkBoxes.size(); i++) {
            linearLayout.addView(checkBoxes.get(i));
            linearLayout.addView(dividers.get(i));
        }
    }

    private void addFabToDynamicLayout() {
        fab.setOnClickListener(view -> {
            resetCheckBoxes();
            Snackbar.make(view, RESET_TEXT, Snackbar.LENGTH_LONG).show();
        });

        coordinatorLayout.addView(fab);
    }

    public void resetCheckBoxes() {
        for (CheckBox e : checkBoxes) {
            e.setChecked(false);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mPrefs = SharedPrefsProvider.get(mContext);
    }

    @Override
    public void onResume() {
        super.onResume();
        restoreChecks();
    }

    private void restoreChecks() {
        ArrayList<Boolean> checks = readChecks();

        for (int i = 0; i < checks.size(); i++) {
            CheckBox cb = checkBoxes.get(i);
            cb.setChecked(checks.get(i));
            cb.jumpDrawablesToCurrentState();
        }
    }

    private ArrayList<Boolean> readChecks() {
        try {
            String serializedEmptyArray = ObjectSerializer.serialize(new ArrayList<Boolean>());
            String serializedChecks = mPrefs.getString(CHECKS_PREFS_KEY + index, serializedEmptyArray);
            return (ArrayList<Boolean>) ObjectSerializer.deserialize(serializedChecks);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        storeChecks();
    }

    private void storeChecks() {
        ArrayList<Boolean> checks = checkBoxes.stream().map(CheckBox::isChecked).collect(Collectors.toCollection(ArrayList::new));
        SharedPreferences.Editor editor = mPrefs.edit();
        try {
            String serializedChecks = ObjectSerializer.serialize(checks);
            editor.putString(CHECKS_PREFS_KEY + index, serializedChecks);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}