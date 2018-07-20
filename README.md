# AP_ItemReviewViewer
Welcome to the Item Review Viewer App

# Software requirements
All set up steps for this project were done using intellij IDE. For best results please use intellij IDE.

# Setting up environment

## Getting the Code
To get the code pull the code from the repository using the command below.
```
git clone -b dev https://github.com/osu-cass/AP_ItemReviewViewer.git
```

## Setting up Local Files
First, create the following directory structure in your c drive. These directories are used by Item Review Viewer to download and extract the items.
```
C:\home\tomcat7\content\gitlab
C:\home\tomcat7\content\gitlab_archive
```

## Installing tomcat
To run this project you will need Apache Tomcat Sever v7.0.81. To install Tomcat download the installer and run it. The installer can be found [here](https://archive.apache.org/dist/tomcat/tomcat-7/v7.0.81/bin/apache-tomcat-7.0.81.exe).

## Setting up the context.
Once tomcat is installed you will need to edit the context file to add the iris encryption key. To do this go to C:\Program Files\C:\Program Files\Apache Software Foundation\Tomcat 7.0\conf and open context as admin.

You then need to copy this line into the <context> tag.
```
   <Parameter name="tds.iris.EncryptionKey" override="false" value="msnfodnendisondoendonend"/>
```

## Opening the project

 Open the following `pom.xml` file in IntelliJ as a project (from the project root):
```
<repo-name>\item-review-viewer\pom.xml
```

## Configuring the project
To start the configuration Follow the steps below.
- Change the name in the name field at the top of the window to "Run"
- Click "Run-> Edit Configurations"
- Click "+" at the top left corner of window.
- Then from the menu of items on the left hand side select Tomcat server-> local
- Under the "Server" tab, Verify that "Application Server" is set to Tomcat 7.0.81.

### Configuring tomcat.
- If the Application Server is not set to Tomcat 7.0.81 click the configure button next to the Application server.
- Next A pop up window will pop up. From this window you will need to set your tomcat home. To do this click the "..." button to the right of the Tomcat Home Field.
- From the pop up file explorer in Intellij navigate to and select your tomcat home directory. If you installed tom cat using the installer above on windows ten this will be at C:\Program Files\Apache Software Foundation\Tomcat 7.0.
- Next we will need to select the libraries we will need in our project from tomcat server. This can be done by clicking the "+" button under the libraries section of the pop up window that is already open.
- This will cause the Intellij File explorer to pop up. From the explorer navigate to C:\Program Files\Apache Software Foundation\Tomcat 7.0\ and select jsp-api.jar and servlet-api.jar.
- Click Ok This will cause the Tomcat configuration window to close.
- Under the tomcat server Settings near the bottom of the window set HTTP port to 8050.

### Configuring The deployment
- Navigate to the Deployment tab
- Under the "Deployment" tab, click the "+" and add "item-review-viewer:war exploded"
- Ensure that the application context is "/".

### Configuring the environment variables.
- Click "Run-> Edit Configurations"
- Click "+" under the "Before launch: ..." section.
- Select "Run Maven Goal", then type `process-resources` in the Command line input.
- Make sure the order of items in the "Before launch: ..." is
  - "Run Maven Goal 'item-review-viewer: process-resources'"
  - "Build"
  - "Build 'item-review-viewer:war exploded' artifact"
- You can change the environment in the "Maven Projects" tab
- Select an environment in "Profiles" section, default profile is 'production'

A guide on how to add environment variables can be found [here](https://www.jetbrains.com/help/idea/run-debug-configuration-application.html#1).

Now you can run the project by selecting "Run -> Debug (Run)"

If in setting up the Application you find any missing steps feel free to add them to this readme.

