[mvncentral]: https://mvnrepository.com/artifact/com.github.ygimenez/Simple-Moshi

[jitpack]: https://jitpack.io/#ygimenez/Simple-Moshi

[build]: https://github.com/ygimenez/Simple-Moshi/tree/master

[license]: https://github.com/ygimenez/Simple-Moshi/blob/master/LICENSE

[issue]: https://github.com/ygimenez/Simple-Moshi/issues

[mvncentral-shield]: https://img.shields.io/maven-central/v/com.github.ygimenez/Simple-Moshi?label=Maven%20Central

[jitpack-shield]: https://img.shields.io/badge/Download-Jitpack-success

[build-shield]: https://img.shields.io/github/actions/workflow/status/ygimenez/Simple-Moshi/maven.yml?label=Build

[license-shield]: https://img.shields.io/github/license/ygimenez/Simple-Moshi?color=lightgrey&label=License

[issue-shield]: https://img.shields.io/github/issues/ygimenez/Simple-Moshi?label=Issues

[ ![mvncentral-shield][] ][mvncentral]
[ ![jitpack-shield][] ][jitpack]
[ ![build-shield][] ][build]
[ ![license-shield][] ][license]
[ ![issue-shield][] ][issue]

# Simple Moshi - A wrapper to make Moshi even simpler

This wrapper simplifies the usage of popular JSON library [Moshi](https://github.com/square/moshi) by making adding collections
for handling arbitrary structures.

<details open>
<summary>Java</summary>

````java
JSONObject obj = new JSONObject();
obj.put("hello", "world");
obj.put("example", 123);

String str = obj.toString();
System.out.println(str); // { "hello": "world", "example": 123 }

obj = new JSONObject(str);
System.out.println(str); // { "hello": "world", "example": 123 }
System.out.println(obj.getString("hello")); // world

obj.remove("hello");
System.out.println(str); // { "example": 123 }
````

````java
JSONArray arr = new JSONArray();
arr.add(123);
arr.add("hello");
arr.add("world");

String str = obj.toString();
System.out.println(str); // [ 123, "hello", "world" ]

arr = new JSONArray(str);
System.out.println(arr); // [ 123, "hello", "world" ]
System.out.println(arr.getString(1)); // hello

arr.remove("hello");
System.out.println(arr); // [ 123, "world" ]
````

</details>

<details>
<summary>Java</summary>

````groovy
def obj = new JSONObject()
obj.hello = "world"
obj.example = 123

String str = obj.toString()
println str // { "hello": "world", "example": 123 }

obj = new JSONObject(str)
println str // { "hello": "world", "example": 123 }
println str.getString("hello") // world

obj >> "hello"
println str // { "example": 123 }
````

````groovy
import com.ygimenez.json.JSONArray

def arr = new JSONArray()
arr << 123
arr << "hello"
arr << "world"

String str = obj.toString()
println str // [ 123, "hello", "world" ]

arr = new JSONArray(str)
println arr // [ 123, "hello", "world" ]
println arr.getString(1) // hello

arr >> "hello"
println arr // [ 123, "world" ]
````

</details>

## How do I get it?

This library is available for manual installation and through Maven Central:

### To install manually

* Click on the releases tab on the top of this repository
* Download the latest release
* Put the .jar file somewhere in your project
* Add it to the buildpath
* Done!

### To install via Maven Central

* Add this library as a dependency:

Gradle:

```gradle
dependencies {
    implementation group: 'com.github.ygimenez', name: 'Simple-Moshi', version: 'VERSION'
}
```

Maven:

```xml

<dependency>
  <groupId>com.github.ygimenez</groupId>
  <artifactId>Simple-Moshi</artifactId>
  <version>VERSION</version>
</dependency>
```

* Replace `VERSION` with the one shown here (without "v"): [ ![mvncentral-shield][] ][mvncentral]
* Done!

## Feedback

If you have any issues using this library feel free to create a new issue, I'll review it as soon as possible!