/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.sql.ppl;

import static org.opensearch.sql.legacy.TestsConstants.*;
import static org.opensearch.sql.util.MatcherUtils.columnName;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

/**
 * Integration test for additional edge cases and syntax variations of the PPL fields command.
 * These tests complement the core functionality tests in {@link FieldsCommandIT}.
 */
public class FieldsCommandAdditionalIT extends PPLIntegTestCase {

  @Override
  public void init() throws Exception {
    super.init();
    loadIndex(Index.ACCOUNT);
  }

  /**
   * Test fields command with explicit '+' prefix which should behave the same as the default include behavior.
   */
  @Test
  public void testFieldsWithExplicitPlus() throws IOException {
    JSONObject result =
        executeQuery(String.format("source=%s | fields + firstname, lastname", TEST_INDEX_ACCOUNT));
    String resultStr = result.toString();
    assertTrue("Should contain firstname", resultStr.contains("firstname"));
    assertTrue("Should contain lastname", resultStr.contains("lastname"));
  }

  /**
   * Test fields command with complex wildcard pattern that matches any field containing 'name'.
   */
  @Test
  public void testFieldsWithComplexWildcards() throws IOException {
    JSONObject result =
        executeQuery(String.format("source=%s | fields *name*", TEST_INDEX_ACCOUNT));
    // Verify that fields containing "name" are in the result
    String resultStr = result.toString();
    assertTrue("Should contain firstname", resultStr.contains("firstname"));
    assertTrue("Should contain lastname", resultStr.contains("lastname"));
  }

  /**
   * Test fields command with empty field list, which is not supported by the PPL parser.
   */
  @Test
  public void testFieldsWithEmptyFieldList() {
    // Empty field list is not supported by the PPL parser
    Exception e = assertThrows(
        Exception.class,
        () -> executeQuery(String.format("source=%s | fields", TEST_INDEX_ACCOUNT)));
    assertTrue(e.getMessage().contains("is not a valid term"));
  }

  /**
   * Test fields command with a non-existent field, which is not supported by the PPL parser.
   */
  @Test
  public void testFieldsWithNonExistentField() {
    // Non-existent fields are not supported by the PPL parser
    Exception e = assertThrows(
        Exception.class,
        () -> executeQuery(String.format("source=%s | fields nonexistent_field", TEST_INDEX_ACCOUNT)));
    assertTrue(e.getMessage().contains("can't resolve Symbol"));
  }

  /**
   * Test fields command with a mix of existing and non-existent fields, which is not supported by the PPL parser.
   */
  @Test
  public void testFieldsWithMixOfExistingAndNonExistentFields() {
    // Non-existent fields are not supported by the PPL parser
    Exception e = assertThrows(
        Exception.class,
        () -> executeQuery(String.format("source=%s | fields firstname, nonexistent_field", TEST_INDEX_ACCOUNT)));
    assertTrue(e.getMessage().contains("can't resolve Symbol"));
  }

  /**
   * Test fields command with multiple wildcard patterns.
   */
  @Test
  public void testFieldsWithMultipleWildcards() throws IOException {
    JSONObject result =
        executeQuery(String.format("source=%s | fields f*name, *ast*", TEST_INDEX_ACCOUNT));
    // Verify that fields matching the patterns are in the result
    String resultStr = result.toString();
    assertTrue("Should contain firstname", resultStr.contains("firstname"));
    assertTrue("Should contain lastname", resultStr.contains("lastname"));
  }

  /**
   * Test fields command with explicit '-' prefix to exclude fields.
   */
  @Test
  public void testFieldsExcludeWithExplicitMinus() throws IOException {
    JSONObject result =
        executeQuery(String.format("source=%s | fields firstname, lastname | fields - lastname", TEST_INDEX_ACCOUNT));
    // Should only have firstname
    String resultStr = result.toString();
    assertTrue("Should contain firstname", resultStr.contains("firstname"));
    // Check that lastname is not in the result in a way that's more reliable
    assertFalse("Should not contain lastname", resultStr.matches(".*\"name\"\s*:\s*\"lastname\".*"));
  }

  /**
   * Test fields command excluding a non-existent field, which is not supported by the PPL parser.
   */
  @Test
  public void testFieldsExcludeNonExistentField() {
    // Non-existent fields are not supported by the PPL parser
    Exception e = assertThrows(
        Exception.class,
        () -> executeQuery(String.format("source=%s | fields firstname, lastname | fields - nonexistent_field", TEST_INDEX_ACCOUNT)));
    assertTrue(e.getMessage().contains("can't resolve Symbol"));
  }
}