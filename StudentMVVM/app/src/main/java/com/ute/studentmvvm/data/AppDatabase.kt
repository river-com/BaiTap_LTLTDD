package com.ute.studentmvvm.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ute.studentmvvm.model.Student
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Student::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun studentDao(): StudentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope = CoroutineScope(Dispatchers.IO)): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ute_student_room.db"
                )
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.studentDao())
                }
            }
        }

        suspend fun populateDatabase(studentDao: StudentDao) {
            val initialStudents = listOf(
                Student(
                    fullName = "Nguyễn Văn An",
                    studentCode = "20110001",
                    email = "an.nv@ute.udn.vn",
                    gpa = 3.85,
                    major = "Công Nghệ Thông Tin"
                ),
                Student(
                    fullName = "Trần Thị Mai",
                    studentCode = "20110002",
                    email = "mai.tt@ute.udn.vn",
                    gpa = 3.92,
                    major = "Kỹ Thuật Phần Mềm"
                ),
                Student(
                    fullName = "Lê Hoàng Phúc",
                    studentCode = "20110003",
                    email = "phuc.lh@ute.udn.vn",
                    gpa = 3.45,
                    major = "An Toàn Thông Tin"
                ),
                Student(
                    fullName = "Phạm Quốc Dũng",
                    studentCode = "20110004",
                    email = "dung.pq@ute.udn.vn",
                    gpa = 3.60,
                    major = "Mạng Máy Tính"
                )
            )
            studentDao.insertAll(initialStudents)
        }
    }
}
