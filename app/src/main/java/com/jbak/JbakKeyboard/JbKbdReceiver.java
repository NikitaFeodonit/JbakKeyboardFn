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

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;


public class JbKbdReceiver
        extends BroadcastReceiver
{
    @Override
    public void onReceive(
            Context c,
            Intent in)
    {
        if (Intent.ACTION_BOOT_COMPLETED.equals(in.getAction())) {
            c.startService(new Intent(c, ClipbrdService.class));
        }
    }


    static Intent getClipbrdServiceIntent(Context c)
    {
        ComponentName cn = new ComponentName(
                JbKbdReceiver.class.getPackage().getName(), ClipbrdService.class.getName());
        Intent in = new Intent().setComponent(cn);
        return in;
    }
}
