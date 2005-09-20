/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation .
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/ 

package org.eclipse.birt.report.designer.internal.lib.editparts;

import org.eclipse.birt.report.designer.internal.lib.editors.figures.EmptyFigure;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;


/**
 * The Editpart is create in the library editor when the seleection object form outline is not a visual element.
 * 
 */
public class EmptyEditPart extends ReportElementEditPart
{

	/**
	 * @param model
	 */
	public EmptyEditPart( Object model )
	{
		super( model );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#elementChanged(org.eclipse.birt.report.model.api.DesignElementHandle, org.eclipse.birt.report.model.api.activity.NotificationEvent)
	 */
	public void elementChanged( DesignElementHandle arg0, NotificationEvent arg1 )
	{
		//do nothing
	}

	/*Doesn't install any police
	 *  (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#createEditPolicies()
	 */
	protected void createEditPolicies( )
	{
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#refreshFigure()
	 */
	public void refreshFigure( )
	{
		getFigure().setSize(getFigure().getParent().getClientArea().getSize());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure( )
	{
		return new EmptyFigure();
	}

	
	/* Doesn't install any police include install by parent
	 * (non-Javadoc)
	 * @see org.eclipse.gef.EditPart#installEditPolicy(java.lang.Object, org.eclipse.gef.EditPolicy)
	 */
	public void installEditPolicy( Object role, EditPolicy editPolicy )
	{
		//do nother
	}
}
