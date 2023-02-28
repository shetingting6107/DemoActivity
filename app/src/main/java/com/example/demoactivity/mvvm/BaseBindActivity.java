package com.example.demoactivity.mvvm;

import androidx.fragment.app.FragmentActivity;

public abstract class BaseBindActivity extends FragmentActivity {

    protected abstract void initViewModel();

    protected abstract DataBindingConfig getDataBindingConfig();


}
