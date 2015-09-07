# Download pre-build bundles #

We provide binary bundles for the following OS:

  * Windows 32bit, XP and above
  * Linux 64bit
  * Mac OSX 32bit

Other downloads are available, but not thoroughly tested. Please check out our [download section](http://code.google.com/a/eclipselabs.org/p/swtqt/downloads/list).

### You need one of the platform specific SWT bundles: ###

  * Windows 32bit [(download)](http://swtqt.eclipselabs.org.codespot.com/files/org.eclipselabs.swtqt_win32-20100921.zip)
  * Linux 64bit [(download)](http://swtqt.eclipselabs.org.codespot.com/files/org.eclipselabs.swtqt_linux64-20100921.zip)
  * Max OSX 32bit [(download)](http://swtqt.eclipselabs.org.codespot.com/files/org.eclipselabs.swtqt_macosx-20100921.zip)

After the download, you need to unzip the archive.

### Get the platform independent Qt Jambi bundle [(download)](http://swtqt.eclipselabs.org.codespot.com/files/qtjambi-4.5.2_01_osgipatch.jar) and one of the platform specific Qt Jambi bundles (containing Qt): ###

  * Windows 32bit [(download)](http://swtqt.eclipselabs.org.codespot.com/files/qtjambi-win32-msvc2005-4.5.2_01.jar)
  * Linux 64bit [(download)](http://swtqt.eclipselabs.org.codespot.com/files/qtjambi-linux64-gcc-4.5.2_01.jar)
  * Max OSX 32bit [(download)](http://swtqt.eclipselabs.org.codespot.com/files/qtjambi-macosx-gcc-4.5.2_01.jar)

As an example for Windows 32bit you should now have the following bundles:
  1. org.eclipse.swt\_3.5.2.v3557f.jar
  1. qtjambi-4.5.2\_01\_osgipatch.jar
  1. qtjambi-win32-msvc2005-4.5.2\_01.jar

### We tested with the following eclipse packages (Galileo SR2): ###

Eclipse Java IDE:
  * Windows 32bit [(download)](http://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/galileo/SR2/eclipse-java-galileo-SR2-win32.zip)
  * Linux 64bit [(download)](http://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/galileo/SR2/eclipse-java-galileo-SR2-linux-gtk-x86_64.tar.gz)
  * Mac OSX 32bit [(download)](http://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/galileo/SR2/eclipse-java-galileo-SR2-macosx-cocoa.tar.gz)

Eclipse RCP IDE:
  * Windows 32bit [(download)](http://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/galileo/SR2/eclipse-rcp-galileo-SR2-win32.zip)
  * Linux 64bit [(download)](http://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/galileo/SR2/eclipse-rcp-galileo-SR2-linux-gtk-x86_64.tar.gz)
  * Mac OSX 32bit [(download)](http://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/galileo/SR2/eclipse-rcp-galileo-SR2-macosx-cocoa.tar.gz)

To setup Eclipse with SWT/Qt remove all org.eclipse.swt`*` bundles from the plugin directory. Then copy the downloaded bundles (SWT and 2 Qt Jambi) to the plugin directory.
Due to a bug, you need to start Eclipse with the "-noSplash" parameter.
```
eclipse -noSplash
```


To run your application with SWT/Qt make sure your launch configuration contains the Qt Jambi bundles and you choose the SWT/Qt bundle and no other SWT bundles are selected. Don't be irritated by the be name difference between the project (org.eclipselabs.swtqt) and the bundles (org.eclipse.swt).

# Advanced Styler plugin #

To test the styling capabilities, and we are sure you want to, download the Advanced Styler plugin [(download)](http://swtqt.eclipselabs.org.codespot.com/files/org.eclipselabs.swtqt.advancedstyler_0.3.0.jar) and place it in your "eclipse/dropins/" folder (restart required). It will help you playing with the amazing Qt styling. We included some examples for an easy start.

For more information look at the Qt Style Sheet documentation found [here](http://doc.trolltech.com/4.5/stylesheet.html).

If you launch an Eclipse RCP application, you can add the Advanced Styler plugin to your launch config. To launch the Advanced Styler hit the green paint brush icon in the toolbar or the menu item Edit -> AdvancedStyler. You will find some styling examples to get you started.