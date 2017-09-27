# Problème:

Pivoter un csv.
Exemple 30 col 1 ligne => 1 colonne 30 lignes

------

Le IR va lire par \n

[1: a,b,c,d,....,
 2: r,t,u,v,....
]

Le map va créer des clés valeur en prenant le string et le splittant par virgule
[
{
  line: 1,
  col: 1,
  value: 'a'
},
{
  line: 1,
  col: 2,
  value: 'b'
}, ...,
{
  line: 2,
  col: 1
  value: 'r'
}
]

Le sort doit trier par 'line' et 'col' croissant :
List<k2, v2>
[
  {line: 1, col: 1, value: 'a'},
  {line: 2, col: 1, value: 'r'},
  {line: 1, col: 2, value: 'b'},
  ...
]

Le reduce prend cette liste triée

parcourir la liste

[
  ['a', 'r'],
  ['b', ...
]



[
  ['a', [1,1,1,1,1,1]]
]


