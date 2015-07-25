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

import android.app.ProgressDialog;
import android.content.Context;
import com.jbak.JbakKeyboard.st;
import com.jbak.JbakKeyboard.st.UniObserver;


/**
 * Класс для вывода прогресс-бара в долгоиграющих операциях
 */
public abstract class ProgressOperation
        extends st.SyncAsycOper
{
    /**
     * Диалог прогресс-бара
     */
    public ProgressDialog m_progress;
    public int     m_position = 0;
    public int     m_total    = 0;
    public boolean m_bCancel  = false;
    SameThreadTimer m_tt;


    public ProgressOperation(
            UniObserver obs,
            Context c)
    {
        super(obs);
        m_progress = new ProgressDialog(c);
        m_progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        m_progress.setMax(100);
        m_progress.setIndeterminate(false);
        m_progress.setTitle("");
        m_progress.setMessage("");
        m_tt = new SameThreadTimer(0, 500)
        {
            @Override
            public void onTimer(SameThreadTimer timer)
            {
                if (m_progress == null) {
                    cancel();
                    return;
                }
                onProgress();
            }
        };
    }


    public void start()
    {
        startAsync();
        m_progress.show();
        m_tt.start();
    }


    public int getPercent()
    {
        if (m_total == 0) {
            return 0;
        }
        long pc = m_position;
        return (int) pc * 100 / m_total;
    }


    @Override
    protected void onProgressUpdate(Void... values)
    {
        if (m_progress != null) {
            m_progress.dismiss();
            m_progress = null;
        }
        super.onProgressUpdate(values);
    }


    ;


    /**
     * Функция вызывается, когда необходимо перерисовать прогресс-бар (по таймеру)
     */
    public abstract void onProgress();
}
