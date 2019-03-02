package com.raymondctc.udacity.popularmovies.di;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MovieAppViewModelFactory implements ViewModelProvider.Factory {

    @Inject
    public Map<Class<? super ViewModel>, Provider<ViewModel>> creators;

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {

            Map.Entry<Class<? super ViewModel>, Provider<ViewModel>> found = null;
            for (Map.Entry<Class<? super ViewModel>, Provider<ViewModel>> classProviderEntry : creators.entrySet()) {
                boolean isAssignable = modelClass.isAssignableFrom(classProviderEntry.getKey());
                if (isAssignable) {
                    found = classProviderEntry;
                    break;
                }
            }

            if (found == null || found.getValue() == null)
                throw new IllegalArgumentException("unknown model class " + modelClass);

            return (T) found.getValue();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
