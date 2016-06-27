package com.almond.nativetest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private CustomSwipeAdapter adapter;
    private Context ctx;
    private ImageButton btnTrigger;
    private DrawerLayout drawer;
    private Handler handler;
    private Thread thread;
    private int curViewPage = 0;
    private NavigationView navigationView;

    private Spinner select1;
    private Spinner select2;

    private SpinnerAdapter spinnerAdapter;
    private ViewFlipper vf;
    private ViewFlipper vfHistory;
    private int m_nPreTouchPosX = 0;
    private ImageButton btnHome;
    private Button menu1;
    private Button menu2;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(vf.getDisplayedChild() != 0) {
                vf.showPrevious();
            } else {
                super.onBackPressed();
            }
        }
    }


    private void MoveNextView() {
        vfHistory.setInAnimation(AnimationUtils.loadAnimation(this,
                R.anim.appear_from_right));
        vfHistory.setOutAnimation(AnimationUtils.loadAnimation(this,
                R.anim.disappear_to_left));
        vfHistory.showNext();
    }

    private void MovePreviousView() {
        vfHistory.setInAnimation(AnimationUtils.loadAnimation(this,
                R.anim.appear_from_left));
        vfHistory.setOutAnimation(AnimationUtils.loadAnimation(this,
                R.anim.disappear_to_right));
        vfHistory.showPrevious();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.ctx = this;

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        /* whoweare 스피너 */
        select1 = (Spinner) findViewById(R.id.spinnerSub);
        spinnerAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.whoweare, R.layout.spinner_item);
        select1.setAdapter(spinnerAdapter);
        select1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ctx, ""+position, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /* history 스피너 */
        select2 = (Spinner) findViewById(R.id.spinnerSub2);
        spinnerAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.whoweare, R.layout.spinner_item);
        select2.setAdapter(spinnerAdapter);
        select2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ctx, "his : "+position, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        vf = (ViewFlipper) findViewById(R.id.vf);
        vf.setInAnimation(ctx, R.anim.fadein);
        vf.setOutAnimation(ctx, R.anim.fadeout);
        vf.setDisplayedChild(0);

        btnHome = (ImageButton) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vf.setDisplayedChild(0);
            }
        });

        menu1 = (Button) findViewById(R.id.menuWhoweare);
        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vf.setDisplayedChild(1);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        menu2 = (Button) findViewById(R.id.menuHistory);
        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vf.setDisplayedChild(2);
                drawer.closeDrawer(GravityCompat.START);
                select2.setSelection(1);
            }
        });

        /* history viewflipper */
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
                        Toast.makeText(ctx, "다음", Toast.LENGTH_SHORT).show();
                    } else if (nTouchPosX > m_nPreTouchPosX) {
                        MovePreviousView();
                        Toast.makeText(ctx, "이전", Toast.LENGTH_SHORT).show();
                    }
                    m_nPreTouchPosX = nTouchPosX;
                }

                return true;
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        btnTrigger = (ImageButton) findViewById(R.id.btnTrigger);

        btnTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new CustomSwipeAdapter(this);

        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View view, float position) {
                final float MIN_SCALE = 0.75f;

                int pageWidth = view.getWidth();

                if (position < -1) { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    view.setAlpha(0);

                } else if (position <= 0) { // [-1,0]
                    // Use the default slide transition when moving to the left page
                    view.setAlpha(1);
                    view.setTranslationX(0);
                    view.setScaleX(1);
                    view.setScaleY(1);

                } else if (position <= 1) { // (0,1]
                    // Fade the page out.
                    view.setAlpha(1 - position);

                    // Counteract the default slide transition
                    view.setTranslationX(pageWidth * -position);

                    // Scale the page down (between MIN_SCALE and 1)
                    float scaleFactor = MIN_SCALE
                            + (1 - MIN_SCALE) * (1 - Math.abs(position));
                    view.setScaleX(scaleFactor);
                    view.setScaleY(scaleFactor);

                } else { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    view.setAlpha(0);
                }
            }
        });
    }
}
