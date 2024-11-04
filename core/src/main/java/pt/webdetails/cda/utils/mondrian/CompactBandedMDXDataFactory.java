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


package pt.webdetails.cda.utils.mondrian;

import javax.swing.table.TableModel;

import mondrian.olap.Result;
import org.pentaho.reporting.engine.classic.core.DataRow;
import org.pentaho.reporting.engine.classic.core.ReportDataFactoryException;
import org.pentaho.reporting.engine.classic.extensions.datasources.mondrian.AbstractNamedMDXDataFactory;

/**
 * This data-factory operates in Legacy-Mode providing a preprocessed view on the mondrian result. It behaves exactly as
 * known from the Pentaho-Platform and the Hitachi Vantara-Report-Designer. This mode of operation breaks the structure of the
 * resulting table as soon as new rows are returned by the server.
 */
public class CompactBandedMDXDataFactory extends AbstractNamedMDXDataFactory {

  private static final long serialVersionUID = 1L;

  public CompactBandedMDXDataFactory() {
  }

  /**
   * Queries a datasource. The string 'query' defines the name of the query. The Parameterset given here may contain
   * more data than actually needed for the query.
   * <p/>
   * The parameter-dataset may change between two calls, do not assume anything, and do not hold references to the
   * parameter-dataset or the position of the columns in the dataset.
   *
   * @param queryName  the query name
   * @param parameters the parameters for the query
   * @return the result of the query as table model.
   * @throws ReportDataFactoryException if an error occured while performing the query.
   */
  public TableModel queryData( final String queryName, final DataRow parameters ) throws ReportDataFactoryException {
    final Result cellSet = performQuery( queryName, parameters );
    return new CompactBandedMDXTableModel( cellSet, extractQueryLimit( parameters ) );
  }
}
