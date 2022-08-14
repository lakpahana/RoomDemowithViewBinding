package com.example.studentregister

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentregister.db.Student
import com.example.studentregister.db.StudentDAO
import kotlinx.coroutines.launch

class StudentViewModel(private val dao: StudentDAO ) : ViewModel() {
    val students = dao.getAllStudent()
    fun insertStudent(student:Student)=viewModelScope.launch {
        dao.insertStudent(student)
    }
    fun updateStudent(student:Student)=viewModelScope.launch {
        dao.updateStudent(student)
    }
    fun deleteStudent(student:Student)=viewModelScope.launch {
        dao.deleteStudent(student)
    }

}