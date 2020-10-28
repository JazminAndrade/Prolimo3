package com.example.prolimo3.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.prolimo3.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    View vista;
    ToggleButton conectado;
    ProgressBar señal;
    ProgressBar bateria;
    ProgressBar liquido;
    int x = 100;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_home, container, false);
        conectado = (ToggleButton)vista.findViewById(R.id.toggleButton);
        señal = (ProgressBar)vista.findViewById(R.id.progressBar);
        bateria = (ProgressBar)vista.findViewById(R.id.progressBar2);
        liquido = (ProgressBar)vista.findViewById(R.id.progressBar3);

        señal.setMax(100);
        bateria.setMax(100);
        liquido.setMax(100);

        conectado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    funcion(x);
                    funcion2(x);
                    funcion3(x);
                }
            }
        });
        return vista;
    }
    public void funcion(final int i){
        señal.setProgress(i);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(10000);
                } catch (Exception e){
                    e.printStackTrace();
                }
                funcion(i-10);
            }
        });
        thread.start();
    }

    public void funcion2(final int i){
        bateria.setProgress(i);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(5000);
                } catch(Exception e){
                    e.printStackTrace();
                }
                funcion2(i-10);
            }
        });
        thread.start();
    }

    public void funcion3(final int i){
        liquido.setProgress(i);
       // if (i==40){
         //   Toast.makeText(getContext(), "Se está acabando el líquido",Toast.LENGTH_LONG).show();
        //};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(3000);
                } catch (Exception e){
                    e.printStackTrace();
                }
                funcion3(i-10);
            }
        });
        thread.start();
    }
}