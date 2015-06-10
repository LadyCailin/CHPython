# Functions
## Functions
### array python(script, environment, toReturn):
Runs a python script. The script can set variables beforehand with the environment variable, which should be an associative array mapping variable names to values. Arrays are not directly supported, as everything is simply passed in as a string. Values can be returned from the script, by giving a list of named values to toReturn, which will cause those values to be returned as a part of the associative array returned.

