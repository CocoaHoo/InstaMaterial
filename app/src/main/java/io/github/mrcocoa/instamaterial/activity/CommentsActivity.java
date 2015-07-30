package io.github.mrcocoa.instamaterial.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.mrcocoa.instamaterial.R;
import io.github.mrcocoa.instamaterial.adapter.CommentsAdapter;
import io.github.mrcocoa.instamaterial.utils.Utils;

public class CommentsActivity extends ActionBarActivity {

    public static final String ARG_DRAWING_START_LOCATION = "arg_drawing_start_location";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.contentRoot)
    LinearLayout mContentRoot;
    @Bind(R.id.rvComments)
    RecyclerView mRvComments;
    @Bind(R.id.llAddComment)
    LinearLayout mLlAddComment;

    private int drawingStartLocation;
    private CommentsAdapter mCommentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ButterKnife.bind(this);
        setupToolbar();
        setupComments();
        drawingStartLocation = getIntent().getIntExtra(ARG_DRAWING_START_LOCATION, 0);
        if (savedInstanceState == null) {
            mContentRoot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mContentRoot.getViewTreeObserver().removeOnPreDrawListener(this);
                    startIntroAnimation();
                    return true;
                }
            });
        }

    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_menu_white);
    }

    private void setupComments() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRvComments.setLayoutManager(linearLayoutManager);
        mRvComments.setHasFixedSize(true);

        mCommentsAdapter = new CommentsAdapter(this);
        mRvComments.setAdapter(mCommentsAdapter);
        mRvComments.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    mCommentsAdapter.setAnimationsLocked(true);
                }
            }
        });

    }

    private void startIntroAnimation() {
        mContentRoot.setScaleY(0.1f);
        mContentRoot.setPivotY(drawingStartLocation);
        mLlAddComment.setTranslationY(100);

        mContentRoot.animate()
                .scaleY(1)
                .setDuration(200)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animateContent();
                    }
                })
                .start();
    }

    @Override
    public void onBackPressed() {
        mContentRoot.animate()
                .translationY(Utils.getScreenHeight(this))
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        CommentsActivity.super.onBackPressed();
                        overridePendingTransition(0, 0);
                    }
                }).start();
    }

    private void animateContent() {
        mCommentsAdapter.updateItems();
        mLlAddComment.animate().translationY(0)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(200)
                .start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem inboxMenuItem = menu.findItem(R.id.action_inbox);
        inboxMenuItem.setActionView(R.layout.menu_item_view);
        return true;
    }

    @OnClick(R.id.btnSendComment)
    public void onSendCommentClick() {
        mCommentsAdapter.addItem();
        mCommentsAdapter.setAnimationsLocked(false);
        mCommentsAdapter.setDelayEnterAnimation(false);
        mRvComments.smoothScrollBy(0, mRvComments.getChildAt(0).getHeight() * mCommentsAdapter.getItemCount());
    }
}
