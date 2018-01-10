# gdx-utils

[![](https://jitpack.io/v/skyousuke/gdx-utils.svg)](https://jitpack.io/#skyousuke/gdx-utils) 

### Gradle setup

add the JitPack repository in your root build.gradle at the end of repositories:

```
allprojects {
    repositories {
    ...
    maven { url 'https://jitpack.io' }
    }
}
  ```
  
**Core dependency**
```
compile "com.github.skyousuke:gdx-utils:0.0.15"
```

**Html dependency** (only if you are using HTML)

```
compile "com.github.skyousuke:gdx-utils:0.0.15:sources"
```
module XML (GdxDefinition.gwt.xml)
```
<inherits name='com.github.skyousuke.gdxutils' />
```

### LibGDX compatibility

gdx-utils is complied with **LibGDX 1.9.8**

Using not matching versions may cause compilation error or runtime exceptions.

## See also

* [javadoc](https://skyousuke.github.io/gdx-utils/index.html)
