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

package org.eclipse.birt.report.item.crosstab.internal.ui.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.re.CrosstabQueryUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.plugin.CrosstabPlugin;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.LevelAttributeHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.LevelAttribute;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * CrosstabUIHelper
 */
public class CrosstabUIHelper
{
	public static String CROSSTAB_LINK_IMAGE = "icons/pal/crosstab-link.gif"; //$NON-NLS-1$
	public static String CROSSTAB_IMAGE = "icons/pal/crosstab.gif";//$NON-NLS-1$
	public static String MEASURE_IMAGE = "icons/pal/data.gif";//$NON-NLS-1$
	public static String COLUMNS_AREA_IMAGE = "icons/pal/column-area.gif";//$NON-NLS-1$
	public static String ROWS_AREA_IMAGE = "icons/pal/row-area.gif";//$NON-NLS-1$
	public static String DETAIL_AREA_IMAGE = "icons/pal/detail-area.gif";//$NON-NLS-1$
	public static String LEVEL_IMAGE = "icons/pal/level.gif";//$NON-NLS-1$
	public static String CELL_IMAGE = "icons/pal/cell.gif";//$NON-NLS-1$
	public static String DETAIL_IMAGE = "icons/pal/details.gif";//$NON-NLS-1$
	public static String HEADER_IMAGE = "icons/pal/header.gif";//$NON-NLS-1$
	public static String AGGREGATION_IMAGE = "icons/pal/aggregation.gif";//$NON-NLS-1$
	public static String LEVEL_AGGREGATION = "icons/pal/cell-level-aggregation.gif";//$NON-NLS-1$
	public static String SHOW_HIDE_LEVEL = "icons/pal/show-hide-level.gif";//$NON-NLS-1$
	public static String LEVEL_ARROW = "icons/pal/level-arrow.gif";//$NON-NLS-1$
	public static String COLUMN_GRAND_TOTAL_IMAGE="icons/pal/col-grand-total.gif"; //$NON-NLS-1$
	public static String ROW_GRAND_TOTAL_IMAGE="icons/pal/row-grand-total.gif"; //$NON-NLS-1$
	public static String COLUMN_SUB_TOTAL_IMAGE="icons/pal/col-subtotal.gif"; //$NON-NLS-1$
	public static String ROW_SUB_TOTAL_IMAGE="icons/pal/row-subtotal.gif"; //$NON-NLS-1$
	public static String OPTIONS_ICON = "icons/pal/options.gif";//$NON-NLS-1$
	public static String ADD_DERIVED_MEASURE = "icons/pal/derived-measure.gif";//$NON-NLS-1$

	
	private static Image createImage( String sPluginRelativePath )
	{
		Image img = null;
		try
		{
			try
			{
				img = new Image( Display.getCurrent( ),
						getURL( sPluginRelativePath ).openStream( ) );
			}
			catch ( MalformedURLException e1 )
			{
				img = new Image( Display.getCurrent( ),
						new FileInputStream( getURL( sPluginRelativePath ).toString( ) ) );
			}
		}
		catch ( FileNotFoundException e )
		{

		}
		catch ( IOException e )
		{

		}

		// If still can't load, return a dummy image.
		if ( img == null )
		{
			img = new Image( Display.getCurrent( ), 1, 1 );
		}
		return img;
	}

	/**
	 * This method returns an URL for a resource given its plugin relative path.
	 * It is intended to be used to abstract out the usage of the UI as a plugin
	 * or standalone component when it comes to accessing resources.
	 * 
	 * @param sPluginRelativePath
	 *            The path to the resource relative to the plugin location.
	 * @return URL representing the location of the resource.
	 */
	public static URL getURL( String sPluginRelativePath )
	{
		URL url = null;

		try
		{
			url = new URL( CrosstabPlugin.getDefault( )
					.getBundle( )
					.getEntry( "/" ), sPluginRelativePath ); //$NON-NLS-1$
		}
		catch ( MalformedURLException e )
		{
		}

		return url;
	}

