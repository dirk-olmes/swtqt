== Benutzung der Debug Libraries von Qt/QtJambi ==

* Ben�tigt wird MS Visual C++ 2008 Express Edition 
     ==> http://www.microsoft.com/germany/Express/download/downloaddetails.aspx?p=vcpp
	
* Aus dem Verzeichnis p:\SWT-QT\QT-Debug Libs\ muss das Archiv qt-4.5.2_01-debug.zip nach C:\qt ausgepackt werden.

* Das Eclipse-Projekt de.compeople.scp.swt.qt auschecken und
** die 3 DLLs + das *.manifest aus extra_debug_jars/copy_To_JVM_bin in das bin-Verzeichnis der verwendeten JVM kopieren
** das META-INF/MANIFEST.MF mit dem META-INF/DEBUG_MANIFEST.MF �berschreiben
** das .classpath mit dem .classpath_qt_debug �berschreiben
** im Verzeichnis documents liegt auch eine QTJambi-Hilfe "QTJambi 4.5.2.chm"

* Die Launch-Deskriptoren (Qt) m�ssen mit der Java System Property -Dcom.trolltech.qt.debug versehen werden.

* Erster Versuch
** In EclipseStarter.run(String[] args, Runnable endSplashHandler) einen Breakpoint auf "return run(null);" setzen. Und mit Debug starten.
** Wenn an dem Breakpoint angekommen, die Proze�-Id des Java-Prozesses ermittlen und diese in dem Visual Studio �ber "Extras" > "An den Proze� anh�ngen" ausw�hlen.
** Im Visual Studio unter "Debuggen" > "Ausnahmen" > "Win32 Exceptions" die ben�tigten Ausnahmen ausw�hlen, z.B. Access Violation
** In Eclipse dann weiter laufen lassen
