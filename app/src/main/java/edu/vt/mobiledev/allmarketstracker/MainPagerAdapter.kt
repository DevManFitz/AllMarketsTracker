package edu.vt.mobiledev.allmarketstracker

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment = when(position) {
        0 -> CryptoListFragment()
        1 -> PortfolioFragment()
        else -> throw IllegalArgumentException("Invalid position")
    }
}