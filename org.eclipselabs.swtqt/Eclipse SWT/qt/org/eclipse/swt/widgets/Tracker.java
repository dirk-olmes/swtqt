/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * Portion Copyright (c) 2009-2010 compeople AG (http://www.compeople.de).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Compeople AG	- QtJambi/Qt based implementation for Windows/Mac OS X/Linux
 *******************************************************************************/
package org.eclipse.swt.widgets;

import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QRubberBand;
import com.trolltech.qt.gui.QRubberBand.Shape;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.qt.QtSWTConverter;

/**
 * Instances of this class implement rubber banding rectangles that are drawn
 * onto a parent <code>Composite</code> or <code>Display</code>. These
 * rectangles can be specified to respond to mouse and key events by either
 * moving or resizing themselves accordingly. Trackers are typically used to
 * represent window geometries in a lightweight manner.
 * 
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>LEFT, RIGHT, UP, DOWN, RESIZE</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Move, Resize</dd>
 * </dl>
 * <p>
 * Note: Rectangle move behavior is assumed unless RESIZE is specified.
 * </p>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#tracker">Tracker
 *      snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Tracker extends Widget {
	private Composite parent;
	private boolean cancelled, stippled;
	private Rectangle[] rectangles = new Rectangle[0];
	private Rectangle[] proportions = rectangles;
	private Rectangle bounds;
	private int cursorOrientation = SWT.NONE;
	private QRubberBand qRubberBand;

	/*
	 * The following values mirror step sizes on Windows
	 */
	final static int STEPSIZE_SMALL = 1;
	final static int STEPSIZE_LARGE = 9;

	/**
	 * Constructs a new instance of this class given its parent and a style
	 * value describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must
	 * be built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code>
	 * style constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 * 
	 * @param parent
	 *            a widget which will be the parent of the new instance (cannot
	 *            be null)
	 * @param style
	 *            the style of widget to construct
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                <li>ERROR_INVALID_SUBCLASS - if this class is not an
	 *                allowed subclass</li>
	 *                </ul>
	 * 
	 * @see SWT#LEFT
	 * @see SWT#RIGHT
	 * @see SWT#UP
	 * @see SWT#DOWN
	 * @see SWT#RESIZE
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public Tracker(Composite parent, int style) {
		super(parent, checkStyle(style));
		this.parent = parent;
		this.qRubberBand = createQWidget(this.style);
	}

	/**
	 * Constructs a new instance of this class given the display to create it on
	 * and a style value describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must
	 * be built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code>
	 * style constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 * <p>
	 * Note: Currently, null can be passed in for the display argument. This has
	 * the effect of creating the tracker on the currently active display if
	 * there is one. If there is no current display, the tracker is created on a
	 * "default" display. <b>Passing in null as the display argument is not
	 * considered to be good coding style, and may not be supported in a future
	 * release of SWT.</b>
	 * </p>
	 * 
	 * @param display
	 *            the display to create the tracker on
	 * @param style
	 *            the style of control to construct
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                <li>ERROR_INVALID_SUBCLASS - if this class is not an
	 *                allowed subclass</li>
	 *                </ul>
	 * 
	 * @see SWT#LEFT
	 * @see SWT#RIGHT
	 * @see SWT#UP
	 * @see SWT#DOWN
	 */
	public Tracker(Display display, int style) {
		if (display == null) {
			display = Display.getCurrent();
		}
		if (display == null) {
			display = Display.getDefault();
		}
		if (!display.isValidThread()) {
			error(SWT.ERROR_THREAD_INVALID_ACCESS);
		}
		this.style = checkStyle(style);
		this.display = display;
		this.qRubberBand = createQWidget(this.style);
	}

	protected QRubberBand createQWidget(int style) {
		QRubberBand rubberBand = new QRubberBand(Shape.Rectangle, parent != null ? parent.getQMasterWidget() : null);
		display.addControl(rubberBand, this);
		return rubberBand;
	}

	@Override
	void releaseQWidget() {
		display.removeControl(qRubberBand);
		super.releaseQWidget();
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the control is moved or resized, by sending it one of the messages
	 * defined in the <code>ControlListener</code> interface.
	 * 
	 * @param listener
	 *            the listener which should be notified
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see ControlListener
	 * @see #removeControlListener
	 */
	public void addControlListener(ControlListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Resize, typedListener);
		addListener(SWT.Move, typedListener);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when keys are pressed and released on the system keyboard, by sending it
	 * one of the messages defined in the <code>KeyListener</code> interface.
	 * 
	 * @param listener
	 *            the listener which should be notified
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see KeyListener
	 * @see #removeKeyListener
	 */
	public void addKeyListener(KeyListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.KeyUp, typedListener);
		addListener(SWT.KeyDown, typedListener);
	}

	Point adjustMoveCursor() {
		if (bounds == null) {
			return null;
		}
		int newX = bounds.x + bounds.width / 2;
		int newY = bounds.y;
		//		POINT pt = new POINT();
		//		pt.x = newX;
		//		pt.y = newY;
		//		/*
		//		 * Convert to screen coordinates iff needed
		//		 */
		//		if (parent != null) {
		//			//OS.ClientToScreen(parent.handle, pt);
		//		}
		//		OS.SetCursorPos(pt.x, pt.y);
		//TODO
		return new Point(newX, newY);
	}

	Point adjustResizeCursor() {
		if (bounds == null) {
			return null;
		}
		int newX, newY;

		if ((cursorOrientation & SWT.LEFT) != 0) {
			newX = bounds.x;
		} else if ((cursorOrientation & SWT.RIGHT) != 0) {
			newX = bounds.x + bounds.width;
		} else {
			newX = bounds.x + bounds.width / 2;
		}

		if ((cursorOrientation & SWT.UP) != 0) {
			newY = bounds.y;
		} else if ((cursorOrientation & SWT.DOWN) != 0) {
			newY = bounds.y + bounds.height;
		} else {
			newY = bounds.y + bounds.height / 2;
		}

		//		POINT pt = new POINT();
		//		pt.x = newX;
		//		pt.y = newY;
		//		/*
		//		 * Convert to screen coordinates iff needed
		//		 */
		//		if (parent != null) {
		//			//OS.ClientToScreen(parent.handle, pt);
		//		}
		//		OS.SetCursorPos(pt.x, pt.y);
		//
		//		/*
		//		 * If the client has not provided a custom cursor then determine the
		//		 * appropriate resize cursor.
		//		 */
		//		if (clientCursor == null) {
		//			int /* long */newCursor = 0;
		//			switch (cursorOrientation) {
		//			case SWT.UP:
		//				newCursor = OS.LoadCursor(0, OS.IDC_SIZENS);
		//				break;
		//			case SWT.DOWN:
		//				newCursor = OS.LoadCursor(0, OS.IDC_SIZENS);
		//				break;
		//			case SWT.LEFT:
		//				newCursor = OS.LoadCursor(0, OS.IDC_SIZEWE);
		//				break;
		//			case SWT.RIGHT:
		//				newCursor = OS.LoadCursor(0, OS.IDC_SIZEWE);
		//				break;
		//			case SWT.LEFT | SWT.UP:
		//				newCursor = OS.LoadCursor(0, OS.IDC_SIZENWSE);
		//				break;
		//			case SWT.RIGHT | SWT.DOWN:
		//				newCursor = OS.LoadCursor(0, OS.IDC_SIZENWSE);
		//				break;
		//			case SWT.LEFT | SWT.DOWN:
		//				newCursor = OS.LoadCursor(0, OS.IDC_SIZENESW);
		//				break;
		//			case SWT.RIGHT | SWT.UP:
		//				newCursor = OS.LoadCursor(0, OS.IDC_SIZENESW);
		//				break;
		//			default:
		//				newCursor = OS.LoadCursor(0, OS.IDC_SIZEALL);
		//				break;
		//			}
		//			OS.SetCursor(newCursor);
		//			if (resizeCursor != 0) {
		//				OS.DestroyCursor(resizeCursor);
		//			}
		//			resizeCursor = newCursor;
		//		}
		//TODO
		return new Point(newX, newY);
	}

	static int checkStyle(int style) {
		if ((style & (SWT.LEFT | SWT.RIGHT | SWT.UP | SWT.DOWN)) == 0) {
			style |= SWT.LEFT | SWT.RIGHT | SWT.UP | SWT.DOWN;
		}
		return style;
	}

	/**
	 * Stops displaying the tracker rectangles. Note that this is not considered
	 * to be a cancelation by the user.
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void close() {
		checkWidget();
		qRubberBand.hide();
	}

	Rectangle computeBounds() {
		if (rectangles.length == 0) {
			return null;
		}
		int xMin = rectangles[0].x;
		int yMin = rectangles[0].y;
		int xMax = rectangles[0].x + rectangles[0].width;
		int yMax = rectangles[0].y + rectangles[0].height;

		for (int i = 1; i < rectangles.length; i++) {
			if (rectangles[i].x < xMin) {
				xMin = rectangles[i].x;
			}
			if (rectangles[i].y < yMin) {
				yMin = rectangles[i].y;
			}
			int rectRight = rectangles[i].x + rectangles[i].width;
			if (rectRight > xMax) {
				xMax = rectRight;
			}
			int rectBottom = rectangles[i].y + rectangles[i].height;
			if (rectBottom > yMax) {
				yMax = rectBottom;
			}
		}

		return new Rectangle(xMin, yMin, xMax - xMin, yMax - yMin);
	}

	Rectangle[] computeProportions(Rectangle[] rects) {
		Rectangle[] result = new Rectangle[rects.length];
		bounds = computeBounds();
		if (bounds != null) {
			for (int i = 0; i < rects.length; i++) {
				int x = 0, y = 0, width = 0, height = 0;
				if (bounds.width != 0) {
					x = (rects[i].x - bounds.x) * 100 / bounds.width;
					width = rects[i].width * 100 / bounds.width;
				} else {
					width = 100;
				}
				if (bounds.height != 0) {
					y = (rects[i].y - bounds.y) * 100 / bounds.height;
					height = rects[i].height * 100 / bounds.height;
				} else {
					height = 100;
				}
				result[i] = new Rectangle(x, y, width, height);
			}
		}
		return result;
	}

	/**
	 * Returns the bounds that are being drawn, expressed relative to the parent
	 * widget. If the parent is a <code>Display</code> then these are screen
	 * coordinates.
	 * 
	 * @return the bounds of the Rectangles being drawn
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Rectangle[] getRectangles() {
		checkWidget();
		Rectangle[] result = new Rectangle[rectangles.length];
		for (int i = 0; i < rectangles.length; i++) {
			Rectangle current = rectangles[i];
			result[i] = new Rectangle(current.x, current.y, current.width, current.height);
		}
		return result;
	}

	/**
	 * Returns <code>true</code> if the rectangles are drawn with a stippled
	 * line, <code>false</code> otherwise.
	 * 
	 * @return the stippled effect of the rectangles
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public boolean getStippled() {
		checkWidget();
		return stippled;
	}

	void moveRectangles(int xChange, int yChange) {
		if (bounds == null) {
			return;
		}
		if (xChange < 0 && (style & SWT.LEFT) == 0) {
			xChange = 0;
		}
		if (xChange > 0 && (style & SWT.RIGHT) == 0) {
			xChange = 0;
		}
		if (yChange < 0 && (style & SWT.UP) == 0) {
			yChange = 0;
		}
		if (yChange > 0 && (style & SWT.DOWN) == 0) {
			yChange = 0;
		}
		if (xChange == 0 && yChange == 0) {
			return;
		}
		bounds.x += xChange;
		bounds.y += yChange;
		for (int i = 0; i < rectangles.length; i++) {
			rectangles[i].x += xChange;
			rectangles[i].y += yChange;
		}
	}

	/**
	 * Displays the Tracker rectangles for manipulation by the user. Returns
	 * when the user has either finished manipulating the rectangles or has
	 * cancelled the Tracker.
	 * 
	 * @return <code>true</code> if the user did not cancel the Tracker,
	 *         <code>false</code> otherwise
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public boolean open() {
		checkWidget();
		cancelled = false;

		/*
		 * If exactly one of UP/DOWN is specified as a style then set the cursor
		 * orientation accordingly (the same is done for LEFT/RIGHT styles
		 * below).
		 */
		int vStyle = style & (SWT.UP | SWT.DOWN);
		if (vStyle == SWT.UP || vStyle == SWT.DOWN) {
			cursorOrientation |= vStyle;
		}
		int hStyle = style & (SWT.LEFT | SWT.RIGHT);
		if (hStyle == SWT.LEFT || hStyle == SWT.RIGHT) {
			cursorOrientation |= hStyle;
		}

		/*
		 * If this tracker is being created without a mouse drag then we need to
		 * create a transparent window that fills the screen in order to get all
		 * mouse/keyboard events that occur outside of our visible windows (ie.-
		 * over the desktop).
		 */
		updateSize();
		qRubberBand.show();
		qRubberBand.setFocus();

		while (qRubberBand.isVisible()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		return !cancelled;
	}

	protected void updateSize() {
		if (rectangles.length > 0) {
			qRubberBand.setRubberBandGeometry(QtSWTConverter.convert(rectangles[0]));
		}
	}

	@Override
	void releaseWidget() {
		super.releaseWidget();
		parent = null;
		rectangles = proportions = null;
		bounds = null;
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the control is moved or resized.
	 * 
	 * @param listener
	 *            the listener which should no longer be notified
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see ControlListener
	 * @see #addControlListener
	 */
	public void removeControlListener(ControlListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
		eventTable.unhook(SWT.Resize, listener);
		eventTable.unhook(SWT.Move, listener);
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when keys are pressed and released on the system keyboard.
	 * 
	 * @param listener
	 *            the listener which should no longer be notified
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see KeyListener
	 * @see #addKeyListener
	 */
	public void removeKeyListener(KeyListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
		eventTable.unhook(SWT.KeyUp, listener);
		eventTable.unhook(SWT.KeyDown, listener);
	}

	void resizeRectangles(int xChange, int yChange) {
		if (bounds == null) {
			return;
		}
		/*
		 * If the cursor orientation has not been set in the orientation of this
		 * change then try to set it here.
		 */
		if (xChange < 0 && (style & SWT.LEFT) != 0 && (cursorOrientation & SWT.RIGHT) == 0) {
			cursorOrientation |= SWT.LEFT;
		}
		if (xChange > 0 && (style & SWT.RIGHT) != 0 && (cursorOrientation & SWT.LEFT) == 0) {
			cursorOrientation |= SWT.RIGHT;
		}
		if (yChange < 0 && (style & SWT.UP) != 0 && (cursorOrientation & SWT.DOWN) == 0) {
			cursorOrientation |= SWT.UP;
		}
		if (yChange > 0 && (style & SWT.DOWN) != 0 && (cursorOrientation & SWT.UP) == 0) {
			cursorOrientation |= SWT.DOWN;
		}

		/*
		 * If the bounds will flip about the x or y axis then apply the
		 * adjustment up to the axis (ie.- where bounds width/height becomes 0),
		 * change the cursor's orientation accordingly, and flip each
		 * Rectangle's origin (only necessary for > 1 Rectangles)
		 */
		if ((cursorOrientation & SWT.LEFT) != 0) {
			if (xChange > bounds.width) {
				if ((style & SWT.RIGHT) == 0) {
					return;
				}
				cursorOrientation |= SWT.RIGHT;
				cursorOrientation &= ~SWT.LEFT;
				bounds.x += bounds.width;
				xChange -= bounds.width;
				bounds.width = 0;
				if (proportions.length > 1) {
					for (int i = 0; i < proportions.length; i++) {
						Rectangle proportion = proportions[i];
						proportion.x = 100 - proportion.x - proportion.width;
					}
				}
			}
		} else if ((cursorOrientation & SWT.RIGHT) != 0) {
			if (bounds.width < -xChange) {
				if ((style & SWT.LEFT) == 0) {
					return;
				}
				cursorOrientation |= SWT.LEFT;
				cursorOrientation &= ~SWT.RIGHT;
				xChange += bounds.width;
				bounds.width = 0;
				if (proportions.length > 1) {
					for (int i = 0; i < proportions.length; i++) {
						Rectangle proportion = proportions[i];
						proportion.x = 100 - proportion.x - proportion.width;
					}
				}
			}
		}
		if ((cursorOrientation & SWT.UP) != 0) {
			if (yChange > bounds.height) {
				if ((style & SWT.DOWN) == 0) {
					return;
				}
				cursorOrientation |= SWT.DOWN;
				cursorOrientation &= ~SWT.UP;
				bounds.y += bounds.height;
				yChange -= bounds.height;
				bounds.height = 0;
				if (proportions.length > 1) {
					for (int i = 0; i < proportions.length; i++) {
						Rectangle proportion = proportions[i];
						proportion.y = 100 - proportion.y - proportion.height;
					}
				}
			}
		} else if ((cursorOrientation & SWT.DOWN) != 0) {
			if (bounds.height < -yChange) {
				if ((style & SWT.UP) == 0) {
					return;
				}
				cursorOrientation |= SWT.UP;
				cursorOrientation &= ~SWT.DOWN;
				yChange += bounds.height;
				bounds.height = 0;
				if (proportions.length > 1) {
					for (int i = 0; i < proportions.length; i++) {
						Rectangle proportion = proportions[i];
						proportion.y = 100 - proportion.y - proportion.height;
					}
				}
			}
		}

		// apply the bounds adjustment
		if ((cursorOrientation & SWT.LEFT) != 0) {
			bounds.x += xChange;
			bounds.width -= xChange;
		} else if ((cursorOrientation & SWT.RIGHT) != 0) {
			bounds.width += xChange;
		}
		if ((cursorOrientation & SWT.UP) != 0) {
			bounds.y += yChange;
			bounds.height -= yChange;
		} else if ((cursorOrientation & SWT.DOWN) != 0) {
			bounds.height += yChange;
		}

		Rectangle[] newRects = new Rectangle[rectangles.length];
		for (int i = 0; i < rectangles.length; i++) {
			Rectangle proportion = proportions[i];
			newRects[i] = new Rectangle(proportion.x * bounds.width / 100 + bounds.x, proportion.y * bounds.height
					/ 100 + bounds.y, proportion.width * bounds.width / 100, proportion.height * bounds.height / 100);
		}
		rectangles = newRects;
	}

	/**
	 * Sets the <code>Cursor</code> of the Tracker. If this cursor is
	 * <code>null</code> then the cursor reverts to the default.
	 * 
	 * @param newCursor
	 *            the new <code>Cursor</code> to display
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setCursor(Cursor newCursor) {
		checkWidget();
		if (newCursor != null) {
			qRubberBand.setCursor(newCursor.getQCursor());
		} else {
			qRubberBand.setCursor(null);
		}
	}

	/**
	 * Specifies the rectangles that should be drawn, expressed relative to the
	 * parent widget. If the parent is a Display then these are screen
	 * coordinates.
	 * 
	 * @param rectangles
	 *            the bounds of the rectangles to be drawn
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the set of rectangles is null
	 *                or contains a null rectangle</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setRectangles(Rectangle[] rectangles) {
		checkWidget();
		if (rectangles == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		this.rectangles = new Rectangle[rectangles.length];
		for (int i = 0; i < rectangles.length; i++) {
			Rectangle current = rectangles[i];
			if (current == null) {
				error(SWT.ERROR_NULL_ARGUMENT);
			}
			this.rectangles[i] = new Rectangle(current.x, current.y, current.width, current.height);
		}
		proportions = computeProportions(rectangles);
		updateSize();
	}

	/**
	 * Changes the appearance of the line used to draw the rectangles.
	 * 
	 * @param stippled
	 *            <code>true</code> if rectangle should appear stippled
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setStippled(boolean stippled) {
		checkWidget();
		this.stippled = stippled;
	}

	void update() {
		if (parent == null) {
			return;
		}
		if (parent != null) {
			if (parent.isDisposed()) {
				return;
			}
			Shell shell = parent.getShell();
			shell._update();
		} else {
			display.update();
		}
	}

	@Override
	public boolean qtMouseMoveEvent(QObject source, QMouseEvent mouseEvent) {
		if (source == qRubberBand) {
			//System.out.println("rubber mouse move: " + mouseEvent);
			Event event = new Event();
			event.x = mouseEvent.x();
			event.y = mouseEvent.y();
			sendEvent(SWT.Move, event);
			return true;
		}
		return false;
	}

	@Override
	public boolean qtMouseButtonReleaseEvent(QObject source, QMouseEvent mouseEvent) {
		if (source == qRubberBand) {
			//System.out.println("rubber mouse up");
			qRubberBand.hide();
			return true;
		}
		return false;
	}

}
