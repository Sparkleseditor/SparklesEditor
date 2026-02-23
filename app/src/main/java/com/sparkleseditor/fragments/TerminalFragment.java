package com.sparkleseditor.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.SizeUtils;
import com.sparkleseditor.R;
import com.sparkleseditor.components.virtualkeys.VirtualKeysConstants;
import com.sparkleseditor.components.virtualkeys.VirtualKeysInfo;
import com.sparkleseditor.databinding.FragmentTerminalBinding;
import com.sparkleseditor.utils.TerminalBackEnd;
import com.sparkleseditor.components.virtualkeys.VirtualKeysListener;
import com.termux.terminal.TerminalEmulator;
import com.termux.terminal.TerminalSession;
import com.termux.view.TerminalView;
import java.io.File;

import org.json.JSONException;

public class TerminalFragment extends BaseFragment {

    public FragmentTerminalBinding binding;
    private TerminalBackEnd backEnd;

    private static final String VIRTUAL_KEYS = """
    [
        ["ESC", {"key":"/","popup":"\\\\"}, {"key":"-","popup":"|"}, "HOME", "UP", "END", "PGUP"],
        ["TAB", "CTRL", "ALT", "LEFT", "DOWN", "RIGHT", "PGDN"]
    ]
    """.trim();
    private TerminalView terminal;


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentTerminalBinding.inflate(inflater, container, false);
        backEnd = new TerminalBackEnd(new TerminalFragment());
        setupTerminalView();
        try {
            setupVirtualKeys();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        return binding.getRoot();
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void setupVirtualKeys() throws JSONException {

        if (binding.extraKeysView == null) {
            android.util.Log.e("TerminalFragment", "extraKeysView is null — check fragment_terminal.xml");
            return;
        }
        binding.extraKeysView.setVirtualKeysViewClient(new VirtualKeysListener(terminal.mTermSession));
        binding.extraKeysView.reload(new VirtualKeysInfo(VIRTUAL_KEYS, "", VirtualKeysConstants.CONTROL_CHARS_ALIASES));
    }

    private void setupTerminalView() {
        setupToolbar();
        terminal = new TerminalView(requireContext(), null);
        backEnd.setTerminal(terminal);
        terminal.setTerminalViewClient(backEnd);
        TerminalSession session = createSession();
        terminal.attachSession(session);
        terminal.setKeepScreenOn(true);
        terminal.requestFocus();
        terminal.setTextSize(SizeUtils.dp2px(14f));
        terminal.setFocusableInTouchMode(true);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        params.weight = 1f;
        binding.getRoot().addView(terminal, 1, params);

    }

    private void setupToolbar() {
        com.google.android.material.appbar.MaterialToolbar toolbar = new com.google.android.material.appbar.MaterialToolbar(requireContext());
        toolbar.setTitle("Terminal");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_ios_new_rounded_24);
        toolbar.setNavigationOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        LinearLayout.LayoutParams toolbarParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                (int) (56 * getResources().getDisplayMetrics().density)
        );
        binding.getRoot().addView(toolbar, 0, toolbarParams);

    }

    private TerminalSession createSession() {

        String[] env = {
                "HOME=" + requireContext().getFilesDir().getAbsolutePath(),
                "PUBLIC_HOME=" + requireContext().getExternalFilesDir(null).getAbsolutePath(),
                "COLORTERM=truecolor",
                "TERM=xterm-256color"
        };

        String shell = "/system/bin/sh";
        String[] args = new String[] {};

        return new TerminalSession(
                shell,
                requireContext().getFilesDir().getAbsolutePath(),
                args,
                env,
                TerminalEmulator.DEFAULT_TERMINAL_TRANSCRIPT_ROWS,
                backEnd);

    }



    @Override
    public void onDestroyView() {
        if (terminal != null && terminal.mTermSession != null) {
            terminal.mTermSession.finishIfRunning();
        }
        super.onDestroyView();
        binding = null;
    }

}