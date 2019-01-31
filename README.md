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
2. Click "+" at the top left corner of window.
3. From the menu of items on the left hand side, select "Tomcat Server" -> Local
4. Under the "Server" tab, Verify that "Application server:" is set to "Tomcat 7.0.81".

### Configuring tomcat.
If the "Application server:" is not set to "Tomcat 7.0.81".
1. Click the "Configure" button next to the "Application server:".
2. In a pop up "Application Servers" popped up, click the "..." button to the right of the "Tomcat Home" Field.
3. From the pop up file explorer in Intellij, navigate to and select your tomcat home directory. If you installed tomcat using the installer above on windows then this will be at `C:\Program Files\Apache Software Foundation\Tomcat 7.0`.
4. Next we will need to select the libraries we will need in our project from tomcat server. This can be done by clicking the "+" button under the libraries section of the pop up window that is already open.
5. This will cause the Intellij File explorer to pop up. From the explorer navigate to `C:\Program Files\Apache Software Foundation\Tomcat 7.0\` and select `jsp-api.jar` and `servlet-api.jar`.
6. Click Ok. This will cause the Tomcat configuration window to close.

1. Under the "Tomcat Server Settings" near the bottom of the window set "HTTP port:" to "8050".### Configuring The deployment
2. Navigate to the "Deployment" tab in "Run/Debug Configurations" pop up.
3. Click the "+" and add "item-review-viewer:war exploded"
4. Ensure that the "Application context:" is "/".

### Configuring the environment variables.
1. Click "+" under the "Before launch: Maven Gaol, BUild, ..." section in "Run/Debug Configurations" pop up.
2. Select "Run Maven Goal", then type `process-resources` in the Command line input.
3. Make sure the order of items in the the section is
   a. "Run Maven Goal 'item-review-viewer: process-resources'"
   b. "Build"
   c. "Build 'item-review-viewer:war exploded' artifact"
4. You can change the environment in the "Maven Projects" tab
5. Select an environment in "Profiles" section, default profile is 'production'

A guide on how to add environment variables can be found [here](https://www.jetbrains.com/help/idea/run-debug-configuration-application.html#1).

Now you can run the project by selecting "Run -> Debug (Run)"

If in setting up the Application you find any missing steps feel free to add them to this readme.

### Running the Client
Though you can run the back end of the application without running the client it will probably be most useful to run the client while developing IRV. To run the client go to the client directory (item-review-view/src/main/client). Once at the client run the following command:

```
> npm i
> npm run build
> npm run dev
```
 You may also want to have ts watch the files so that as you are developing the client will update as you save your changes. You can do that by doing the following:

 ```
> npm run watch
 ```

Finally, If you are also doing development that requires changes in sb-components (our components library see repo for more details) you may want to link this this project to the client so that changes made to sb components will update the client in real time. Do the following:

First run this command on sb-components:
```
> npm link
```
next run this command on the client:
```
> npm link @osu-cass/sb-components
```

## Preparing for Deployment

### Deployment Checklist
- Check to make sure that the application properties are set correctly for the profile that you would like to run. These can be found in /item-review-viewer/src/main/resources. For specifics on the contents of this file see the readme for the IRV configuration readme.
- Check to make sure that the settings in settings-mysql.xml are set up for you deployment. For specifics on the contents of this file see the readme for the IRV config readme.
- Ensure that the security config in the web.xml is not commented out (This is commonly done for development purposes)
- Ensure that the server.xml file has the correct url for your deployment under the Connector element.
- Ensure that the client is built. To do this ensure that the client/dist folder exists and has content init.

### Packaging the deployment

Below are the steps required to package the code and build a docker image for the purposes of deploying the Item Review Viewer

- Build the client.
- run ```mvn process-resources -P{desired profile}```
- Build the war file, you can do this one of two ways. You can run ```mvn package -P{desired profile}``` Or You can build the war using intellij by click on the build menu, build artifacts and selecting item review viewer:war.
- Once the war has been built you will need to build the docker image. This can be done by running this command ``` docker build -t {desired tag} .``` this will need to be done in the item review viewer directory.
- Finally once the image has been build you will need to push the image to docker hub. ```docker push {tag from build}```

Once you have completed the above steps you are ready to proceed to the deployment. The steps for the deployment of the IRV project will be in the readme in the IRV deployment repository.



