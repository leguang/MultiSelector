package cn.itsite.multiselector.demo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.itsite.multiselector.MultiSelectorDialog;
import cn.itsite.multiselector.MultiSelectorInterface;
import cn.itsite.multiselector.MultiSelectorView;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private LinearLayout llContainer;
    private MultiSelectorDialog dialog;
    private MultiSelectorView multiSelectorView;
    private EditText etLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initDialog();
        initListener();

        findViewById(R.id.bt_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiSelectorView.setLevel(Integer.valueOf(etLevel.getText().toString()));
                //模拟网络延迟。
                multiSelectorView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        multiSelectorView.notifyDataSetChanged(getData(0));
                    }
                }, 1000);
            }
        });

        findViewById(R.id.bt_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDialog();
                dialog.show();
            }
        });
    }

    private void initView() {
        llContainer = (LinearLayout) findViewById(R.id.ll_container);
        etLevel = (EditText) findViewById(R.id.et_level);
        multiSelectorView = (MultiSelectorView) findViewById(R.id.multiSelectorView);
    }

    private void initDialog() {
        dialog = MultiSelectorDialog.builder(MainActivity.this)
                .setIndicatorColor(0xFFFF0000)
                .setNomalColor(0xFF000000)
                .setSelectedColor(0xFFFF0000)
                .setTitle("设置你的标题")
                .setTabVisible(false)
                .setLevel(Integer.valueOf(etLevel.getText().toString()))
                .setTabText("选择")
                .setOnItemClickListener(new MultiSelectorInterface.OnItemClickListener() {
                    @Override
                    public void onItemClick(final int pagerPosition, int optionPosition, CharSequence option) {
                        ToastUtils.showToast(MainActivity.this
                                , "pagerPosition-->" + pagerPosition + "\r\noptionPosition-->" + optionPosition + "\r\noption-->" + option);

                        //模拟网络延迟。
                        multiSelectorView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.notifyDataSetChanged(getData(pagerPosition + 1));
                            }
                        }, 1000);
                    }
                })
                .setOnSelectedListener(new MultiSelectorInterface.OnSelectedListener() {
                    @Override
                    public void onSelect(List<CharSequence> select) {
                        dialog.dismiss();
                        for (CharSequence s : select) {
                            Log.e(TAG, s.toString());
                        }
                    }
                }).show();

        //模拟网络延迟。
        multiSelectorView.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.notifyDataSetChanged(getData(0));

            }
        }, 1000);
    }

    private void initListener() {
        multiSelectorView.setOnItemClickListener(new MultiSelectorInterface.OnItemClickListener() {
            @Override
            public void onItemClick(final int pagerPosition, int optionPosition, CharSequence option) {
                ToastUtils.showToast(MainActivity.this
                        , "pagerPosition-->" + pagerPosition + "\r\noptionPosition-->" + optionPosition + "\r\noption-->" + option);

                //模拟网络延迟。
                multiSelectorView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        multiSelectorView.notifyDataSetChanged(getData(pagerPosition + 1));
                    }
                }, 1000);

            }
        }).setOnSelectedListener(new MultiSelectorInterface.OnSelectedListener() {
            @Override
            public void onSelect(List<CharSequence> select) {
                dialog.dismiss();
                for (CharSequence s : select) {
                    Log.e(TAG, s.toString());
                }
            }
        });
    }

    /**
     * 模拟获取的网络数据。
     *
     * @param currentPager
     * @return
     */
    public List<String> getData(int currentPager) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            list.add("第" + currentPager + "页" + "第" + i + "个");
        }
        return list;
    }

    public static class ToastUtils {
        public static Toast mToast;

        public static void showToast(Context mContext, String msg) {
            if (mToast == null) {
                mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
            }
            mToast.setText(msg);
            mToast.show();
        }
    }
}