	/**
	 * This is a convenience method to get an imgIcon from a URL.
	 * 
	 * @param sPluginRelativePath
	 *            The URL for the imgIcon.
	 * @return The imgIcon represented by the given URL.
	 * @see #setImageCached( boolean )
	 */
	public static Image getImage( String sPluginRelativePath )
	{
		ImageRegistry registry = JFaceResources.getImageRegistry( );
		Image image = registry.get( sPluginRelativePath );
		if ( image == null )
		{
			image = createImage( sPluginRelativePath );
			registry.put( sPluginRelativePath, image );
		}
		return image;
	}

	private static final String LABEL_NAME = Messages.getString( "AddSubTotalAction.LabelName" );//$NON-NLS-1$

	public static void CreateGrandTotalLabel( CrosstabCellHandle cellHandle )
			throws SemanticException
	{
		LabelHandle dataHandle = DesignElementFactory.getInstance( )
				.newLabel( null );

		// dataHandle.setDisplayName( NAME );
		dataHandle.setText( LABEL_NAME );
		cellHandle.addContent( dataHandle );
	}

	private static final String DISPALY_NAME = Messages.getString( "AddSubTotalAction.TotalName" );//$NON-NLS-1$


	public static void CreateSubTotalLabel( LevelViewHandle levelView,
			CrosstabCellHandle cellHandle ) throws SemanticException
	{
		CrosstabReportItemHandle crosstab = levelView.getCrosstab( );
		DataItemHandle dataHandle = createColumnBindingAndDataItemForSubTotal( (ExtendedItemHandle) crosstab.getModelHandle( ),levelView.getCubeLevel( ));
		cellHandle.addContent( dataHandle );
	}
	
	public static DataItemHandle createColumnBindingAndDataItemForSubTotal(
			ReportItemHandle owner, LevelHandle levelHandle )
			throws SemanticException
	{
		ComputedColumn bindingColumn = CrosstabAdaptUtil.createLevelDisplayComputedColumn( owner,
				levelHandle );
		String expression = bindingColumn.getExpression( );
		bindingColumn.setDataType( DesignChoiceConstants.COLUMN_DATA_TYPE_STRING );		
		bindingColumn.setExpression( expression + "+\" " + DISPALY_NAME + "\"");
		bindingColumn.setName( levelHandle.getName( ) + "_" + DISPALY_NAME );
		
		ComputedColumnHandle bindingHandle = owner.addColumnBinding( bindingColumn,
				false );

		DataItemHandle dataHandle = DesignElementFactory.getInstance( )
				.newDataItem( levelHandle.getName( ) + "_" + DISPALY_NAME );
		dataHandle.setResultSetColumn( bindingHandle.getName( ) );

		if ( levelHandle.getDateTimeFormat( ) != null )
		{
			if ( levelHandle.getContainer( ) != null
					&& levelHandle.getContainer( ).getContainer( ) != null )
			{
				Iterator itr = levelHandle.attributesIterator( );

				boolean hasDateAttribute = false;

				while ( itr != null && itr.hasNext( ) )
				{
					LevelAttributeHandle att = (LevelAttributeHandle) itr.next( );

					if ( LevelAttribute.DATE_TIME_ATTRIBUTE_NAME.equals( att.getName( ) ) )
					{
						hasDateAttribute = true;
						break;
					}
				}

				if ( hasDateAttribute )
				{
					String dimensionName = levelHandle.getContainer( )
							.getContainer( )
							.getName( );

					bindingHandle.setDataType( DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME );
					bindingHandle.setExpression( ExpressionUtil.createJSDimensionExpression( dimensionName,
							levelHandle.getName( ),
							LevelAttribute.DATE_TIME_ATTRIBUTE_NAME ) );

					dataHandle.getPrivateStyle( )
							.setDateTimeFormatCategory( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_CUSTOM );
					dataHandle.getPrivateStyle( )
							.setDateTimeFormat( levelHandle.getDateTimeFormat( ) );
				}
			}
		}

		return dataHandle;
	}
	

	public static ICubeQueryDefinition createBindingQuery(
			CrosstabReportItemHandle crosstabItem ) throws Exception
	{
		return CrosstabQueryUtil.createCubeQuery( crosstabItem,
				null,
				false,
				true,
				true,
				true,
				false,
				false );
	}

}
