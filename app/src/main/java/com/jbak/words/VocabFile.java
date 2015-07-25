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

package com.jbak.words;

import com.jbak.JbakKeyboard.st;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class VocabFile
{
    public static final String DEF_EXT = ".dic";
    public final        String REGEXP  = "([A-z]{2})_v(\\d+).*?\\" + DEF_EXT;
    Pattern m_pattern;
    Matcher m_matcher;
    int     m_version;
    String  m_filePath;


    public VocabFile()
    {
        m_pattern = Pattern.compile(REGEXP, Pattern.CASE_INSENSITIVE);
    }


    public boolean match(String filename)
    {
        try {
            m_matcher = m_pattern.matcher(filename);
            boolean bRet = m_matcher.find();
            return bRet;
        } catch (Exception e) {
        }
        return false;
    }


    public String getLang()
    {
        return m_matcher.group(1);
    }


    public int getVersion()
    {
        try {
            return Integer.decode(m_matcher.group(2));
        } catch (Exception e) {
        }
        return 0;
    }


    public String processDir(
            String lang,
            File[] files)
    {
        m_filePath = null;
        m_version = 0;
        if (files == null) {
            return m_filePath;
        }
        try {
            for (File f : files) {
                if (match(f.getName())) {
                    String l = getLang();
                    if (l != null && l.equals(lang)) {
                        int v = getVersion();
                        if (v > m_version) {
                            m_filePath = f.getAbsolutePath();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return m_filePath;
    }


    public String processDir(
            String path,
            String lang)
    {
        return processDir(lang, st.getFilesByExt(new File(path), DEF_EXT));
    }
}
