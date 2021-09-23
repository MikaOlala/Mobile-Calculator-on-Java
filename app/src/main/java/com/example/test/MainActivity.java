package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class MainActivity extends AppCompatActivity {

    private TextView input;
    private TextView result;

    boolean canAddOperation = false;
    boolean afterEquals = false;

    String inputValue = "";
    String resultValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("working");
        initTextView();
    }

    private void initTextView() {
        input = (TextView) findViewById(R.id.input);
        result = (TextView) findViewById(R.id.result);
    }

    private void setInput(String value) {
        inputValue = inputValue + value;
        input.setText(inputValue);
    }

    public void allClear(View view) {
        input.setText("");
        result.setText("");
        inputValue = "";
        resultValue = "";
        canAddOperation = false;
        afterEquals = false;
    }

    public void backspace(View view) {
        if(inputValue != null) {
            inputValue = inputValue.substring(0, inputValue.length()-1);
            input.setText(inputValue);
        }
    }

    public void equals(View view) {
        Double resultCalc = null;
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("rhino");
        try {
            resultCalc = (double)engine.eval(inputValue);
        } catch (ScriptException e) {
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
        }
        if(resultCalc!=null) {
            removeZeroes(String.valueOf(resultCalc.doubleValue()));
        }
        setInput("=");
        resultValue = String.valueOf(result.getText());
        afterEquals = true;
    }

    public void numberAction(View view) {
        Button b = (Button)view;
        String buttonText = b.getText().toString();
        if(isNumeric(buttonText) && !(afterEquals)) {
            setInput(buttonText);
            canAddOperation = true;
        }
        else if(canAddOperation && !(isNumeric(buttonText)) && !(afterEquals))
        {
            setInput(buttonText);
            canAddOperation = false;
        }
        else if (afterEquals && isNumeric(buttonText)) {
            inputValue = "";
            input.setText("");
            setInput(buttonText);
            canAddOperation = true;
            afterEquals = false;
        }
        else if (afterEquals && !(isNumeric(buttonText))) {
            if (buttonText.equals("."))
                inputValue = "0.";
            else
                inputValue = resultValue + buttonText;
            input.setText(inputValue);
            resultValue = "";
            canAddOperation = false;
            afterEquals = false;
        }
    }

    public void removeZeroes(String str) {
        str = !str.contains(".") ? str : str.replaceAll("0*$", "").replaceAll("\\.$", "");
        result.setText(str);
    }

    public boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}