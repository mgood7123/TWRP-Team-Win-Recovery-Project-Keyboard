package com.example.twrpkeyboard;

import android.app.Dialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.os.IBinder;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.MetaKeyKeyListener;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.*;

import java.util.List;

public class Team_Win_Recovery_Project_Keyboard extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private KeyboardView kv;
    private Keyboard keyboard;

    private boolean shift = false;
    private boolean caps = false;
    private boolean any_other_key = false;
    private boolean ctrl = false;
    private boolean alt = false;

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        playClick(primaryCode);
        InputConnection ic = getCurrentInputConnection();
        int cursorPosition = ic.getTextBeforeCursor(Integer.MAX_VALUE, 0).length();
        CharSequence s = ic.getTextBeforeCursor(1, 0);
        CharSequence e = ic.getTextAfterCursor(1, 0);
        switch(primaryCode){
            case TWRP.keys.UP:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_UP));
                break;
            case TWRP.keys.RIGHT:
                if (!TextUtils.isEmpty(e)) ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_RIGHT));
                break;
            case TWRP.keys.DOWN:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_DOWN));
                break;
            case TWRP.keys.LEFT:
                if (!TextUtils.isEmpty(s)) ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_LEFT));
                break;
            case TWRP.keys.CTRL:
                ctrl = !ctrl;
                break;
            case TWRP.keys.ALT:
                alt = !alt;
                break;
            case TWRP.keys.TAB:
                ic.commitText("    ",1);
                break;
            case Keyboard.KEYCODE_MODE_CHANGE :
                break;
            case Keyboard.KEYCODE_DELETE :
                CharSequence selectedText = ic.getSelectedText(0);

                if (TextUtils.isEmpty(selectedText)) {
                    ic.deleteSurroundingText(1, 0);
                } else {
                    ic.commitText("", 1);
                }
                break;
            case Keyboard.KEYCODE_SHIFT:
                caps = !caps;
//                if (caps) {
//                    keyboard = new Keyboard(this, R.xml.qwerty);
//                    kv.setKeyboard(keyboard);
//                } else {
//                    keyboard = new Keyboard(this, R.xml.qwerty);
//                    kv.setKeyboard(keyboard);
//                }
                keyboard.setShifted(caps);
                kv.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            default:
                char code = (char)primaryCode;
                if (ctrl && !alt) {
                    if (code == 'v')
                        ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_PASTE));
                    else if (code == 'c')
                        ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_COPY));
                    else if (code == 'x')
                        ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_CUT));
                    else if (code == 'l') {
                        ic.performContextMenuAction(android.R.id.selectAll);
                        ic.commitText("", 1);
                    }
                    else if (code == 'a') {
                        if (!caps) ic.setSelection(0, 0);
                        else ic.performContextMenuAction(android.R.id.selectAll);
                    }
                    else if (code == 'e') {
                        ExtractedText extractedText = ic.getExtractedText(new ExtractedTextRequest(), 0);
                        if (extractedText != null) {
                            if (extractedText.text != null) {
                                int index = extractedText.text.length();
                                ic.setSelection(index, index);
                            }
                        }
                    }
                    break;
                }
                if (alt && !ctrl) {
                    if ((int)code == TWRP.keys.TAB)
                        ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_APP_SWITCH));
                    break;
                }
                if(Character.isLetter(code) && caps){
                    code = Character.toUpperCase(code);
                }
                ic.commitText(String.valueOf(code),1);
        }
    }

    @Override
    public void onPress(int primaryCode) {
    }

    @Override
    public void onRelease(int primaryCode) {
    }

    @Override
    public void onText(CharSequence text) {
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeUp() {
    }

    @Override
    public View onCreateInputView() {
        kv = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this, R.xml.qwerty);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        return kv;
    }

    private void playClick(int keyCode){
        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
        switch(keyCode){
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case Keyboard.KEYCODE_DONE:
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default: am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }

//    @Override
//    public void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//
//        Paint paint = new Paint();
//        paint.setTextAlign(Paint.Align.CENTER);
//        paint.setTextSize(25);
//        paint.setColor(Color.RED);
//
//        List<Keyboard.Key> keys = kv.getKeyboard().getKeys();
//        for(Keyboard.Key key: keys) {
//            if(key.label != null)
//                canvas.drawText(key.label.toString(), key.x + (key.width/2), key.y + 25, paint);
//        }
//    }
}


