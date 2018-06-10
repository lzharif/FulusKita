package com.lzharif.fuluskita.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lzharif.fuluskita.R;
import com.lzharif.fuluskita.helper.Constant;

import java.text.DecimalFormat;

public class CalculatorActivity extends AppCompatActivity {
    private static final char NORMAL = '0';
    private static final char ADDITION = '+';
    private static final char SUBTRACTION = '-';
    private static final char MULTIPLICATION = '*';
    private static final char DIVISION = '/';
    private EditText editTextCalc;
    private TextView textViewInfo;
    private char CURRENT_ACTION;

    private double valueOne = Double.NaN;
    private double valueTwo;

    private DecimalFormat decimalFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        CURRENT_ACTION = NORMAL;

        decimalFormat = new DecimalFormat("#.##########");

        Button btnZero = findViewById(R.id.buttonZero);
        Button btnTripleZero = findViewById(R.id.buttonTripleZero);
        Button btnOne = findViewById(R.id.buttonOne);
        Button btnTwo = findViewById(R.id.buttonTwo);
        Button btnThree = findViewById(R.id.buttonThree);
        Button btnFour = findViewById(R.id.buttonFour);
        Button btnFive = findViewById(R.id.buttonFive);
        Button btnSix = findViewById(R.id.buttonSix);
        Button btnSeven = findViewById(R.id.buttonSeven);
        Button btnEight = findViewById(R.id.buttonEight);
        Button btnNine = findViewById(R.id.buttonNine);
        Button btnDot = findViewById(R.id.buttonDot);
        Button btnClear = findViewById(R.id.buttonClear);
        Button btnEqual = findViewById(R.id.buttonEqual);
        Button btnAdd = findViewById(R.id.buttonAdd);
        Button btnSubtract = findViewById(R.id.buttonSubtract);
        Button btnMultiply = findViewById(R.id.buttonMultiply);
        Button btnDivide = findViewById(R.id.buttonDivide);
        editTextCalc = findViewById(R.id.editTextValue);
        textViewInfo = findViewById(R.id.infoTextView);

        btnDot.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                editTextCalc.setText(editTextCalc.getText() + ".");
            }
        });

        btnZero.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                editTextCalc.setText(editTextCalc.getText() + "0");
            }
        });

        btnTripleZero.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                editTextCalc.setText(editTextCalc.getText() + "000");
            }
        });

        btnOne.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                editTextCalc.setText(editTextCalc.getText() + "1");
            }
        });

        btnTwo.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                editTextCalc.setText(editTextCalc.getText() + "2");
            }
        });

        btnThree.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                editTextCalc.setText(editTextCalc.getText() + "3");
            }
        });

        btnFour.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                editTextCalc.setText(editTextCalc.getText() + "4");
            }
        });

        btnFive.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                editTextCalc.setText(editTextCalc.getText() + "5");
            }
        });

        btnSix.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                editTextCalc.setText(editTextCalc.getText() + "6");
            }
        });

        btnSeven.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                editTextCalc.setText(editTextCalc.getText() + "7");
            }
        });

        btnEight.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                editTextCalc.setText(editTextCalc.getText() + "8");
            }
        });

        btnNine.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                editTextCalc.setText(editTextCalc.getText() + "9");
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                computeCalculation();
                CURRENT_ACTION = ADDITION;
                textViewInfo.setText(decimalFormat.format(valueOne) + "+");
                editTextCalc.setText(null);
            }
        });

        btnSubtract.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                computeCalculation();
                CURRENT_ACTION = SUBTRACTION;
                textViewInfo.setText(decimalFormat.format(valueOne) + "-");
                editTextCalc.setText(null);
            }
        });

        btnMultiply.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                computeCalculation();
                CURRENT_ACTION = MULTIPLICATION;
                textViewInfo.setText(decimalFormat.format(valueOne) + "*");
                editTextCalc.setText(null);
            }
        });

        btnDivide.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                computeCalculation();
                CURRENT_ACTION = DIVISION;
                textViewInfo.setText(decimalFormat.format(valueOne) + "/");
                editTextCalc.setText(null);
            }
        });

        btnEqual.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (CURRENT_ACTION == NORMAL) {
                    if (Double.isNaN(valueOne))
                        valueOne = Double.parseDouble(editTextCalc.getText().toString());

                    Intent intent = new Intent();
                    intent.putExtra(Constant.VALUE, valueOne);
                    setResult(1, intent);
                    finish();
                } else {
                    computeCalculation();
                    textViewInfo.setText(textViewInfo.getText().toString() +
                            decimalFormat.format(valueTwo) + " = " + decimalFormat.format(valueOne));
                    CURRENT_ACTION = NORMAL;
                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextCalc.getText().length() > 0) {
                    CharSequence currentText = editTextCalc.getText();
                    editTextCalc.setText(currentText.subSequence(0, currentText.length() - 1));
                } else {
                    valueOne = Double.NaN;
                    valueTwo = Double.NaN;
                    editTextCalc.setText("");
                    textViewInfo.setText("");
                }
            }
        });
    }

    private void computeCalculation() {
        if (!Double.isNaN(valueOne)) {
            if (editTextCalc.getText().toString().equals(""))
                valueTwo = valueOne;
            else {
                if (CURRENT_ACTION == NORMAL) {
                    try {
                        valueOne = Double.parseDouble(editTextCalc.getText().toString());
                    } catch (Exception ignored) {
                    }
                } else {
                    valueTwo = Double.parseDouble(editTextCalc.getText().toString());
                    editTextCalc.setText(null);
                }
            }
            if (CURRENT_ACTION == ADDITION)
                valueOne = this.valueOne + valueTwo;
            else if (CURRENT_ACTION == SUBTRACTION)
                valueOne = this.valueOne - valueTwo;
            else if (CURRENT_ACTION == MULTIPLICATION)
                valueOne = this.valueOne * valueTwo;
            else if (CURRENT_ACTION == DIVISION)
                valueOne = this.valueOne / valueTwo;
        } else {
            try {
                valueOne = Double.parseDouble(editTextCalc.getText().toString());
            } catch (Exception ignored) {
            }
        }
    }

}
