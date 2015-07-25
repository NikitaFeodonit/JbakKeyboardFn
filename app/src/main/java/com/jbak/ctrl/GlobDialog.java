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

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jbak.JbakKeyboard.st;


public class GlobDialog
{
    String         m_text;
    String         m_ok;
    String         m_cancel;
    Context        m_c;
    View           m_view;
    st.UniObserver m_obs;
    public static final int NO_FINISH = 1;


    public GlobDialog(Context c)
    {
        m_c = c;
    }


    View.OnClickListener m_clkListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (m_obs != null && m_obs.OnObserver(Integer.valueOf(v.getId()), this) != NO_FINISH) {
                finish();
            }
        }
    };
    View.OnKeyListener   m_keyListener = new View.OnKeyListener()
    {
        @Override
        public boolean onKey(
                View v,
                int keyCode,
                KeyEvent event)
        {
            return false;
        }
    };


    public View createView()
    {
        LinearLayout ll = new LinearLayout(m_c);
        ll.setBackgroundResource(android.R.drawable.dialog_frame);
        ll.setOnKeyListener(m_keyListener);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(lp);
        ll.setPadding(20, 20, 20, 20);
        if (m_text != null) {
            TextView tv = new TextView(m_c);
            tv.setPadding(20, 20, 20, 20);
            tv.setText(m_text);
            tv.setMinWidth(200);
            tv.setGravity(Gravity.CENTER);
            ll.addView(tv);
        }
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp1.setMargins(20, 20, 20, 20);
        LinearLayout butLayout = new LinearLayout(m_c);
        butLayout.setLayoutParams(lp1);
        butLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        butLayout.setOrientation(LinearLayout.HORIZONTAL);
        if (m_ok != null) {
            butLayout.addView(mkButton(m_ok, AlertDialog.BUTTON_POSITIVE));
        }
        if (m_cancel != null) {
            butLayout.addView(mkButton(m_cancel, AlertDialog.BUTTON_NEGATIVE));
        }
        ll.addView(butLayout);
        return ll;
    }


    Button mkButton(
            String text,
            int id)
    {
        Button b = new Button(m_c);
        b.setMinWidth(100);
        b.setText(text);
        b.setId(id);
        b.setOnClickListener(m_clkListener);
        return b;
    }


    public void onLayout(WindowManager.LayoutParams lp)
    {
    }


    public void showAlert()
    {
        WindowManager wm = (WindowManager) m_c.getSystemService(Service.WINDOW_SERVICE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.type = WindowManager.LayoutParams.TYPE_PHONE;
        lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.format = PixelFormat.TRANSLUCENT;
        lp.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                   WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lp.dimAmount = (float) 0.2;
        onLayout(lp);
        m_view = createView();
        wm.addView(m_view, lp);
    }


    public void finish()
    {
        WindowManager wm = (WindowManager) m_c.getSystemService(Service.WINDOW_SERVICE);
        wm.removeView(m_view);
    }


    public void set(
            String text,
            String ok,
            String cancel)
    {
        m_text = text;
        m_ok = ok;
        m_cancel = cancel;
    }


    public void set(
            int text,
            int ok,
            int cancel)
    {
        set(
                m_c.getString(text), ok == 0 ? null : m_c.getString(ok),
                cancel == 0 ? null : m_c.getString(cancel));
    }


    public void setObserver(st.UniObserver obs)
    {
        m_obs = obs;
    }
}
