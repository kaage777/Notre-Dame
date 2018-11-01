package ca.etsmtl.applets.etsmobile.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import ca.etsmtl.applets.etsmobile.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_profile.recyclerViewProfile
import kotlinx.android.synthetic.main.fragment_profile.swipeRefreshLayoutProfile
import javax.inject.Inject

/**
 * Displays the student's information (name, permanent code, balance, etc.)
 *
 * Created by Sonphil on 15-03-18.
 */

class ProfileFragment : DaggerFragment() {

    private val profileViewModel: ProfileViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(ProfileViewModel::class.java)
    }
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val adapter: GroupAdapter<ViewHolder> = GroupAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_profile, container, false)

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if (isVisibleToUser) {
            setUpSwipeRefresh()
            setUpRecyclerView()
            subscribeUI()
        }
    }

    private fun setUpSwipeRefresh() {
        swipeRefreshLayoutProfile.setColorSchemeResources(R.color.colorPrimary)
        swipeRefreshLayoutProfile.setOnRefreshListener { profileViewModel.refresh() }
    }

    private fun setUpRecyclerView() {
        recyclerViewProfile.adapter = adapter
        recyclerViewProfile.setHasFixedSize(true)
    }

    private fun subscribeUI() {
        profileViewModel.profile.observe(this, Observer<List<Section>> {
            it?.let { adapter.update(it) }
        })
        profileViewModel.loading.observe(this, Observer<Boolean> {
            it?.let { swipeRefreshLayoutProfile.isRefreshing = it }
        })
        this.lifecycle.addObserver(profileViewModel)
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}
