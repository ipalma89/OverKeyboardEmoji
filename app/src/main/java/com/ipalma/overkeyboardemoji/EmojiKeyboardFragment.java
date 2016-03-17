package com.ipalma.overkeyboardemoji;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

/**
 *
 */
public class EmojiKeyboardFragment extends Fragment {
    private Context context;
    private View popupView;
    private PopupWindow popupWindow;
    private int keyboardHeight = 50;
    private LinearLayout parentLayout;
    private boolean isKeyboardVisible;
    private int previousHeightDifference;
    private GridView gridView;
    private EmojiKeyboardListener listener;

    public EmojiKeyboardFragment() {/* Required empty public constructor */}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_emoji_keyboard, container, false);
        this.parentLayout = (LinearLayout) v.findViewById(R.id.emojiKeyboardParentLayout);
//        this.setHeight((int) getResources().getDimension(R.dimen.emojis_default_height));

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;

        try {
            this.listener = (EmojiKeyboardListener) context;
        } catch(ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement EmojiKeyboardListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // If all the components were already initialized just return.
        if (this.popupWindow != null && this.popupView != null && this.gridView != null /*&&
                this.emojis != null*/) {
            return;
        }

        // Create popup view and show it at the bottom of the activity (over the keyboard)
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.popupView = inflater.inflate(R.layout.popup_emoji_keyboard, null);
        this.popupWindow = new PopupWindow(this.popupView, ViewGroup.LayoutParams.MATCH_PARENT, this.keyboardHeight, false);
        this.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setParentLayoutVisibility(false);
            }
        });

        this.gridView = (GridView) this.popupView.findViewById(R.id.stickers_grid);

//        this.loadOrFetchStickers();
    }

    @Override
    public void onStop() {
        super.onStop();
        this.hideStickers();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
        this.listener = null;
    }

    private void setHeight(int height) {
        this.keyboardHeight = height;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, this.keyboardHeight);
        this.parentLayout.setLayoutParams(params);
    }

    private void setPopupHeight(int height) {
        this.popupWindow.setHeight(height);
    }

    private void setParentLayoutVisibility(boolean isVisible) {
        this.parentLayout.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void showStickers() {//TODO parameter activity and id?
        this.popupWindow.showAtLocation(this.getActivity().findViewById(R.id.activityParentLayout), Gravity.BOTTOM, 0, 0);

        if (this.listener != null) {
            this.listener.onStickersVisibilityChange(true);
        }
    }

    public void hideStickers() {
        if (this.popupWindow.isShowing()){
            this.popupWindow.dismiss();
        }

        if (this.listener != null) {
            this.listener.onStickersVisibilityChange(false);
        }
    }

    public void toggleStickersView() {
        if (!this.popupWindow.isShowing()) {

            this.setPopupHeight(this.keyboardHeight);
            if (this.isKeyboardVisible) {
                this.setParentLayoutVisibility(false);
            } else {
                this.setParentLayoutVisibility(true);
            }
            this.showStickers();
        } else {
            this.hideStickers();
        }
    }

    public void onGlobalLayoutChange(int heightDifference) {
        if (this.previousHeightDifference - heightDifference > 50) {
            this.hideStickers();
        }

        this.previousHeightDifference = heightDifference;

        if (heightDifference > 100) {
            this.isKeyboardVisible = true;
            this.setHeight(heightDifference);
        } else {
            this.isKeyboardVisible = false;
        }
    }

    public interface EmojiKeyboardListener {
        void onStickersVisibilityChange(boolean isVisible);
        void onStickersClick(String stickerId);
    }
}
