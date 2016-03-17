package com.ipalma.overkeyboardemoji;

import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import com.ipalma.overkeyboardemoji.EmojiKeyboardFragment.EmojiKeyboardListener;


public class MainActivity extends AppCompatActivity implements EmojiKeyboardListener {

    private EmojiKeyboardFragment emojiKeyboard;
    private RelativeLayout parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.parentLayout = (RelativeLayout) this.findViewById(R.id.activityParentLayout);
        this.emojiKeyboard = new EmojiKeyboardFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.emojiFragmentContainer, this.emojiKeyboard).commit();
        this.setKeyboardHeightListener();

        this.findViewById(R.id.my_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emojiKeyboard.toggleStickersView();
            }
        });
    }

    // Set the layout listener to detect when the keyboard is opened and its height.
    private void setKeyboardHeightListener() {
        this.parentLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                parentLayout.getWindowVisibleDisplayFrame(r);

                int screenHeight = parentLayout.getRootView().getHeight();
                int heightDifference = screenHeight - r.bottom;

                emojiKeyboard.onGlobalLayoutChange(heightDifference);
            }
        });
    }

    @Override
    public void onStickersVisibilityChange(boolean isVisible) {

    }

    @Override
    public void onStickersClick(String stickerId) {

    }
}
