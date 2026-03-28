# GLO3004_TP2 - Système Publisher-Subscriber (VERSION AMÉLIORÉE)

**Cours:** GLO-3004 - Spécification et vérification formelle de logiciels
**Session:** Hiver 2026
**Version:** 2.0 (Code amélioré)

## 🎯 Description

Implémentation améliorée d'un système publisher-subscriber en Java 21 basée sur la spécification FSP du TP1. Le système utilise des threads Java pour représenter les processus parallèles et des sémaphores pour la synchronisation.

## ✨ Améliorations principales

Ce code représente une **version améliorée** du code original avec les modifications suivantes :

### 1. **Architecture broker global**
- ✅ Un seul broker partagé par toutes les applications (i et t)
- ✅ Conforme à la spécification FSP `SYSTEM11`

### 2. **Séparation des actions**
- ✅ Actions FSP séparées en méthodes distinctes
- ✅ `connectPublisher()`, `publish()`, `closePublisher()`
- ✅ `connectSubscriber()`, `subscribe()`, `closeSubscriber()`

### 3. **Gestion du temps et ordre correct**
- ✅ Vérification de `endTime` à chaque action
- ✅ Affichage des traces au bon moment (pas de blocage entre SUPPLY et CONNECT_PUB)
- ✅ Délais réalistes avec `Thread.sleep()`

### 4. **TraceManager centralisé**
- ✅ Nouvelle classe pour gérer les traces de manière thread-safe
- ✅ Format cohérent : `appType.actorType.id ACTION`

### 5. **Documentation professionnelle**
- ✅ Javadoc complète sur toutes les classes
- ✅ Références à la spécification FSP
- ✅ Commentaires en anglais

### 6. **Scripts automatisés**
- ✅ `build.sh` - Compilation et création du JAR
- ✅ `package.sh` - Création de l'archive de remise

## 📁 Structure du projet

```
GLO3004_TP2/
├── src/
│   ├── Broker.java         (3.3KB) - Gestion de la file de messages
│   ├── Main.java           (3.8KB) - Point d'entrée du programme
│   ├── Publisher.java      (2.7KB) - Thread publisher
│   ├── Subscriber.java     (2.7KB) - Thread subscriber
│   ├── TraceManager.java   (943B)  - Gestion des traces
│   └── LisezMoi.txt        (1.6KB) - Documentation
├── tp2.jar                 (6.2KB) - JAR exécutable
├── build.sh                (1.1KB) - Script de compilation
├── package.sh              (1.0KB) - Script de packaging
├── tp2-remise.zip          (14KB)  - Archive de remise
├── AMELIORATIONS.md        - Documentation des améliorations
└── README.md               - Ce fichier
```

## 🚀 Compilation

### Avec le script build.sh (recommandé)
```bash
./build.sh
```

### Manuellement
```bash
mkdir -p out
javac -d out src/*.java
jar cfe tp2.jar src.Main -C out .
```

## ▶️ Exécution

### Commande de base
```bash
java -Dn=<N> -Dp=<P> -Ds=<S> -Dt=<T> -jar tp2.jar
```

### Paramètres
- `-Dn` : Nombre de messages simultanés dans le broker (N dans FSP)
- `-Dp` : Nombre de publishers par type d'application (NB_P dans FSP)
- `-Ds` : Nombre de subscribers par type d'application (NB_S dans FSP)
- `-Dt` : Durée d'exécution en millisecondes

### Exemples
```bash
# Commande de correction (100ms)
java -Dn=2 -Dp=2 -Ds=3 -Dt=100 -jar tp2.jar

# Durée plus longue pour voir plus de traces (500ms)
java -Dn=2 -Dp=2 -Ds=3 -Dt=500 -jar tp2.jar

# Configuration minimale
java -Dn=1 -Dp=1 -Ds=1 -Dt=200 -jar tp2.jar

# Grande capacité
java -Dn=5 -Dp=3 -Ds=4 -Dt=1000 -jar tp2.jar
```

