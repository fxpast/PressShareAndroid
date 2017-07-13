package com.prv.pressshare.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.prv.pressshare.R;

public class FormFragment extends Fragment {

    private TextView mTitle;
    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.info_window_form_fragment, container, false);
        mTitle = (TextView) view.findViewById(R.id.editTextFormInfoWindow);
        String title = mTitle.getText() +  getArguments().getString("title");
        mTitle.setText(title);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Well I don't have any...", Toast.LENGTH_SHORT).show();
            }
        };

        view.findViewById(R.id.bt_ok).setOnClickListener(onClickListener);
        view.findViewById(R.id.bt_cancel).setOnClickListener(onClickListener);
    }
}
