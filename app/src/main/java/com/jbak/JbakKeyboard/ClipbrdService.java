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

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.ClipboardManager;
import com.jbak.ctrl.SameThreadTimer;

import java.util.Timer;


/**
 * Сервис для забора значений по таймеру
 */
public class ClipbrdService
        extends SameThreadTimer
{
    public ClipbrdService(Context c)
    {
        super(CLIPBRD_INTERVAL, CLIPBRD_INTERVAL);
        inst = this;
        m_cm = (ClipboardManager) c.getSystemService(Service.CLIPBOARD_SERVICE);
        IntentFilter filt = new IntentFilter();
        filt.addAction(Intent.ACTION_SCREEN_ON);
        filt.addAction(Intent.ACTION_SCREEN_OFF);
        c.registerReceiver(m_recv, filt);
        start();
    }


    static ClipbrdService inst;


    public void delete(Context c)
    {
        inst = null;
        c.unregisterReceiver(m_recv);
    }


    void checkClipboardString()
    {
        if (!m_cm.hasText()) {
            return;
        }
        checkString(m_cm.getText().toString());
    }


    void checkString(String str)
    {
        cancel();
        try {
            if (str.equals(m_sLastClipStr)) {
                return;
            }
            st.stor().checkClipboardString(str);
            m_sLastClipStr = str;
        } catch (Throwable e) {

        }
        start();
    }


    BroadcastReceiver m_recv = new BroadcastReceiver()
    {
        @Override
        public void onReceive(
                Context context,
                Intent intent)
        {
            String act = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(act)) {
                start();
            }
            if (Intent.ACTION_SCREEN_OFF.equals(act)) {
                cancel();
            }
        }
    };
    String m_sLastClipStr;
    /**
     * Интервал взятия значений из буфера обмена в милисекундах
     */
    public static final int CLIPBRD_INTERVAL = 5000;
    ClipboardManager m_cm;
    Timer            m_timer;


    @Override
    public void onTimer(SameThreadTimer timer)
    {
        checkClipboardString();
    }
}
