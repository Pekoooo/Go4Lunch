package com.example.go4lunch.ui.CoworkerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.go4lunch.databinding.FragmentCoworkersBinding;
import com.example.go4lunch.model.AppModel.User;
import com.example.go4lunch.viewmodel.ViewModelCoworkerList;
import com.google.android.material.divider.MaterialDividerItemDecoration;

import java.util.List;


public class CoworkersFragment extends Fragment {
    private static final String TAG = "MyCoworkersFragment";
    private ViewModelCoworkerList viewModel;
    private FragmentCoworkersBinding binding;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(ViewModelCoworkerList.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCoworkersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getAllCoworkers().observe(getViewLifecycleOwner(), this::setRecyclerView);
    }

    private void setRecyclerView(List<User> users) {
        CoworkersListViewAdapter adapter = new CoworkersListViewAdapter(users);
        binding.coworkerRecyclerview.setAdapter(adapter);
        binding.coworkerRecyclerview.setHasFixedSize(true);
        binding.coworkerRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        MaterialDividerItemDecoration materialDividerItemDecoration = new MaterialDividerItemDecoration(requireContext(), MaterialDividerItemDecoration.VERTICAL);
        materialDividerItemDecoration.setDividerInsetStart(265);
        binding.coworkerRecyclerview.addItemDecoration(materialDividerItemDecoration);

    }
}