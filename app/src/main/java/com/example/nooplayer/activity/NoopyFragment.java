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
    private TextView textView;
    private float[] expression = new float[6];

    private boolean _hasLoadedOnce= false; // your boolean field


    @Override
    public void setUserVisibleHint(boolean isFragmentVisible_) {
        super.setUserVisibleHint(true);

        if (this.isVisible()) {
            // we check that the fragment is becoming visible
            if (!isFragmentVisible_ && !_hasLoadedOnce) {
                _hasLoadedOnce = true;
            }
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_noopy, container, false);

        textView = view.findViewById(R.id.textView);
        cameraDetector = new CameraDetector(getContext(), CameraDetector.CameraType.CAMERA_FRONT, (SurfaceView)view.findViewById(R.id.cameraPreview));;
        cameraDetector.setImageListener(this);
        cameraDetector.setDetectSmile(true);
        cameraDetector.setDetectAnger(true);
        cameraDetector.setDetectDisgust(true);
        cameraDetector.setDetectJoy(true);
        cameraDetector.setDetectSadness(true);
        cameraDetector.setMaxProcessRate(5);

        cameraDetector.start();
        return view;
    }

    @Override
    public void onImageResults(List<Face> faces, Frame frame, float v) {
        if (faces.size() == 0) {
            textView.setText("No face detected");
        } else {
            Face face = faces.get(0);
            String res = "";

            expression[0] = face.expressions.getSmile();
            expression[1] = face.emotions.getAnger();
            expression[2] = face.emotions.getJoy();
            expression[3] = face.emotions.getDisgust();
            expression[4] = face.emotions.getSadness();
            expression[5] = face.emotions.getSurprise();

            float flag = expression[0];
            int index = 0;

            for (int i = 1; i < 6; i++) {
                if (expression[i] > flag) {
                    index = i;
                    flag = expression[i];
                }
            }

            if (flag < 10) { // Threshold
                res = "You're Natural.";
            } else {
                switch (index) {
                    case 0:
                        res = "You're Happy!";
                        break;
                    case 1:
                        res = "You're Angry!";
                        break;
                    case 2:
                        res = "You're Joyous!";
                        break;
                    case 3:
                        res = "You're Disgusted!";
                        break;
                    case 4:
                        res = "You're Sad!";
                        break;
                    case 5:
                        res = "You're Surprised!";
                        break;
                }
            }

            textView.setText(res);
        }
    }
}
