/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.sql.calcite.remote;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.opensearch.sql.legacy.TestsConstants.TEST_INDEX_ACCOUNT;

import org.opensearch.sql.ppl.FieldsCommandAdditionalIT;

/**
 * Integration test for additional edge cases and syntax variations of the PPL fields command using
 * the Calcite engine. Extends {@link FieldsCommandAdditionalIT} to run the same tests with Calcite
 * enabled.
 *
 * <p>This class enables the Calcite query engine and disallows fallback to ensure all tests are
 * executed using the Calcite engine. It inherits all test methods from the parent class {@link
 * FieldsCommandAdditionalIT}.
 *
 * <p>The tests verify that wildcard patterns in field names work correctly with the Calcite engine.
 */
public class CalciteFieldsCommandAdditionalIT extends FieldsCommandAdditionalIT {
  @Override
  public void init() throws Exception {
    super.init();
    enableCalcite();
    disallowCalciteFallback();
  }

  /**
   * Override the test for non-existent fields in Calcite mode. Calcite throws a different exception
   * for non-existent fields.
   */
  @Override
  public void testFieldsWithNonExistentField() {
    Exception e =
        assertThrows(
            Exception.class,
            () ->
                executeQuery(
                    String.format("source=%s | fields nonexistent_field", TEST_INDEX_ACCOUNT)));
    assertTrue(e.getMessage().contains("field [nonexistent_field] not found"));
  }

  /**
   * Override the test for mix of existing and non-existent fields in Calcite mode. Calcite throws a
   * different exception for non-existent fields.
   */
  @Override
  public void testFieldsWithMixOfExistingAndNonExistentFields() {
    Exception e =
        assertThrows(
            Exception.class,
            () ->
                executeQuery(
                    String.format(
                        "source=%s | fields firstname, nonexistent_field", TEST_INDEX_ACCOUNT)));
    assertTrue(e.getMessage().contains("field [nonexistent_field] not found"));
  }

  /**
   * Override the test for excluding non-existent fields in Calcite mode. Calcite throws a different
   * exception for non-existent fields.
   */
  @Override
  public void testFieldsExcludeNonExistentField() {
    Exception e =
        assertThrows(
            Exception.class,
            () ->
                executeQuery(
                    String.format(
                        "source=%s | fields firstname, lastname | fields - nonexistent_field",
                        TEST_INDEX_ACCOUNT)));
    assertTrue(e.getMessage().contains("field [nonexistent_field] not found"));
  }
}
