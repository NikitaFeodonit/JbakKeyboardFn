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

import com.jbak.CustomGraphics.BitmapCachedGradBack;
import com.jbak.CustomGraphics.GradBack;
import com.jbak.JbakKeyboard.IKeyboard.KbdDesign;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.Vector;


public class CustomKbdDesign
{
    public static final byte P_KeyBackStartColor              = 0;
    public static final byte P_KeyBackEndColor                = 1;
    public static final byte P_KeyBackGradientType            = 2;
    public static final byte P_KeyTextColor                   = 3;
    public static final byte P_KeyTextBold                    = 4;
    public static final byte P_KeyGapSize                     = 5;
    public static final byte P_KeyStrokeStartColor            = 6;
    public static final byte P_KeyStrokeEndColor              = 7;
    public static final byte P_KeyboardBackgroundStartColor   = 8;
    public static final byte P_KeyboardBackgroundEndColor     = 9;
    public static final byte P_KeyboardBackgroundGradientType = 10;
    public static final byte P_SpecKeyBackStartColor          = 11;
    public static final byte P_SpecKeyBackEndColor            = 12;
    public static final byte P_SpecKeyStrokeStartColor        = 13;
    public static final byte P_SpecKeyStrokeEndColor          = 14;
    public static final byte P_SpecKeyTextColor               = 15;
    public static final byte P_KeyBackCornerX                 = 16;
    public static final byte P_KeyBackCornerY                 = 17;
    public static final byte P_KeySymbolColor                 = 18;
    public static final byte P_KeyTextPressedColor            = 19;
    public static final byte P_KeySymbolPressedColor          = 20;
    public static final byte P_SpecKeySymbolColor             = 21;
    public static final byte P_SpecKeyTextPressedColor        = 22;
    public static final byte P_SpecKeySymbolPressedColor      = 23;
    public static final byte P_KeyBackPressedStartColor       = 24;
    public static final byte P_KeyBackPressedEndColor         = 25;
    public static final byte P_KeyBackPressedGradientType     = 26;
    public static final byte P_KeyPressedStrokeStartColor     = 27;
    public static final byte P_KeyPressedStrokeEndColor       = 28;
    public static final byte P_SpecKeyBackPressedStartColor   = 29;
    public static final byte P_SpecKeyBackPressedEndColor     = 30;
    public static final byte P_SpecKeyBackPressedGradientType = 31;
    public static final byte P_SpecKeyPressedStrokeStartColor = 32;
    public static final byte P_SpecKeyPressedStrokeEndColor   = 33;
    int              errorLine = 0;
    String           arNames[] = new String[] {
            "KeyBackStartColor",
            "KeyBackEndColor",
            "KeyBackGradientType",
            "KeyTextColor",
            "KeyTextBold",
            "KeyGapSize",
            "KeyStrokeStartColor",
            "KeyStrokeEndColor",
            "KeyboardBackgroundStartColor",
            "KeyboardBackgroundEndColor",
            "KeyboardBackgroundGradientType",
            "SpecKeyBackStartColor",
            "SpecKeyBackEndColor",
            "SpecKeyStrokeStartColor",
            "SpecKeyStrokeEndColor",
            "SpecKeyTextColor",
            "KeyBackCornerX",
            "KeyBackCornerY",
            "KeySymbolColor",
            "KeyTextPressedColor",
            "KeySymbolPressedColor",
            "SpecKeySymbolColor",
            "SpecKeyTextPressedColor",
            "SpecKeySymbolPressedColor",
            "KeyBackPressedStartColor",
            "KeyBackPressedEndColor",
            "KeyBackPressedGradientType",
            "KeyPressedStrokeStartColor",
            "KeyPressedStrokeEndColor",
            "SpecKeyBackPressedStartColor",
            "SpecKeyBackPressedEndColor",
            "SpecKeyBackPressedGradientType",
            "SpecKeyPressedStrokeStartColor",
            "SpecKeyPressedStrokeEndColor",};
    String           skinPath  = "";
    Vector<IntEntry> arValues  = new Vector<IntEntry>();


