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

package pt.webdetails.robochef;

import static org.pentaho.di.core.Const.isEmpty;
import static org.pentaho.di.core.Const.trim;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.di.trans.step.RowListener;

/**
 * A container for all the necessary configuration required to execute a transformation. XML snippits for Steps can
 * easily be generated by creating and configuring a step in Spoon then highlighting the step icon on the canvas and
 * copying it to the clipboard. Example use:
 * <p/>
 * <pre>
 * </pre>
 */
public class DynamicTransConfig {
  private static final Log logger = LogFactory.getLog( DynamicTransConfig.class );

  /**
   * The types of config entries that can be used. Exactly one TRANS entry is required.
   */
  public enum EntryType {
    STEP, HOP, STEP_ERROR_HANDLING, VARIABLE, PARAMETER
  }

  private final Map<String, RowProducerBridge> inputs;
  private final Map<String, RowListener> outputs;
  private final EnumMap<EntryType, Map<String, String>> configEntries;

  private final AtomicBoolean frozen = new AtomicBoolean( false );

  public DynamicTransConfig() {
    inputs = new HashMap<String, RowProducerBridge>( 2 );
    outputs = new HashMap<String, RowListener>( 1 );
    configEntries = new EnumMap<EntryType, Map<String, String>>( EntryType.class );
    for ( final EntryType entryType : EntryType.values() ) {
      configEntries.put( entryType, new HashMap<String, String>() );
    }
  }

  /**
   * @return an unmodifiable view of the inputs
   * @throws IllegalAccessError if the config is not yet frozen
   */
  protected Map<String, RowProducerBridge> getFrozenInputs() {
    if ( !frozen.get() ) {
      throw new IllegalAccessError( "Config is not yet frozen" );
    }
    return Collections.unmodifiableMap( inputs );
  }

  /**
   * Adds a single input mapping to the config
   *
   * @param targetStepName the name of the InjectorStep this RowProducer will feed
   * @param input          the RowProducer that will generate rows at runtime
   * @throws IllegalAccessError       if the config is frozen
   * @throws IllegalArgumentException if the input mapping is invalid
   */
  public void addInput( final String targetStepName, final RowProducerBridge input ) {
    if ( frozen.get() ) {
      throw new IllegalAccessError( "Config is frozen" );
    }
    if ( isEmpty( trim( targetStepName ) ) ) {
      throw new IllegalArgumentException( "targetStepName is null" );
    }
    if ( input == null ) {
      throw new IllegalArgumentException( "inputRowProducer is null" );
    }
    inputs.put( targetStepName, input );
  }

  /**
   * Adds a set of input mappings to the config
   *
   * @param inputs
   * @throws IllegalAccessError       if the config is frozen
   * @throws IllegalArgumentException if any input mapping is invalid
   */
  public void addInputs( final Map<String, RowProducerBridge> inputs ) {
    if ( inputs == null ) {
      throw new IllegalArgumentException( "input mappings is null" );
    }
    for ( final Entry<String, RowProducerBridge> entry : inputs.entrySet() ) {
      addInput( entry.getKey(), entry.getValue() );
    }
  }

  /**
   * @return an unmodifiable view of the outputs
   * @throws IllegalAccessError if the config is not yet frozen
   */
  protected Map<String, RowListener> getFrozenOutputs() {
    if ( !frozen.get() ) {
      throw new IllegalAccessError( "Config is not yet frozen" );
    }
    return Collections.unmodifiableMap( outputs );
  }

  /**
   * Adds a single output mapping to the config
   *
   * @param targetStepName the name of the step to which this RowListener will connect
   * @param output         the RowListener that will build a resultset at runtime
   * @throws IllegalAccessError       if the config is frozen
   * @throws IllegalArgumentException if the output mapping is invalid
   */
  public void addOutput( final String targetStepName, final RowListener output ) {
    if ( frozen.get() ) {
      throw new IllegalAccessError( "Config is frozen" );
    }
    if ( isEmpty( trim( targetStepName ) ) ) {
      throw new IllegalArgumentException( "targetStepName is null" );
    }
    if ( output == null ) {
      throw new IllegalArgumentException( "outputListener is null" );
    }
    outputs.put( targetStepName, output );
  }

  /**
   * Adds a set of output mappings to the config
   *
   * @param outputs
   * @throws IllegalAccessError       if the config is frozen
   * @throws IllegalArgumentException if any output mapping is invalid
   */
  public void addOutputs( final Map<String, RowListener> outputs ) {
    if ( outputs == null ) {
      throw new IllegalArgumentException( "output mappings is null" );
    }
    for ( final Entry<String, RowListener> entry : outputs.entrySet() ) {
      addOutput( entry.getKey(), entry.getValue() );
    }
  }

