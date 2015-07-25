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

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import com.jbak.JbakKeyboard.IKeyboard.Lang;

import java.util.Vector;


public class LangSetActivity
        extends AppCompatActivity
{
    static LangSetActivity inst;
    LangAdapter m_adapt;
    ListView    m_list;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        inst = this;
        CustomKeyboard.loadCustomKeyboards(false);
        m_adapt = new LangAdapter(this, R.layout.lang_list_item);
        for (Lang l : st.arLangs) {
            m_adapt.add(l);
        }
        View v = getLayoutInflater().inflate(R.layout.pref_view_2, null);
        m_list = (ListView) v.findViewById(android.R.id.list);
        m_list.setAdapter(m_adapt);
        Button topView = (Button) v.findViewById(R.id.top_item);
        topView.setVisibility(View.VISIBLE);
        topView.setText(R.string.set_key_ac_load_vocab);
//        TextView tw = (TextView) topView.findViewById(R.id.text);
//        tw.setText(R.string.set_key_ac_load_vocab);
//        tw.setTextColor(0xff0000ff);
//        tw.setBackgroundResource(android.R.drawable.btn_default);
//        ((TextView) topView.findViewById(R.id.desc)).setText(" ");
        topView.setOnClickListener(
                new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        st.runAct(UpdVocabActivity.class);
                    }
                });
        setContentView(v);
    }


    @Override
    protected void onDestroy()
    {
        String langs = m_adapt.getLangString();
        st.pref().edit().putString(st.PREF_KEY_LANGS, langs).commit();
        inst = null;
        super.onDestroy();
    }


    View.OnClickListener m_butListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            Lang l = (Lang) v.getTag();
            try {
                Intent in = new Intent(Intent.ACTION_VIEW).setComponent(
                        new ComponentName(v.getContext(), SetKbdActivity.class))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra(st.SET_INTENT_ACTION, st.SET_SELECT_KEYBOARD)
                        .putExtra(st.SET_INTENT_LANG_NAME, l.name);
                startActivity(in);
            } catch (Throwable e) {
                st.logEx(e);
            }


        }
    };


    class LangAdapter
            extends ArrayAdapter<IKeyboard.Lang>
    {
        String getLangString()
        {
            String ret = "";
            for (String s : m_arLangs) {
                if (ret.length() > 0) {
                    ret += ",";
                }
                ret += s;
            }
            return ret;
        }


        int searchLang(String lang)
        {
            int pos = 0;
            for (String lng : m_arLangs) {
                if (lang.equals(lng)) {
                    return pos;
                }
                pos++;
            }
            return -1;
        }


        OnCheckedChangeListener m_chkListener = new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(
                    CompoundButton buttonView,
                    boolean isChecked)
            {
                Lang l = (Lang) buttonView.getTag();
                if (isChecked) {
                    int f = searchLang(l.name);
                    if (f < 0) {
                        m_arLangs.add(l.name);
                    }
                } else {
                    int f = searchLang(l.name);
                    if (f > -1) {
                        m_arLangs.remove(f);
                    }
                }
            }
        };


        public LangAdapter(
                Context context,
                int textViewResourceId)
        {
            super(context, textViewResourceId);
            rId = textViewResourceId;
            m_arLangs = new Vector<String>();
            for (String l : st.getLangsArray(context)) {
                boolean canAdd = false;
                if (searchLang(l) == -1) {
                    for (Lang lang : st.arLangs) {
                        if (lang.name.equals(l)) {
                            canAdd = true;
                            break;
                        }
                    }
                }
                if (canAdd) {
                    m_arLangs.add(l);
                }
            }
        }


        /**
         * Возвращает язык для позиции pos, так, чтобы виртуальные языки были снизу
         */
        Lang getLangAtPos(int pos)
        {
            int cp = 0;
            int vp = -1;
            for (Lang l : st.arLangs) {
                if (l.isVirtualLang()) {
                    if (vp < 0) {
                        vp = cp;
                    }
                } else {
                    if (cp == pos) {
                        return l;
                    }
                    ++cp;
                }
            }
            return st.arLangs[vp + pos - cp];
        }


        @Override
        public View getView(
                int position,
                View convertView,
                ViewGroup parent)
        {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(rId, null);
            }
            Lang l = getLangAtPos(position);
            CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkbox);
            cb.setText(l.getName(inst));
            cb.setTag(l);
            cb.setEnabled(!l.isVirtualLang());
            boolean bCheck = false;
            for (String s : m_arLangs) {
                if (s.equals(l.name)) {
                    bCheck = true;
                    break;
                }
            }
            cb.setChecked(bCheck);

            cb.setOnCheckedChangeListener(m_chkListener);
            Button b = (Button) convertView.findViewById(R.id.button);
            if (st.getKeybrdArrayByLang(l.name).size() > 1) {
                b.setOnClickListener(m_butListener);
                b.setVisibility(View.VISIBLE);
            } else {
                b.setVisibility(View.GONE);
                //b.getLayoutParams().width = 0;
            }
            b.setTag(l);
            return convertView;
        }


        int            rId;
        Vector<String> m_arLangs;
    }
}
