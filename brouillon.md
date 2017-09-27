# Problème:

Pivoter un csv.
Exemple 30 col 1 ligne => 1 colonne 30 lignes

Brouillon/Réflexion

------

## ir

Le IR va lire par \n

```
[1: a,b,c,d,....,
 2: r,t,u,v,....
]
```
## map

Le map va créer des clés valeur en prenant le string et le splittant par virgule

Clé = numéro de colonne
Valeur = liste des lettres

```js
// 1
{
  0: ['a']
}

// 2
{
  0: ['a'],
  1: ['b'],
}

// ...
// ligne 2 - 1
{
  0: ['a', 'r'],
  1: ['b'],
  // ...
}
```

## sort

Sort par clé

## reduce
Le reduce prend cette liste triée

parcourir la liste

```js
// key: numéros de colonnes (0,1,2,3,4)
// list: le tableau retourné par sort
for (key in list) {
  let newRow = [];
  
  for (let i = list[key].length; i > 0; i--) {
    newRow.push(list[key][i]);
  }

  context.write(key, newRow);
  // 0 = r, a
  // 1 = t, b
}
```
