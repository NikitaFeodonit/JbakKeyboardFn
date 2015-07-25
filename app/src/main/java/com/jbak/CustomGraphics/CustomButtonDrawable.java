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

import android.R;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;


public class CustomButtonDrawable
        extends StateListDrawable
{
    GradBack m_grad;
    Drawable m_drw;
    /**
     * Объект, зависящий от текущего, получает уведомления при изменении размеров и статусов
     */
    StateListDrawable m_dependedDrawable;


    public CustomButtonDrawable()
    {
        m_grad = new GradBack();
    }


    public CustomButtonDrawable(GradBack gb)
    {
        m_grad = gb;
        m_drw = m_grad.getDrawable();
        addState(new int[] {}, m_drw);
        addState(new int[] {R.attr.state_checkable}, m_drw);
        addState(new int[] {R.attr.state_checkable, R.attr.state_checked}, m_drw);
        addState(new int[] {R.attr.state_checkable, R.attr.state_pressed}, m_drw);
    }


    public CustomButtonDrawable setCorners(
            int radiusX,
            int radiusY)
    {
        m_grad.setCorners(radiusX, radiusY);
        return this;
    }


    @Override
    public boolean selectDrawable(int idx)
    {
        return true;
    }


    @Override
    protected boolean onStateChange(int[] stateSet)
    {
        if (m_dependedDrawable != null) {
            m_dependedDrawable.setState(stateSet);
        }
        if (m_grad != null) {
            m_grad.changeState(stateSet);
        }
        super.onStateChange(stateSet);
        invalidateSelf();
        return false;
    }


    @Override
    public void draw(Canvas canvas)
    {
        m_grad.draw(canvas, null);
    }


    @Override
    public Drawable getCurrent()
    {
        return m_drw;
    }


    @Override
    public void setBounds(
            int left,
            int top,
            int right,
            int bottom)
    {
        super.setBounds(left, top, right, bottom);
        m_grad.resize(right - left, bottom - top);
        if (m_dependedDrawable != null) {
            m_dependedDrawable.setBounds(left, top - 1, right, bottom);
        }
    }


    public void setDependentDrawable(StateListDrawable depDrawable)
    {
        m_dependedDrawable = depDrawable;
    }


    @Override
    public boolean getPadding(Rect padding)
    {
        int g = m_grad.m_gap + 1;
        padding.set(g, g, g, g);
        return true;
    }


    public void recycle()
    {
        if (m_grad instanceof BitmapCachedGradBack) {
            ((BitmapCachedGradBack) m_grad).recycle();
        }
    }
}
