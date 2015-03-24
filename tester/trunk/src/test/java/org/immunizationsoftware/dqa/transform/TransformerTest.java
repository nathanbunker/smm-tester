package org.immunizationsoftware.dqa.transform;

import org.junit.Test;

import junit.framework.TestCase;

public class TransformerTest extends TestCase
{
  @Test
  public void testCreateTokenList() {
    assertEquals(0, Transformer.createTokenList("").size());
    assertEquals(1, Transformer.createTokenList("1").size());
    assertEquals(2, Transformer.createTokenList("hello goodbye").size());
    assertEquals(2, Transformer.createTokenList("hello  goodbye").size());
    assertEquals(3, Transformer.createTokenList("hello  good bye").size());
    assertEquals("hello", Transformer.createTokenList("hello  goodbye").removeFirst());
    assertEquals("hello", Transformer.createTokenList("'hello'  goodbye").removeFirst());
    assertEquals("hello goodbye", Transformer.createTokenList("'hello goodbye'").removeFirst());
    assertEquals("I'm", Transformer.createTokenList("I'm great").removeFirst());
    assertEquals("I'm great", Transformer.createTokenList("'I''m great'").removeFirst());

  }
}
