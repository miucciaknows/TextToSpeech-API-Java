package org.example;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions;
import com.ibm.watson.text_to_speech.v1.util.WaveUtils;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;



public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Dotenv dotenv = Dotenv.configure()
                .load();

        var apiKey = dotenv.get("APIKEY");
        var url = dotenv.get("URL");

        IamAuthenticator authenticator = new IamAuthenticator(apiKey);
        TextToSpeech textToSpeech = new TextToSpeech(authenticator);
        textToSpeech.setServiceUrl(url);

        try {
            SynthesizeOptions synthesizeOptions =
                    new SynthesizeOptions.Builder()
                            .text("Hallo, ich bin Watson!")
                            .accept("audio/wav")
                            .voice("de-DE_DieterV3Voice")
                            .build();

            InputStream inputStream =
                    textToSpeech.synthesize(synthesizeOptions).execute().getResult();
            InputStream in = WaveUtils.reWriteWaveHeader(inputStream);

            OutputStream out = new FileOutputStream("hallo.wav");
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

    }}