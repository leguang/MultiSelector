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
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

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
        params.height = (int) (getContext().getResources().getDisplayMetrics().heightPixels * 0.618F + 0.5F);
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
        private MultiSelectorView.OnItemClickListener mOnItemClickListener;
        private MultiSelectorView.OnSelectedListener mOnSelectedListener;

        public Builder(@NonNull Context context) {
            this.context = context;
        }

        public Builder setTitle(@Nullable CharSequence title) {
            this.title = title;
            return this;
        }

        public Builder setTitle(@StringRes int textId) {
            this.title = context.getText(textId);
            return this;
        }

        public Builder setTitleColor(@ColorInt int color) {
            this.titleColor = color;
            return this;
        }

        public Builder setNomalColor(@ColorInt int color) {
            this.nomalColor = color;
            return this;
        }

        public Builder setSelectedColor(@ColorInt int color) {
            this.selectedColor = color;
            return this;
        }

        public Builder setTabMode(@TabLayout.Mode int mode) {
            this.mode = mode;
            return this;
        }

        public Builder setIndicatorColor(int colorId) {
            this.indicatorColor = colorId;
            return this;
        }

        public Builder setLevel(int level) {
            this.level = level;
            return this;
        }

        public Builder setOnItemClickListener(@Nullable MultiSelectorView.OnItemClickListener listener) {
            this.mOnItemClickListener = listener;
            return this;
        }

        public Builder setOnSelectedListener(@Nullable MultiSelectorView.OnSelectedListener listener) {
            this.mOnSelectedListener = listener;
            return this;
        }

        public MultiSelectorDialog build() {
            final MultiSelectorDialog dialog = new MultiSelectorDialog(context);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_selector, null);

            //初始化Title部分。
            AppCompatTextView tvTitle = (AppCompatTextView) view.findViewById(R.id.tv_title);
            tvTitle.setText(title);
            tvTitle.setTextColor(titleColor);

            //初始化点击取消部分。
            ImageView ivCancel = (ImageView) view.findViewById(R.id.iv_cancel);
            ivCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            //初始化级联View
            MultiSelectorView multiSelectorView = (MultiSelectorView) view.findViewById(R.id.multiSelectorView);
            multiSelectorView.setLevel(level)
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
