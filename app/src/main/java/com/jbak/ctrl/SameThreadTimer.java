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

package com.jbak.ctrl;

import android.os.Handler;


/**
 * Таймер, который запускается всегда в том же потоке, в котором он был создан
 */
public abstract class SameThreadTimer
{
    public int m_delay;
    public int m_period;
    Handler m_h = new Handler()
    {
        @Override
        public void handleMessage(android.os.Message msg)
        {
            callTimer();
        }


        ;
    };


    final void callTimer()
    {
        onTimer(this);
        if (m_period != 0) {
            m_h.sendMessageDelayed(m_h.obtainMessage(1), m_period);
        }
    }


    public SameThreadTimer(
            int delay,
            int period)
    {
        m_delay = delay;
        m_period = period;
    }


    public void start()
    {
        m_h.sendMessageDelayed(m_h.obtainMessage(1), m_delay);
    }


    public void cancel()
    {
        m_h.removeMessages(1);
    }


    public abstract void onTimer(SameThreadTimer timer);
}
