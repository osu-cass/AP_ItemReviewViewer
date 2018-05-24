# AP_ItemReviewViewer
Welcome to the Item Review Viewer App

# Setting up environment
First, create the following directory structure in your c drive. These directories are used by Item Review Viewer to download and extract the items.
```
C:\home\tomcat7\content\gitlab
C:\home\tomcat7\content\gitlab_archive
```
This project has been built in Intellij so it is suggested that you use that editor. Open the following `pom.xml` file in IntelliJ as a project (from the project root):
```
<repo-name>\item-review-viewer\pom.xml
```
Add the following configuration to the project.
- Chang the name to "Run"
- Click "Run-> Edit Configurations"
- Click "+" at the top left corner of window.
- Under the "Server" tab, Verify that "Application Server" is set to Tomcat 7.0.81.
- Under the "Deployment" tab, click the "+" and add "item-review-viewer:war exploded"
- Under the "Startup/Connection" tab. Add the variable `env` with value `dev`. A guide on how to add environment variables can be found [here](https://www.jetbrains.com/help/idea/run-debug-configuration-application.html#1).

Now you can run the project by selecting "Run -> Debug (Run)"

