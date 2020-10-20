package com.projects.winesmeller_v10

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment

/**
 * A simple [Fragment] subclass.
 * Use the [ActionBarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ActionBarFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_action_bar, container, false)
    }
}