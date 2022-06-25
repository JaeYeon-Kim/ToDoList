package com.kjy.todolist.Adapter

import androidx.recyclerview.widget.DiffUtil
import com.kjy.todolist.data.Todo

class TodoDiffUtilCallback: DiffUtil.ItemCallback<Todo>() {

    // 아이템이 같음을 판단하는 규칙
    // id가 다르면 변경된 것으로 판단하여 해당 아이템은 교체
    override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
       return oldItem.id == newItem.id
    }

    // 내용을 비교하는 규칙
    // DB는 id가 유니크하기 때문에 동일한 규칙 적용.
    override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem.id == newItem.id
    }
}