package dev.bti.chrona.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class VoiceHelper {

    private final SpeechRecognizer speechRecognizer;
    private final Intent speechRecognizerIntent;
    private final Context context;
    private final EditText targetEditText;
    private final VoiceListener listener; // NEW: Callback interface

    // NEW: Interface to talk back to MainActivity
    public interface VoiceListener {
        void onListeningFinished(); // Called on success OR error
    }

    public VoiceHelper(Context context, EditText targetEditText, VoiceListener listener) {
        this.context = context;
        this.targetEditText = targetEditText;
        this.listener = listener;

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);

        setupListener();
    }

    private void setupListener() {
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                targetEditText.setHint("Listening...");
            }

            @Override
            public void onBeginningOfSpeech() {
                targetEditText.setText("");
            }

            @Override
            public void onRmsChanged(float rmsdB) {}

            @Override
            public void onBufferReceived(byte[] buffer) {}

            @Override
            public void onEndOfSpeech() {
                targetEditText.setHint("Type or speak a prompt...");
                listener.onListeningFinished(); // Tell MainActivity to re-enable UI
            }

            @Override
            public void onError(int error) {
                String errorMessage = getErrorText(error);
                Toast.makeText(context, "Voice Error: " + errorMessage, Toast.LENGTH_LONG).show();
                targetEditText.setHint("Type or speak a prompt...");
                listener.onListeningFinished(); // Tell MainActivity to re-enable UI even on error!
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    targetEditText.setText(matches.get(0));
                    targetEditText.setSelection(targetEditText.getText().length());
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                ArrayList<String> matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    targetEditText.setText(matches.get(0));
                }
            }

            @Override
            public void onEvent(int eventType, Bundle params) {}
        });
    }

    // NEW: Translate Android's integer errors into readable text
    private String getErrorText(int errorCode) {
        return switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO -> "Audio recording error (Check Mic)";
            case SpeechRecognizer.ERROR_CLIENT -> "Client side error";
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions";
            case SpeechRecognizer.ERROR_NETWORK -> "Network error";
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout";
            case SpeechRecognizer.ERROR_NO_MATCH -> "No match (Didn't hear anything)";
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RecognitionService busy";
            case SpeechRecognizer.ERROR_SERVER -> "Error from server";
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input";
            default -> "Didn't understand, please try again. Code: " + errorCode;
        };
    }

    public void startListening() {
        speechRecognizer.startListening(speechRecognizerIntent);
    }

    public void destroy() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }
}