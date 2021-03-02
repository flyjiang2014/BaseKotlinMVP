package com.kotlin.mvp.dagger.module;
import android.app.Activity;
import com.kotlin.mvp.dagger.scope.FragmentScope;
import androidx.fragment.app.Fragment;
import dagger.Module;
import dagger.Provides;

/**
 * Created by flyjiang on 2019/8/2.
 */

@Module
public class FragmentModule {

    private Fragment fragment;

    public FragmentModule(Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @FragmentScope
    public Activity provideActivity() {
        return fragment.getActivity();
    }
}
