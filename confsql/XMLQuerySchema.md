All these materials are based on the [publication](http://dl.acm.org/citation.cfm?doid=2383276.2383278) of Boris Novikov, Anna Yarygina and Natalia Vassilieva.

# General schema
```xml
<query id = "..." type = "...">
    <parameters>
        <parameter name = "..." value = "..."/>
        ...
    </parameters>
    <schema>
        <expression type = "...">...</expression>
        ...
        <expression type = "...">...</expression>
    </schema>
    <arguments>
        <query>...</query>
        ...
        <query>...</query>
        <expression type = "...">...</expression>
        ...
        <expression type = "...">...</expression>
    </arguments>
</query>
```
---
# Elements
## 1. Expressions
There are such expressions:
* binary: `+, -, *, /, ^, and, or, >, <, <=, >=, ==`
* unary : `not, -`
* `function calls`
* constants
* object attributes
* group object

that can be structured as tree
>               *
           /     \
        3         +
               /     \
        emp.salary dep.bonus

that equals `3 * (emp.salary + dep.bonus)` and finally is expressed via XML as
```xml
<expression type = "*">
    <expression type = "constant" value = "3"/>
    <expression type = "+">
        <expression type = "attribute" name = "salary" ref = "emp"/>
        <expression type = "attribute" name = "bonus" ref = "dep"/>
    </expression>
</expression>
```
## 2. Schema
Schema structure introduces result object's structure that can be declared as expression list.
Example:
```xml
<schema>
    <expression type = "attribute" name = "id" ref = "emp"/>
    <expression type = "attribute" name = "name" ref = "dep"/>
    <expression type = "*">
        <expression type = "constant" value = "3"/>
        <expression type = "+">
            <expression type = "attribute" name = "salary" ref = "emp"/>
            <expression type = "attribute" name = "bonus" ref = "dep"/>
        </expression>
    </expression>
</schema>
```
Here we selects `emp.id, dep.name, 3 * (emp.salary + dep.bonus)` from query result.

**You can leave schema tag empty. In this case there will be no schema modification at all, except if it will do some filter operation**

## 3. Parameters
Parameters are need to configure our query (to specify query algorithm, its constants, external library paths and etc.). Every parameter tag has two attributes: name and value.
```xml
<parameters>
    <parameter name = "algorithm" value = "hash join"/>
    ...
</parameters>
```
---
## 4. Arguments
This tag contains nested query arguments - subqueries and expressions
```xml
<arguments>
    <query type = "filter">...</query>
    <query type = "primary">...</query>
    <expression type = "==">
        <expression type = "attribute" name = "id" ref = "emp"/>
        <expression type = "attribute" name = "id" ref = "dep"/>
    </expression>
</arguments>
```
---
## 5. Query
### Types
####1. Filter

> All filtering operations return the base set of the argument q-set and preserve identities of all objects in the base set. Simple filters re-calculate scores of individual objects, where a new score of an object may only depend on the attribute values of this object and a limited number of additional parameters. Consequently, filters can be executed on streams of objects without any need in a materialization or temporary storage of (any part of) the q-set.

#####XML template:
```xml
<query id = "..." type = "filter">
    <parameters>...</parameters>
    <schema>...</schema>
    <arguments>
        <query ...>...</query>
        <expression ...>...</expression>
    </arguments>
</query>
```
This template contains subquery as a source for operation, schema modification definition, parameters and an expression definition if it will be used by filter.

####2. Fusion.

> The generic fusion operation is intended to extend set-theoretic operations, both exact and fuzzy. Traditional set-theoretic operations e.g. in relational database require arguments have the same type. There is no strict concept of type in our model. Consequently, instead of required arguments to be of the same type, we state that
attributes from all arguments are included into the output q-set. Most of specializations of the fusion are symmetric, commutative, and might be either binary or multi-argument. Many of important specializations are not associative and for many of them multi-argument version cannot be implemented as an expression of binary versions.

#####XML template:
```xml
<query id = "..." type = "fusion">
    <arguments>
        <query ...>...</query>
        ...
        <query ...>...</query>
    </arguments>
</query>
```

As shown above, the fusion operator hasn't schema definition, because of it modifies result object schema by itself.

####3. Join

> The generic join/product operation is constructed as a generalization of theta-join operation of relational model. Namely, the base set of the output is a direct product of argument base sets. The definition of a join operation assumes a (fuzzy) predicate function on attributes of arguments. The value of this function is used as an additional component for the resulting score. Thus, the score for a join always depends on 3 factors: incoming scores and the predicate score. Depending on the predicate, the generic join can express traditional exact (natural) database joins, spatial joins, similarity joins, or fuzzy joins. The identities of output objects are constructed as surrogates. As in relational theory, the generic join is redundant and can be expressed as a product with subsequent filtering. However, the knowledge of the join predicate provides for more efficient implementation of join algorithms than just calculation of the complete product.

#####XML template:
```xml
<query id = "..." type = "join">
    <parameters>...</parameters>
    <arguments>
        <query ...>...</query>
        <query ...>...</query>
        <expression ...>...</expression>
    </arguments>
</query>
```

This template has two subqueries to join with some predicate that can be specified with expression definition.

####4. Aggregation

> The aggregation operation constructs an object of result base set from several objects. It can be considered as a fuzzy replacement and generalization for exact queries with GROUP BY clause, which defines a set of incoming objects to be grouped into a one outgoing object. In addition to grouping based on exact match of values of grouping attributes, the objects can be grouped based on classification, clustering of incoming objects.

#####XML template:
```xml
<query id = "..." type = "aggregation">
    <parameters>...</parameters>
    <schema>
        <expression type="function" name = "...">
            <expression type = "group"/>
        </expression>
        ...
    </schema>
    <arguments>
        <query ...>...</query>
        <expression ...>...</expression>
    </arguments>
</query>
```

Such template shows that aggregation operator should contain subquery, expression to group by and schema definition where it will be specified what a function will be evaluated on grouped objects.

####5. Nest

>The nest operation is a special case of aggregation, which creates an attribute of an aggregated object, which consists of all grouped objects.

#####XML template:
```xml
<query id = "..." type = "nest">
    <parameters>...</parameters>
    <schema>
        <expression ...>...</expression>
        ...
        <expression type = "group"/>
    </schema>
    <arguments>
        <query ...>...</query>
        <expression ...>...</expression>
    </arguments>
</query>
```

The main difference between aggregation and nest operator is that in the second one we save aggregated groups to some new attribute.

####6. Unnest

>The unnest operation is a kind of an inverse for the nest and is constructed as follows. The objects in the base set of the argument must have a q-set valued attribute. The output objects are constructed from each object of the nested q-set augmented with the identity of the incoming base object (and probably its other attributes). The score of the outgoing object should be calculated from the score of an incoming object and a score of nested object. Thus, the unnest operation is also generic.


#####XML template:
```xml
<query id = "..." type = "unnest">
    <parameters>...</parameters>
    <arguments>
        <query ...>...</query>
        <expression type = "attribute" value = "..."/>
    </arguments>
</query>
```

We need to define an  object attribute where this group is stored to unnest objects with sub groups.


####7. Group join

>A group join operation which is defined as a join followed by nesting with aggregation on the identity of the first argument. Effectively this operation combines the power of joins and aggregations and both joins and aggregations can be considered as a special case of the group join.

#####XML template:
```xml
<query id = "..." type = "group join">
    <parameters>...</parameters>
    <schema>
        <expression ...>...</expression>
        ...
        <expression type="function" name = "...">
            <expression type = "group"/>
            ...
        </expression>
    </schema>
    <arguments>
        <query ...>...</query>
        <query ...>...</query>
        <expression id = "where clause">...</expression>
        <expression id = "group by clause">...</expression>
    </arguments>
</query>
```
The group join xml template contains two subqueries to join with predicate and an expression that will be used by aggregation operator. The aggregation function need to be defined in schema tag.