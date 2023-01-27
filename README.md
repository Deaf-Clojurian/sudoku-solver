[![CI/CD](https://github.com/Deaf-Clojurian/sudoku-solver/actions/workflows/cicd.yml/badge.svg)](https://github.com/Deaf-Clojurian/sudoku-solver/actions/workflows/cicd.yml)
# Sudoku Solver
### (or should be "Sudoku Spoiler"... hmmm... :thinking:)

Hard to solve some mind tricking Sudokus? Or just want to challenge if a bot can solve this well? Or want to cheat your friend in a head-to-head game? Just feed me with a Sudoku challenge!

![](https://media.giphy.com/media/l41Yy6jvn3BXYDRu0/giphy.gif)

## ![clojure](https://user-images.githubusercontent.com/26586714/215013658-47a84870-4675-4bb7-82c9-996e8337addb.png) Clojure
- Hexagonal Architecture
- Macros
- Namespaces
- RING for RESTful
- Leiningen
- Stateful through atom

## Usage

To run locally:
- Clone the project
- Run with `lein ring server`
- Open your favorite API Client (the postman... only clojurians will get the ambiguous meaning :grimacing:)
- Access http://localhost:3000/solve/pretty
- Run with body, from a sudoku challenge, as example:
```
-------------------------
| 6 2   | 1     |   8 7 |
| 4   5 |       |     9 |
| 7 9   |   5   | 3 1   |
-------------------------
|   6   |     7 | 9 5 1 |
|     2 |       | 8 7   |
| 9   1 |     5 |     3 |
-------------------------
| 2   7 |     9 |   3 5 |
| 5     |       | 6 4   |
|   4 6 |       | 7 9 8 |
-------------------------
```
Should be converted into a vector of line vectors in JSON (I'll work with different possible inputs):
```json
[[ 6,     2,    null,  1,    null,  null,  null,  8,    7  ],
 [ 4,    null,   5,   null,  null,  null,  null, null,  9  ],
 [ 7,     9,    null, null,   5,    null,   3,    1,   null],
 [null,   6,    null, null,  null,   7,     9,    5,    1  ],
 [null,  null,   2,   null,  null,  null,   8,    7,   null],
 [ 9,    null,   1,   null,  null,   5,    null, null,  3  ],
 [ 2,    null,   7,   null,  null,   9,    null,  3,    5  ],
 [ 5,    null,  null, null,  null,  null,   6,    4,   null],
 [null,   4,     6,   null,  null,  null,   7,    9,    8  ]]
```
- And tadaaaa! The resolution (or the "spoiler" :see_no_evil:)

## Next steps:
- [x] Unit tests
- [x] Integration tests
- [x] Run with a local server
- [x] Prettify resolutions
- [ ] Different inputs
- [ ] Run as cli
- [ ] Solve expert level
- [ ] Generate new Sudoku challenges
- [ ] Create a frontend (using a Clojurescript framework or React with reagent)
- [ ] Get fund to buy a domain and make it available in a server - [vakinha](https://www.vakinha.com.br/3428676)

## License

This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.

You are free to:

- Share: copy and redistribute the material in any medium or format
- Adapt: remix, transform, and build upon the material for any purpose, even commercially

Under the following terms:

- Attribution: You must give appropriate credit, provide a link to the license, and indicate if changes were made. You may do so in any reasonable manner, but not in any way that suggests the licensor endorses you or your use.
- ShareAlike: If you remix, transform, or build upon the material, you must distribute your contributions under the same license as the original.

For more information, see https://creativecommons.org/licenses/by-sa/4.0/.
