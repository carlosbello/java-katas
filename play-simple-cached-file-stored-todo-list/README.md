# Simple cached file stored to-do list API using Play Framework

## Requirements

### To-do model

| Field | Type |
|-------|------|
| title | String |
| done | Boolean |
| creationDate | Date |

### Persistence

Save the to-do list in a json file.

### Actions

* Add new
* Get all
* Get item
* Update
* Delete

## Cache

* "Read" operations must be cached
* Invalidate cache for a "get all" operations when a new todo item is added
* Invalidate cache for a "get item" operations when the item is updated
* Do not invalidate any read cache on "delete" operations

## Execution

> sbt run

# The process

[![CodeKata: Cached To-do REST API (how-to create a REST API)](https://img.youtube.com/vi/qRP32ktO7K4/hqdefault.jpg)](https://youtu.be/qRP32ktO7K4 "Click to watch - CodeKata: Cached To-do REST API (how-to create a REST API)")