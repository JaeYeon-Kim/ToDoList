package com.kjy.todolist.data

import androidx.room.Database
import androidx.room.RoomDatabase


// 데이터베이스를 정의하는 클래스
// 이 클래스는 앱이 사용할 DAO 객체를 반환하는 메서드를 가지게 됨.
// 데이터베이스 클래스는 RoomDatabase 클래스를 상속받는 추상 클래스.
// 내부에는 TodoDao 객체를 반환하는 추상 메서드를 제공.
/*
@Database 지정, 엔터티(다수 지정 가능)는 Todo이며, 데이터베이스 버전은 1로 지정.
앱이 업데이트 되어 데이터베이스 구조가 변경되거나 할 때 이 버전을 올려줘야 함.
*/
@Database(entities = [Todo::class], version = 1)
abstract class TodoDatabase: RoomDatabase() {
    abstract fun todoDao(): TodoDao
}