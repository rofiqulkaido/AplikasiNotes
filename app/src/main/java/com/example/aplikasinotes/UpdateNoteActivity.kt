package com.example.aplikasinotes

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.aplikasinotes.databinding.ActivityUpdateNoteBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateNoteBinding
    private lateinit var db: NotesDatabaseHelper
    private var noteId: Int = -1
    private val calendar = Calendar.getInstance()
    private lateinit var selectDateEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //untuk kalender
        selectDateEditText = findViewById(R.id.tanggalEditText)
        // Set a click listener to show the DatePickerDialog when the EditText is clicked
        selectDateEditText.setOnClickListener {
            showDatePickerDialog()
        }

        db = NotesDatabaseHelper(this)

        noteId = intent.getIntExtra("note_id", -1 )
        if (noteId == -1){
            finish()
            return
        }

        val note = db.getNoteByID(noteId)
        binding.titleEditText.setText(note.title)
        binding.tanggalEditText.setText(note.tanggal)
        binding.contentEditText.setText(note.content)

        binding.updateSaveButton.setOnClickListener{
            val newTitle = binding.titleEditText.text.toString()
            val newTanggal = binding.tanggalEditText.text.toString()
            val newContent = binding.contentEditText.text.toString()
            val updatedNote = NotesModel(noteId, newTitle, newTanggal, newContent)
            db.updateNote(updatedNote)
            finish()
            Toast.makeText(this, "Perubahan berhasil disimpan", Toast.LENGTH_SHORT).show()
        }

    }
    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                // Update the TextInputEditText with the selected date
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)

                if (selectedDate < Calendar.getInstance()) {
                    // Update the TextInputEditText with the selected current or future date
                    updateDateInView(selectedDate)
                } else {
                    // Show a message if the selected date is in the past
                    // This can be customized based on your requirements
                    // For example, you might want to show a message and not update the date
                    // Or you can show a Toast message
                    Toast.makeText(this, "Periksa kembali tanggal lahir anda", Toast.LENGTH_SHORT).show()
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Show the DatePickerDialog
        datePickerDialog.show()
    }
    //untuk kalender
    private fun updateDateInView(calendar: Calendar) {
        val myFormat = "dd/MM/yyyy" // specify your format here
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        selectDateEditText.setText(sdf.format(calendar.time))
    }
}