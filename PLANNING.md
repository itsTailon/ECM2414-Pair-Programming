# Multi-threaded card game simulation:
  - All players play simultaneously (i.e. not sequentially).
  - If two or more players are given four cards with the same value — i.e. multiple winners — we do not need to account for it (we can just run everything again).

# Game setup:
- *n* players and a pack of *8n* cards.
- Hands drawn like in poken - round-robin fashion along table, up until 4 cards per player.
- Remaining cards in pack split equally to players, forming decks.

---

# Implementation notes:
- Can use `synchronized` keyword for data structure methods that modify underlying variables/representation. This should ensure thread safety.

## Classes
- `Card`
- `Player` (implements `Runnable`)
- `Queue<T>`
- `Deck`
- `CardGame` (Main class, executable)

---

### Card
**Properties**
- -rank: _int_

**Methods**
- +Card(rank: _int_)
- +getRank(): _int_

---

### Player
**Properties**
- -drawDeck: _Deck_
- -discardDeck: _Deck_
- -hand: _ArrayList\<Card>_
- -logLines: _ArrayList\<String>_

**Methods**
- +Player(drawDeck: _Deck_, discardDeck: _Deck_)
- +play(): void
  - Discard card, pick up card
- +run(): void
- +saveLogFile(filename: String): void

---

### Queue\<T>
**Properties**
- -values: _ArrayList\<T>_
- -head: _int_
- -tail: _int_

**Methods**
- +Queue()
- +enqueue(item: _T_): void
- +dequeue(): _T_
- +peek(): _T_
- +size(): _int_

---

### Deck
**Properties**
- -cards: _Queue\<Card>_

**Methods**
- +Deck()
- +insert(card: _Card_): void
  - Inserts a card at the bottom of the deck.
- +draw(): _Card_
  - Draws a card from the top of the deck.

---

### CardGame
**Properties**
- -players: _Player[]_
- -decks: _Deck[]_
- -pack: _Deck[]_

**Methods**
- (static) +main(args: String[]): void
- -loadPackFromFile(filename: String): void
- -initGame(): void
- -run(): void