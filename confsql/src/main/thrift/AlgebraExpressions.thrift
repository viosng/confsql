enum ThriftExpressionType{
    AND = 1,
    OR = 2,
    BIT_AND = 3,
    BIT_OR = 4,
    BIT_XOR = 5,
    BIT_NEG = 6,
    PLUS = 7,
    MINUS = 8,
    MULTIPLY = 9,
    DIVIDE = 10,
    MODULAR = 11,
    POWER = 12,
    EQUAL = 13,
    GT = 14,
    LT = 15,
    GE = 16,
    LE = 17,
    NOT_EQUAL = 18,
    NOT = 19,
    CONCATENATION = 20,
    FUNCTION_CALL = 21,
    CONSTANT = 22,
    ATTRIBUTE = 23,
    GROUP = 24,
    IF = 25,
    QUERY = 26,
    ORDER = 27,
    PARAMETER = 28
}

enum ThriftQueryType {
    FICTIVE = 1,
    PRIMARY = 2,
    FILTER = 3,
    FUSION = 5,
    JOIN = 6,
    AGGREGATION = 7,
    NEST = 8,
    UNNEST = 9,
    GROUP_JOIN = 10
}

struct ThriftParameter {
    1: required string name,
    2: required ThriftExpression value
}

struct ThriftExpression {
    1: required string id,
    2: required ThriftExpressionType type,
    3: optional ThriftQueryType queryType,
    4: optional string objectReference,
    5: optional string value,
    6: optional string orderType,
    7: optional list<ThriftExpression> schema,
    8: optional list<ThriftParameter> parameters,
    9: optional list<ThriftExpression> arguments,
}