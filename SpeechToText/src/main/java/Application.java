import java.io.File;
import java.io.FileNotFoundException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechRecognitionResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;

public class Application {
	public static File file1 = new File("1.wav");
    public static File file2 = new File("2.wav");
    public static File file3 = new File("3.wav");
    public static File file4 = new File("4.wav");
    public static File file5 = new File("5.wav");
    public static File file6 = new File("6.wav");
    public static File file7 = new File("7.wav");
    public static File file8 = new File("8.wav");
    
    public static void main(String[] args) throws FileNotFoundException, InterruptedException, Exception {
        /**
         * TODO: Text to speech
         */
        /*TextToSpeech textToSpeech = new TextToSpeech();
        textToSpeech.setUsernameAndPassword("eb657d72-4c20-4d27-b0b2-03bd2711f365", "60A18b20tDmz");
        
        try {
            SynthesizeOptions synthesizeOptions = new SynthesizeOptions.Builder().text("Obrigada pelas informações.").accept("audio/wav")
                .voice(Voice.PT_BR_ISABELAVOICE).build();
            
            InputStream inputStream = textToSpeech.synthesize(synthesizeOptions).execute();
            InputStream in = WaveUtils.reWriteWaveHeader(inputStream);
            
            OutputStream out = new FileOutputStream("test11.wav");
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            
            out.close();
            in.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        
        
        playSound(file1);
        
        /**
         * TODO: Speech to text
         */
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
        
        RecognizeOptions options = new RecognizeOptions.Builder().interimResults(true)
            // .inactivityTimeout(5) // use this to stop listening when the speaker pauses, i.e. for 5s
            .audio(audio).contentType(HttpMediaType.AUDIO_RAW + "; rate=" + sampleRate).model("pt-BR_BroadbandModel").build();
        
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
    
    static void playSound(File sound) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(sound));
            clip.start();
            
            Thread.sleep(clip.getMicrosecondLength() / 1000);
        } catch (Exception e) {
            
        }
    }
}
