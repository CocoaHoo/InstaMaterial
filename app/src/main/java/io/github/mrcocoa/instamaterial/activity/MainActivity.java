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
public class MainActivity extends ActionBarActivity {
    private static final int ANIM_DURATION_TOOLBAR = 300;
    private static final int ANIM_DELAY_TOOLBAR = 300;

    private static final int ANIM_DURATION_LOGO = 400;
    private static final int ANIM_DELAY_LOGO = 400;

    private static final int ANIM_DURATION_FAB = 400;
    private static final int ANIM_DELAY_FAB = 400;

    private static final int ANIM_DURATION_MENUITEM = 300;
    private static final int ANIM_DELAY_MENUITEM = 500;

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.ivLogo) ImageView ivLogo;
    @Bind(R.id.rvFeed) RecyclerView rvFeed;
    @Bind(R.id.btnCreate) ImageButton btnCreate;

    private MenuItem inboxMenuItem;
    private FeedAdapter feedAdapter;

    private boolean pendingIntroAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupToolbar();
        setupFeed();

        if (savedInstanceState == null){
            pendingIntroAnimation = true;
        }
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white);
    }

    private void setupFeed() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvFeed.setLayoutManager(linearLayoutManager);
        feedAdapter = new FeedAdapter(this);
        rvFeed.setAdapter(feedAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        inboxMenuItem = menu.findItem(R.id.action_inbox);
        inboxMenuItem.setActionView(R.layout.menu_item_view);
        if (pendingIntroAnimation){
            pendingIntroAnimation = false;
            startIntroAnimation();
        }
        return true;
    }

    private void startIntroAnimation() {
        btnCreate.setTranslationY(2 * getResources().getDimensionPixelOffset(R.dimen.btn_fab_size));

        int actionbarSize = Utils.dpToPx(56);
        toolbar.setTranslationY(-actionbarSize);
        ivLogo.setTranslationY(-actionbarSize);
        inboxMenuItem.getActionView().setTranslationY(-actionbarSize);

        toolbar.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(ANIM_DELAY_TOOLBAR);

        ivLogo.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_LOGO)
                .setStartDelay(ANIM_DELAY_LOGO);

        inboxMenuItem.getActionView().animate()
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
        btnCreate.animate()
                .translationY(0)
                .setInterpolator(new OvershootInterpolator(1.f))
                .setStartDelay(ANIM_DELAY_FAB)
                .setDuration(ANIM_DURATION_FAB)
                .start();
        feedAdapter.updateItems();
    }
}
