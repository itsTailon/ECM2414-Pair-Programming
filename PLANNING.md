- Multi-threaded card game simulation:
  - All players play simultaneously (i.e. not sequentially).
  - If two or more players are given four cards with the same value — i.e. multiple winners — we do not need to account for it (we can just run everything again).

- Game setup:
  - *n* players and *n* decks.
  - Players have hands of 4 cards, drawn from a pack of *8n* cards (hands are drawn like in poker).
  - Once hands have been drawn, *n* decks are formed from drawing remaining cards from the pack.
