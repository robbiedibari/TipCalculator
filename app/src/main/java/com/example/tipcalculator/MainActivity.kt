package com.example.tipcalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val amountEditText = findViewById<EditText>(R.id.checkText)
        val tipAmount = findViewById<EditText>(R.id.TipTotal)
        val totalAmountTextView = findViewById<EditText>(R.id.TotalAmount)
        val percentage = resources.getStringArray(R.array.Percentage)
        val spinner = findViewById<Spinner>(R.id.spin)
        val numberOfPeopleEditText = findViewById<EditText>(R.id.numOfPeople)
        var excludeDontBeCheap = false;

        if (spinner != null && totalAmountTextView != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, percentage)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    val selectedItem = parent.getItemAtPosition(position).toString()
                    if (selectedItem == "Dont be Cheap!") {
                        excludeDontBeCheap = true
                    } else {
                        excludeDontBeCheap = false
                        calculateTotal(amountEditText, totalAmountTextView, percentage, spinner, tipAmount, numberOfPeopleEditText)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Handle the case when nothing is selected if needed
                }
            }

            amountEditText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    calculateTotal(amountEditText, totalAmountTextView, percentage, spinner, tipAmount, numberOfPeopleEditText)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // Unused
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Unused
                }
            })

            numberOfPeopleEditText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    // Call the function to update the total amount per person
                    updateAmountPerPerson(amountEditText, totalAmountTextView, numberOfPeopleEditText, percentage, tipAmount,spinner)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // Unused
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Unused
                }
            })
        }
    }

    // Add this function to calculate and update the total amount per person
    private fun updateAmountPerPerson(
        amountEditText: EditText,
        totalAmountTextView: EditText,
        numberOfPeopleEditText: EditText,
        percentage: Array<String>,
        tipAmount: EditText,
        spinner: Spinner
    ) {
        calculateTotal(amountEditText, totalAmountTextView, percentage, spinner, tipAmount, numberOfPeopleEditText)
    }

    private fun calculateTotal(
        amountEditText: EditText,
        totalAmountTextView: EditText,
        percentage: Array<String>,
        spinner: Spinner,
        tipAmount: EditText,
        numberOfPeopleEditText: EditText  // New parameter for the number of people
    ) {
        // Get the selected percentage as a string (e.g., "10%")
        val selectedPercentageStr = percentage[spinner.selectedItemPosition]
        if (selectedPercentageStr == "Don't be Cheap!") {
            return
        }

        // Remove the '%' character and convert to a double
        val selectedPercentage = selectedPercentageStr.replace("%", "").toDouble() / 100.0

        // Get the total amount entered by the user from amountEditText
        val AmountStr = amountEditText.text.toString()
        val Amount = if (AmountStr.isNotEmpty()) AmountStr.toDouble() else 0.0

        // Calculate the tip amount
        val tipAmountValue = Amount * selectedPercentage

        // Display the tip amount in the tipAmount EditText
        tipAmount.setText(String.format("%.2f", tipAmountValue))

        // Get the check amount entered by the user from amountEditText
        val checkAmountStr = amountEditText.text.toString()
        val checkAmount = if (checkAmountStr.isNotEmpty()) checkAmountStr.toDouble() else 0.0

        // Calculate the total check amount (sum of check amount and tip)
        val totalCheckAmount = checkAmount + tipAmountValue

        // Get the number of people entered by the user
        val numberOfPeopleStr = numberOfPeopleEditText.text.toString()
        val numberOfPeople = if (numberOfPeopleStr.isNotEmpty()) numberOfPeopleStr.toInt() else 1 // Default to 1 if empty or invalid input

        // Calculate the amount per person
        val amountPerPerson = totalCheckAmount / numberOfPeople

        // Display the result in the totalAmountTextView
        totalAmountTextView.setText(String.format("%.2f", amountPerPerson))
    }
}
