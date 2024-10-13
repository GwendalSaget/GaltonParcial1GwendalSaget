Parcial 1 Gwendal Saget

Resumen del Proyecto 
El proyecto es una simulación de un árbol de Galton, donde se utilizan múltiples hilos para generar resultados y se almacenan en una base de datos SQLite. Luego, los resultados se visualizan en un gráfico. A continuación, se describen las diferentes clases y su funcionamiento.

1. Clase Main
La clase Main es el punto de entrada del programa. En ella se inicializan los componentes y se controla la aplicación.

Conexión a la Base de Datos: Al inicio, se limpia la base de datos (si esta llena de datos) utilizando el método DatabaseConnection.clearDatabase().
Creación de Hilos: Se crean múltiples hilos (corresponden a las bolas, por ejemplo, 300) para ejecutar en paralelo la generación del árbol de Galton y el cálculo de los resultados.
Sincronización: Se utiliza un CountDownLatch para esperar a que todos los hilos terminen antes de mostrar el gráfico. Asi podemos tambien evitar problemas de gestion de databse (dos threads escribiendo en una misma area)
Visualización del Gráfico: Una vez que todos los resultados han sido almacenados en la base de datos, se invoca GraphDisplay para mostrar los resultados en un gráfico.

2. Clase DatabaseConnection
Esta clase maneja la conexión a la base de datos SQLite.

getConnection(): Establece y devuelve una conexión a la base de datos.
clearDatabase(): Limpia la tabla de resultados (results) para permitir nuevas inserciones.

3. Clase GaltonNode
Esta clase representa un nodo en el árbol de Galton. Cada nodo contiene:

id: Identificador del nodo.
rang: Profundidad o nivel del nodo en el árbol.
leftchild y rightchild: Referencias a los hijos izquierdo y derecho del nodo.
leftpadre y rightpadre: Referencias a los padres izquierdo y derecho del nodo.

4. Clase GaltonTree
Esta clase representa el árbol de Galton en su conjunto.

root: El nodo raíz del árbol.
nbNode: Contador del número de nodos en el árbol. No se usa en elcodigo si no pora ayudar el debug

5. Clase TreeFunc
Esta clase contiene las funcionalidades principales relacionadas con la creación y el recorrido del árbol de Galton.

onTreeCreate(): Inicializa el árbol y establece su raíz.
CreateNode(int id, int rang): Crea un nuevo nodo y lo añade al arreglo de nodos.
Getleftchild(GaltonNode current) y Getrightchild(GaltonNode current): Devuelven el hijo izquierdo y derecho de un nodo actual. Si el hijo no existe, lo crean.
onTreeConstruct(GaltonTree gt, GaltonNode current): Construye el árbol completando los nodos de acuerdo a las reglas del árbol de Galton.
ParcoursResult(GaltonTree tree): Realiza un recorrido aleatorio del árbol y devuelve el resultado basado en el recorrido.
printNodes(): Imprime los nodos en un formato específico que muestra la relación entre nodos y sus hijos/padres.
Conclusión
Este proyecto combina el uso de multithreading para generar resultados, almacenamiento en una base de datos SQLite para persistencia, y visualización gráfica para presentar los resultados de manera efectiva. El diseño modular del código permite una fácil ampliación y mantenimiento.

6. Clase GraphDisplay
La clase GraphDisplay se encarga de crear y mostrar un gráfico basado en los resultados almacenados en la base de datos, que provienen de la simulación del árbol de Galton. Utiliza bibliotecas de gráficos en Java, como JFreeChart.

Atributos
results: Lista de enteros que contiene los resultados recuperados de la base de datos.
Métodos Principales
createDataset():

Crea un conjunto de datos que cuenta las ocurrencias de cada resultado (de 1 a 11) y lo convierte en un CategoryDataset para el gráfico.
createChart():

Genera un gráfico de barras a partir del conjunto de datos, estableciendo títulos para los ejes y personalizando la apariencia.
createAndShowChartPanel():
Crea un ChartPanel y lo muestra en una ventana, haciendo que el gráfico sea visible para el usuario.

Conclusión
Este proyecto combina el uso de multithreading para generar resultados, almacenamiento en una base de datos SQLite para persistencia, y visualización gráfica para presentar los resultados de manera efectiva. El diseño modular del código permite una fácil ampliación y mantenimiento.

https://github.com/GwendalSaget/GaltonParcial1GwendalSaget
