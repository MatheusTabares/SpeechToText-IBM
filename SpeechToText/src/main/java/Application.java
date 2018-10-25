import java.io.FileNotFoundException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechRecognitionResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;

public class Application {
    
    public static void main(String[] args) throws FileNotFoundException, InterruptedException, Exception {
        SpeechToText service = new SpeechToText();
        service.setUsernameAndPassword("8ef046d2-0b24-45c1-b12d-8b9e37cf535a", "w0jWHgRbtg46");

        // Signed PCM AudioFormat with 16kHz, 16 bit sample size, mono
        int sampleRate = 16000;
        AudioFormat format = new AudioFormat(sampleRate, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        
        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Line not supported");
            System.exit(0);
        }
        
        TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
        line.open(format);
        line.start();
        
        AudioInputStream audio = new AudioInputStream(line);

        RecognizeOptions options = new RecognizeOptions.Builder()
            .interimResults(true)
            // .inactivityTimeout(5) // use this to stop listening when the speaker pauses, i.e. for 5s
          .audio(audio)
            .contentType(HttpMediaType.AUDIO_RAW + "; rate=" + sampleRate)
          .build();

        service.recognizeUsingWebSocket(options, new BaseRecognizeCallback() {
          @Override
          public void onTranscription(SpeechRecognitionResults speechResults) {
            System.out.println(speechResults);
          }
        });

        System.out.println("Listening to your voice for the next 30s...");
        Thread.sleep(30 * 1000);
        
        // closing the WebSockets underlying InputStream will close the WebSocket itself.
        line.stop();
        line.close();
        
        System.out.println("Fin.");
    }
    
}
