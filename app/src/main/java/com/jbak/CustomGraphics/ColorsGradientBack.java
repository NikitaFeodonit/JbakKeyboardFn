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

package com.jbak.CustomGraphics;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;


public class ColorsGradientBack
        extends GradBack
{
    public int colors[] = new int[] {
            Color.GREEN, Color.CYAN, Color.YELLOW, Color.BLUE, Color.MAGENTA,};


    public ColorsGradientBack set(int colors[])
    {
        this.colors = colors;
        refresh();
        return this;
    }


    public void refresh()
    {
        m_ptFill = makeBackground(getWidth(), getHeight());
    }


    @Override
    protected Paint makeBackground(
            float width,
            float height)
    {
        Paint pt = newPaint();
        LinearGradient grad =
                new LinearGradient(0, 0, width, height, colors, null, TileMode.REPEAT);
//        SweepGradient grad = new SweepGradient(0, height, 
//                colors,
//                null);
        pt.setShader(grad);
        return pt;
    }


    public static class RotatedColorsBackground
    {
        ColorsGradientBack m_back;
        View               m_view;
        Timer              tm;
        Drawable           m_drw;
        Handler            m_refresh;


        public void set(
                View v,
                ColorsGradientBack back)
        {
            m_refresh = new Handler();
            m_view = v;
            m_back = back;
            m_drw = back.getStateDrawable();
            m_view.setBackgroundDrawable(m_drw);
            tm = new Timer();
            tm.schedule(
                    new TimerTask()
                    {

                        @Override
                        public void run()
                        {
                            m_refresh.post(
                                    new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            int c = m_back.colors[0];
                                            for (int i = 1; i < m_back.colors.length; i++) {
                                                m_back.colors[i - 1] = m_back.colors[i];
                                            }
                                            m_back.colors[m_back.colors.length - 1] = c;
                                            m_back.refresh();
                                            m_drw.invalidateSelf();
                                        }
                                    });
                        }
                    }, 600, 600);
        }


        public void destroy()
        {
            if (tm != null) {
                tm.cancel();
            }
        }

    }
}