## 📊 Format de sortie

Chaque action est affichée dans le format :
```
appType.actorType.id ACTION
```

### Actions des Publishers
- `SUPPLY` : Génération d'un nouveau message
- `CONNECT_PUB` : Connexion au broker
- `PUB` : Publication du message
- `CLOSE_PUB` : Fermeture de la connexion

### Actions des Subscribers
- `CONNECT_SUB` : Connexion au broker
- `SUB` : Réception d'un message
- `CLOSE_SUB` : Fermeture de la connexion
- `CONSUME` : Consommation du message

### Exemple de trace
```
i.publisher.2 SUPPLY
i.publisher.2 CONNECT_PUB
i.publisher.2 PUB
i.publisher.2 CLOSE_PUB
i.subscriber.1 CONNECT_SUB
i.subscriber.1 SUB
i.subscriber.1 CLOSE_SUB
i.subscriber.1 CONSUME
```

## 🔧 Implémentation FSP

### Processus PUB3 (Publisher)
```fsp
PUB3 = (supply -> connect_pub -> pub -> PUB3).
```
Implémenté dans `Publisher.java` :
- SUPPLY → CONNECT_PUB → PUB → CLOSE_PUB → (recommence)

### Processus SUB3 (Subscriber)
```fsp
SUB3 = (connect_sub -> sub -> consume -> SUB3).
```
Implémenté dans `Subscriber.java` :
- CONNECT_SUB → SUB → CLOSE_SUB → CONSUME → (recommence)

### Processus BROKER4
```fsp
BROKER4 = PUBSUB[0],
  PUBSUB[i:0..N] = (when (i < N) connect_pub -> pub -> queue -> PUBSUB[i + 1]
                  | when (i > 0) connect_sub -> sub -> dequeue -> PUBSUB[i - 1]).
```
Implémenté dans `Broker.java` avec deux sémaphores :
- `slots` : Capacité N (slots disponibles pour publishers)
- `messages` : Messages disponibles pour subscribers

## 📦 Création de l'archive de remise

```bash
./package.sh
```

Cela créera `tp2-remise.zip` contenant :
- Code source Java (`src/`)
- JAR exécutable (`tp2.jar`)
- Documentation (`src/LisezMoi.txt`)
- Script de build (`build.sh`)

## 📖 Documentation complète

Pour voir toutes les améliorations apportées au code, consultez :
- **AMELIORATIONS.md** : Documentation détaillée de tous les changements
- **src/LisezMoi.txt** : Instructions d'exécution et interprétation

## ✅ Tests effectués

- ✅ Test avec commande de correction (n=2, p=2, s=3, t=100)
- ✅ Test capacité minimale (n=1, p=1, s=1, t=200)
- ✅ Test grande capacité (n=5, p=3, s=4)
- ✅ Vérification intégrité archive
- ✅ Compilation multi-plateforme

## 🎓 Remise

**Date limite:** 28 mars 2026 à 23h50

**Fichier à soumettre:** `tp2-remise.zip`

**⚠️ Important:** Soumettre via le portail du cours (PAS par courriel)

## 📝 Notes

- Version Java requise : **Java 21**
- Le système fonctionne correctement et respecte toutes les propriétés FSP
- Code thread-safe avec synchronisation appropriée
- Documentation professionnelle complète

## 🔗 Comparaison avec version originale

| Aspect | Version originale | Version améliorée |
|--------|------------------|-------------------|
| Broker | Un par application | Global (partagé) |
| Actions | Fusionnées | Séparées |
| Documentation | Commentaires FR | Javadoc complète EN |
| Scripts | Commandes manuelles | Scripts automatisés |
| Gestion temps | Basique | Complète avec endTime |
| Traces | synchronized(System.out) | TraceManager centralisé |

Pour plus de détails, voir **AMELIORATIONS.md**
