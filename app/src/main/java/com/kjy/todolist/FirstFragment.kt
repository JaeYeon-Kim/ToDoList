package com.kjy.todolist

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kjy.todolist.Adapter.TodoListAdapter
import com.kjy.todolist.databinding.FragmentFirstBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    private val viewModel by activityViewModels<MainViewModel>()    // ①

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ①
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val todoListAdapter = TodoListAdapter { todo ->
            // 클릭시 처리
            viewModel.selectedTodo = todo
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        binding.recyclerView.adapter = todoListAdapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.items.collect {
                    Log.d("FirstFragment", it.toString())
                    todoListAdapter.submitList(it)      // ②
                }
            }
        }

        binding.addFab.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}