    boolean load(String path)
    {
        skinPath = path;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(path));
        } catch (Throwable e) {
            st.logEx(e);
        }
        if (reader == null) {
            return false;
        }
        return load(reader);
    }


    boolean load(BufferedReader r)
    {
        int line = 1;
        try {
            String s;
            while ((s = r.readLine()) != null) {
                int index = parseParam(s);
                if (index > -1) {
                    int dec = -1;
                    dec = processStringInt(m_val);
                    arValues.add(new IntEntry(index, dec));
                }
                ++line;
            }
        } catch (Throwable e) {
            errorLine = line;
            return false;
        }
        return true;
    }


    final int getColor(int index)
    {
        return getIntValue(index, st.DEF_COLOR);
    }


    final int getIntValue(
            int index,
            int defVal)
    {
        for (IntEntry ie : arValues) {
            if (ie.index == index) {
                return ie.value;
            }
        }
        return defVal;
    }


    KbdDesign getDesign()
    {
        KbdDesign ret = new KbdDesign(0, 0, st.DEF_COLOR, 0, 0);
        ret.path = skinPath;
        int startColor, endColor, gradType;
        startColor = getIntValue(P_KeyBackStartColor, st.DEF_COLOR);
        endColor = getIntValue(P_KeyBackEndColor, st.DEF_COLOR);
        gradType = getIntValue(P_KeyBackGradientType, GradBack.GRADIENT_TYPE_LINEAR);
        int gap = getIntValue(P_KeyGapSize, GradBack.DEFAULT_GAP);
        int cornerX = getIntValue(P_KeyBackCornerX, GradBack.DEFAULT_CORNER_X);
        int cornerY = getIntValue(P_KeyBackCornerY, GradBack.DEFAULT_CORNER_Y);
        if (startColor != st.DEF_COLOR) {
            ret.setKeysBackground(
                    new BitmapCachedGradBack(startColor, endColor).setGradType(gradType)
                            .setGap(gap)
                            .setCorners(cornerX, cornerY)
                            .setGradType(
                                    getIntValue(
                                            P_KeyBackPressedGradientType,
                                            GradBack.GRADIENT_TYPE_LINEAR)));
        }
        startColor = getIntValue(P_KeyboardBackgroundStartColor, st.DEF_COLOR);
        endColor = getIntValue(P_KeyboardBackgroundEndColor, st.DEF_COLOR);
        gradType = getIntValue(P_KeyboardBackgroundGradientType, GradBack.GRADIENT_TYPE_LINEAR);
        if (startColor != st.DEF_COLOR) {
            ret.setKbdBackground(
                    new BitmapCachedGradBack(startColor, endColor).setGradType(gradType)
                            .setGap(0)
                            .setCorners(0, 0));
        }
        startColor = getIntValue(P_KeyStrokeStartColor, st.DEF_COLOR);
        endColor = getIntValue(P_KeyStrokeEndColor, st.DEF_COLOR);
        if (startColor != st.DEF_COLOR && ret.m_keyBackground != null) {
            ret.m_keyBackground.setStroke(
                    new GradBack(startColor, endColor).setGap(gap - 1)
                            .setCorners(cornerX, cornerY));
        }
        startColor = getColor(P_KeyBackPressedStartColor);
        endColor = getColor(P_KeyBackPressedEndColor);
        if (startColor != st.DEF_COLOR && ret.m_keyBackground != null) {
            GradBack pressed = new GradBack(startColor, endColor).setGap(gap)
                    .setCorners(cornerX, cornerY)
                    .setGradType(
                            getIntValue(
                                    P_SpecKeyBackPressedGradientType,
                                    GradBack.GRADIENT_TYPE_LINEAR));
            startColor = getColor(P_KeyPressedStrokeStartColor);
            endColor = getColor(P_KeyPressedStrokeEndColor);
            if (startColor != st.DEF_COLOR) {
                pressed.setStroke(
                        new GradBack(startColor, endColor).setGap(gap - 1)
                                .setCorners(cornerX, cornerY));
            }
            ret.m_keyBackground.setPressedGradBack(pressed);
        }

        ret.textColor = getIntValue(P_KeyTextColor, st.DEF_COLOR);
        if (getIntValue(P_KeyTextBold, 0) == 1) {
            ret.flags |= st.DF_BOLD;
        }
        startColor = getIntValue(P_SpecKeyBackStartColor, st.DEF_COLOR);
        endColor = getIntValue(P_SpecKeyBackEndColor, st.DEF_COLOR);
        if (startColor != st.DEF_COLOR) {
            int textColor = getIntValue(P_SpecKeyTextColor, st.DEF_COLOR);
            GradBack gb = new BitmapCachedGradBack(startColor, endColor).setGradType(
                    ret.m_kbdBackground.m_gradType).setGap(gap);
            gb.setCorners(cornerX, cornerY);
            startColor = getIntValue(P_SpecKeyStrokeStartColor, st.DEF_COLOR);
            endColor = getIntValue(P_SpecKeyStrokeEndColor, st.DEF_COLOR);
            if (startColor != st.DEF_COLOR) {
                gb.setStroke(
                        new GradBack(startColor, endColor).setCorners(cornerX, cornerY)
                                .setGap(gap - 1));
            }
            ret.setFuncKeysDesign(new KbdDesign(0, 0, textColor, 0, 0).setKeysBackground(gb));
            ret.m_kbdFuncKeys.setColors(
                    ret.m_kbdFuncKeys.textColor, getColor(P_SpecKeySymbolColor),
                    getColor(P_SpecKeyTextPressedColor), getColor(P_SpecKeySymbolPressedColor));
            startColor = getColor(P_SpecKeyBackPressedStartColor);
            endColor = getColor(P_SpecKeyBackPressedEndColor);
            if (startColor != st.DEF_COLOR && ret.m_keyBackground != null) {
                GradBack pressed =
                        new GradBack(startColor, endColor).setGap(gap).setCorners(cornerX, cornerY);
                startColor = getColor(P_KeyPressedStrokeStartColor);
                endColor = getColor(P_KeyPressedStrokeEndColor);
                if (startColor != st.DEF_COLOR) {
                    pressed.setStroke(
                            new GradBack(startColor, endColor).setGap(gap - 1)
                                    .setCorners(cornerX, cornerY));
                }
                ret.m_kbdFuncKeys.m_keyBackground.setPressedGradBack(pressed);
            }

        }
        ret.setColors(
                ret.textColor, getColor(P_KeySymbolColor), getColor(P_KeyTextPressedColor),
                getColor(P_KeySymbolPressedColor));
        return ret;
    }


    int parseParam(String s)
    {
        int index = -1;
        if (s == null) {
            return index;
        }
        s = s.trim();
        if (s.length() == 0) {
            return index;
        }
        int f = s.indexOf('=');
        if (f < 0) {
            return index;
        }
        String name = s.substring(0, f).trim();
        index = findName(name);
        if (index > -1) {
            m_val = s.substring(f + 1);
        }
        return index;
    }


    String m_val;


    int findName(String name)
    {
        int index = 0;
        for (String s : arNames) {
            if (s.compareTo(name) == 0) {
                return index;
            }
            index++;
        }
        return -1;
    }


    public static class IntEntry
    {
        int index = -1;
        int value = -1;


        public IntEntry()
        {
        }


        public IntEntry(
                int i,
                int v)
        {
            index = i;
            value = v;
        }
    }


    int processStringInt(String s)
    {
        s = s.trim();
        if (s.indexOf('#') == 0 || s.startsWith("0x")) // 16-ричное значение
        {
            return st.parseInt(s.substring(1), 16);
        }
        return Integer.valueOf(s);
    }


    String getErrString()
    {
        if (errorLine > 0) {
            return "Parse err: " + new File(skinPath).getName() + ", line: " + errorLine + "\n";
        } else {
            return "Can't read: " + new File(skinPath).getName();
        }

    }


    static String loadCustomSkins()
    {
        String err = "";
        try {
            String path = st.getSettingsPath() + "skins";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
                return err;
            }
            File skins[] = dir.listFiles(
                    new FilenameFilter()
                    {
                        @Override
                        public boolean accept(
                                File dir,
                                String filename)
                        {
                            int pos = filename.lastIndexOf('.');
                            if (pos < 0) {
                                return false;
                            }
                            return filename.substring(pos + 1).compareTo("skin") == 0;
                        }
                    });
            if (skins.length == 0) {
                return err;
            }
            Vector<KbdDesign> ar = new Vector<IKeyboard.KbdDesign>();
            for (File fs : skins) {
                ar.add(new KbdDesign(fs.getAbsolutePath()));
            }
            int pos = 0;
            for (KbdDesign kd : st.arDesign) {
                if (kd.path != null) {
                    break;
                }
                ++pos;
            }
            KbdDesign des[] = new KbdDesign[pos + ar.size()];
            System.arraycopy(st.arDesign, 0, des, 0, pos);
            for (KbdDesign kd : ar) {
                des[pos] = kd;
                pos++;
            }
            st.arDesign = des;
        } catch (Throwable e) {
            return "System error";
        }
        return err;
    }
}
