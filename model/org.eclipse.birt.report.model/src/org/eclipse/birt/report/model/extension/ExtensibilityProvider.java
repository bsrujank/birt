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

package org.eclipse.birt.report.model.extension;

import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.ExtensionElementDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

/**
 * Represents the extensibility provider which make design element extendable.
 */

public abstract class ExtensibilityProvider implements IExtendableElement
{

	/**
	 * The design element which is extendable.
	 */

	DesignElement element;

	/**
	 * The overridden extension element definition.
	 */

	ExtensionElementDefn cachedExtDefn = null;

	/**
	 * The name of the extension which is used to extend the extendable element.
	 */

	String extensionName = null;

	/**
	 * Constructs the extensibility provider with the extendable element and the
	 * extension name.
	 * 
	 * @param element
	 *            the extendable element
	 * @param extensionName
	 *            the extension name
	 */

	ExtensibilityProvider( DesignElement element, String extensionName )
	{
		this.extensionName = extensionName;
		this.element = element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IExtendableElement#getExtDefn()
	 */

	public ExtensionElementDefn getExtDefn( )
	{
		if ( extensionName == null )
			return null;

		if ( cachedExtDefn == null )
		{
			MetaDataDictionary dd = MetaDataDictionary.getInstance( );
			cachedExtDefn = (ExtensionElementDefn) dd
					.getExtension( extensionName );
		}

		return cachedExtDefn;
	}

	/**
	 * Returns the read-only list of all property definitions, including those
	 * defined in Model and extension definition file. The returned list is
	 * read-only, so no modification is allowed on this list. Each one in list
	 * is the instance of <code>IPropertyDefn</code>.
	 * 
	 * @return the read-only list of all property definitions. Return empty list
	 *         if there is no property defined.
	 */

	public List getPropertyDefns( )
	{
		ExtensionElementDefn extDefn = getExtDefn( );

		// If no extension element definition exists, just return the list of
		// property definition on this extension element definition.

		if ( extDefn != null && extDefn.getProperties( ) != null )
		{
			return extDefn.getProperties( );
		}

		return Collections.EMPTY_LIST;
	}

	/**
	 * Returns the element property definition with the given name from Model or
	 * the extension definition file.
	 * 
	 * @param propName
	 *            name of the property
	 * @return the element property definition with the given name
	 */

	public IPropertyDefn getPropertyDefn( String propName )
	{
		ExtensionElementDefn extDefn = getExtDefn( );

		if ( extDefn != null && extDefn.getProperties( ) != null )
		{
			return extDefn.getProperty( propName );
		}

		return null;
	}

	/**
	 * Returns the element property definition with the given name from Model or
	 * the extension definition file.
	 * 
	 * @param propName
	 *            name of the property
	 * @return the element property definition with the given name
	 */

	public IPropertyDefn getExtensionPropertyDefn( String propName )
	{
		ExtensionElementDefn extDefn = getExtDefn( );

		if ( extDefn != null && extDefn.getLocalProperties( ) != null )
		{
			return extDefn.getProperty( propName );
		}

		return null;
	}

	/**
	 * Checks whether the extendable element this provider supports can extends
	 * from the given parent element.
	 * 
	 * @param parent
	 *            the parent element to check
	 * @throws ExtendsException
	 *             if the extendable element this provide supports can not
	 *             extends from the given parent element.
	 */

	public void checkExtends( DesignElement parent ) throws ExtendsException
	{
		String parentExt = (String) parent.getProperty( null,
				EXTENSION_NAME_PROP );

		assert extensionName != null;
		if ( !extensionName.equalsIgnoreCase( parentExt ) )
			throw new ExtendsException( element, parent,
					ExtendsException.DESIGN_EXCEPTION_WRONG_EXTENSION_TYPE );
	}
}
