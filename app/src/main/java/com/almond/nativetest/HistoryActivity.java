package com.almond.nativetest;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class HistoryActivity extends MainActivity {

    private ViewFlipper vfHistory;
    private Spinner select;
    private SpinnerAdapter spinnerAdapter;
    private int m_nPreTouchPosX = 0;
    private ImageButton btnHistoryPrev;
    private ImageButton btnHistoryNext;

    private void MoveNextView() {
        if (vfHistory.getDisplayedChild() < (vfHistory.getChildCount()-1)) {
            vfHistory.setInAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.appear_from_right));
            vfHistory.setOutAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.disappear_to_left));
            vfHistory.showNext();
        } else {
            Toast.makeText(this, "마지막 년도입니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void MovePreviousView() {
        if (vfHistory.getDisplayedChild() > 0) {
            vfHistory.setInAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.appear_from_left));
            vfHistory.setOutAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.disappear_to_right));
            vfHistory.showPrevious();
        } else {
            Toast.makeText(this, "가장 첫년도입니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_history, frameLayout);

        select = (Spinner) findViewById(R.id.spinnerSub);
        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.whoweare, R.layout.spinner_item);
        select.setAdapter(spinnerAdapter);
        select.setOnItemSelectedListener(selectedListener);
        select.setSelection(1);

        vfHistory = (ViewFlipper) findViewById(R.id.vfHistory);
        vfHistory.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    m_nPreTouchPosX = (int) event.getX();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int nTouchPosX = (int) event.getX();
                    if (nTouchPosX < m_nPreTouchPosX) {
                        MoveNextView();
                    } else if (nTouchPosX > m_nPreTouchPosX) {
                        MovePreviousView();
                    }
                    m_nPreTouchPosX = nTouchPosX;
                }

                return true;
            }
        });
        vfHistory.setDisplayedChild(vfHistory.getChildCount() - 1);

        btnHistoryPrev = (ImageButton)findViewById(R.id.btnHistoryPrev);
        btnHistoryPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovePreviousView();
            }
        });
        btnHistoryNext = (ImageButton)findViewById(R.id.btnHistoryNext);
        btnHistoryNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoveNextView();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        select.setSelection(1);
    }
}
