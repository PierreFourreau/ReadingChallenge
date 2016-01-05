package com.fourreau.readingchallenge.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.fourreau.readingchallenge.R;
import com.fourreau.readingchallenge.core.ReadingChallengeApplication;
import com.fourreau.readingchallenge.model.Category;
import com.fourreau.readingchallenge.model.Suggestion;
import com.fourreau.readingchallenge.service.ApiService;
import com.fourreau.readingchallenge.util.Utils;
import com.gc.materialdesign.views.ButtonRectangle;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.melnykov.fab.FloatingActionButton;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

public class CategoryActivity extends BaseActivity implements ObservableScrollViewCallbacks {

    @Inject
    ApiService apiService;

    private int checkBefore, checkAfter;
    private String categoryId;
    private Boolean frLanguage;
    private ButtonRectangle buttonAddSuggestion;
    private EditText editTextLibelle, editTextEmail;
    private LinearLayout containerSuggestions;

    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;

    private View mImageView;
    private View mOverlayView;
    private ObservableScrollView mScrollView;
    private TextView mTitleView;
    private TextView textViewTitle;
    private TextView textViewDescription;
    private TextView textViewSuggestionsNone;
    private View mFab;
    private int mActionBarSize;
    private int mFlexibleSpaceShowFabOffset;
    private int mFlexibleSpaceImageHeight;
    private int mFabMargin;
    private boolean mFabIsShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ((ReadingChallengeApplication) getApplication()).inject(this);

        categoryId = ((ReadingChallengeApplication) this.getApplication()).getCategoryId();
        frLanguage = ((ReadingChallengeApplication) getApplicationContext()).getLanguage().equals(Utils.FR);

        //get category by choosen id
        apiService.getCategoryById(categoryId, new Callback<Category>() {
            @Override
            public void success(Category category, Response response) {
                displayCategory(category);
            }

            @Override
            public void failure(RetrofitError error) {
                displayErrorSnackBar(getString(R.string.activity_category_error));
                Timber.e("Error get category by id : " + error.getMessage());
            }
        });

