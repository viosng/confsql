include "expression.thrift"

service Translator {
   expression.ThriftExpression translate(1:string query)
}