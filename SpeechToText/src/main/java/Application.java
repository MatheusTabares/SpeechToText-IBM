import java.io.File;
import java.io.FileNotFoundException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Application {
    
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
        
        /**
         * TODO: Chat
         */
        
        File file = new File("1.wav");
        playSound(file);
        
        /**
         * TODO: Speech to text
         */
       /* SpeechToText service = new SpeechToText();
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
            .audio(audio).contentType(HttpMediaType.AUDIO_RAW + "; rate=" + sampleRate).build();
        
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
        
        System.out.println("Fin.");*/
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