  /**
   * @return an unmodifiable view of the config entries
   * @throws IllegalAccessError if the config is not yet frozen
   */
  protected Map<EntryType, Map<String, String>> getFrozenConfigEntries() {
    if ( !frozen.get() ) {
      throw new IllegalAccessError( "Config is not yet frozen" );
    }
    final Map<EntryType, Map<String, String>> clone = configEntries.clone();
    for ( final Entry<EntryType, Map<String, String>> entry : clone.entrySet() ) {
      entry.setValue( Collections.unmodifiableMap( entry.getValue() ) );
    }
    return Collections.unmodifiableMap( clone );
  }

  /**
   * @return an unmodifiable view of the Step config entries
   * @throws IllegalAccessError if the config is not yet frozen
   */
  protected Map<String, String> getFrozenStepConfigEntries() {
    if ( !frozen.get() ) {
      throw new IllegalAccessError( "Config is not yet frozen" );
    }
    return Collections.unmodifiableMap( configEntries.get( EntryType.STEP ) );
  }

  /**
   * @return an unmodifiable view of the Hop config entries
   * @throws IllegalAccessError if the config is not yet frozen
   */
  protected Map<String, String> getFrozenHopConfigEntries() {
    if ( !frozen.get() ) {
      throw new IllegalAccessError( "Config is not yet frozen" );
    }
    return Collections.unmodifiableMap( configEntries.get( EntryType.HOP ) );
  }

  /**
   * @return an unmodifiable view of the Step error handling config entries
   * @throws IllegalAccessError if the config is not yet frozen
   */
  protected Map<String, String> getFrozenStepErrorHandlingConfigEntries() {
    if ( !frozen.get() ) {
      throw new IllegalAccessError( "Config is not yet frozen" );
    }
    return Collections.unmodifiableMap( configEntries.get( EntryType.STEP_ERROR_HANDLING ) );
  }

  /**
   * @return an unmodifiable view of the variable config entries
   * @throws IllegalAccessError if the config is not yet frozen
   */
  protected Map<String, String> getFrozenVariableConfigEntries() {
    if ( !frozen.get() ) {
      throw new IllegalAccessError( "Config is not yet frozen" );
    }
    return Collections.unmodifiableMap( configEntries.get( EntryType.VARIABLE ) );
  }

  /**
   * @return an unmodifiable view of the Step error handling config entries
   * @throws IllegalAccessError if the config is not yet frozen
   */
  protected Map<String, String> getFrozenParameterConfigEntries() {
    if ( !frozen.get() ) {
      throw new IllegalAccessError( "Config is not yet frozen" );
    }
    return Collections.unmodifiableMap( configEntries.get( EntryType.PARAMETER ) );
  }

  /**
   * Adds a single entry to the config
   *
   * @param entryType
   * @param key
   * @param value
   * @throws IllegalAccessError       if the config is frozen
   * @throws IllegalArgumentException if the config entry is invalid
   */
  public void addConfigEntry( final EntryType entryType, final String key, final String value ) {
    if ( frozen.get() ) {
      throw new IllegalAccessError( "Config is frozen" );
    }
    if ( entryType == null ) {
      throw new IllegalArgumentException( "entryType is null" );
    }
    if ( isEmpty( trim( key ) ) ) {
      throw new IllegalArgumentException( String.format( "%s key is null for value %s", entryType, value ) );
    }
    if ( isEmpty( trim( value ) ) && entryType != EntryType.VARIABLE && entryType != EntryType.PARAMETER ) {
      throw new IllegalArgumentException( String.format( "value is required for %s key %s", entryType, key ) );
    }
    configEntries.get( entryType ).put( key, value );
  }

  /**
   * Adds a set of entries to the config
   *
   * @param configEntries
   * @throws IllegalAccessError       if the config is frozen
   * @throws IllegalArgumentException if any output mapping is invalid
   */
  public void addConfigEntries( final Map<EntryType, Map<String, String>> configEntries ) {
    if ( configEntries == null ) {
      throw new IllegalArgumentException( "configEntries is null" );
    }
    for ( final Entry<EntryType, Map<String, String>> typeEntry : configEntries.entrySet() ) {
      for ( final Entry<String, String> entry : typeEntry.getValue().entrySet() ) {
        addConfigEntry( typeEntry.getKey(), entry.getKey(), entry.getValue() );
      }
    }
  }

  /**
   * Prevents any modification to the configuration that could result in inconsistent Transformation execution.
   *
   * @return boolean indicating whether the config was previously unfrozen.
   */
  public boolean freeze() {
    final boolean wasUnfrozen = frozen.compareAndSet( false, true );
    validate();
    return wasUnfrozen;
  }

  /**
   * Performs basic validation that the config is in a consistent state
   *
   * @throws IllegalStateException if the config is invalid
   */
  private void validate() {
    if ( inputs.size() == 0 ) {
      logger.warn(
        "No inputs specified. If the transformation has no input defined elsewhere, the transformation could hang." );
    }
    if ( outputs.size() == 0 ) {
      logger.warn( "No outputs specified." );
    }
  }
}
