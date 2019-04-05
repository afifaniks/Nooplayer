package com.example.nooplayer.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    private CameraDetector cameraDetector;
    private SurfaceView cameraPreview;
    private View thisView;
    private TextView textView;
    private float[] expression = new float[5];
    boolean shown = false;

    // Labels
    private TextView txtHappy;
    private TextView txtAngry;
    private TextView txtSad;
    private TextView txtDisgust;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cameraDetector.stop();
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
        View view = inflater.inflate(R.layout.fragment_noopy, container, false);

        cameraDetector = new CameraDetector(getContext(), CameraDetector.CameraType.CAMERA_FRONT,
                (SurfaceView) view.findViewById(R.id.cameraPreview));;

        textView = view.findViewById(R.id.textView);

        txtAngry = view.findViewById(R.id.txtAnger);
        txtHappy = view.findViewById(R.id.txtHappiness);
        txtDisgust = view.findViewById(R.id.txtDisgust);
        txtSad = view.findViewById(R.id.txtSad);

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
        } else {
            textView.setText("");
            Face face = faces.get(0);

            expression[0] = face.expressions.getSmile();
            expression[1] = face.emotions.getAnger();
            expression[2] = face.emotions.getSadness();
            expression[3] = face.emotions.getDisgust();

            txtHappy.setText("Happiness: " + expression[0]);
            txtAngry.setText("Anger: " + expression[1]);
            txtSad.setText("Sadness: " + expression[2]);
            txtDisgust.setText("Disgust: " + expression[3]);
        }
    }
}
