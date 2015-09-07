# Getting the code #

Eclipse Labs uses [Mercurial](http://mercurial.selenic.com/) as source code management (SCM) tool. If you do not have a client, please visit their [download section](http://mercurial.selenic.com/downloads/).

Their is also an Eclipse plugin, [MercurialEclipse](http://javaforge.com/project/HGE).

To checkout the code please execute the following command in your favorite shell:
```
hg clone https://hg.codespot.com/a/eclipselabs.org/swtqt/ swtqt 
```

After the checkout you end up with a "swtqt" directory containing the following three projects:

  1. **org.eclipselabs.swtqt** - this contains the SWT/Qt source code
  1. **org.eclipselabs.swtqt.bundles** - the required the Qt Jambi Java and native bundles for Windows, Linux and Mac OS X
  1. **org.eclipselabs.swtqt.advancedstyler** - this contains the Eclipse plugin for easy styling of your application

# Setting it up #

Import the three projects to Eclipse via File -> Import... -> Existing Projects into Workspace. Select the location where you checked out the projects.

The org.eclipselabs.swtqt contains three .classpath files, one for each of the supported operating systems.

  1. .classpath\_qt\_linux64
  1. .classpath\_qt\_osx
  1. .classpath\_qt\_win32

Copy the one for your OS and save it as ".classpath". Rebuild the project.

SWT/Qt needs the Qt Jambi bundles. To make them known, you have to add them to your target platform:
  1. go to Window -> Preferences -> Plugin Development -> Target Platform
  1. click "Add..."
  1. select "Default..."
  1. click "Next"
  1. click "Add..."
  1. select "Directory"
  1. click "Next"
  1. enter the location of the "swtqt" directory, you checked out
  1. click "Finish"
  1. go to the "Content" tab
  1. make sure no org.eclipse.swt`*` bundles are selected
  1. select "qtjambi-4.5.2\_01\_osgipatch"
  1. select **only one** of the qtjambi bundles for your platform (win32, linux64, etc.)
  1. click "Finish"
  1. select your new Target Platform
  1. click "OK" and eclipse will rebuild your workspace

You should not have any compile errors.