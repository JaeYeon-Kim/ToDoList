package com.kjy.todolist.Adapter

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kjy.todolist.data.Todo
import com.kjy.todolist.databinding.ItemTodoBinding

// 생성자로 아이템이 클릭되었을 때 처리할 함수를 인자로 받습니다.
class TodoListAdapter(private val onClick: (Todo) -> Unit,
): ListAdapter<Todo, TodoListAdapter.TodoViewHolder>(TodoDiffUtilCallback())  {
// ListAdapter 상속시 제네릭 타입지정, 인자로 TodoDiffUtilCallback 받음.


    // 바인딩 객체를 저장할 변수 선언.
    private lateinit var binding: ItemTodoBinding


    // 바인딩 객체를 얻고 뷰 홀더 생성.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {

        binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding, onClick)     // 뷰 홀더 객체 생성. 전달 인자 => 바인딩 객체, 클릭시 수행할 함수
    }

    // 화면에 각 아이템이 보여질 때마다 호출. 여기서 실제로 보여질 내용 설정.
    // 아이템이 계속 바뀌기 때문에 바뀔때 마다 클릭 이벤트 설정도 다시 해줌.
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.setOnClickListener(getItem(position))

    }

    class TodoViewHolder (
        // 바인딩 객체
        private val binding: ItemTodoBinding,
        // 클릭 시
        private val onClick: (Todo) -> Unit

    // 어댑터는 RecyclerView.ViewHolder 클래스를 상속하고 아이템의 레이아웃의 뷰 인스턴스를 인자로 전달.
    // 뷰 바인딩 객체의 root 프로퍼티로 얻음.
    ): RecyclerView.ViewHolder(binding.root) {


        // 할 일 객체를 인자로 전달받아 실제로 화면에 표시
        fun bind(todo: Todo) {
            binding.text1.text = todo.title
            // Long 형태의 시간을 DateFormat.format() 함수로 년/월/일 의 형태로 변환.
            binding.text2.text = DateFormat.format("yyyy/MM/dd", todo.date)

        }

        // 바인딩 객체가 클릭되었을 때
        fun setOnClickListener(todo: Todo) {
            binding.root.setOnClickListener {
                onClick(todo)
            }
        }
    }
}