package com.viosng.confsql.semantic.model.thrift; /**
 * Autogenerated by Thrift Compiler (0.9.2)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */

import org.apache.thrift.TEnum;

public enum ThriftExpressionType implements TEnum {
  AND(1),
  OR(2),
  BIT_AND(3),
  BIT_OR(4),
  BIT_XOR(5),
  BIT_NEG(6),
  PLUS(7),
  MINUS(8),
  MULTIPLY(9),
  DIVIDE(10),
  MODULAR(11),
  POWER(12),
  EQUAL(13),
  GT(14),
  LT(15),
  GE(16),
  LE(17),
  NOT_EQUAL(18),
  NOT(19),
  CONCATENATION(20),
  FUNCTION_CALL(21),
  CONSTANT(22),
  ATTRIBUTE(23),
  GROUP(24),
  IF(25),
  QUERY(26),
  ORDER(27),
  PARAMETER(28),
  CASE(29);

  private final int value;

  private ThriftExpressionType(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  public static ThriftExpressionType findByValue(int value) { 
    switch (value) {
      case 1:
        return AND;
      case 2:
        return OR;
      case 3:
        return BIT_AND;
      case 4:
        return BIT_OR;
      case 5:
        return BIT_XOR;
      case 6:
        return BIT_NEG;
      case 7:
        return PLUS;
      case 8:
        return MINUS;
      case 9:
        return MULTIPLY;
      case 10:
        return DIVIDE;
      case 11:
        return MODULAR;
      case 12:
        return POWER;
      case 13:
        return EQUAL;
      case 14:
        return GT;
      case 15:
        return LT;
      case 16:
        return GE;
      case 17:
        return LE;
      case 18:
        return NOT_EQUAL;
      case 19:
        return NOT;
      case 20:
        return CONCATENATION;
      case 21:
        return FUNCTION_CALL;
      case 22:
        return CONSTANT;
      case 23:
        return ATTRIBUTE;
      case 24:
        return GROUP;
      case 25:
        return IF;
      case 26:
        return QUERY;
      case 27:
        return ORDER;
      case 28:
        return PARAMETER;
      case 29:
        return CASE;
      default:
        return null;
    }
  }
}
