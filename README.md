# Wordle

Aplicación Wordle sencilla para la asignatura ADSI.

/////////////////////////////

ISAD irakasgairako Wordle aplikazio sinplea

## Requisitos

- Java 17
- Maven 3.8 o superior

## Compilar y probar // Konpilatu eta probatu

```bash
mvn test
mvn package
```

## Ejecutar // Egikaritu

```bash
java -cp target/wordle-inicial-1.0-SNAPSHOT.jar eus.ehu.adsi.wordle.WordleApp
```

El programa permite jugar partidas independientes de Wordle con palabras de 5 letras
y un máximo de 6 intentos.

La aplicación incluye únicamente las funcionalidades necesarias para el juego inicial:
reglas de la partida, evaluación de intentos, diccionario e interfaz gráfica.

Esta versión no incluye usuarios, persistencia, rankings, logros, retos, modos de juego
ni otros servicios adicionales.

/////////////////////////////

Programak 5 letrako hitzekin eta gehienez 6 saiakera egiteko aukera ematen du Wordle 
partidak modu independentean jokatzeko.

Aplikazioak hasierako jokorako beharrezkoak diren funtzionalitateak baino ez ditu 
inplementatzen: partidaren arauak, saiakeren ebaluazioa, hiztegia eta interfaze grafikoa.

Bertsio honek ez ditu erabiltzaileak, iraunkortasuna, sailkapenak, lorpenak, erronkak, 
joko-moduak edo beste zerbitzu gehigarri batzuk barne hartzen.
