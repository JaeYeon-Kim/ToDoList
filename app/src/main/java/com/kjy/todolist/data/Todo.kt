package com.kjy.todolist.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


// 할 일 데이터 클래스 작성
// 엔터티는 클래스를 data 클래스로 필요한 항목을 가지는 클래스를 만듬. data는 날짜를 저장할 객체.
// 따로 지정하지 않아도 기본값으로 현재 날짜를 사용하도록 Calendar 클래스를 활용.
@Entity         // Room 데이터베이스에서 사용할 엔터티 클래스는 @Entity 주석을 써줌.
data class Todo (
    var title: String,
    var date: Long = Calendar.getInstance().timeInMillis
        ) {
    // id는 유일한 값, 기본키 제약을 주석으로 추가.
    // 기본키는 중복혀용 x, 추가로 기본키를 지정하지 않아도 자동으로 증가하도록 autoGenerate 옵션을 추가.
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

