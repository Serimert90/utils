This utility class helps to cover definition classes when you can't ignore in SonarQube or any other QA tool.
It can cover superclass and interface default methods recursively. 

<b> !!!!!!!Break design patterns at your own risk. </b>

<code>coverClasses(new CoverOptions.Builder().all(), "full_package_path.someclassname");

or

coverClasses("full_package_path.someclassname");
</code>
