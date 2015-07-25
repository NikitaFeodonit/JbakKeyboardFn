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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


public class AboutActivity
        extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        View v = getLayoutInflater().inflate(R.layout.about, null);
//        v.setBackgroundDrawable(
//                new ColorsGradientBack().setCorners(0, 0).setGap(0).getStateDrawable());
        try {
            String vers = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            String app = getString(R.string.about_version) + " " + vers + "\n\n" +
                         getString(R.string.about_market) +
                         " https://market.android.com/details?id=" + getPackageName() + "\n\n" +
                         getString(R.string.about_web);
            ((TextView) v.findViewById(R.id.version)).setText(app);
        } catch (Throwable e) {
        }

        setContentView(v);
    }
}
