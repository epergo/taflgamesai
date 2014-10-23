package classes.ia;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import classes.Board;
import classes.Movements;
import classes.Tuple;

public class MiniMax {
	// List of possible boards, they are the result of each of the possible
	// movements
	private List<Board> nextMovements;

	// The board which we are working with
	private Board currentBoard;

	// Turn can be 0 if it's black's turn or 0 if it's white's
	private int whoIsMaxing;

	// Depth the algorithm will go most
	private int depth;
	
	// Do we have to use the standard evaluation function?
	public boolean standard;

	public MiniMax(Board actualBoard, int whoIsMaxing, int depth) {
		this.whoIsMaxing = whoIsMaxing;
		this.currentBoard = actualBoard;
		
		if(depth == 3){
			this.depth = depth - 1;
			standard = false;
		}else{
			this.depth = depth;
			standard = true;
		}

		// Possible movements with this board
		this.nextMovements = getPosibleMovements(actualBoard, whoIsMaxing);
	}

	public int minimaxAlphaBeta(Board node, int depth, int alpha, int beta,
			boolean isMaxing, int turn) {

		// Check if we are going to need to go 'lower', if the depth is 0 or the
		// node is terminal
		// return the value of the node
		if (depth == 0 || node.isTerminal()) {
			// We get the value of the node board
			int value;
			if(standard){
				value = node.evaluate(whoIsMaxing);
			}else{
				value = node.evaluateHard(whoIsMaxing);
			}
			
			node.setValue(value);
			return node.getValue();
		}

		// Get the posible movements
		List<Board> movements;

		// If the actual depth is the same that the full minimax depth, it means
		// we dont need to call 'getPossibleMovements' method again because we
		// called it in the constructor
		if (this.depth == depth) {
			movements = new ArrayList<Board>(nextMovements);
		} else {
			movements = getPosibleMovements(node, turn);
		}

		if (isMaxing == true) {
			for (Board b : movements) {
				alpha = Math.max(
						alpha,
						minimaxAlphaBeta(b, depth - 1, alpha, beta, false,
								swapTurns(turn)));
				if (beta < alpha) {
					break;
				}
			}

			if (nextMovements.contains(node)) {
				int aux = nextMovements.indexOf(node);
				node.setValue(alpha);
				nextMovements.set(aux, node);
			}

			return alpha;

		} else {
			for (Board b : movements) {
				beta = Math.min(
						beta,
						minimaxAlphaBeta(b, depth - 1, alpha, beta, true,
								swapTurns(turn)));
				if (beta < alpha) {
					break;
				}
			}

			if (nextMovements.contains(node)) {
				int aux = nextMovements.indexOf(node);
				node.setValue(beta);
				nextMovements.set(aux, node);
			}

			return beta;
		}

	}

	public List<Board> getPosibleMovements(Board node, int currentPiece) {
		List<Board> result = new ArrayList<Board>();
		int boardLength = node.getBoard().length;
		int pieceAt = 0;

		Tuple<Integer, Integer> origin;
		Tuple<Integer, Integer> destiny;

		Board resultOfMovement;

		for (int i = 0; i < boardLength; i++) {
			for (int j = 0; j < boardLength; j++) {
				pieceAt = node.atPosition(i, j);

				if (pieceAt != -1) {

					if (pieceAt == currentPiece || pieceAt == Board.KINGPIECE
							&& currentPiece == Board.WHITEPIECE) {
						origin = new Tuple<Integer, Integer>(i, j);

						// Check top positions
						for (int i2 = i - 1; i2 >= 0; i2--) {
							destiny = new Tuple<Integer, Integer>(i2, j);

							if (Movements.isValid(node, origin, destiny,
									currentPiece)) {
								resultOfMovement = new Board(node);

								Movements.executeMovement(resultOfMovement,
										currentPiece, origin, destiny);

								result.add(resultOfMovement);
							} else {
								break;
							}
						}

						// Check right positions
						for (int j2 = j + 1; j2 < boardLength; j2++) {
							destiny = new Tuple<Integer, Integer>(i, j2);

							if (Movements.isValid(node, origin, destiny,
									currentPiece)) {
								resultOfMovement = new Board(node);

								Movements.executeMovement(resultOfMovement,
										currentPiece, origin, destiny);

								result.add(resultOfMovement);
							} else {
								break;
							}
						}

						// Check bottom positions
						for (int i2 = i + 1; i2 < boardLength; i2++) {
							destiny = new Tuple<Integer, Integer>(i2, j);

							if (Movements.isValid(node, origin, destiny,
									currentPiece)) {
								resultOfMovement = new Board(node);

								Movements.executeMovement(resultOfMovement,
										currentPiece, origin, destiny);

								result.add(resultOfMovement);
							} else {
								break;
							}
						}

						// Check left positions
						for (int j2 = j - 1; j2 >= 0; j2--) {
							destiny = new Tuple<Integer, Integer>(i, j2);

							if (Movements.isValid(node, origin, destiny,
									currentPiece)) {
								resultOfMovement = new Board(node);

								Movements.executeMovement(resultOfMovement,
										currentPiece, origin, destiny);

								result.add(resultOfMovement);
							} else {
								break;
							}
						}

					}

				}
			}
		}

		return result;
	}

