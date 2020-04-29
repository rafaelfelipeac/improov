package com.rafaelfelipeac.improov.features.profile.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.gone
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment() {

    private val profileViewModel by lazy { viewModelFactory.get<ProfileViewModel>(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)
    }

    override fun onResume() {
        super.onResume()

        if (preferences.name.isNotEmpty()) {
            profile_user_name.text = preferences.name
        } else {
            profile_user_name.gone()
            profile_edit_profile_button.text = getString(R.string.profile_add_name_message)
        }

        main.closeToolbar()
        main.closeBottomSheetTips()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        main.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        main.supportActionBar?.title = getString(R.string.profile_title)

        showNavigation()

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profile_show_welcome_button.setOnClickListener {
            preferences.welcome = false
            preferences.fistTimeAdd = true
            preferences.fistTimeList = false

            navController.navigate(ProfileFragmentDirections.actionNavigationProfileToNavigationWelcome())
        }

        fab.setOnClickListener {
            main.closeBottomSheetTips()

            navController.navigate(ProfileFragmentDirections.actionNavigationProfileToNavigationGoalForm())
        }

        profile_edit_profile_button.setOnClickListener {
            navController.navigate(R.id.action_navigation_profile_to_navigation_profile_edit)
        }

        profile_settings_button.setOnClickListener {
            navController.navigate(R.id.action_navigation_profile_to_navigation_settings)
        }
    }
}
