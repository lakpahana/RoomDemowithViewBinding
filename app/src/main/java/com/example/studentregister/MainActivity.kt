package com.example.studentregister

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentregister.databinding.ActivityMainBinding
import com.example.studentregister.db.Student
import com.example.studentregister.db.StudentDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var nameEditText : EditText
    private lateinit var emailEditText : EditText
    private lateinit var saveButton : Button
    private lateinit var clearButton : Button
    private lateinit var studentRecyclerView: RecyclerView
    private lateinit var adapater: StudentRecyclerViewAdapater
    private lateinit var selectedStudent: Student
    private lateinit var viewModel: StudentViewModel
    private var isItemClicked:Boolean = false
    //viewBinding
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //setContentView(R.layout.activity_main)

        binding.apply {
            nameEditText = etName
            emailEditText = etEmail
            saveButton = btnSave
            clearButton = btnClear
            studentRecyclerView = rvStudent
        }

        val dao = StudentDatabase.getInstance(application).studentDao
        val factory = StudentViewModelFactory(dao)
        viewModel = ViewModelProvider(this,factory).get(StudentViewModel::class.java)

        saveButton.setOnClickListener {
            if (!isItemClicked) {
                saveStudentData()
                clearInput()
            }else{
                updateStudentData()
            }

        }
        clearButton.setOnClickListener {
            if (!isItemClicked) {
                clearInput()
            }else{
                deleteStudentData()
            }
        }

        initRecyclerView()
    }

    private fun updateStudentData(){
        viewModel.updateStudent(
            Student(
            selectedStudent.id,
            nameEditText.text.toString(),
            emailEditText.text.toString()
        ))
        editedDeleted()
    }

    private fun deleteStudentData(){
        viewModel.deleteStudent(
            Student(
                selectedStudent.id,
                nameEditText.text.toString(),
                emailEditText.text.toString()
        )
        )
        editedDeleted()
    }

    private fun editedDeleted(){
        //selectedStudent = null
        saveButton.text = "Save"
        clearButton.text = "Clear"
        isItemClicked = false
        clearInput()
    }

    private fun saveStudentData(){
//        val enteredName = nameEditText.text.toString()
//        val enteredEmail = emailEditText.text.toString()
//        val student = Student(0,enteredName,enteredEmail)
//        viewModel.insertStudent(student)
            viewModel.insertStudent(
                Student(
                    0,
                    nameEditText.text.toString(),
                    emailEditText.text.toString()
                )
            )

    }

    private fun clearInput(){
        nameEditText.setText("")
        emailEditText.setText("")
    }

    private fun initRecyclerView(){
        studentRecyclerView.layoutManager = LinearLayoutManager(this)
        adapater = StudentRecyclerViewAdapater{
            selectedItem:Student -> listItemClicked(selectedItem)
        }
        studentRecyclerView.adapter = adapater
        displayStudentsList()
    }

    private fun displayStudentsList(){
        viewModel.students.observe(this, Observer{
            adapater.setList(it)
            adapater.notifyDataSetChanged()
        })
    }

    private fun listItemClicked(student:Student){
        selectedStudent = student
        saveButton.text = "Update"
        clearButton.text = "Delete"
        nameEditText.setText(selectedStudent.name)
        emailEditText.setText(selectedStudent.email)
        isItemClicked = true
        //        emailEditText.text = student.email
    }
}