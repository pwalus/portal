# System do analizy wpisów na portalu

[Wiki projektu](https://github.com/pwalus/portal/wiki/System-do-analizy-wpisów-na-portalu)

## Kompilacja i uruchomienie
**Do uruchomienia konieczna jest Java w wersji 8 oraz baza danych mysql**

1. Kompilacja systemu

```
./gradlew clean build
```

2. Uruchomienie systemu

```
java -jar build/libs/portal.jar
```

3. Po całkowitym uruchomieniu systemu, możliwe będzie wpisywanie odpowiednich komend. Gdy system będzie gotowy zostanie wyświetlone

```
2020-01-12 16:50:13.006  INFO 61926 --- [           main] portal.Main                              : Started Main in 7.216 seconds (JVM running for 7.913)
shell:>
```
