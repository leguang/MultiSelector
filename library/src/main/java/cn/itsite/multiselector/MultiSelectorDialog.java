package cn.itsite.multiselector;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.design.widget.TabLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


/**
 * Created by leguang on 2017/7/12 0012.
 * Email：langmanleguang@qq.com
 * <p>
 * 一个级联多选择Dialog，适合地区选择等场景。
 */
public class MultiSelectorDialog extends Dialog {
    private static final int DEFAULT_THEME_ID = R.style.BottomDialogStyle;
    private MultiSelectorView mMultiSelectorView;

    private MultiSelectorDialog(@NonNull Context context) {
        this(context, DEFAULT_THEME_ID);
    }

    private MultiSelectorDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
    }

    /**
     * init the attributes of window.
     */
    private void initWindow() {
        Window window = getWindow();
        if (window == null) {
            throw new IllegalStateException("the content view of selector has not been attach to window yet.");
        }
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = (int) (getContext().getResources().getDisplayMetrics().heightPixels * 0.5F);
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
    }

    public static class Builder {
        private Context context;
        private CharSequence title;
        private int titleColor = 0xFF222222;
        private int mode;
        private int nomalColor = 0xFF222222;
        private int selectedColor = 0xFFFF0000;
        private int indicatorColor = 0xFFFF0000;
        private int level = 1;
        private CharSequence tabText = "请选择";
        private boolean tabVisible;
        private MultiSelectorInterface.OnItemClickListener mOnItemClickListener;
        private MultiSelectorInterface.OnSelectedListener mOnSelectedListener;

        public Builder(@NonNull Context context) {
            this.context = context;
        }

        /**
         * Set the title displayed in the {@link Dialog}.
         *
         * @param title CharSequence.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setTitle(@Nullable CharSequence title) {
            this.title = title;
            return this;
        }

        /**
         * Set the title using the given resource id.
         *
         * @param textId resource id.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setTitle(@StringRes int textId) {
            this.title = context.getText(textId);
            return this;
        }

        /**
         * Set the color of title using the given resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setTitleColor(@ColorInt int color) {
            this.titleColor = color;
            return this;
        }

        /**
         * Set the color of item and indicator using the given color on nomal state.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setNomalColor(@ColorInt int color) {
            this.nomalColor = color;
            return this;
        }

        /**
         * Set the color of item and indicator using the given color on selected state.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setSelectedColor(@ColorInt int color) {
            this.selectedColor = color;
            return this;
        }

        /**
         * Set the mode of the tab like the {@link TabLayout}.
         *
         * @param mode one of {@link TabLayout.MODE_FIXED} or {@link TabLayout.MODE_SCROLLABLE}.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setTabMode(@TabLayout.Mode int mode) {
            this.mode = mode;
            return this;
        }

        /**
         * Set the tab indicator's color for the currently selected tab.
         *
         * @param colorId color to use for the indicator
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setIndicatorColor(int colorId) {
            this.indicatorColor = colorId;
            return this;
        }

        /**
         * Set the steps of the selector.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setLevel(int level) {
            this.level = level;
            return this;
        }

        /**
         * Set the tips of the tab.
         *
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setTabText(CharSequence text) {
            this.tabText = text;
            return this;
        }

        /**
         * Set the visible of the tab.
         *
         * @param tabVisible true is visible,false is gone.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public Builder setTabVisible(boolean tabVisible) {
            this.tabVisible = tabVisible;
            return this;
        }

        /**
         * Set a listener to be invoked when the item in the list is pressed.
         *
         * @param listener The {@link MultiSelectorInterface.OnItemClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setOnItemClickListener(@Nullable MultiSelectorInterface.OnItemClickListener listener) {
            this.mOnItemClickListener = listener;
            return this;
        }

        /**
         * Set a listener to be invoked when the last item in the list is pressed.
         *
         * @param listener The {@link MultiSelectorInterface.OnSelectedListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setOnSelectedListener(@Nullable MultiSelectorInterface.OnSelectedListener listener) {
            this.mOnSelectedListener = listener;
            return this;
        }

        public MultiSelectorDialog build() {
            final MultiSelectorDialog dialog = new MultiSelectorDialog(context);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_selector, null);

            //init Title.
            TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvTitle.setText(title);
            tvTitle.setTextColor(titleColor);

            //set click dismiss dialog.
            ImageView ivCancel = (ImageView) view.findViewById(R.id.iv_cancel);
            ivCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            //init MultiSelectorView object.
            MultiSelectorView multiSelectorView = (MultiSelectorView) view.findViewById(R.id.multiSelectorView);
            multiSelectorView.setLevel(level)
                    .setTabText(tabText)
                    .setTabVisible(tabVisible)
                    .setTabTextColors(nomalColor, selectedColor)
                    .setTabMode(mode)
                    .setIndicatorColor(indicatorColor)
                    .setOnItemClickListener(mOnItemClickListener)
                    .setOnSelectedListener(mOnSelectedListener);

            dialog.mMultiSelectorView = multiSelectorView;
            dialog.setContentView(view);
            return dialog;
        }

        public MultiSelectorDialog show() {
            final MultiSelectorDialog dialog = build();
            dialog.show();
            return dialog;
        }
    }

    public static Builder builder(Context context) {
        return new Builder(context);
    }

    public void notifyDataSetChanged(List<String> date) {
        if (mMultiSelectorView != null) {
            mMultiSelectorView.notifyDataSetChanged(date);
        }
    }
}
