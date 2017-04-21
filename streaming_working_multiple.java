/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import static org.apache.log4j.ConsoleAppender.SYSTEM_OUT;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1beta1.RecognitionConfig;
import com.google.cloud.speech.v1beta1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1beta1.SpeechGrpc;
import com.google.cloud.speech.v1beta1.StreamingRecognitionConfig;
import com.google.cloud.speech.v1beta1.StreamingRecognitionResult;
import com.google.cloud.speech.v1beta1.StreamingRecognizeRequest;
import com.google.cloud.speech.v1beta1.StreamingRecognizeResponse;
import com.google.protobuf.ByteString;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.auth.ClientAuthInterceptor;
import io.grpc.stub.StreamObserver;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import opennlp.tools.namefind.*;
/**
 * Client that sends streaming audio to Speech.Recognize and returns streaming transcript.
 */

public class streaming_backup1 {

  private static final Logger logger = Logger.getLogger(streaming_backup1.class.getName());

  private final ManagedChannel channel;
  private final SpeechGrpc.SpeechStub speechClient;
  private static final List<String> OAUTH2_SCOPES =
      Arrays.asList("https://www.googleapis.com/auth/cloud-platform");
  public static ArrayList<String> wordsList= new ArrayList<String>();
  static final int BYTES_PER_SAMPLE = 2; // bytes per sample for LINEAR16

  private final int samplingRate;
  final int bytesPerBuffer; // buffer size in bytes

  // Used for testing
  protected TargetDataLine mockDataLine = null;