        //get suggestions by category id
        apiService.listSuggestions(categoryId, new Callback<List<Suggestion>>() {
            @Override
            public void success(List<Suggestion> suggestions, Response response) {
                displaySuggestions(suggestions);
            }

            @Override
            public void failure(RetrofitError error) {
                displayErrorSnackBar(getString(R.string.activity_category_suggestions_error));
                Timber.e("Error get suggestions : " + error.getMessage());
            }
        });

        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mFlexibleSpaceShowFabOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_show_fab_offset);
        mActionBarSize = getActionBarSize();

        mImageView = findViewById(R.id.image);
        mOverlayView = findViewById(R.id.overlay);
        mScrollView = (ObservableScrollView) findViewById(R.id.scroll);
        mScrollView.setScrollViewCallbacks(this);
        mTitleView = (TextView) findViewById(R.id.title);
        mFab = findViewById(R.id.fab);
        containerSuggestions = (LinearLayout) findViewById(R.id.container_suggestions);
        buttonAddSuggestion = (ButtonRectangle) findViewById(R.id.button_add_suggestion);
        setTitle(R.string.title_activity_category);

        //get if read or not
        if (readSharedPreferences(getString(R.string.category_id) + categoryId) == 1) {
            setTitle(getTitle() + " " + getString(R.string.read));
            checkBefore = 1;
            checkAfter = 1;
        } else {
            setTitle(getTitle() + " " + getString(R.string.unread));
            checkBefore = 0;
            checkAfter = 0;
        }

        //add suggestion open a dialog
        buttonAddSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(CategoryActivity.this);
                final View promptsView = li.inflate(R.layout.dialog_add_suggestion, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CategoryActivity.this);
                alertDialogBuilder.setView(promptsView);
                editTextLibelle = (EditText) promptsView.findViewById(R.id.editTextLibelle);
                editTextEmail = (EditText) promptsView.findViewById(R.id.editTextEmail);
                editTextEmail.setText(readSharedPreferencesString(getString(R.string.user_email)));
                alertDialogBuilder
                        .setCancelable(true)
                        .setPositiveButton(R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        String libelle_fr = "";
                                        String libelle_en = "";
                                        String user_email = "";
                                        String user_language = "";

                                        if (editTextLibelle.getText() != null) {
                                            String lib = editTextLibelle.getText().toString();
                                            if (lib != null && lib.trim() != "" && lib.length() > 1) {
                                                //detect language
                                                if (frLanguage) {
                                                    libelle_fr = lib;
                                                    user_language = "fr";
                                                } else {
                                                    libelle_en = lib;
                                                    user_language = "en";
                                                }

                                                //email
                                                String email = editTextEmail.getText().toString();
                                                if (email != null && email.trim() != "" && email.length() > 1) {
                                                    user_email = email;
                                                    writeSharedPreferencesString(getString(R.string.user_email), user_email);
                                                }
                                                apiService.addProposition(libelle_fr, libelle_en, user_email, user_language, categoryId, new Callback<Integer>() {
                                                    @Override
                                                    public void success(Integer id, Response response) {
                                                        displayAlertDialog(getString(R.string.dialog_suggestion_success_title), getString(R.string.dialog_suggestion_success));
                                                    }

                                                    @Override
                                                    public void failure(RetrofitError error) {
                                                        displayErrorSnackBar(getString(R.string.dialog_suggestion_error));
                                                        Timber.e("Error add suggestion : " + error.getMessage());
                                                    }
                                                });
                                            }
                                        }
                                    }
                                });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setTitle(getString(R.string.button_add_suggestion));
                alertDialog.show();
            }
        });

        final FloatingActionButton floatButtonCheck = (FloatingActionButton) findViewById(R.id.fab);
        if (readSharedPreferences(getString(R.string.category_id) + categoryId) == 1) {
            floatButtonCheck.setColorNormal(getResources().getColor(R.color.accent));
        }
        //read/unread button
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTitle(R.string.title_activity_category);
                if (readSharedPreferences(getString(R.string.category_id) + categoryId) == 1) {
                    displayErrorSnackBar(getString(R.string.category_unread));
                    writeSharedPreferences(getString(R.string.category_id) + categoryId, 0);
                    setTitle(getTitle() + " " + getString(R.string.unread));
                    floatButtonCheck.setColorNormal(getResources().getColor(R.color.primary));
                    checkAfter = 0;
                } else {
                    displayErrorSnackBar(getString(R.string.category_read));
                    writeSharedPreferences(getString(R.string.category_id) + categoryId, 1);
                    setTitle(getTitle() + " " + getString(R.string.read));
                    floatButtonCheck.setColorNormal(getResources().getColor(R.color.accent));
                    checkAfter = 1;
                }

            }
        });
        mFabMargin = getResources().getDimensionPixelSize(R.dimen.margin_standard);
        ViewHelper.setScaleX(mFab, 0);
        ViewHelper.setScaleY(mFab, 0);

        ScrollUtils.addOnGlobalLayoutListener(mScrollView, new Runnable() {
            @Override
            public void run() {
                mScrollView.scrollTo(0, mFlexibleSpaceImageHeight - mActionBarSize);

                // If you'd like to start from scrollY == 0, don't write like this:
                mScrollView.scrollTo(0, 0);
                // The initial scrollY is 0, so it won't invoke onScrollChanged().
                // To do this, use the following:
                onScrollChanged(0, false, false);

                // You can also achieve it with the following codes.
                // This causes scroll change from 1 to 0.
                //mScrollView.scrollTo(0, 1);
                //mScrollView.scrollTo(0, 0);
            }
        });

        //analytics
        ReadingChallengeApplication application = (ReadingChallengeApplication) getApplication();
        Tracker mTracker = application.getDefaultTracker();
        mTracker.setScreenName("CategoryActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    /**
     * Display the category choosen.
     *
     * @param category
     */
    public void displayCategory(Category category) {
        //get imageView
        mImageView = findViewById(R.id.image);
        textViewTitle = (TextView) findViewById(R.id.text_view_title);
        textViewDescription = (TextView) findViewById(R.id.text_view_description);

        if (frLanguage) {
            //set title category
            if (category.getLibelle_fr() != null) {
                textViewTitle.setText(category.getLibelle_fr());
            }
            //set description category
            if (category.getDescription_fr() != null) {
                textViewDescription.setText(category.getDescription_fr());
            }
        } else {
            //set title category
            if (category.getLibelle_en() != null) {
                textViewTitle.setText(category.getLibelle_en());
            }
            //set description category
            if (category.getDescription_en() != null) {
                textViewDescription.setText(category.getDescription_en());
            }
        }

        //set image
        if (category.getImage() != null && !category.getImage().isEmpty()) {
            Picasso.with(getApplicationContext()).load(Utils.BASE_URL + Utils.URL_UPLOAD + category.getImage()).fit().centerCrop().into((ImageView) mImageView);
        } else {
            Picasso.with(getApplicationContext()).load(R.drawable.default_category).fit().centerCrop().into((ImageView) mImageView);
        }
    }

    /**
     * Display suggestion by choosen category from api.
     *
     * @param suggestions
     */
    public void displaySuggestions(List<Suggestion> suggestions) {
        textViewSuggestionsNone = (TextView) findViewById(R.id.text_view_suggestions_none);
        if (suggestions.size() > 0) {
            for (Suggestion s : suggestions) {

                //add row
                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.suggestion_row, null);
                final MaterialRippleLayout rippleView = (MaterialRippleLayout) addView.findViewById(R.id.suggestionRipple);
                //get fields
                TextView textViewSuggestion = (TextView) addView.findViewById(R.id.textViewSuggestion);
                //set fields
                final String libelle;
                final String url;
                if (frLanguage) {
                    libelle = s.getLibelle_fr();
                    url = s.getUrl_fr();
                } else {
                    libelle = s.getLibelle_en();
                    url = s.getUrl_en();
                }
                textViewSuggestion.setText(libelle);
                addView.setBackgroundResource(R.drawable.frame);
                addView.setPadding(10, 10, 20, 10);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(2, 5, 2, 5);
                //link to amazon search
                rippleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (url == null) {
                            displayErrorSnackBar(getString(R.string.activity_web_view_no_content));
                        } else {
                            Intent intent = new Intent(CategoryActivity.this, WebViewActivity.class);
                            intent.putExtra("url", url);
                            startActivity(intent);
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                        }
                    }
                });
                addView.setLayoutParams(lp);
                //add row to container
                containerSuggestions.addView(addView);
            }
        } else {
            textViewSuggestionsNone.setVisibility(View.VISIBLE);
        }
        Timber.d("Number of suggestions retrieved : " + suggestions.size());
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        // Translate overlay and image
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();
        ViewHelper.setTranslationY(mOverlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(mImageView, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));

        // Change alpha of overlay
        ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));

        // Scale title text
        float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
        ViewHelper.setPivotX(mTitleView, 0);
        ViewHelper.setPivotY(mTitleView, 0);
        ViewHelper.setScaleX(mTitleView, scale);
        ViewHelper.setScaleY(mTitleView, scale);

        // Translate title text
        int maxTitleTranslationY = (int) (mFlexibleSpaceImageHeight - mTitleView.getHeight() * scale);
        int titleTranslationY = maxTitleTranslationY - scrollY;
        ViewHelper.setTranslationY(mTitleView, titleTranslationY);

        // Translate FAB
        int maxFabTranslationY = mFlexibleSpaceImageHeight - mFab.getHeight() / 2;
        float fabTranslationY = ScrollUtils.getFloat(
                -scrollY + mFlexibleSpaceImageHeight - mFab.getHeight() / 2,
                mActionBarSize - mFab.getHeight() / 2,
                maxFabTranslationY);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            // On pre-honeycomb, ViewHelper.setTranslationX/Y does not set margin,
            // which causes FAB's OnClickListener not working.
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFab.getLayoutParams();
            lp.leftMargin = mOverlayView.getWidth() - mFabMargin - mFab.getWidth();
            lp.topMargin = (int) fabTranslationY;
            mFab.requestLayout();
        } else {
            ViewHelper.setTranslationX(mFab, mOverlayView.getWidth() - mFabMargin - mFab.getWidth());
            ViewHelper.setTranslationY(mFab, fabTranslationY);
        }

        // Show/hide FAB
        if (fabTranslationY < mFlexibleSpaceShowFabOffset) {
            hideFab();

        } else {
            showFab();
        }
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    private void showFab() {
        if (!mFabIsShown) {
            ViewPropertyAnimator.animate(mFab).cancel();
            ViewPropertyAnimator.animate(mFab).scaleX(1).scaleY(1).setDuration(200).start();
            mFabIsShown = true;
        }
    }

    private void hideFab() {
        if (mFabIsShown) {
            ViewPropertyAnimator.animate(mFab).cancel();
            ViewPropertyAnimator.animate(mFab).scaleX(0).scaleY(0).setDuration(200).start();
            mFabIsShown = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                backToPreviousActivity();
                finish();
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                return true;

            case R.id.action_share:
                TextView title = (TextView) findViewById(R.id.text_view_title);
                //share content
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String hashtag;
                if(frLanguage) {
                    hashtag = getString(R.string.action_share_hashtag_fr);
                }
                else {
                    hashtag = getString(R.string.action_share_hashtag_en);
                }
                sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.action_share_category) + " " + title.getText() + " " + hashtag);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        backToPreviousActivity();
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    protected void backToPreviousActivity() {
        Intent intent = new Intent();
        if (checkBefore != checkAfter) {
            intent.putExtra("checkChanged", true);
        }
        setResult(RESULT_OK, intent);
    }
}
