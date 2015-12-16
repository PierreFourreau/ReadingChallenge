package com.fourreau.readingchallenge.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fourreau.readingchallenge.R;
import com.fourreau.readingchallenge.core.ReadingChallengeApplication;
import com.fourreau.readingchallenge.model.ColorShades;
import com.fourreau.readingchallenge.view.CirclePageIndicator;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.CheckBox;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


public class IntroActivity extends BaseActivity {

    private static final String SAVING_STATE_SLIDER_ANIMATION = "SliderAnimationSavingState";
    private boolean isSliderAnimation = true;

    private ButtonFlat buttonPassIntro;
    private CheckBox checkBoxIntro;
    private LinearLayout layoutNotAnymore;
    private RadioGroup radioGroupLevel;
    private RadioButton radioButtonLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //util to clear shared prefs
//        SharedPreferences sharedPref = IntroActivity.this.getSharedPreferences("readingchallenge", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.clear();
//        editor.commit();

        //if user doesn't want to see intro
        if (readSharedPreferences(getString(R.string.intro)) == 1) {
            Intent intent = new Intent(IntroActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        //set user level
        ((ReadingChallengeApplication) getApplicationContext().getApplicationContext()).setLevel(readSharedPreferences(getString(R.string.level)));

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_intro);

        layoutNotAnymore = (LinearLayout) findViewById(R.id.layoutNotAnymore);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new ViewPagerAdapter(R.array.icons, R.array.titles, R.array.hints));
        CirclePageIndicator mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(viewPager);
        viewPager.setPageTransformer(true, new CustomPageTransformer());

        //check box rememeber intro
        checkBoxIntro = (CheckBox) findViewById(R.id.checkBoxIntro);

