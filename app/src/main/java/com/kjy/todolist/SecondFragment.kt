package com.kjy.todolist

import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.kjy.todolist.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private val viewModel by activityViewModels<MainViewModel>()

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.selectedTodo?.let {
            binding.todoEditText.setText(it.title)
            binding.calendarView.date = it.date
        }

        // CalendarView에서 선택한 날짜를 저장할 Calendar 객체를 선언.
        val calendar = Calendar.getInstance()

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            calendar.apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }
        }

        binding.doneFab.setOnClickListener {
            if (binding.todoEditText.text.toString().isNotEmpty()) {
                if (viewModel.selectedTodo != null) {
                    viewModel.updateTodo(
                        binding.todoEditText.text.toString(),
                        calendar.timeInMillis,    // 수정시 객체에 저장된 시간 정보를 활용.
                    )
                } else {
                    viewModel.addTodo(binding.todoEditText.text.toString(),
                        calendar.timeInMillis,    // 수정시 객체에 저장된 시간 정보를 활용
                    )
                }
                findNavController().popBackStack()
            }

        }
        binding.deleteFab.setOnClickListener {
            // deleteTodo 메서드는 끝나면 화면을 이전 화면으로 전환.
            viewModel.deleteTodo(viewModel.selectedTodo!!.id)
            findNavController().popBackStack()
        }

        // 선택된 할일이 없을 때는 지우기 버튼 감추기
        // 뷰의 visibility 속성에 View.GONE 설정하여 화면에서 감춤.
        if (viewModel.selectedTodo == null) {
            binding.deleteFab.visibility = View.GONE
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}