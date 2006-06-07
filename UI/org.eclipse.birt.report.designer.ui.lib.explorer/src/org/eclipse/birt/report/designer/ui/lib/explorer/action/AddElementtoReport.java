/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.lib.explorer.action;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DNDUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.views.contentoutline.ContentOutline;

/**
 * 
 */

public class AddElementtoReport extends Action
{

	private StructuredViewer viewer;
	private Object element;

	private static final String ACTION_TEXT = Messages.getString( "AddElementtoAction.Text" ); //$NON-NLS-1$

	public void setSelectedElement( Object element )
	{
		this.element = element;
	}

	private int canContain;

	private Object target;

	/**
	 * @param text
	 * @param style
	 */
	public AddElementtoReport( StructuredViewer viewer )
	{
		super( ACTION_TEXT );
		// TODO Auto-generated constructor stub
		this.viewer = viewer;
		canContain = DNDUtil.CONTAIN_NO;
	}

	/*
	 * (non-Javadoc) Method declared on IAction.
	 */
	public boolean isEnabled( )
	{
		Object target = getTarget( );
		this.target = target;
		if ( canContain( target, element ) )
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public Object getTarget( )
	{
		IViewPart viewPart = UIUtil.getView( IPageLayout.ID_OUTLINE );
		if ( !( viewPart instanceof ContentOutline ) )
		{
			return null;
		}
		ContentOutline outlineView = (ContentOutline) viewPart;

		ISelection selection = outlineView.getSelection( );
		if ( selection instanceof StructuredSelection )
		{
			StructuredSelection strSelection = (StructuredSelection) selection;
			if ( strSelection.size( ) == 1 )
			{
				return strSelection.getFirstElement( );
			}
		}
		return null;
	}

	public void run( )
	{
		copyData( target, element );
	}

	protected boolean canContain( Object target, Object transfer )
	{
		if ( DNDUtil.handleValidateTargetCanContainMore( target,
				DNDUtil.getObjectLength( transfer ) ) )
		{
			canContain = DNDUtil.handleValidateTargetCanContain( target,
					transfer,
					true );
			return canContain == DNDUtil.CONTAIN_THIS;
		}
		return false;

	}

	private int getPosition( Object target )
	{

		int position = DNDUtil.calculateNextPosition( target, canContain );
		if ( position > -1 )
		{
			this.target = DNDUtil.getDesignElementHandle( target )
					.getContainerSlotHandle( );
		}
		return position;
	}

	protected boolean copyData( Object target, Object transfer )
	{
		// When get position, change target value if need be
		int position = getPosition( target );
		boolean result = false;

		if ( transfer != null && transfer instanceof DesignElementHandle )
		{
			DesignElementHandle sourceHandle;
			if ( ( sourceHandle = (DesignElementHandle) transfer ).getRoot( ) instanceof LibraryHandle )
			{
				// transfer element from a library.
				LibraryHandle library = (LibraryHandle) sourceHandle.getRoot( );
				ModuleHandle moduleHandle = SessionHandleAdapter.getInstance( )
						.getReportDesignHandle( );
				try
				{
					if ( moduleHandle != library )
					{
						// element from other library not itself, create a new
						// extended element.
						if ( UIUtil.includeLibrary( moduleHandle, library ) )
						{
							DNDUtil.addElementHandle( this.target,
									moduleHandle.getElementFactory( )
											.newElementFrom( sourceHandle,
													sourceHandle.getName( ) ) );
							result = true;
						}
					}
					else
					{
						result = DNDUtil.copyHandles( transfer,
								this.target,
								position );
					}
				}
				catch ( Exception e )
				{
					ExceptionHandler.handle( e );
				}
			}
			else
			{
				result = DNDUtil.copyHandles( transfer, this.target, position );
			}
		}
		else if ( transfer != null && transfer instanceof EmbeddedImageHandle )
		{

			result = DNDUtil.copyHandles( transfer, target, position );

		}
		if ( result )
		{
			viewer.reveal( this.target );
		}

		return result;
	}
}
