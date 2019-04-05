package com.example.nooplayer.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.affectiva.android.affdex.sdk.Frame;
import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.Detector;
import com.affectiva.android.affdex.sdk.detector.Face;
import com.example.nooplayer.R;

import java.util.List;

/**
 * @author: Afif Al Mamun
 * @created_in: 4/4/19
 * @project_name: Nooplayer-master
 **/
public class NoopyFragment extends Fragment implements Detector.ImageListener {
    private static int MOOD_NUM = 4;
    private CameraDetector cameraDetector;
    private SurfaceView cameraPreview;
    private View thisView;
    private TextView textView;
    private FloatingActionButton actionButton;
    private float[] expression = new float[MOOD_NUM];
    private boolean faceDetected = false;
    boolean shown = false;

    // Labels
    private TextView txtHappy;
    private TextView txtAngry;
    private TextView txtSad;
    private TextView txtDisgust;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            cameraDetector.start();
            shown = true;
        } else {
            if (shown)
            {
                cameraDetector.stop();
                shown =false;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_noopy, container, false);

        cameraDetector = new CameraDetector(getContext(), CameraDetector.CameraType.CAMERA_FRONT,
                (SurfaceView) view.findViewById(R.id.cameraPreview));;

        textView = view.findViewById(R.id.textView);

        txtAngry = view.findViewById(R.id.txtAnger);
        txtHappy = view.findViewById(R.id.txtHappiness);
        txtDisgust = view.findViewById(R.id.txtDisgust);
        txtSad = view.findViewById(R.id.txtSad);
        actionButton = view.findViewById(R.id.getSuggestion);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (faceDetected) {
                    Toast.makeText(view.getContext(), "Suggestion Generated!", Toast.LENGTH_SHORT).show();

                    float indicatorValue = expression[0];
                    int indicator = 0;

                    for (int i = 1; i < MOOD_NUM; i++) {
                        if (indicatorValue < expression[i]) {
                            indicatorValue= expression[i];
                            indicator = i;
                        }
                    }

                    String currentMood = "";

                    switch (indicator) {
                        case 0:
                            currentMood = "HAPPY";
                            break;
                        case 1:
                            currentMood = "ANGRY";;
                            break;
                        case 2:
                            currentMood = "SAD";
                            break;
                        case 3:
                            currentMood = "DISGUST";
                            break;
                    }

                    NoopySuggestionFragment.setMood(currentMood);

                } else {
                    Toast.makeText(view.getContext(), "No face detected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cameraDetector.setImageListener(this);
        cameraDetector.setDetectSmile(true);
        cameraDetector.setDetectAnger(true);
        cameraDetector.setDetectDisgust(true);
        cameraDetector.setDetectJoy(true);
        cameraDetector.setDetectSadness(true);
        cameraDetector.setMaxProcessRate(10);

        return view;
    }

    @Override
    public void onImageResults(List<Face> faces, Frame frame, float v) {
        if (faces.size() == 0) {
            textView.setText("No face detected");
            faceDetected = false;
        } else {
            faceDetected = true;
            textView.setText("");
            Face face = faces.get(0);

            expression[0] = face.expressions.getSmile();
            expression[1] = face.emotions.getAnger();
            expression[2] = face.emotions.getSadness();
            expression[3] = face.emotions.getDisgust();

            // Setting data on screen
            txtHappy.setText("Happiness: " + expression[0]);
            txtAngry.setText("Anger: " + expression[1]);
            txtSad.setText("Sadness: " + expression[2]);
            txtDisgust.setText("Disgust: " + expression[3]);
        }
    }
}
