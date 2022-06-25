package com.kjy.todolist.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow


// 데이터 액세스 오브젝트(DAO) 작성
// 엔터티 클래스를 앱에서 조작할 수 있도록 이런 저런 메서드를 가지는 DAO
// DAO 객체는 @Dao 주석을 추가한 interface로 생성.
/*
DAO 객체에는 여러 메서드 작성 가능. 데이터를 가져올 때는 @Query 주석에 SQL 쿼리 작성.
쿼리는 내림차순 정렬
추가, 수정 , 삭제는 각각 @Insert, @Update, @Delete 주석을 사용.
 */
@Dao
interface TodoDao {
    @Query("SELECT * FROM todo ORDER BY date DESC")
    fun getAll(): Flow<List<Todo>>      // 반환 타입 Flow => Flow는 코틀린의 고급 기능 중 하나로 데이터를 관찰

    // Insert 시에 Primary Key 가 겹치는게 있으면 덮어 쓴다는 의미
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: Todo)

    @Update
    suspend fun update(entity: Todo)

    @Delete
    suspend fun delete(entity: Todo)

    // DB에서 데이터를 얻는 동작 의외의 추가, 수정, 삭제는 모두 비동기로 오래 걸리는 처리에 속함.
    // 따라서 suspend 키워드 추가
    // 따라서 해당 메서드들은 코루틴을 이용해 처리.

}