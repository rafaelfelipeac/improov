package com.rafaelfelipeac.mountains.ui.fragments.today

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rafaelfelipeac.mountains.models.Routine
import com.rafaelfelipeac.mountains.models.User
import com.rafaelfelipeac.mountains.ui.base.BaseViewModel

class TodayViewModel: BaseViewModel() {
    private var routines: LiveData<List<Routine>>? = null

    var user: MutableLiveData<User>? = MutableLiveData()

    init {
        verifyUser()

        routines = routineRepository.getRoutines()
    }

    private fun verifyUser() {
        if (userRepository.getUserByUUI(auth.currentUser?.uid!!) == null) {
            val userToSave = User()

            userToSave.uui = auth.currentUser?.uid!!
            userToSave.email = auth.currentUser?.email!!

            userRepository.save(userToSave)
        }

        user?.value = userRepository.getUserByUUI(auth.currentUser?.uid!!)
    }

    // Routine
    fun getRoutines(): LiveData<List<Routine>>? {
        return routines
    }

    fun saveRoutine(routine: Routine) {
        routineRepository.save(routine)

        this.routines = routineRepository.getRoutines()
    }
}