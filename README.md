# MultiSelector

[![Release](https://jitpack.io/v/leguang/MultiSelector.svg)](https://jitpack.io/#leguang/MultiSelector)

仿京东的地址选择器，只是多了一个每一级的滑动切换效果，使用TabLayout+ViewPager实现。
## 能做什么？([下载 apk](https://github.com/leguang/MultiSelector/blob/master/app-debug.apk))
- **提供自定View和Dialog的两种形式**
- **可配置级联数**
- **简洁的API，简单的配置**

## 如何使用它？

1. 先在项目目录下的的build.gradle 的 repositories 添加:
```
	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```

2. 然后在App目录下的dependencies添加:
```
	dependencies {
	     //多级选择器
   		 compile 'com.github.leguang:MultiSelector:1.4'
	}
```
此时同步一下，即已完成引入。

3. 代码中简单使用：

自定义View的使用如下：首先在XML文件中配置
```
 <cn.itsite.multiselector.MultiSelectorView
        android:id="@+id/multiSelectorView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:indicatorColor="@color/colorAccent"
        app:level="3"
        app:mode="scrollable"
        app:nomalColor="@color/base_black"
        app:selectedColor="@color/colorAccent"
        app:tabText="选择" />
```
在代码中使用：
```
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
```

在对话框中使用：
```
 dialog = MultiSelectorDialog.builder(MainActivity.this)
                .setIndicatorColor(0xFFFF0000)
                .setNomalColor(0xFF000000)
                .setSelectedColor(0xFFFF0000)
                .setTabVisible(true)
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
                }).build();
```

>**持续更新!，欢迎Issues+Star项目**

## 感谢
感谢京东给出这么优秀的交互^_^

## License

```
Copyright 2016 李勇

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

```
