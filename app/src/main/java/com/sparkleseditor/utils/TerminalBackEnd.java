package com.sparkleseditor.utils;

import static com.blankj.utilcode.util.KeyboardUtils.showSoftInput;

import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.sparkleseditor.components.virtualkeys.VirtualKeysView;
import com.sparkleseditor.databinding.FragmentTerminalBinding;
import com.sparkleseditor.fragments.TerminalFragment;
import com.sparkleseditor.components.virtualkeys.SpecialButton;
import com.termux.terminal.TerminalEmulator;
import com.termux.terminal.TerminalSession;
import com.termux.terminal.TerminalSessionClient;
import com.termux.view.TerminalView;
import com.termux.view.TerminalViewClient;

public class TerminalBackEnd implements TerminalViewClient, TerminalSessionClient {

  private int fontSize = SizeUtils.dp2px(24f);
  private TerminalView terminal;
  private final TerminalFragment fragment;

  private VirtualKeysView extraKeysView;

  public void setExtraKeysView(VirtualKeysView view) {
    this.extraKeysView = view;
  }

  public TerminalBackEnd(Fragment fragment) {
      this.fragment = new TerminalFragment();
  }

  public void setTerminal(TerminalView terminalView) {
    this.terminal = terminalView;
  }

  @Override
  public void onTextChanged(TerminalSession changedSession) {
    terminal.onScreenUpdated();
  }

  @Override
  public void onTitleChanged(TerminalSession changedSession) {}

  @Override
  public void onSessionFinished(TerminalSession finishedSession) {}

  @Override
  public void onCopyTextToClipboard(TerminalSession session, String text) {
    ClipboardUtils.copyText("Terminal", text);
  }

  @Override
  public void onPasteTextFromClipboard(TerminalSession session) {
    String clip = ClipboardUtils.getText().toString();
    if (!clip.trim().isEmpty() && terminal.mEmulator != null) {
      terminal.mEmulator.paste(clip);
    }
  }

  @Override
  public void onBell(TerminalSession session) {}

  @Override
  public void onColorsChanged(TerminalSession session) {}

  @Override
  public void onTerminalCursorStateChange(boolean state) {}

  @Override
  public Integer getTerminalCursorStyle() {
    return TerminalEmulator.DEFAULT_TERMINAL_CURSOR_STYLE;
  }

  @Override
  public void logError(String tag, String message) {
    Log.e(tag, message);
  }

  @Override
  public void logWarn(String tag, String message) {
    Log.w(tag, message);
  }

  @Override
  public void logInfo(String tag, String message) {
    Log.i(tag, message);
  }

  @Override
  public void logDebug(String tag, String message) {
    Log.d(tag, message);
  }

  @Override
  public void logVerbose(String tag, String message) {
    Log.v(tag, message);
  }

  @Override
  public void logStackTraceWithMessage(String tag, String message, Exception e) {
    Log.e(tag, message);
    if (e != null) {
      e.printStackTrace();
    }
  }

  @Override
  public void logStackTrace(String tag, Exception e) {
    if (e != null) {
      e.printStackTrace();
    }
  }

  @Override
  public float onScale(float scale) {
    return fontSize;
  }

  @Override
  public void onSingleTapUp(MotionEvent e) {
    showSoftInput();
  }

  @Override
  public boolean shouldBackButtonBeMappedToEscape() {
    return false;
  }

  @Override
  public boolean shouldEnforceCharBasedInput() {
    return true;
  }

  @Override
  public boolean shouldUseCtrlSpaceWorkaround() {
    return false;
  }

  @Override
  public boolean isTerminalViewSelected() {
    return true;
  }

  @Override
  public void copyModeChanged(boolean copyMode) {}

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent e, TerminalSession session) {
    if (extraKeysView == null) {
      return false ;
    }
    if (keyCode == KeyEvent.KEYCODE_ENTER && !session.isRunning()) {
      fragment.requireActivity().finish();
      return true;
    }
    return false;
  }

  @Override
  public boolean onKeyUp(int keyCode, KeyEvent e) {
    return false;
  }

  @Override
  public boolean onLongPress(MotionEvent event) {
    return false;
  }

  @Override
  public boolean readControlKey() {

    if (extraKeysView == null) {
      return false ;
    }


    Boolean state = fragment.binding.extraKeysView.readSpecialButton(SpecialButton.CTRL, true);
    return state != null && state;
  }

  @Override
  public boolean readAltKey() {

    if (extraKeysView == null) {
      return false ;
    }

    Boolean state = fragment.binding.extraKeysView.readSpecialButton(SpecialButton.ALT, true);
    return state != null && state;
  }

  @Override
  public boolean readShiftKey() {
    if (extraKeysView == null) {
      return false ;
    }

    Boolean state = fragment.binding.extraKeysView.readSpecialButton(SpecialButton.SHIFT, true);
    return state != null && state;
  }

  @Override
  public boolean readFnKey() {

    if (extraKeysView == null) {
      return false ;
    }

    Boolean state = fragment.binding.extraKeysView.readSpecialButton(SpecialButton.FN, true);
    return state != null && state;
  }

  @Override
  public boolean onCodePoint(int codePoint, boolean ctrlDown, TerminalSession session) {
    return false;
  }

  @Override
  public void onEmulatorSet() {
    setTerminalCursorBlinkingState(true);
  }

  private void setTerminalCursorBlinkingState(boolean start) {
    if (terminal.mEmulator != null) {
      terminal.setTerminalCursorBlinkerState(start, true);
    }
  }

  private void showSoftInput() {
    terminal.requestFocus();
    KeyboardUtils.showSoftInput(terminal);
  }
}
