package www.ahest.cn.base.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import www.ahest.cn.base.base.BaseFragment

class MenuFragmentAdapter(fragmentManager: FragmentManager, val mFragments: List<BaseFragment>) :
    FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

    override fun getCount(): Int {
        return mFragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return ""
    }
}