  /**
   * Construct client connecting to Cloud Speech server at {@code host:port}.
   */
  public streaming_backup1(ManagedChannel channel, int samplingRate)
      throws IOException {
    this.samplingRate = samplingRate;
    this.channel = channel;
    this.bytesPerBuffer = samplingRate * BYTES_PER_SAMPLE / 10; // 100 ms

    speechClient = SpeechGrpc.newStub(channel);

    // Send log4j logs to Console
    // If you are going to run this on GCE, you might wish to integrate with
    // google-cloud-java logging. See:
    // https://github.com/GoogleCloudPlatform/google-cloud-java/blob/master/README.md#stackdriver-logging-alpha
    ConsoleAppender appender = new ConsoleAppender(new SimpleLayout(), SYSTEM_OUT);
    logger.addAppender(appender);
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  static ManagedChannel createChannel(String host, int port) throws IOException {
    GoogleCredentials creds = GoogleCredentials.getApplicationDefault();
    creds = creds.createScoped(OAUTH2_SCOPES);
    ManagedChannel channel =
        ManagedChannelBuilder.forAddress(host, port)
            .intercept(new ClientAuthInterceptor(creds, Executors.newSingleThreadExecutor()))
            .build();

    return channel;
  }

  /**
   * Return a Line to the audio input device.
   */
  private TargetDataLine getAudioInputLine() {
    // For testing
    if (null != mockDataLine) {
      return mockDataLine;
    }

    AudioFormat format = new AudioFormat(samplingRate, BYTES_PER_SAMPLE * 8, 1, true, false);
    DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
    if (!AudioSystem.isLineSupported(info)) {
      throw new RuntimeException(String.format(
            "Device doesn't support LINEAR16 mono raw audio format at {%d}Hz", samplingRate));
    }
    try {
      TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
      // Make sure the line buffer doesn't overflow while we're filling this thread's buffer.
      line.open(format, bytesPerBuffer * 5);
      return line;
    } catch (LineUnavailableException e) {
      throw new RuntimeException(e);
    }
  }

  /** Send streaming recognize requests to server. */
  public void recognize() throws InterruptedException, IOException {
    final CountDownLatch finishLatch = new CountDownLatch(1);
    StreamObserver<StreamingRecognizeResponse> responseObserver =
        new StreamObserver<StreamingRecognizeResponse>() {
          private int sentenceLength = 1;
          /**
           * Prints the transcription results. Interim results are overwritten by subsequent
           * results, until a final one is returned, at which point we start a new line.
           *
           * Flags the program to exit when it hears "exit".
           */
          //@Override
          public void onNext(StreamingRecognizeResponse response) {
            List<StreamingRecognitionResult> results = response.getResultsList();
            if (results.size() < 1) {
              return;
            }
            
            StreamingRecognitionResult result = results.get(0);
            String transcript = result.getAlternatives(0).getTranscript();

            // Print interim results with a line feed, so subsequent transcriptions will overwrite
            // it. Final result will print a newline.
            String format = "%-" + this.sentenceLength + 's';
            if (result.getIsFinal()) {
              //format += '\n';
              this.sentenceLength = 1;
              System.out.println("h0");
  //            if (transcript.toLowerCase().indexOf("exit") >= 0) {
                
  //          	  finishLatch.countDown();
  //            }
              	
              wordsList.add(String.format(format, transcript));
    //          System.out.println(wordsList);
              finishLatch.countDown();
   //        System.out.println(wordsList.size());
              System.out.println(String.format(format, transcript));
            } else {
              format += '\r';
              this.sentenceLength = transcript.length();
            }
          //  System.out.println(wordsList.size());

//            if(wordsList.size()>0){
//            	 finishLatch.countDown();
//            }
            
            
          }

          
          public void onError(Throwable error) {
            logger.log(Level.ERROR, "recognize failed: {0}", error);
            finishLatch.countDown();
          }

         // @Override
          public void onCompleted() {
            logger.info("recognize completed.");
            finishLatch.countDown();
          }
        };

    StreamObserver<StreamingRecognizeRequest> requestObserver =
        speechClient.streamingRecognize(responseObserver);
    try {
      // Build and send a StreamingRecognizeRequest containing the parameters for
      // processing the audio.
      RecognitionConfig config =
          RecognitionConfig.newBuilder()
              .setEncoding(AudioEncoding.LINEAR16)
              .setSampleRate(samplingRate)
              .build();
      StreamingRecognitionConfig streamingConfig =
          StreamingRecognitionConfig.newBuilder()
              .setConfig(config)
              .setInterimResults(true)
              .setSingleUtterance(true)
              .build();

      StreamingRecognizeRequest initial =
          StreamingRecognizeRequest.newBuilder().setStreamingConfig(streamingConfig).build();
      requestObserver.onNext(initial);

      // Get a Line to the audio input device.
      TargetDataLine in = getAudioInputLine();
      byte[] buffer = new byte[bytesPerBuffer];
      int bytesRead;

      in.start();
    //  System.out.println("hi");
      // Read and send sequential buffers of audio as additional RecognizeRequests.
     while (finishLatch.getCount() > 0
          && (bytesRead = in.read(buffer, 0, buffer.length)) != -1) {
        StreamingRecognizeRequest request =
            StreamingRecognizeRequest.newBuilder()
                .setAudioContent(ByteString.copyFrom(buffer, 0, bytesRead))
                .build();
        requestObserver.onNext(request);
      } 
    } catch (RuntimeException e) {
      // Cancel RPC.
      requestObserver.onError(e);
      throw e;
    }
    // Mark the end of requests.
    requestObserver.onCompleted();

    // Receiving happens asynchronously.
  //  finishLatch.await(1, TimeUnit.MINUTES);
  }


  
  
  public static void main(String[] args) throws Exception {
	 // BasicConfigurator.configure();
	Scanner keyboard = new Scanner(System.in);  
	System.out.println("enter an integer");
	int myint = keyboard.nextInt();
	String file="";
//	final String[] a = {"test.txt","test2.txt"};
	
	if(myint==1){
	file="test.txt";
		
	}
	else{
	file="test2.txt";
		
	}
	boolean run=true;
//	while(run){
    String host = null;
    Integer port = null;
    Integer sampling = null;
    
    CommandLineParser parser = new DefaultParser();
    
    Options options = new Options();
    options.addOption(
        Option.builder()
            .longOpt("host")
            .desc("endpoint for api, e.g. speech.googleapis.com")
            .hasArg()
            .argName("ENDPOINT")
            .build());
    options.addOption(
        Option.builder()
            .longOpt("port")
            .desc("SSL port, usually 443")
            .hasArg()
            .argName("PORT")
            .build());
    options.addOption(
        Option.builder()
            .longOpt("sampling")
            .desc("Sampling Rate, i.e. 16000")
            .hasArg()
            .argName("RATE")
            .build());
while(run){
    try {
      CommandLine line = parser.parse(options, args);
      
      host = line.getOptionValue("host", "speech.googleapis.com");
      port = Integer.parseInt(line.getOptionValue("port", "443"));
    // System.out.println(port);
      /*if (line.hasOption("sampling")) {
        sampling = Integer.parseInt(line.getOptionValue("sampling"));
      } else {
        System.err.println("An Audio sampling rate (--sampling) must be specified. (e.g. 16000)");
        System.exit(1);
      }
      */
     // Avoid the command line thing
     sampling = 16000;
    } catch (ParseException exp) {
      System.err.println("Unexpected exception:" + exp.getMessage());
      System.exit(1);
    }
  
    	
 
    	
   
    
    ManagedChannel channel = createChannel(host, port);
    streaming_backup1 client = new streaming_backup1(channel, sampling);
    try {
    
      client.recognize();
    } finally {
    	TemplateConverter t = new TemplateConverter("/Users/*/Documents/workspace/hello/src/main/java/"+file);
    //	wordsList.remove(wordsList.size()-1);
    	System.out.println(wordsList);
    	if(wordsList.size()==10){
    	client.shutdown();
    	run=false;	
    		
    	}
//    	t.InsertField(wordsList);
//    	System.out.println(t.everything);
       //	System.out.println(wordsList);
       	//System.out.println("Hi, my name is " + wordsList.get(0) + " and I like to eat " + wordsList.get(1) + ". I go to UNC and I have"+ wordsList.get(2) + " dogs." );
      client.shutdown();
    }
  }
	
//  }
  }
}

