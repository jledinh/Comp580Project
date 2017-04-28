package com.examples.cloud.speech;

import java.io.FileOutputStream;
import com.voicerss.tts.AudioCodec;
import com.voicerss.tts.AudioFormat;
import com.voicerss.tts.Languages;
import com.voicerss.tts.SpeechDataEvent;
import com.voicerss.tts.SpeechDataEventListener;
import com.voicerss.tts.SpeechErrorEvent;
import com.voicerss.tts.SpeechErrorEventListener;
import com.voicerss.tts.VoiceParameters;
import com.voicerss.tts.VoiceProvider;
import java.io.File;
import java.io.FileInputStream;


public class TextToMp3 {
	String pathname;
    public TextToMp3(String s, String pathname) throws Exception {
        VoiceProvider tts = new VoiceProvider("0f2813468abf4e26889e9445118fc550");
        VoiceParameters params = new VoiceParameters(s, Languages.English_UnitedStates);
        params.setCodec(AudioCodec.WAV);
        params.setFormat(AudioFormat.Format_44KHZ.AF_44khz_16bit_stereo);
        params.setBase64(false);
        params.setSSML(false);
        params.setRate(0);
		
        byte[] voice = tts.speech(params);
		System.out.println("HELLO");
        FileOutputStream fos = new FileOutputStream(pathname);
        fos.write(voice, 0, voice.length);
        fos.flush();
        fos.close();
        this.pathname = pathname;
    }
}
