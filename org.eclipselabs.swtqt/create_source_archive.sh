#!/bin/sh

DATE=`date "+%F-%H%M"`
ARC=de.compeople.scp.swt.qt-source_$DATE.zip
rm $ARC
zip -r $ARC \
	"Eclipse SWT/qt" \
	"Eclipse SWT Drag and Drop/qt" \
	"Eclipse SWT Browser/qt" \
	-x "*/CVS*"

