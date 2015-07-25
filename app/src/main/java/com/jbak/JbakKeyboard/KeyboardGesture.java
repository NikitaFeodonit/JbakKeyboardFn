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

import android.inputmethodservice.Keyboard.Key;
import android.view.GestureDetector;
import android.view.MotionEvent;
import com.jbak.JbakKeyboard.JbKbd.LatinKey;


public class KeyboardGesture
        extends GestureDetector
{
    public KeyboardGesture(JbKbdView view)
    {
        super(view.getContext(), new KvListener().setKeyboardView(view));
    }


    static class KvListener
            extends SimpleOnGestureListener
    {
        JbKbdView m_kv;
        int   minGestSize = 100;
        float deltaDelim  = (float) 1.5;
        int   minVelocity = 150;


        public KvListener setKeyboardView(JbKbdView kv)
        {
            m_kv = kv;
            return this;
        }


        @Override
        public boolean onFling(
                MotionEvent e1,
                MotionEvent e2,
                float velocityX,
                float velocityY)
        {
            int downX = (int) e1.getX();
            int downY = (int) e1.getY();
            float dx = e2.getX() - e1.getX();
            float dy = e2.getY() - e1.getY();
            float mdx = Math.abs(dx);
            float mdy = Math.abs(dy);
//            st.log("dx="+dx+"; dy="+dy+";vX="+velocityX+";vY="+velocityY+"|downX="+downX+"; downY="+downY);
            if (mdx >= minGestSize && (mdy == 0 || mdx / mdy >= deltaDelim) &&
                Math.abs(velocityX) > minVelocity) {
                int type = velocityX > 0 ? GestureInfo.RIGHT : GestureInfo.LEFT;
                m_kv.gesture(new GestureInfo(getKey(downX, m_kv.m_vertCorr + downY), type));
                return true;
            }
            if (mdy >= minGestSize && (mdx == 0 || mdy / mdx >= deltaDelim) &&
                Math.abs(velocityY) > minVelocity &&
                (velocityX == 0 || Math.abs(velocityY / velocityX) >= deltaDelim)) {
                int type = velocityY > 0 ? GestureInfo.DOWN : GestureInfo.UP;
                m_kv.gesture(new GestureInfo(getKey(downX, m_kv.m_vertCorr + downY), type));
                return true;
            }
            if (mdx >= minGestSize || mdy >= minGestSize) {
                return true;
            }
            return false;
        }


        final LatinKey getKey(
                int x,
                int y)
        {
            for (Key k : m_kv.getCurKeyboard().getKeys()) {
                if (k.isInside(x, y)) {
                    return (LatinKey) k;
                }
            }
            return null;
        }
    }


    ;


    public static class GestureInfo
    {
        public static final int LEFT  = 0;
        public static final int RIGHT = 1;
        public static final int UP    = 2;
        public static final int DOWN  = 3;
        LatinKey downKey;
        int      dir;


        public GestureInfo(
                LatinKey k,
                int dir)
        {
            downKey = k;
            this.dir = dir;
        }

    }
}
