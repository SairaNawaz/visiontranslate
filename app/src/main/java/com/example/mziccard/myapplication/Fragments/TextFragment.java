package com.example.mziccard.myapplication.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mziccard.myapplication.MainActivity;
import com.example.mziccard.myapplication.R;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translation;

public class TextFragment extends Fragment {

    View v;
    EditText typing_view;
    TextView translation_view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_text, container, false);
        initViews();
        return v;
    }

    void initViews() {

        typing_view = v.findViewById(R.id.typing_view);
        translation_view = v.findViewById(R.id.translation_view);

        typing_view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {

                if (s.length() > 3) {

                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            final Translation translation =
                                    MainActivity.google_translate.translate(s.toString(),
                                            Translate.TranslateOption.targetLanguage(MainActivity.target_language));
                            translation_view.post(new Runnable() {
                                @Override
                                public void run() {

                                    translation_view.setText(translation.getTranslatedText());

                                }
                            });
                            return null;
                        }
                    }.execute();
                }else{
                    translation_view.setText("");
                }
            }
        });

    }

    public void updateTranslation() {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                final Translation translation =
                        MainActivity.google_translate.translate(typing_view.getText().toString(),
                                Translate.TranslateOption.targetLanguage(MainActivity.target_language));
                translation_view.post(new Runnable() {
                    @Override
                    public void run() {

                        translation_view.setText(translation.getTranslatedText());

                    }
                });
                return null;
            }
        }.execute();
    }

}
