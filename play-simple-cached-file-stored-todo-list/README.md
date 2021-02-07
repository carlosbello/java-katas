# Simple cached file stored to-do list API using Play Framework

## Requirements

### To-do model

| Field | Type |
|-------|------|
| title | String |
| done | Boolean |
| creationDate | Date |

### Actions

* Add new
* Get all
* Get item
* Update
* Delete

## Cache

* Read operations must be cached
* Invalidate cache for get all operations when new todo item is added
* Invalidate cache for get item operations when the item is updated
* Do not invalidate any read cache on delete

## Execution

> sbt run