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

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.speech.RecognizerIntent;

import java.util.ArrayList;


public class VRTest
{
    BroadcastReceiver m_recv = new BroadcastReceiver()
    {
        @Override
        public void onReceive(
                Context context,
                Intent intent)
        {
            ArrayList<String> matches = intent.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            if (ServiceJbKbd.inst != null) {
                ServiceJbKbd.inst.onVoiceRecognition(matches);
            }
        }
    };
    String            bcastAction = "Jbtm.VOICE_RECOGNIZER_RESULTS";


    void startVoice()
    {
        ServiceJbKbd.inst.forceHide();
        ServiceJbKbd.inst.registerReceiver(m_recv, new IntentFilter(bcastAction));
        Intent activityIntent = new Intent(ServiceJbKbd.inst, ServiceJbKbd.class);
        // this intent wraps results activity intent
        PendingIntent resultsPendingIntent =
                PendingIntent.getBroadcast(ServiceJbKbd.inst, 0, new Intent(bcastAction), 0);
        PendingIntent.getService(ServiceJbKbd.inst, 0, activityIntent, 0);
        // this intent calls the speech recognition
        Intent voiceIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        voiceIntent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, st.c().getString(R.string.ime_name));
        voiceIntent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION |
                Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY |
                Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_RESULTS_PENDINGINTENT, resultsPendingIntent);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
        // this intent wraps voice recognition intent
        PendingIntent pendingIntent =
                PendingIntent.getActivity(ServiceJbKbd.inst, 0, voiceIntent, 0);
        try {
            pendingIntent.send(0, m_fin, null);
        } catch (Throwable e) {
            st.logEx(e);
        }
    }


    PendingIntent.OnFinished m_fin = new PendingIntent.OnFinished()
    {
        @Override
        public void onSendFinished(
                PendingIntent pendingIntent,
                Intent intent,
                int resultCode,
                String resultData,
                Bundle resultExtras)
        {

        }
    };
}
