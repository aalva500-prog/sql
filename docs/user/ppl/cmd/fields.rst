=============
fields
=============

.. rubric:: Table of contents

.. contents::
   :local:
   :depth: 2


Description
============
| Using ``field`` command to keep or remove fields from the search result.


Syntax
============
field [+|-] <field-list>

* index: optional. if the plus (+) is used, only the fields specified in the field list will be keep. if the minus (-) is used, all the fields specified in the field list will be removed. **Default** +
* field list: mandatory. comma-delimited keep or remove fields. Supports wildcards (*) in field names.

Wildcard Support
---------------
* Asterisk (*) matches zero or more characters in field names
* Wildcards can be used at the beginning, middle, or end of field names
* Examples of wildcard patterns:
  * ``user.*``: Matches all fields that start with "user."
  * ``*name``: Matches all fields that end with "name"
  * ``*id*``: Matches all fields that contain "id"
  * ``a*b``: Matches all fields that start with "a" and end with "b"


Example 1: Select specified fields from result
==============================================

The example show fetch account_number, firstname and lastname fields from search results.

PPL query::

    os> source=accounts | fields account_number, firstname, lastname;
    fetched rows / total rows = 4/4
    +----------------+-----------+----------+
    | account_number | firstname | lastname |
    |----------------+-----------+----------|
    | 1              | Amber     | Duke     |
    | 6              | Hattie    | Bond     |
    | 13             | Nanette   | Bates    |
    | 18             | Dale      | Adams    |
    +----------------+-----------+----------+

Example 2: Remove specified fields from result
==============================================

The example show fetch remove account_number field from search results.

PPL query::

    os> source=accounts | fields account_number, firstname, lastname | fields - account_number ;
    fetched rows / total rows = 4/4
    +-----------+----------+
    | firstname | lastname |
    |-----------+----------|
    | Amber     | Duke     |
    | Hattie    | Bond     |
    | Nanette   | Bates    |
    | Dale      | Adams    |
    +-----------+----------+

Example 3: Select fields using wildcards
=======================================

The example shows how to select all fields that start with "user.".

PPL query::

    os> source=accounts | fields user.* ;
    fetched rows / total rows = 4/4
    +-------------+----------------+---------------+
    | user.email  | user.firstname | user.lastname |
    |-------------+----------------+---------------|
    | amber@duke  | Amber          | Duke          |
    | hattie@bond | Hattie         | Bond          |
    | nanette@bates | Nanette      | Bates         |
    | dale@adams  | Dale           | Adams         |
    +-------------+----------------+---------------+

Example 4: Remove fields using wildcards
=======================================

The example shows how to remove all fields that end with "name".

PPL query::

    os> source=accounts | fields - *name ;
    fetched rows / total rows = 4/4
    +----------------+-------------+----------+
    | account_number | user.email  | age      |
    |----------------+-------------+----------|
    | 1              | amber@duke  | 32       |
    | 6              | hattie@bond | 36       |
    | 13             | nanette@bates | 28     |
    | 18             | dale@adams  | 33       |
    +----------------+-------------+----------+

