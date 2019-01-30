# AP_ItemReviewViewer
Welcome to the Item Review Viewer App

# Software requirements
All set up steps for this project were done using [IntelliJ IDEA](https://www.jetbrains.com/idea/?fromMenu). For best results, please use IntelliJ IDEA.

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
Once tomcat is installed you will need to edit the context file to add the iris encryption key.
To do this, open following file  as **admin**
`C:\Program Files\Apache Software Foundation\Tomcat 7.0\conf\context.xml`

You then need to copy `<Parameter...>` line into the `<Context>` tag.
```xml
<Context>
   ...
   <Parameter name="tds.iris.EncryptionKey" override="false" value="msnfodnendisondoendonend"/>
</Context>
```

## Opening the project
 Open the following `pom.xml` file in IntelliJ as a project (from the project root):
```xml
<repo-name>\item-review-viewer\pom.xml
```

## Configuring the project in IntelliJ
To start the configuration Follow the steps below.
1. Click "Run -> Edit Configurations"
1. Click "+" at the top left corner of window.
1. From the menu of items on the left hand side, select "Tomcat Server" -> Local
1. Under the "Server" tab, Verify that "Application server:" is set to "Tomcat 7.0.81".

### Configuring tomcat.
If the "Application server:" is not set to "Tomcat 7.0.81".
1. Click the "Configure" button next to the "Application server:".
1. In a pop up "Application Servers" popped up, click the "..." button to the right of the "Tomcat Home" Field.
1. From the pop up file explorer in Intellij, navigate to and select your tomcat home directory. If you installed tomcat using the installer above on windows then this will be at `C:\Program Files\Apache Software Foundation\Tomcat 7.0`.
1. Next we will need to select the libraries we will need in our project from tomcat server. This can be done by clicking the "+" button under the libraries section of the pop up window that is already open.
1. This will cause the Intellij File explorer to pop up. From the explorer navigate to `C:\Program Files\Apache Software Foundation\Tomcat 7.0\` and select `jsp-api.jar` and `servlet-api.jar`.
1. Click Ok. This will cause the Tomcat configuration window to close.

1. Under the "Tomcat Server Settings" near the bottom of the window set "HTTP port:" to "8050".### Configuring The deployment
1. Navigate to the "Deployment" tab in "Run/Debug Configurations" pop up.
1. Click the "+" and add "item-review-viewer:war exploded"
1. Ensure that the "Application context:" is "/".

### Configuring the environment variables.
1. Click "+" under the "Before launch: Maven Gaol, BUild, ..." section in "Run/Debug Configurations" pop up.
1. Select "Run Maven Goal", then type `process-resources` in the Command line input.
1. Make sure the order of items in the the section is
   1. "Run Maven Goal 'item-review-viewer: process-resources'"
   1. "Build"
   1. "Build 'item-review-viewer:war exploded' artifact"
1. You can change the environment in the "Maven Projects" tab
1. Select an environment in "Profiles" section, default profile is 'production'

A guide on how to add environment variables can be found [here](https://www.jetbrains.com/help/idea/run-debug-configuration-application.html#1).

Now you can run the project by selecting "Run -> Debug (Run)"

If in setting up the Application you find any missing steps feel free to add them to this readme.
