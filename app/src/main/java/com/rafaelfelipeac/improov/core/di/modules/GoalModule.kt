package com.rafaelfelipeac.improov.core.di.modules

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.improov.core.di.modules.viewModel.ViewModelKey
import com.rafaelfelipeac.improov.core.di.modules.viewModel.FragmentKey
import com.rafaelfelipeac.improov.features.goal.data.repository.*
import com.rafaelfelipeac.improov.features.goal.domain.repository.*
import com.rafaelfelipeac.improov.features.goal.presentation.goaldetail.GoalDetailFragment
import com.rafaelfelipeac.improov.features.goal.presentation.goaldetail.GoalDetailViewModel
import com.rafaelfelipeac.improov.features.goal.presentation.goalform.GoalFormFragment
import com.rafaelfelipeac.improov.features.goal.presentation.goalform.GoalFormViewModel
import com.rafaelfelipeac.improov.features.goal.presentation.goallist.GoalListFragment
import com.rafaelfelipeac.improov.features.goal.presentation.goallist.GoalListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class GoalModule {

    @Binds
    abstract fun goalRepository(goalRepositoryImpl: GoalRepositoryImpl): GoalRepository

    @Binds
    abstract fun itemRepository(itemRepositoryImpl: ItemRepositoryImpl): ItemRepository

    @Binds
    abstract fun historicRepository(historicRepositoryImpl: HistoricRepositoryImpl): HistoricRepository

    @Binds
    abstract fun firstTimeAddRepository(firstTimeAddRepositoryImpl: FirstTimeAddRepositoryImpl): FirstTimeAddRepository

    @Binds
    abstract fun firstTimeListRepository(firstTimeListRepositoryImpl: FirstTimeListRepositoryImpl): FirstTimeListRepository

    @Binds
    @IntoMap
    @FragmentKey(GoalListFragment::class)
    abstract fun bindGoalListFragment(goalListFragment: GoalListFragment): Fragment

    @Binds
    @IntoMap
    @ViewModelKey(GoalListViewModel::class)
    abstract fun bindGoalListViewModel(goalListViewModel: GoalListViewModel): ViewModel

    @Binds
    @IntoMap
    @FragmentKey(GoalDetailFragment::class)
    abstract fun bindGoalDetailFragment(goalDetailFragment: GoalDetailFragment): Fragment

    @Binds
    @IntoMap
    @ViewModelKey(GoalDetailViewModel::class)
    abstract fun bindGoalDetailViewModel(goalDetailViewModel: GoalDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @FragmentKey(GoalFormFragment::class)
    abstract fun bindGoalFormFragment(goalFormFragment: GoalFormFragment): Fragment

    @Binds
    @IntoMap
    @ViewModelKey(GoalFormViewModel::class)
    abstract fun bindGoalFormViewModel(goalFormViewModel: GoalFormViewModel): ViewModel
}
