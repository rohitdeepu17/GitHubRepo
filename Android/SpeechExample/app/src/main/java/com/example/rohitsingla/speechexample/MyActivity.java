package com.example.rohitsingla.speechexample;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;


public class MyActivity extends Activity {
    EditText edWords;
    TextToSpeech tts;
    static final int REQUEST_CODE=1;
    private static final String TAG = "MyActivity";
    SpeechRecognizer sr;
    private String filePath,fileName, fname;
    private MediaRecorder recorder;
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        edWords=(EditText)findViewById(R.id.edWords);
        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status)
            {
                tts.setLanguage(Locale.US);
            }
        });

        sr= SpeechRecognizer.createSpeechRecognizer(this);
        sr.setRecognitionListener(new Listener());

        //////////////// Preparing Directory////////
        try
        {
            Log.d("Starting", "Checking up directory");
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), "SpeechAndAudio");
            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if (! mediaStorageDir.exists())
            {
                if (! mediaStorageDir.mkdir())
                {
                    Log.e("Directory Creation Failed",mediaStorageDir.toString());


                }
                else
                {
                    Log.i("Directory Creation","Success");
                }
            }
        }
        catch(Exception ex)
        {
            Log.e("Directory Creation",ex.getMessage());
        }
        //filePath= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getPath()+"/"+"SpeechAndAudio/";
        filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
        /////////////////////////////////////////////

    }

    private void showToast(String message)
    {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            // Populate the wordsList with the String values the recognition engine thought it heard
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);

            showToast(matches.get(0));
            edWords.setText(edWords.getText().toString().trim()+" "+matches.get(0));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id)
        {
            case R.id.menuVoiceRecog:
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice recognition Demo...");
                startActivityForResult(intent, REQUEST_CODE);


                break;
            case R.id.menuVoiceRecogClass:
                Intent intent1 = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent1.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent1.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "voice.recognition.test");

                intent1.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
                sr.startListening(intent1);
                break;
            case R.id.menuTTS:
                tts.setPitch(.4f);
                tts.setSpeechRate(.1f);
                tts.speak(edWords.getText().toString().trim(), TextToSpeech.QUEUE_FLUSH, null);
                break;
            case R.id.menuRecord:
                if(item.getTitle().equals("Record"))
                {// Record
                    fileName = "sample_record.3gp";
                    fname=filePath+fileName;
                    Log.d(TAG,"The file to be saved : "+fname);
                    item.setTitle("Stop");

                    recorder = new MediaRecorder();
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    recorder.setOutputFile(fname);
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                    try {
                        recorder.prepare();
                    } catch (Exception e) {
                        Log.e("In Recording", "prepare() failed");
                    }

                    recorder.start();


                }
                else
                {
                    item.setTitle("Record");
                    recorder.stop();
                    recorder.reset();
                    recorder.release();
                    recorder = null;
                    //Stop
                    ////////////// Now Play/////////////
                    player = new MediaPlayer();
                    try {
                        player.setDataSource(fname);
                        player.prepare();
                        player.start();
                    } catch (Exception e) {
                        Log.e("Player Exception", "prepare() failed");
                    }

                }
                break;
            case R.id.menuAudioProcess:


                AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);
                PitchDetectionHandler pdh = new PitchDetectionHandler() {
                    @Override
                    public void handlePitch(PitchDetectionResult result,AudioEvent e)
                    {
                        final float pitchInHz = result.getPitch();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run()
                            {

                                TextView text = (TextView) findViewById(R.id.tv);
                                text.setText("" + pitchInHz);

                            }
                        });
                    }
                };
                AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
                dispatcher.addAudioProcessor(p);
                new Thread(dispatcher,"Audio Dispatcher").start();

        }
        return super.onOptionsItemSelected(item);
    }

    class Listener implements RecognitionListener
    {
        public void onReadyForSpeech(Bundle params)
        {
            Log.d(TAG, "onReadyForSpeech");
        }
        public void onBeginningOfSpeech()
        {
            Log.d(TAG, "onBeginningOfSpeech");
        }
        public void onRmsChanged(float rmsdB)
        {
            Log.d(TAG, "onRmsChanged");
        }
        public void onBufferReceived(byte[] buffer)
        {
            Log.d(TAG, "onBufferReceived");
        }
        public void onEndOfSpeech()
        {
            Log.d(TAG, "onEndofSpeech");
        }
        public void onError(int error)
        {
            Log.d(TAG, "error " + error);

        }
        public void onResults(Bundle results)
        {
            String str = new String();
            Log.d(TAG, "onResults " + results);
            ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            str = data.get(0).toString();

            edWords.setText(edWords.getText().toString()+" "+str);


        }
        public void onPartialResults(Bundle partialResults)
        {
            Log.d(TAG, "onPartialResults");
        }
        public void onEvent(int eventType, Bundle params)
        {
            Log.d(TAG, "onEvent " + eventType);
        }
    }
}
