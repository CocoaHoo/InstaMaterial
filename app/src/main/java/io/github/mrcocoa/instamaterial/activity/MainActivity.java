package io.github.mrcocoa.instamaterial.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.mrcocoa.instamaterial.R;
import io.github.mrcocoa.instamaterial.adapter.FeedAdapter;
import io.github.mrcocoa.instamaterial.utils.Utils;

/**
 * Created by cocoa on 15/7/28.
 * Email:cocoahoo@gmail.com
 **/
public class MainActivity extends ActionBarActivity implements FeedAdapter.OnFeedItemClickListener {
    private static final int ANIM_DURATION_TOOLBAR = 300;
    private static final int ANIM_DELAY_TOOLBAR = 300;

    private static final int ANIM_DURATION_LOGO = 400;
    private static final int ANIM_DELAY_LOGO = 400;

    private static final int ANIM_DURATION_FAB = 400;
    private static final int ANIM_DELAY_FAB = 400;

    private static final int ANIM_DURATION_MENUITEM = 300;
    private static final int ANIM_DELAY_MENUITEM = 500;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.ivLogo)
    ImageView mIvLogo;
    @Bind(R.id.rvFeed)
    RecyclerView mRvFeed;
    @Bind(R.id.btnCreate)
    ImageButton mBtnCreate;

    private MenuItem mInboxMenuItem;
    private FeedAdapter mFeedAdapter;

    private boolean pendingIntroAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupToolbar();
        setupFeed();

        if (savedInstanceState == null) {
            pendingIntroAnimation = true;
        }
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_menu_white);
    }

    private void setupFeed() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRvFeed.setLayoutManager(linearLayoutManager);
        mFeedAdapter = new FeedAdapter(this);
        mFeedAdapter.setOnFeedItemClickListener(this);
        mRvFeed.setAdapter(mFeedAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mInboxMenuItem = menu.findItem(R.id.action_inbox);
        mInboxMenuItem.setActionView(R.layout.menu_item_view);
        if (pendingIntroAnimation) {
            pendingIntroAnimation = false;
            startIntroAnimation();
        }
        return true;
    }

    private void startIntroAnimation() {
        mBtnCreate.setTranslationY(2 * getResources().getDimensionPixelOffset(R.dimen.btn_fab_size));

        int actionbarSize = Utils.dpToPx(56);
        mToolbar.setTranslationY(-actionbarSize);
        mIvLogo.setTranslationY(-actionbarSize);
        mInboxMenuItem.getActionView().setTranslationY(-actionbarSize);

        mToolbar.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(ANIM_DELAY_TOOLBAR);

        mIvLogo.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_LOGO)
                .setStartDelay(ANIM_DELAY_LOGO);

        mInboxMenuItem.getActionView().animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_MENUITEM)
                .setStartDelay(ANIM_DELAY_MENUITEM)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        startContentAnimation();
                    }
                }).start();
    }

    private void startContentAnimation() {
        mBtnCreate.animate()
                .translationY(0)
                .setInterpolator(new OvershootInterpolator(1.f))
                .setStartDelay(ANIM_DELAY_FAB)
                .setDuration(ANIM_DURATION_FAB)
                .start();
        mFeedAdapter.updateItems();
    }

    @Override
    public void onCommentsClick(View v, int position) {
        final Intent intent = new Intent(this, CommentsActivity.class);
        int[] startingLocation = new int[2];
        v.getLocationOnScreen(startingLocation);
        intent.putExtra(CommentsActivity.ARG_DRAWING_START_LOCATION, startingLocation[1]);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
