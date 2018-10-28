import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import com.ibm.watson.developer_cloud.assistant.v1.Assistant;
import com.ibm.watson.developer_cloud.assistant.v1.model.InputData;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechRecognitionResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.SynthesizeOptions;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.SynthesizeOptions.Voice;
import com.ibm.watson.developer_cloud.text_to_speech.v1.util.WaveUtils;

public class Application {

	public static File welcome = new File("welcome.wav");
	
	public static void main(String[] args) throws FileNotFoundException, InterruptedException, Exception {
    	
    	SpeechToText service = new SpeechToText();
        service.setUsernameAndPassword("8ef046d2-0b24-45c1-b12d-8b9e37cf535a", "w0jWHgRbtg46");
        
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
            .audio(audio).contentType(HttpMediaType.AUDIO_RAW + "; rate=" + sampleRate).model("pt-BR_BroadbandModel").build();
        
        playSound(welcome);
        
        service.recognizeUsingWebSocket(options, new BaseRecognizeCallback() {
            @Override
            public void onTranscription(SpeechRecognitionResults speechResults) {
            	if(speechResults.getResults().get(0).isFinalResults()) {
            		final String result = speechResults.getResults().get(0).getAlternatives().get(0).getTranscript().trim();
	
	            	Assistant serviceA = new Assistant("2018-02-16");
	            	serviceA.setUsernameAndPassword("f17196fb-ad7f-4efe-b9bd-d7468f0a55b4", "fVChilOOMMNx");
	
	            	InputData input = new InputData.Builder(result).build();
	            	MessageOptions option = new MessageOptions.Builder("f4067fdf-826c-4d91-ab91-b095f2cbb6ea")
	            	  .input(input)
	            	  .build();
	            	MessageResponse response = serviceA.message(option).execute();
	            	
	            	TextToSpeech textToSpeech = new TextToSpeech();
	                textToSpeech.setUsernameAndPassword("eb657d72-4c20-4d27-b0b2-03bd2711f365", "60A18b20tDmz");
	                
	                try {
	                    SynthesizeOptions synthesizeOptions = new SynthesizeOptions.Builder().text(response.getOutput().getText().toString()).accept("audio/wav")
	                        .voice(Voice.PT_BR_ISABELAVOICE).build();
	                    
	                    InputStream inputStream = textToSpeech.synthesize(synthesizeOptions).execute();
	                    InputStream in = WaveUtils.reWriteWaveHeader(inputStream);
	                    
	                    OutputStream out = new FileOutputStream("dialogflow.wav");
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
	                }

	                playSound(new File("dialogflow.wav"));    
	            	System.out.println(response);
            	}
            }
        });
           	
    	 System.out.println("Tempo limite para teste de voz 50s...");
         Thread.sleep(50 * 1000);
         
         line.stop();
         line.close();
         
         System.out.println("Tempo esgotado.");
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
