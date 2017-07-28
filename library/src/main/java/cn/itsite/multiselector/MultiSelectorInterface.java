package cn.itsite.multiselector;

import java.util.List;

/**
 * Created by leguang on 2017/7/28 0028.
 * Email：langmanleguang@qq.com
 */

public interface MultiSelectorInterface {
    interface OnItemClickListener {
        /**
         * This method will be invoked when the item clicked.
         *
         * @param pagerPosition  current page positon.
         * @param optionPosition The position of the item in the list that was clicked.
         * @param option         The item in the list that was clicked.
         */
        void onItemClick(int pagerPosition, int optionPosition, CharSequence option);
    }

    interface OnSelectedListener {
        /**
         * This method will be invoked when the item clicked.
         *
         * @param select the collection that selected.
         */
        void onSelect(List<CharSequence> select);
    }
}
