import java.io.File;
import java.io.FileNotFoundException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import com.ibm.watson.developer_cloud.assistant.v1.Assistant;
import com.ibm.watson.developer_cloud.assistant.v1.model.Context;
import com.ibm.watson.developer_cloud.assistant.v1.model.InputData;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechRecognitionResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;

public class Application {
	public static String OI = "oi";
	public static String AJUDA = "ajuda";
	public static String LOCALIZACAO = "cachoeira";
	public static String NOME = "matheus";
	public static String SIM = "sim";
	public static String NAO = "nao";
	public static File file1 = new File("1.wav");
    public static File file2 = new File("2.wav");
    public static File file3 = new File("3.wav");
    public static File file4 = new File("4.wav");
    public static File file5 = new File("5.wav");
    public static File file6 = new File("6.wav");
    public static File file7 = new File("7.wav");
    public static File file8 = new File("8.wav");
    public static final String response = "";
    public static void main(String[] args) throws FileNotFoundException, InterruptedException, Exception {
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
            	if(speechResults.getResults().get(0).isFinalResults()) {
            		final String response = speechResults.getResults().get(0).getAlternatives().get(0).getTranscript().trim();
            		if(OI.trim().equals(response.toString())) {
            			playSound(file1);
            		}
            		if(AJUDA.trim().equals(response.toString())) {
            			playSound(file2);
            		}
            		if(LOCALIZACAO.trim().equals(response.toString())) {
            			playSound(file3);
            		}
            		if(NOME.trim().equals(response.toString())) {
            			playSound(file4);
            		}
            		if(SIM.trim().equals(response.toString())) {
            			playSound(file5);
            		}
            		if(NAO.trim().equals(response.toString())) {
            			playSound(file8);
            		}
            		System.out.println(response);
            		
            		//System.out.println(response);
            	}
            }
        });
        
        System.out.println("Listening to your voice for the next 30s...");
        Thread.sleep(50 * 1000);
        
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
