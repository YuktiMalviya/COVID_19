package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class Symptomchecker extends AppCompatActivity {

    Boolean cough = false;
    Boolean short_breath = false;
    Boolean sneezing = false;
    Boolean running_nose = false;
    Boolean fever = false;
    Boolean back_pain = false;
    Boolean weakness = false;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptomchecker);

    }


    public void getsymptomresults(View view) {

        if (cough || short_breath || sneezing || running_nose || fever || back_pain || weakness){

            if (cough && short_breath && sneezing && running_nose && fever && back_pain && weakness){
                high_risk_Diaglog();
            }
            else {
                medium_risk_Diaglog();
            }
        }
        else {
            low_risk_Diaglog();
        }

    }

    private void low_risk_Diaglog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Symptomchecker.this,R.style.MyDialogTheme);
        builder.setCancelable(false);
        final View customLayout = getLayoutInflater().inflate(R.layout.low_risk_dialog,null,false);
        builder.setView(customLayout);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void medium_risk_Diaglog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Symptomchecker.this,R.style.MyDialogTheme);
        builder.setCancelable(false);
        final View customLayout = getLayoutInflater().inflate(R.layout.medium_risk_dialog,null,false);
        builder.setView(customLayout);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void high_risk_Diaglog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Symptomchecker.this,R.style.MyDialogTheme);
        builder.setCancelable(false);
        final View customLayout = getLayoutInflater().inflate(R.layout.high_risk_dialog,null,false);
        builder.setView(customLayout);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void onShortBreath(View view) {
        if (((RadioButton) view).isChecked()){

            switch(view.getId()) {
                case R.id.short_breath_yes:
                    short_breath = true;
                    break;
                case R.id.short_breath_no:
                    short_breath = false;
                    break;
            }
        }
        else{
            Toast.makeText(this, "Check A Toast", Toast.LENGTH_SHORT).show();
        }
    }

    public void onDryCough(View view) {

        if (((RadioButton) view).isChecked()){

            switch(view.getId()) {
                case R.id.cough_yes:
                    cough = true;
                    break;
                case R.id.cough_no:
                    cough = false;
                    break;
            }
        }
        else{
            Toast.makeText(this, "Check A Toast", Toast.LENGTH_SHORT).show();
        }
    }

    public void onSneezing(View view) {

        if (((RadioButton) view).isChecked()){

            switch(view.getId()) {
                case R.id.sneezing_yes:
                    sneezing = true;
                    break;
                case R.id.sneezing_no:
                    sneezing = false;
                    break;
            }
        }
        else{
            Toast.makeText(this, "Check A Toast", Toast.LENGTH_SHORT).show();
        }

    }

    public void onRunningNose(View view) {

        if (((RadioButton) view).isChecked()){

            switch(view.getId()) {
                case R.id.running_nose_yes:
                    running_nose = true;
                    break;
                case R.id.running_nose_no:
                    running_nose = false;
                    break;
            }
        }
        else{
            Toast.makeText(this, "Check A Toast", Toast.LENGTH_SHORT).show();
        }


    }

    public void onFever(View view) {

        if (((RadioButton) view).isChecked()){

            switch(view.getId()) {
                case R.id.fever_yes:
                    fever = true;
                    break;
                case R.id.fever_no:
                    fever = false;
                    break;
            }
        }
        else{
            Toast.makeText(this, "Check A Toast", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBack_pain(View view) {

        if (((RadioButton) view).isChecked()){

            switch(view.getId()) {
                case R.id.back_pain_yes:
                    back_pain = true;
                    break;
                case R.id.back_pain_no:
                    back_pain = false;
                    break;
            }
        }
        else{
            Toast.makeText(this, "Check A Toast", Toast.LENGTH_SHORT).show();
        }
    }

    public void onWeakness(View view) {

        if (((RadioButton) view).isChecked()){

            switch(view.getId()) {
                case R.id.weakness_yes:
                    weakness = true;
                    break;
                case R.id.weakness_no:
                    weakness = false;
                    break;
            }
        }
        else{
            Toast.makeText(this, "Check A Toast", Toast.LENGTH_SHORT).show();
        }
    }
}
