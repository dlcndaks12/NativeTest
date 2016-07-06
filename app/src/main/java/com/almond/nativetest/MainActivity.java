package com.almond.nativetest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    protected FrameLayout frameLayout;

    private ViewPager viewPager;
    private CustomSwipeAdapter adapter;
    private Context ctx;
    private ImageButton btnTrigger;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ImageButton btnHome;
    private Button menu1;
    private Button menu2;
    private Button menu3;
    private Button menu4;
    private Button menu5;
    private Button menu6;
    private Button menu7;
    private Button menu8;
    private Button menu9;
    private Button menu10;
    private ImageButton btnAbout;
    private ImageButton btnBusiness;
    private ImageButton btnPr;
    private ImageButton btnSetting;
    private BackPressCloseHandler backPressCloseHandler;

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (this.getLocalClassName().equals("MainActivity")) {
                backPressCloseHandler.onBackPressed();
            } else {
                finish();
            }
        }
    }

    public void closeDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    public void onHomeClick() {
        if (this.getLocalClassName().equals("MainActivity")) {
            Log.e("Main : ", "메인 액티비티입니다.");
            return;
        } else {
            Log.e("Main : ", "종료합니다.");
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
        }
    }

    public void itemSelected(View view, int position, String type) {
        Log.e("selected : ", view.getContext().toString());
        Intent intent;

        if (type.equals("about")) {
            switch (position) {
                case 0:
                    intent = new Intent(view.getContext(), WhoweareActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    break;
                case 1:
                    intent = new Intent(view.getContext(), HistoryActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    break;
                case 2:
                    intent = new Intent(view.getContext(), LocationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.ctx = this;
        backPressCloseHandler = new BackPressCloseHandler(this);

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        getLayoutInflater().inflate(R.layout.content_main, frameLayout);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        btnAbout = (ImageButton) findViewById(R.id.btnAbout);
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, WhoweareActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        btnBusiness = (ImageButton) findViewById(R.id.btnBusiness);
        btnBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, PortfolioActivity.class);
                intent.putExtra("biCateSubIdx", 1);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btnPr = (ImageButton) findViewById(R.id.btnPr);
        btnPr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, PortfolioActivity.class);
                intent.putExtra("biCateSubIdx", 4);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btnHome = (ImageButton) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHomeClick();
                /*if (v.getContext() == MainActivity.this.getApplicationContext()) {
                    Log.e("Main : ", "메인 액티비티입니다.");
                    return;
                } else {
                    Log.e("Main : ", "종료합니다.");
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    finish();
                }*/
            }
        });

        btnSetting = (ImageButton) findViewById(R.id.btnSetting);
        btnSetting.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        Intent SettingActivity = new Intent(ctx, SettingActivity.class);
                        startActivity(SettingActivity);
                        v.setAlpha(1.0f);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        v.setAlpha(1.0f);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        v.setAlpha(0.4f);
                        break;
                }
                return false;
            }
        });

        menu1 = (Button) findViewById(R.id.menuWhoweare);
        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                Intent intent = new Intent(ctx, WhoweareActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        menu2 = (Button) findViewById(R.id.menuHistory);
        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                Intent intent = new Intent(ctx, HistoryActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        menu3 = (Button) findViewById(R.id.menuLocation);
        menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                Intent intent = new Intent(ctx, LocationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        menu4 = (Button) findViewById(R.id.menuCharacter);
        menu4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                Intent intent = new Intent(ctx, PortfolioActivity.class);
                intent.putExtra("biCateSubIdx", 1);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        menu5 = (Button) findViewById(R.id.menuWeb);
        menu5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                Intent intent = new Intent(ctx, PortfolioActivity.class);
                intent.putExtra("biCateSubIdx", 4);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        menu6 = (Button) findViewById(R.id.menu3D);
        menu6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                Intent intent = new Intent(ctx, PrintingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        menu7 = (Button) findViewById(R.id.menuHealth);
        menu7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                Intent intent = new Intent(ctx, HealthActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        menu8 = (Button) findViewById(R.id.menuSmart);
        menu8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                Intent intent = new Intent(ctx, SmartToyActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        menu9 = (Button) findViewById(R.id.menuNotice);
        menu9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                Intent intent = new Intent(ctx, NoticeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        menu10 = (Button) findViewById(R.id.menuRecruit);
        menu10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                Intent intent = new Intent(ctx, RecruitActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

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
