# COMP 580 UNC Project

Diction n' Fiction

Mad Libs for the vision impaired

This is our final project for COMP 580. For now, this only works locally and must be run on Eclipse. 

In order to set the project up you have to set up the Google SDK and verify your account. You will then need to go to the Eclipse Marketplace and search then download the Google Plugin for Eclipse. After that, you will need to login in with your google account clicking the G button at the top. Then you can create a new maven based google project. In the pom.xml file of your new maven project, you must import several maven dependencies:

group id: com.google.cloud

artifact id: google-cloud-speech

version: 0.9.3-alpha





You will also need a service account in order to run the program which you can sign up for one here:

https://accounts.google.com/ServiceLogin?service=cloudconsole&ltmpl=api&osid=1&passive=true&continue=https://console.developers.google.com/apis/credentials#identifier

After all of this is done, you must download and unzip the mp3 files, dnfm3.zip into your documents folder and add all the text files into the project as well. You must also change all the file paths in dnf.java to your local file paths.

IF the above was all done correctly, you should be able to run the program now and enjoy Diction n' Fiction
