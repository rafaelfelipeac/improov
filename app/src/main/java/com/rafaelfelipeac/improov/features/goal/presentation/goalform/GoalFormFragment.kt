package com.rafaelfelipeac.improov.features.goal.presentation.goalform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.observe
import com.rafaelfelipeac.improov.core.extension.visible
import com.rafaelfelipeac.improov.core.extension.invisible
import com.rafaelfelipeac.improov.core.extension.gone
import com.rafaelfelipeac.improov.core.extension.resetValue
import com.rafaelfelipeac.improov.core.extension.toFloat
import com.rafaelfelipeac.improov.core.extension.fieldIsEmptyOrZero
import com.rafaelfelipeac.improov.core.extension.checkIfFieldIsEmptyOrZero
import com.rafaelfelipeac.improov.core.extension.getNumberInRightFormat
import com.rafaelfelipeac.improov.core.extension.isNotEmpty
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import com.rafaelfelipeac.improov.features.commons.DialogOneButton
import com.rafaelfelipeac.improov.features.goal.data.enums.GoalType
import com.rafaelfelipeac.improov.features.goal.domain.model.Goal
import com.rafaelfelipeac.improov.features.goal.domain.model.Historic
import com.rafaelfelipeac.improov.features.goal.domain.model.Item
import kotlinx.android.synthetic.main.fragment_goal_form.*
import java.util.Calendar

@Suppress("TooManyFunctions")
class GoalFormFragment : BaseFragment() {

    private var goal: Goal =
        Goal()
    private var goalId: Long? = null
    private var items: List<Item> = listOf()
    private var historics: List<Historic> = listOf()

    private var firstTimeAdd = false

    private var goalsSize: Int? = null

    private var cal = Calendar.getInstance()

    private val viewModel by lazy { viewModelFactory.get<GoalFormViewModel>(this) }

    private val stateObserver = Observer<GoalFormViewModel.ViewState> { response ->
        if (response.goalSaved) {
            navController.navigateUp()
        }

        if (response.goal.goalId > 0) {
            goal = response.goal
            items = response.items
            historics = response.historics

            setupGoal()
        }

        goalsSize = response.goals.size

        firstTimeAdd = response.firstTimeAddLoaded
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)

        goalId = arguments?.let { GoalFormFragmentArgs.fromBundle(it).goalId }

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        main.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        main.supportActionBar?.title = getString(R.string.goal_form_title)
        main.toolbar.inflateMenu(R.menu.menu_save)

        hideNavigation()

        return inflater.inflate(R.layout.fragment_goal_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(viewModel.stateLiveData, stateObserver)

        goalId.let {
            if (goalId!! > 0L) {
                viewModel.setGoalId(goalId!!)
            }
        }

        viewModel.loadData()

        setRadioButtonType()
        setSwitchImproov()

        main.openToolbar()

        goal_form_divide_and_conquest_help.setOnClickListener {
            hideSoftKeyboard()
            setupBottomSheetTipsDivideAndConquer()
            setupBottomSheetTip()
            showBottomSheetTips()
        }

        goal_form_type_help.setOnClickListener {
            hideSoftKeyboard()
            setupBottomSheetTipsGoalType()
            setupBottomSheetTip()
            showBottomSheetTips()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> {
                when {
                    checkIfAnyFieldsAreEmptyOrZero() -> { }
                    getGoalTypeSelected() == GoalType.GOAL_NONE -> {
                        showSnackBarLong(getString(R.string.goal_form_empty_type_goal))

                        hideSoftKeyboard()
                        goal_form_type_title.isFocusableInTouchMode = true
                        goal_form_type_title.requestFocus()
                    }
                    !validateDivideAndConquerValues() -> {
                        showSnackBarLong(getString(R.string.goal_form_gold_silver_bronze_order))

                        goal_form_bronze_value.requestFocus()
                    }
                    else -> {
                        val goalToSave = updateOrCreateGoal()

                        viewModel.onSaveGoal(goalToSave)

                        verifyFistTimeSaving()

                        return true
                    }
                }
            }
        }

        return false
    }

