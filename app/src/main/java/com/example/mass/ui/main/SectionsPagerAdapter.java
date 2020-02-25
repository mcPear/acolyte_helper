package com.example.mass.ui.main;

import android.content.Context;
import android.content.res.Resources;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.example.mass.R;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private String[] preparationSteps;
    private String[] cleanupSteps;
    private final Context mContext;
    private final Resources res;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        res = context.getResources();
        preparationSteps = res.getStringArray(R.array.przygotowanie_lista);
        cleanupSteps = res.getStringArray(R.array.sprzatanie_lista);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        String[] steps = getSteps(position);
        return ChecklistFragment.newInstance(mContext, steps, position);
    }

    private String[] getSteps(int position) {
        if (position == 0) return preparationSteps;
        else return cleanupSteps;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return res.getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}