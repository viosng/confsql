include "ThriftExpressions.thrift"

service Translator {
   ThriftExpressions.ThriftExpression translate(1:string query)
}