        checkBoxIntro.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (checkBoxIntro.isCheck()) {
                    writeSharedPreferences(getString(R.string.intro), 1);
                } else {
                    writeSharedPreferences(getString(R.string.intro), 0);
                }
            }
        });

        //button pass intro
        buttonPassIntro = (ButtonFlat) findViewById(R.id.buttonPassIntro);
        buttonPassIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set display
                int display = readSharedPreferences(getString(R.string.display));
                if (display == 0) {
                    writeSharedPreferences(getString(R.string.display), 2);
                } else {
                    writeSharedPreferences(getString(R.string.display), display);
                }

                if (checkBoxIntro.isCheck()) {
                    writeSharedPreferences(getString(R.string.intro), 1);
                } else {
                    writeSharedPreferences(getString(R.string.intro), 0);
                }

                int level = readSharedPreferences(getString(R.string.level));
                //if user doesn't want to see intro
                if (level == 0) {
                    //dialog level
                    LayoutInflater li = LayoutInflater.from(IntroActivity.this);
                    final View promptsView = li.inflate(R.layout.dialog_level, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(IntroActivity.this);
                    alertDialogBuilder.setView(promptsView);
                    radioGroupLevel = (RadioGroup) promptsView.findViewById(R.id.radioGroupLevel);
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton(R.string.ok,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            int selectedId = radioGroupLevel.getCheckedRadioButtonId();
                                            radioButtonLevel = (RadioButton) promptsView.findViewById(selectedId);
                                            int level = 2;
                                            if (radioButtonLevel.getText().toString().contains(getString(R.string.level_beginner))) {
                                                level = 1;
                                            } else if (radioButtonLevel.getText().toString().contains(getString(R.string.level_intermediate))) {
                                                level = 2;
                                            } else if (radioButtonLevel.getText().toString().contains(getString(R.string.level_expert))) {
                                                level = 3;
                                            }
                                            ((ReadingChallengeApplication) getApplicationContext().getApplicationContext()).setLevel(level);
                                            writeSharedPreferences(getString(R.string.level), level);
                                            finish();
                                            Intent intent = new Intent(IntroActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                                        }
                                    })
                    ;
                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setTitle(getString(R.string.dialog_level_title));
                    alertDialog.show();
                } else {
                    ((ReadingChallengeApplication) getApplicationContext().getApplicationContext()).setLevel(level);
                    finish();
                    Intent intent = new Intent(IntroActivity.this, HomeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {

                View landingBGView = findViewById(R.id.landing_backgrond);
                int colorBg[] = getResources().getIntArray(R.array.landing_bg);


                ColorShades shades = new ColorShades();
                shades.setFromColor(colorBg[position % colorBg.length])
                        .setToColor(colorBg[(position + 1) % colorBg.length])
                        .setShade(positionOffset);

                landingBGView.setBackgroundColor(shades.generate());
            }

            public void onPageSelected(int position) {
                if (position == 2) {
                    layoutNotAnymore.setVisibility(View.VISIBLE);
                    buttonPassIntro.setVisibility(View.VISIBLE);
                } else {
                    layoutNotAnymore.setVisibility(View.INVISIBLE);
                    buttonPassIntro.setVisibility(View.INVISIBLE);
                }
            }

            public void onPageScrollStateChanged(int state) {
            }
        });

        //analytics
        ReadingChallengeApplication application = (ReadingChallengeApplication) getApplication();
        Tracker mTracker = application.getDefaultTracker();
        mTracker.setScreenName("IntroActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public class ViewPagerAdapter extends PagerAdapter {

        private int iconResId, titleArrayResId, hintArrayResId;

        public ViewPagerAdapter(int iconResId, int titleArrayResId, int hintArrayResId) {

            this.iconResId = iconResId;
            this.titleArrayResId = titleArrayResId;
            this.hintArrayResId = hintArrayResId;
        }

        @Override
        public int getCount() {
            return getResources().getIntArray(iconResId).length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            Drawable icon = getResources().obtainTypedArray(iconResId).getDrawable(position);
            String title = getResources().getStringArray(titleArrayResId)[position];
            String hint = getResources().getStringArray(hintArrayResId)[position];


            View itemView = getLayoutInflater().inflate(R.layout.viewpager_item, container, false);


            ImageView iconView = (ImageView) itemView.findViewById(R.id.landing_img_slide);
            TextView titleView = (TextView) itemView.findViewById(R.id.landing_txt_title);
            TextView hintView = (TextView) itemView.findViewById(R.id.landing_txt_hint);


            iconView.setImageDrawable(icon);
            titleView.setText(title);
            hintView.setText(hint);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);

        }
    }

    public class CustomPageTransformer implements ViewPager.PageTransformer {


        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            View imageView = view.findViewById(R.id.landing_img_slide);
            View contentView = view.findViewById(R.id.landing_txt_hint);
            View txt_title = view.findViewById(R.id.landing_txt_title);

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left
            } else if (position <= 0) { // [-1,0]
                // This page is moving out to the left

                // Counteract the default swipe
                setTranslationX(view, pageWidth * -position);
                if (contentView != null) {
                    // But swipe the contentView
                    setTranslationX(contentView, pageWidth * position);
                    setTranslationX(txt_title, pageWidth * position);

                    setAlpha(contentView, 1 + position);
                    setAlpha(txt_title, 1 + position);
                }

                if (imageView != null) {
                    // Fade the image in
                    setAlpha(imageView, 1 + position);
                }

            } else if (position <= 1) { // (0,1]
                // This page is moving in from the right

                // Counteract the default swipe
                setTranslationX(view, pageWidth * -position);
                if (contentView != null) {
                    // But swipe the contentView
                    setTranslationX(contentView, pageWidth * position);
                    setTranslationX(txt_title, pageWidth * position);

                    setAlpha(contentView, 1 - position);
                    setAlpha(txt_title, 1 - position);

                }
                if (imageView != null) {
                    // Fade the image out
                    setAlpha(imageView, 1 - position);
                }

            }
        }
    }

    /**
     * Sets the alpha for the view. The alpha will be applied only if the running android device OS is greater than honeycomb.
     *
     * @param view  - view to which alpha to be applied.
     * @param alpha - alpha value.
     */
    private void setAlpha(View view, float alpha) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && !isSliderAnimation) {
            view.setAlpha(alpha);
        }
    }

    /**
     * Sets the translationX for the view. The translation value will be applied only if the running android device OS is greater than honeycomb.
     *
     * @param view         - view to which alpha to be applied.
     * @param translationX - translationX value.
     */
    private void setTranslationX(View view, float translationX) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && !isSliderAnimation) {
            view.setTranslationX(translationX);
        }
    }

    public void onSaveInstanceState(Bundle outstate) {

        if (outstate != null) {
            outstate.putBoolean(SAVING_STATE_SLIDER_ANIMATION, isSliderAnimation);
        }

        super.onSaveInstanceState(outstate);
    }

    public void onRestoreInstanceState(Bundle inState) {

        if (inState != null) {
            isSliderAnimation = inState.getBoolean(SAVING_STATE_SLIDER_ANIMATION, false);
        }
        super.onRestoreInstanceState(inState);

    }
}

