notsodry
========

A simple copy command that copies files and renames patterns.

The renaming is done inside the files text content and also for the files paths.

for a file named *path/foo/FooFile.txt*

    notsodry path foo:bar Foo:Bar

will copy the file to *path/bar/BarFile.txt* and replace all occurences to foo or Foo with bar respective Bar

why
---

I need it to copy an existing function of some software with huge required bilerplate setup code. After that I can adapt that part to fulfill a new function.

I'm aware that this is pretty bad practice and I don't recommend it to anyone.
