package edu.vt.mobiledev.allmarketstracker

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import edu.vt.mobiledev.allmarketstracker.model.CryptoAsset

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 2
            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> CryptoListFragment()
                    1 -> PortfolioFragment()
                    else -> throw IllegalArgumentException("Invalid tab position")
                }
            }
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Market"
                1 -> "Portfolio"
                else -> null
            }
        }.attach()

        supportFragmentManager.addOnBackStackChangedListener {
            val isDetailVisible = supportFragmentManager.backStackEntryCount > 0
            val container = findViewById<FrameLayout>(R.id.fragment_container)

            if (isDetailVisible) {
                container.alpha = 0f
                container.visibility = View.VISIBLE
                container.animate().alpha(1f).setDuration(250).start()
            } else {
                container.animate()
                    .alpha(0f)
                    .setDuration(250)
                    .withEndAction {
                        container.visibility = View.GONE
                    }
                    .start()
            }
        }

    }

    fun showDetailFragment(asset: CryptoAsset) {
        findViewById<FrameLayout>(R.id.fragment_container).visibility = View.VISIBLE

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, CryptoDetailFragment.newInstance(asset))
            .addToBackStack(null)
            .commit()
    }
}