	public Board chooseMovementAlphaBeta() throws Exception {
		// Result board
		Board result = null;

		// If there is no movements to do, we will return null
		// CATCH THE NULL RESULT WHERE NEEDED
		if (!nextMovements.isEmpty()) {
			// Maybe we will have many boards with the same choosen minimax
			// value
			ArrayList<Board> boardsAIValue = new ArrayList<Board>();

			// Calculate the minimax value
			int minimaxValue = minimaxAlphaBeta(currentBoard, depth,
					Integer.MIN_VALUE, Integer.MAX_VALUE, true, whoIsMaxing);

			// For each movement, if the result board has the minimax value, we
			// add it to the list
			for (Board aux : nextMovements) {
				if (aux.getValue() == minimaxValue) {
					boardsAIValue.add(aux);
				}
			}

			// If we have more than 1 board with the minimax value, we choose
			// one randomly
			if (boardsAIValue.isEmpty()) {
				// Error
				throw new Exception();
			} else if (boardsAIValue.size() == 1) {
				result = boardsAIValue.get(0);
			} else {
				Random randomGenerator = new Random();
				int index = randomGenerator.nextInt(boardsAIValue.size());

				result = boardsAIValue.get(index);
			}
		}

		return result;
	}

	public int swapTurns(int turn) {
		if (turn == 0) {
			return 1;
		} else if (turn == 1) {
			return 0;
		} else {
			throw new IllegalArgumentException();
		}
	}

	/*
	 * REGULAR MINIMAX
	 * 
	 * public int minimax(Board node, int depth, boolean isMaxing, int turn) {
	 * // We get the value of the node board int value =
	 * node.evaluate(whoIsMaxing); node.setValue(value);
	 * 
	 * // Set the bestValue variable, starting 0 int bestValue = 0;
	 * 
	 * // Check if we are going to need to go 'lower', if the depth is 0 or the
	 * // node is terminal // return the value of the node if (depth == 0 ||
	 * node.isTerminal()) { return node.getValue(); }
	 * 
	 * // If is not terminal or depth == 0 , get the posible movements
	 * List<Board> movements = getPosibleMovements(node, turn);
	 * 
	 * if (isMaxing == true) { bestValue = Integer.MIN_VALUE;
	 * 
	 * for (Board b : movements) { value = minimax(b, depth - 1, !isMaxing,
	 * swapTurns(turn)); bestValue = Math.max(bestValue, value); }
	 * 
	 * if (nextMovements.contains(node)) { int aux =
	 * nextMovements.indexOf(node); node.setValue(bestValue);
	 * nextMovements.set(aux, node); }
	 * 
	 * return bestValue;
	 * 
	 * } else { bestValue = Integer.MAX_VALUE; for (Board b : movements) {
	 * 
	 * value = minimax(b, depth - 1, true, swapTurns(turn)); bestValue =
	 * Math.min(bestValue, value); }
	 * 
	 * if (nextMovements.contains(node)) { int aux =
	 * nextMovements.indexOf(node); node.setValue(bestValue);
	 * nextMovements.set(aux, node); }
	 * 
	 * return bestValue; }
	 * 
	 * 
	 * public Board chooseMovement() { minimaxValue = minimax(currentBoard,
	 * depth, true, whoIsMaxing); Board result = null;
	 * 
	 * for (Board aux : nextMovements) { if (aux.getValue() == minimaxValue) {
	 * result = aux; } }
	 * 
	 * if (result == null) { result = nextMovements.get(0); }
	 * 
	 * return result; }
	 * 
	 * }
	 */

}
