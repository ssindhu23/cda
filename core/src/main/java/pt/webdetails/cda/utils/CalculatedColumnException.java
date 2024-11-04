/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2028-08-13
 ******************************************************************************/


package pt.webdetails.cda.utils;

public class CalculatedColumnException extends Exception {

  private static final long serialVersionUID = 1L;

  public CalculatedColumnException( final String s, final Exception cause ) {
    super( s, cause );
  }
}
