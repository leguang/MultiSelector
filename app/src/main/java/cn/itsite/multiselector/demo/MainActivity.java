package cn.itsite.multiselector.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import cn.itsite.multiselector.MultiSelectorDialog;
import cn.itsite.multiselector.MultiSelectorView;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private MultiSelectorView msv;
    private Button bt;
    private LinearLayout ll;
    private MultiSelectorDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ll = (LinearLayout) findViewById(R.id.ll);
        bt = (Button) findViewById(R.id.bt);
        dialog = MultiSelectorDialog.builder(MainActivity.this)
                .setIndicatorColor(0xFFFF0000)
                .setNomalColor(0xFF000000)
                .setSelectedColor(0xFFFF0000)
                .setTitle("请选择地区")
                .setLevel(3)
                .setTabText("我的我的")
                .setOnItemClickListener(new MultiSelectorView.OnItemClickListener() {
                    @Override
                    public void onItemClick(int currentPager, int selectedItem, CharSequence item) {
                        dialog.notifyDataSetChanged(getData(currentPager));

                    }
                })
                .setOnSelectedListener(new MultiSelectorView.OnSelectedListener() {
                    @Override
                    public void onSelect(List<CharSequence> select) {
                        dialog.dismiss();
                        for (CharSequence s : select) {
                            KLog.e(s);
                        }

                    }
                }).show();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();

                dialog.notifyDataSetChanged(getData(1));

            }
        });


        findViewById(R.id.bt1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.notifyDataSetChanged(getData(1));
            }
        });

        msv = (MultiSelectorView) findViewById(R.id.msv);


        msv.setOnItemClickListener(new MultiSelectorView.OnItemClickListener() {
            @Override
            public void onItemClick(int pagerPosition, int optionPosition, CharSequence option) {

                KLog.e("TAG", "pagerPosition-->" + pagerPosition);
                KLog.e("TAG", "optionPosition-->" + optionPosition);
                KLog.e("TAG", "option-->" + option);


                msv.notifyDataSetChanged(getData(pagerPosition));

            }
        });

        msv.setLevel(3);


        msv.postDelayed(new Runnable() {
            @Override
            public void run() {
                msv.notifyDataSetChanged(getData(0));

            }
        }, 500);


    }

    public List<String> getData(int currentPager) {
        KLog.e(TAG, "getData-->" + currentPager);

        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 100; i++) {
            list.add(currentPager + "--" + i);
        }

        return list;
    }
}