    private fun setSwitchImproov() {
        goal_form_switch_divide_and_conquer.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                isDivideAndConquer(true)
            } else {
                isDivideAndConquer(false)
            }
        }
    }

    private fun setRadioButtonType() {
        goal_form_radio_type_list.setOnClickListener {
            if (goal_form_radio_type_list.isChecked) {
                goal_form_radio_type_counter.isChecked = false
                goal_form_radio_type_total.isChecked = false

                goal_form_goal_counter.gone()

                if (goal.type != GoalType.GOAL_NONE && goal.type != GoalType.GOAL_LIST) {
                    showDialogOneButton()
                }
            }
        }

        goal_form_radio_type_counter.setOnClickListener {
            if (goal_form_radio_type_counter.isChecked) {
                goal_form_radio_type_list.isChecked = false
                goal_form_radio_type_total.isChecked = false

                goal_form_goal_counter.visible()

                if (goal.type != GoalType.GOAL_NONE && goal.type != GoalType.GOAL_COUNTER) {
                    showDialogOneButton()
                }
            }
        }

        goal_form_radio_type_total.setOnClickListener {
            if (goal_form_radio_type_total.isChecked) {
                goal_form_radio_type_counter.isChecked = false
                goal_form_radio_type_list.isChecked = false

                goal_form_goal_counter.gone()

                if (goal.type != GoalType.GOAL_NONE && goal.type != GoalType.GOAL_FINAL) {
                    showDialogOneButton()
                }
            }
        }
    }

    private fun isDivideAndConquer(isDivideAndConquer: Boolean) {
        goal.divideAndConquer = isDivideAndConquer

        if (isDivideAndConquer) {
            goal_form_single_value.resetValue()

            goal_form_divide_and_conquer.visible()
            goal_form_single.invisible()
        } else {
            goal_form_bronze_value.resetValue()
            goal_form_silver_value.resetValue()
            goal_form_gold_value.resetValue()

            goal_form_divide_and_conquer.invisible()
            goal_form_single.visible()
        }
    }

    private fun updateOrCreateGoal(): Goal {
        goal.name = goal_form_goal_name.text.toString()
        goal.divideAndConquer = goal_form_switch_divide_and_conquer.isChecked

        if (goal.goalId == 0L) {
            goal.createdDate = getCurrentTime()
            goal.value = 0F
            goal.done = false
            goal.type = getGoalTypeSelected()

            goal.order =
                if (goalsSize == null || goalsSize == 0) {
                    0
                } else {
                    goalsSize!! + 1
                }
        } else {
            val oldGoalType = goal.type
            goal.type = getGoalTypeSelected()

            if (oldGoalType != goal.type) {
                deleteReferencesFromItemsAndHistorics()
            }

            goal.updatedDate = getCurrentTime()
        }

        if (getGoalTypeSelected() == GoalType.GOAL_COUNTER) {
            goal.incrementValue = goal_form_goal_counter_inc_value.toFloat()
            goal.decrementValue = goal_form_goal_counter_dec_value.toFloat()
        }

        if (goal.divideAndConquer) {
            goal.bronzeValue = goal_form_bronze_value.toFloat()
            goal.silverValue = goal_form_silver_value.toFloat()
            goal.goldValue = goal_form_gold_value.toFloat()
        } else {
            goal.singleValue = goal_form_single_value.toFloat()
        }

        return goal
    }

    private fun deleteReferencesFromItemsAndHistorics() {
        items.forEach {
            if (it.goalId == goal.goalId) {
                viewModel.onDeleteItem(it)
            }
        }

        historics.forEach {
            if (it.goalId == goal.goalId) {
                viewModel.onDeleteHistoric(it)
            }
        }
    }

    private fun getGoalTypeSelected(): GoalType {
        return when {
            goal_form_radio_type_list.isChecked -> {
                GoalType.GOAL_LIST
            }
            goal_form_radio_type_counter.isChecked -> {
                GoalType.GOAL_COUNTER
            }
            goal_form_radio_type_total.isChecked -> {
                GoalType.GOAL_FINAL
            }
            else -> {
                GoalType.GOAL_NONE
            }
        }
    }

    private fun setupGoal() {
        goal_form_goal_name.setText(goal.name)

        if (goal.divideAndConquer) {
            goal_form_divide_and_conquer.visible()
            goal_form_single.invisible()

            goal_form_bronze_value.setText(goal.bronzeValue.getNumberInRightFormat())
            goal_form_silver_value.setText(goal.silverValue.getNumberInRightFormat())
            goal_form_gold_value.setText(goal.goldValue.getNumberInRightFormat())

            goal_form_switch_divide_and_conquer.isChecked = true
        } else {
            goal_form_single_value.setText(goal.singleValue.getNumberInRightFormat())
        }

        when (goal.type) {
            GoalType.GOAL_LIST -> {
                goal_form_radio_type_list.isChecked = true
            }
            GoalType.GOAL_COUNTER -> {
                goal_form_radio_type_counter.isChecked = true

                goal_form_goal_counter.visible()

                goal_form_goal_counter_inc_value.setText(goal.incrementValue.getNumberInRightFormat())
                goal_form_goal_counter_dec_value.setText(goal.decrementValue.getNumberInRightFormat())
            }
            GoalType.GOAL_FINAL -> {
                goal_form_radio_type_total.isChecked = true
            }
            else -> { }
        }
    }

    private fun verifyFistTimeSaving() {
        if (firstTimeAdd) {
            viewModel.onSaveFirstTimeAdd(false)
            viewModel.onSaveFirstTimeList(true)
        }
    }

    private fun showDialogOneButton() {
        val dialog = DialogOneButton()

        dialog.setMessage(getString(R.string.goal_form_goal_dialog_one_button_message))

        dialog.setOnClickListener(object : DialogOneButton.OnClickListener {
            override fun onOK() {
                dialog.dismiss()
            }
        })

        dialog.show(requireFragmentManager(), "")
    }

    private fun checkIfAnyFieldsAreEmptyOrZero(): Boolean {
        return when {
            checkIfNameFieldIsEmptyOrZero() || checkIfValuesFieldsAreEmptyOrZero() ||
                    checkIfCounterFieldsAreEmptyOrZero() -> {
                true
            }
            else -> false
        }
    }

    private fun checkIfNameFieldIsEmptyOrZero(): Boolean {
        return when {
            goal_form_goal_name.checkIfFieldIsEmptyOrZero() -> {
                goal_form_goal_name.fieldIsEmptyOrZero(this)
                true
            }
            else -> false
        }
    }

    private fun checkIfValuesFieldsAreEmptyOrZero(): Boolean {
        return when {
            goal_form_single_value.checkIfFieldIsEmptyOrZero() && !goal.divideAndConquer -> {
                goal_form_single_value.fieldIsEmptyOrZero(this)
                true
            }
            goal_form_bronze_value.checkIfFieldIsEmptyOrZero() && goal.divideAndConquer -> {
                goal_form_bronze_value.fieldIsEmptyOrZero(this)
                true
            }
            goal_form_silver_value.checkIfFieldIsEmptyOrZero() && goal.divideAndConquer -> {
                goal_form_silver_value.fieldIsEmptyOrZero(this)
                true
            }
            goal_form_gold_value.checkIfFieldIsEmptyOrZero() && goal.divideAndConquer -> {
                goal_form_gold_value.fieldIsEmptyOrZero(this)
                true
            }
            else -> false
        }
    }

    private fun checkIfCounterFieldsAreEmptyOrZero(): Boolean {
        return when {
            goal_form_goal_counter_dec_value.checkIfFieldIsEmptyOrZero() &&
                    (goal.type == GoalType.GOAL_COUNTER ||
                            getGoalTypeSelected() == GoalType.GOAL_COUNTER) -> {
                goal_form_goal_counter_dec_value.fieldIsEmptyOrZero(this)
                true
            }
            goal_form_goal_counter_inc_value.checkIfFieldIsEmptyOrZero() &&
                    (goal.type == GoalType.GOAL_COUNTER ||
                            getGoalTypeSelected() == GoalType.GOAL_COUNTER) -> {
                goal_form_goal_counter_inc_value.fieldIsEmptyOrZero(this)
                true
            }
            else -> false
        }
    }

    private fun validateDivideAndConquerValues(): Boolean {
        return try {
            val gold = goal_form_gold_value.toFloat()
            val silver = goal_form_silver_value.toFloat()
            val bronze = goal_form_bronze_value.toFloat()

            ((gold > silver) && (silver > bronze))
        } catch (e: NumberFormatException) {
            return goal_form_single_value.isNotEmpty()
        }
    }
}
