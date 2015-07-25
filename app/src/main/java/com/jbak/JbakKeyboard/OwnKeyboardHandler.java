/*
 * Project:  Jbak Keyboard Mod
 * Purpose:  Keyboard for Android
 * Author:   Yuriy Bakunin (aka jtest), jbak1979@gmail.com, http://jbak.ru/jbakkeyboard/
 * Modification:  NikitaFeodonit, nfeodonit@yandex.com
 * *****************************************************************************
 * Copyright (C) 2007-2015 jbak.ru
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.jbak.JbakKeyboard;

import android.content.SharedPreferences;
import android.inputmethodservice.KeyboardView;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.widget.TextView;
import com.jbak.JbakKeyboard.JbKbd.LatinKey;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class OwnKeyboardHandler
        extends Handler
{
    int repeatInterval      = 0;
    int longPressInterval   = 500;
    int deltaLongPress      = 0;
    int deltaRepeatStart    = 0;
    int firstRepeatInterval = 400;
    public static final int MSG_SHOW_PREVIEW   = 1;
    public static final int MSG_REMOVE_PREVIEW = 2;
    public static final int MSG_REPEAT         = 3;
    public static final int MSG_LONGPRESS      = 4;
    public static final int MSG_INVALIDATE     = 5;
    public static final int MSG_MY_REPEAT      = 6;
    public static final int MSG_MY_LONG_PRESS  = 7;
    Handler   m_existHandler;
    TextView  m_PreviewText;
    Method    m_showKey;
    Method    m_repeatKey;
    Method    m_openPopupIfRequired;
    JbKbdView m_kv;
    public        boolean            m_bSuccessInit;
    public static OwnKeyboardHandler inst;


    public OwnKeyboardHandler(
            Handler exist,
            JbKbdView kv)
    {
        super();
        inst = this;
        m_kv = kv;
        m_existHandler = exist;
        m_bSuccessInit = init();
        loadFromSettings();
    }


    void loadFromSettings()
    {
        SharedPreferences p = st.pref(m_kv.getContext());
        longPressInterval = p.getInt(st.PREF_KEY_LONG_PRESS_INTERVAL, 500);
        deltaLongPress = longPressInterval >= 500 ? longPressInterval - 500 : 0;
        firstRepeatInterval = p.getInt(st.PREF_KEY_REPEAT_FIRST_INTERVAL, 400);
        deltaRepeatStart = firstRepeatInterval >= 400 ? firstRepeatInterval - 400 : 0;
        repeatInterval = p.getInt(st.PREF_KEY_REPEAT_NEXT_INTERVAL, 50);
    }


    boolean init()
    {
        try {
            m_showKey = KeyboardView.class.getDeclaredMethod("showKey", int.class);
            m_repeatKey = KeyboardView.class.getDeclaredMethod("repeatKey");
            m_openPopupIfRequired =
                    KeyboardView.class.getDeclaredMethod("openPopupIfRequired", MotionEvent.class);
            m_openPopupIfRequired.setAccessible(true);
            m_repeatKey.setAccessible(true);
            m_showKey.setAccessible(true);
            Field f = KeyboardView.class.getDeclaredField("mPreviewText");
            f.setAccessible(true);
            m_PreviewText = (TextView) f.get(m_kv);
            return true;
        } catch (Throwable e) {
        }
        return false;
    }


    void invokeShowKey(int key)
    {
        try {
            if (m_showKey != null) {
                m_showKey.invoke(m_kv, key);
            }
        } catch (Throwable e) {
            st.logEx(e);
        }
    }


    @Override
    public void handleMessage(Message msg)
    {
        try {
            switch (msg.what) {
                case MSG_INVALIDATE:
                    m_kv.trueInvalidateKey(msg.arg1);
                    break;
//                case MSG_SHOW_PREVIEW:
//                    invokeShowKey(msg.arg1);
//                    break;
//                case MSG_REMOVE_PREVIEW:
//                    if(m_PreviewText!=null)
//                        m_PreviewText.setVisibility(View.INVISIBLE);
//                    break;
                case MSG_REPEAT:
                    break;
                case MSG_MY_REPEAT: {
                    LatinKey lk = (LatinKey) msg.obj;
                    if (lk == null || !lk.pressed || !m_kv.getCurKeyboard().hasKey(lk)) {
                        return;
                    }
                    lk.processed = true;
                    m_kv.onKeyRepeat(lk);
                    sendRepeat(lk, false);
                }
                break;
                case MSG_LONGPRESS:
//                    if(deltaLongPress>0&&msg.arg1==0)
//                    {
//                        sendMessageDelayed(obtainMessage(MSG_LONGPRESS,1,1, msg.obj),deltaLongPress);
//                        return;
//                    }
//                    if(m_openPopupIfRequired!=null)
//                    {
//                        m_openPopupIfRequired.invoke(m_kv, (MotionEvent) msg.obj);
//                    }
                    break;
                case MSG_MY_LONG_PRESS: {
                    LatinKey lk = (LatinKey) msg.obj;
                    if (lk != null && lk.pressed) {
                        lk.processed = true;
                        m_kv.onLongPress(lk);
                    }
                }
                break;
            }
        } catch (Throwable e) {
            st.logEx(e);
        }
    }


    public final void sendRepeat(
            LatinKey k,
            boolean bFirst)
    {
        sendMessageDelayed(
                obtainMessage(MSG_MY_REPEAT, k), bFirst ? firstRepeatInterval : repeatInterval);
    }


    public final void sendLongPress(LatinKey k)
    {
        sendMessageDelayed(obtainMessage(MSG_MY_LONG_PRESS, k), longPressInterval);
    }
}
