package com.kjy.todolist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.kjy.todolist.data.Todo
import com.kjy.todolist.data.TodoDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

// 액티비티나 프래그먼트와 같은 뷰에서 데이터베이스처럼 복잡한 처리를 직접하는 것 보다 별도의 클래스에 복잡한 코드를 작성하고
// 뷰에서는 그것을 활용하는 것이 좋은 방법. 따라서 안드로이드에서 ViewModel, AndroidViewModel을 제공하고 있음.
// 두 클래스의 차이는 Application 객체의 사용가능여부 이다.

// AndroidViewModel은 액티비티와 수명을 같이함.
// AndroidViewModel 클래스는 상속하는 MainViewModel 클래스를 만듬. 생성자로는 Application 객체를 받음.
class MainViewModel(application: Application): AndroidViewModel(application) {
    // Room 데이터베이스 사용
    // databaseBuilder() 메서드에 전달하는 인자로는 Application 객체와 데이터베이스 클래스, 데이터베이스 이름이 필요함.
    private val db = Room.databaseBuilder(
        application,
        TodoDatabase::class.java, "todo"
    ).build()


    // DB의 결과를 관찰할 수 있도록 하는 방법
    // StateFlow는 현재 상태와 새로운 상태 업데이트를 이를 관찰하는 곳에 보내는 데이터 흐름을 표현
    // 상태 = 데이터, 상태를 UI에 노출시킬 때는 StateFlow 사용.
    // 상태를 업데이트 하고 관찰하는 곳으로 상태를 전달하려면 MutableStateFlow 클래스의 value 프로퍼티에 새 값을 할당.
    private val _items = MutableStateFlow<List<Todo>>(emptyList())
    val items: StateFlow<List<Todo>> = _items

    var selectedTodo: Todo? = null

    // 초기화시 모든 데이터를 읽어옴
    /*
    init 함수에서 뷰모델이 초기화될 때 모든 할 일 데이터를 읽어서 StateFlow로 외부에 노출되도록 함.
    suspend 메서드 수행
    getAll() 메서드는 Flow<List<Todo>> 타입을 반환하는데 이를 collect 함수로 현재 상태를 수집 가능.
    현재 상태를 _items.value에 할당하여 최신 정보로 교체

    이는 Flow 데이터를 UI로 노출시키기 위한 하나의 패턴.
     */
    init {
        viewModelScope.launch {
            // Flow 객체는 collect로 현재 값을 가져올 수 있음
            db.todoDao().getAll().collect { todos ->
                // StateFlow 객체는 value 프로퍼티로 현재 상태값을 읽거나 쓸 수 있음
                _items.value = todos
            }
        }
    }


    // 할 일을 추가하는 메서드
    // 비동기 처리를 위해 코루틴 코드는 코루틴 스코프를 이용해 처리 가능
    // ViewModel 클래스에서는 viewModelScope 프로퍼티를 통해서 코루틴 스코프를 쉽게 사용 가능.
    fun addTodo(text: String, date: Long) {
        viewModelScope.launch {
            db.todoDao().insert(Todo(text, date))
        }
    }

    // 할 일을 수정하는 메서드
    fun updateTodo(text: String, date: Long) {
        selectedTodo?.let {  todo ->
                todo.apply {
                    this.title = text
                    this.date = date
                }
                viewModelScope.launch {
                    db.todoDao().update(todo)
                }
            selectedTodo = null
            }
    }

    // 할 일 삭제 메서드
    fun deleteTodo(id: Long) {
        _items.value
            .find { todo -> todo.id == id}
            ?.let { todo ->
                viewModelScope.launch {
                    db.todoDao().delete(todo)
                }
                // 선택한 할일이 삭제되면 null로 할당.
                selectedTodo = null
            }
    }
}
