package com.example.mass.ui.main;

import android.content.SharedPreferences;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import com.example.mass.R;
import com.example.mass.ui.main.util.SharedPrefsProvider;
import com.google.android.material.tabs.TabLayout;

public class ViewPagerConfigurator {
    private static final String SELECTED_PAGE_PREFS = "selected_page";

    public static void setup(FragmentActivity activity) {
        SectionsPagerAdapter sectionsPagerAdapter = getAdapter(activity);
        ViewPager viewPager = getViewPager(activity, sectionsPagerAdapter);
        setupTabs(activity, viewPager);
    }

    private static SectionsPagerAdapter getAdapter(FragmentActivity activity) {
        return new SectionsPagerAdapter(activity, activity.getSupportFragmentManager());
    }

    private static ViewPager getViewPager(FragmentActivity activity, SectionsPagerAdapter sectionsPagerAdapter) {
        ViewPager viewPager = activity.findViewById(R.id.view_pager);
        SharedPreferences mPrefs = SharedPrefsProvider.get(activity);
        setViewPagerListener(viewPager, mPrefs);
        selectLastSelectedPage(viewPager, mPrefs);
        viewPager.setAdapter(sectionsPagerAdapter);
        return viewPager;
    }

    private static void setupTabs(FragmentActivity activity, ViewPager viewPager) {
        TabLayout tabs = activity.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    private static void setViewPagerListener(ViewPager viewPager, SharedPreferences mPrefs) {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                SharedPreferences.Editor editor = mPrefs.edit();
                editor.putInt(SELECTED_PAGE_PREFS, position);
                editor.commit();
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private static void selectLastSelectedPage(ViewPager viewPager, SharedPreferences mPrefs) {
        int lastSelectedPage = mPrefs.getInt(SELECTED_PAGE_PREFS, 0);
        viewPager.setCurrentItem(lastSelectedPage, true);
    }

}
