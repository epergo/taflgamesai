package classes;

import interfaces.IBoard;

import java.util.Arrays;

public class Board implements IBoard {
	// Type of pieces
	public static final int EMPTY = -1;
	public static final int BLACKPIECE = 0;
	public static final int WHITEPIECE = 1;
	public static final int KINGPIECE = 2;

	// Position of the castles
	public final Tuple<Integer, Integer> CONS_UPLEFT_CORNER;
	public final Tuple<Integer, Integer> CONS_UPRIGHT_CORNER;
	public final Tuple<Integer, Integer> CONS_DOWNLEFT_CORNER;
	public final Tuple<Integer, Integer> CONS_DOWNRIGHT_CORNER;
	public final Tuple<Integer, Integer> CONS_THRONE;

	// The board, represented by a bidimensional array
	private int[][] board;

	// Quick access to King's position
	private Tuple<Integer, Integer> kingValue;

	// Number of black pieces
	private int nBlack;

	// Number of white pieces
	private int nWhite;

	// Number of pieces needed to capture the king
	private int capturedBy;

	// Needed to print the board in the console output
	char[] letters = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
			'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
			'Y', 'Z' };

	// Value that the board has. It is defined by the evaluation function.
	private int value;

	/* ===== CONSTRUCTORS ===== */
	public Board(int[][] board, Tuple<Integer, Integer> upleftCorner,
			Tuple<Integer, Integer> downleftCorner,
			Tuple<Integer, Integer> uprightCorner,
			Tuple<Integer, Integer> downrightCorner,
			Tuple<Integer, Integer> throne) {
		super();

		this.board = board;
		CONS_UPLEFT_CORNER = upleftCorner;
		CONS_UPRIGHT_CORNER = uprightCorner;
		CONS_DOWNLEFT_CORNER = downleftCorner;
		CONS_DOWNRIGHT_CORNER = downrightCorner;
		CONS_THRONE = throne;
		this.kingValue = CONS_THRONE;
		this.value = 0;
		this.numberOfWhitesAndBlackCoins();
		this.capturedBy = 2;
	}

	public Board(Board toCopy) {
		CONS_UPLEFT_CORNER = toCopy.CONS_UPLEFT_CORNER;
		CONS_UPRIGHT_CORNER = toCopy.CONS_UPRIGHT_CORNER;
		CONS_DOWNLEFT_CORNER = toCopy.CONS_DOWNLEFT_CORNER;
		CONS_DOWNRIGHT_CORNER = toCopy.CONS_DOWNRIGHT_CORNER;
		CONS_THRONE = toCopy.CONS_THRONE;

		copyBoard(toCopy.board);
		this.kingValue = toCopy.getKingValue();
		this.nBlack = toCopy.getNBlackPieces();
		this.nWhite = toCopy.getNWhitePieces();
		this.value = toCopy.getValue();
		this.capturedBy = toCopy.getCapturedBy();
	}

	/* ===== GETTERS AND SETTERS ===== */

	public int getCapturedBy() {
		return capturedBy;
	}

	public boolean capturedByFour() {
		if (getCapturedBy() == 4) {
			return true;
		} else {
			return false;
		}
	}

	public void setCapturedBy(int capturedBy) {
		this.capturedBy = capturedBy;
	}

	@Override
	public int[][] getBoard() {
		return board;
	}

	@Override
	public void setBoard(int[][] board) {
		this.copyBoard(board);
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public int getNBlackPieces() {
		return nBlack;
	}

	@Override
	public void setnBlackPieces(int nBlack) {
		this.nBlack = nBlack;
	}

	@Override
	public int getNWhitePieces() {
		return nWhite;
	}

	@Override
	public void setnWhitePieces(int nWhite) {
		this.nWhite = nWhite;
	}

	@Override
	public Tuple<Integer, Integer> getKingValue() {
		return kingValue;
	}

	@Override
	public void setKingValue(Tuple<Integer, Integer> kingValue) {
		this.kingValue = kingValue;
	}

	@Override
	public int length() {
		return this.board.length;
	}

	/* ===== END GETTERS AND SETTERS ===== */

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Board other = (Board) obj;
		if (CONS_UPLEFT_CORNER == null) {
			if (other.CONS_UPLEFT_CORNER != null)
				return false;
		} else if (!CONS_UPLEFT_CORNER.equals(other.CONS_UPLEFT_CORNER))
			return false;
		if (CONS_DOWNLEFT_CORNER == null) {
			if (other.CONS_DOWNLEFT_CORNER != null)
				return false;
		} else if (!CONS_DOWNLEFT_CORNER.equals(other.CONS_DOWNLEFT_CORNER))
			return false;
		if (CONS_DOWNRIGHT_CORNER == null) {
			if (other.CONS_DOWNRIGHT_CORNER != null)
				return false;
		} else if (!CONS_DOWNRIGHT_CORNER.equals(other.CONS_DOWNRIGHT_CORNER))
			return false;
		if (CONS_THRONE == null) {
			if (other.CONS_THRONE != null)
				return false;
		} else if (!CONS_THRONE.equals(other.CONS_THRONE))
			return false;
		if (CONS_UPRIGHT_CORNER == null) {
			if (other.CONS_UPRIGHT_CORNER != null)
				return false;
		} else if (!CONS_UPRIGHT_CORNER.equals(other.CONS_UPRIGHT_CORNER))
			return false;
		if (!Arrays.deepEquals(board, other.board))
			return false;
		if (kingValue != other.kingValue)
			return false;
		if (nWhite != other.nWhite)
			return false;
		if (nBlack != other.nBlack)
			return false;
		return true;
	}

	@Override
	public String toString() {
		String s2 = "";
		int nRows = board.length;
		int nCols = board[0].length;

		System.out.print("  ");

		for (int i = 0; i < nRows; i++) {
			System.out.print(letters[i] + " ");
		}

		System.out.println();

		for (int j = 0; j < nCols; j++) {
			for (int i = 0; i < nRows; i++) {

				if (i == 0) {
					s2 += j + " ";
				}

				int piece = board[j][i];
				if (piece == BLACKPIECE) {
					s2 += "N";
				} else if (piece == WHITEPIECE) {
					s2 += "B";
				} else if (piece == KINGPIECE) {
					s2 += "K";
				} else {
					s2 += "·";
				}

				s2 += " ";

			}

			s2 += "\n";

		}

		return s2;
	}

	public void copyBoard(int[][] board) {
		int boardLength = board.length;
		this.board = new int[boardLength][boardLength];

		for (int i = 0; i < boardLength; i++) {
			for (int j = 0; j < boardLength; j++) {
				this.board[i][j] = board[i][j];
			}
		}

	}

	@Override
	public boolean isScape(Tuple<Integer, Integer> pos) {
		if (pos.equals(CONS_DOWNLEFT_CORNER)
				|| pos.equals(CONS_DOWNRIGHT_CORNER)
				|| pos.equals(CONS_UPLEFT_CORNER)
				|| pos.equals(CONS_UPRIGHT_CORNER)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isThrone(Tuple<Integer, Integer> pos) {

		if (pos.equals(CONS_THRONE)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int atPosition(Tuple<Integer, Integer> pos) {
		return atPosition(pos.x, pos.y);
	}

	@Override
	public int atPosition(int x, int y) {
		return getBoard()[x][y];
	}

	/*
	 * For every side of the piece checks: If the next piece is from the other
	 * player check if the next is from the actual player or is a
	 * scape/throne/side of map
	 */
	@Override
	public void checkDeaths(Tuple<Integer, Integer> destination,
			int currentPiece) {
		Tuple<Integer, Integer> toCheck;
		boolean checkKing = false;
		int pieceToCheck = -1;

		if (destination.y > 0) {
			toCheck = new Tuple<Integer, Integer>(destination.x,
					destination.y - 1);

			pieceToCheck = atPosition(toCheck);

			// If the current piece is white and the piece to check is a king,
			// it isnt needed to check anything
			checkKing = !(pieceToCheck == KINGPIECE && currentPiece == WHITEPIECE);

			if (pieceToCheck != EMPTY && pieceToCheck != currentPiece
					&& checkKing) {
				if (!capturedByFour()
						|| (capturedByFour() && pieceToCheck != KINGPIECE)) {
					if (pieceToCheck == KINGPIECE && isThrone(getKingValue())) {
						// Nothing to check
					} else if (isSandwitchedHorizontal(toCheck)) {
						board[toCheck.x][toCheck.y] = -1;
					}
				} else {
					if (isSandwitchedHorizontal(toCheck)
							&& isSandwitchedVertical(toCheck)
							&& !isThrone(getKingValue())) {
						board[toCheck.x][toCheck.y] = -1;
						kingValue = null;
					}
				}
			}
		}

		if (destination.y < length() - 1) {
			toCheck = new Tuple<Integer, Integer>(destination.x,
					destination.y + 1);

			pieceToCheck = atPosition(toCheck);

			// If the current piece is white and the piece to check is a king,
			// it isnt needed to check anything
			checkKing = !(pieceToCheck == KINGPIECE && currentPiece == WHITEPIECE);

			if (pieceToCheck != EMPTY && pieceToCheck != currentPiece
					&& checkKing) {
				if (!capturedByFour()
						|| (capturedByFour() && pieceToCheck != KINGPIECE)) {
					if (pieceToCheck == KINGPIECE && isThrone(getKingValue())) {
						// Nothing to check
					} else if (isSandwitchedHorizontal(toCheck)) {
						board[toCheck.x][toCheck.y] = -1;
					}
				} else {
					if (pieceToCheck == KINGPIECE && isThrone(getKingValue())) {
						// Nothing to check
					} else if (isSandwitchedHorizontal(toCheck)
							&& isSandwitchedVertical(toCheck)
							&& !isThrone(getKingValue())) {
						board[toCheck.x][toCheck.y] = -1;
						kingValue = null;
					}
				}
			}

		}

		if (destination.x > 0) {
			toCheck = new Tuple<Integer, Integer>(destination.x - 1,
					destination.y);

			pieceToCheck = atPosition(toCheck);

			// If the current piece is white and the piece to check is a king,
			// it isnt needed to check anything
			checkKing = !(pieceToCheck == KINGPIECE && currentPiece == WHITEPIECE);

			if (pieceToCheck != EMPTY && pieceToCheck != currentPiece
					&& checkKing) {
				if (!capturedByFour()
						|| (capturedByFour() && pieceToCheck != KINGPIECE)) {
					if (pieceToCheck == KINGPIECE && isThrone(getKingValue())) {
						// Nothing to check
					} else if (isSandwitchedVertical(toCheck)) {
						board[toCheck.x][toCheck.y] = -1;
					}
				} else {
					if (isSandwitchedHorizontal(toCheck)
							&& isSandwitchedVertical(toCheck)
							&& !isThrone(getKingValue())) {
						board[toCheck.x][toCheck.y] = -1;
						kingValue = null;
					}
				}
			}

		}

		if (destination.x < length() - 1) {
			toCheck = new Tuple<Integer, Integer>(destination.x + 1,
					destination.y);

			pieceToCheck = atPosition(toCheck);

			// If the current piece is white and the piece to check is a king,
			// it isnt needed to check anything
			checkKing = !(pieceToCheck == KINGPIECE && currentPiece == WHITEPIECE);

			if (pieceToCheck != EMPTY && pieceToCheck != currentPiece
					&& checkKing) {
				if (!capturedByFour()
						|| (capturedByFour() && pieceToCheck != KINGPIECE)) {
					if (pieceToCheck == KINGPIECE && isThrone(getKingValue())) {
						// Nothing to check
					} else if (isSandwitchedVertical(toCheck)) {

						board[toCheck.x][toCheck.y] = -1;
					}
				} else {
					if (isSandwitchedHorizontal(toCheck)
							&& isSandwitchedVertical(toCheck)
							&& !isThrone(getKingValue())) {
						board[toCheck.x][toCheck.y] = -1;
						kingValue = null;
					}
				}
			}

		}
	}

	/*
	 * Given a position of the board, checks if the piece inside is surrounded
	 * horizontally.
	 */
	public boolean isSandwitchedHorizontal(Tuple<Integer, Integer> pos) {
		boolean res = false;

		Tuple<Integer, Integer> aux1;
		Integer cellsContent;

		boolean leftSide = false;
		boolean rightSide = false;

		int currentPiece = atPosition(pos);

		if (currentPiece == EMPTY) {
			// The cell is empty
			// Do nothing and return false
		} else {
			// Check left side
			if (pos.y > 0) {
				if (currentPiece == KINGPIECE) {
					// If the piece is the king, we assume that is a white piece
					// just because is easier to work with
					currentPiece = WHITEPIECE;
				}

				aux1 = new Tuple<Integer, Integer>(pos.x, pos.y - 1);

				cellsContent = atPosition(aux1);
				if (cellsContent == KINGPIECE) {
					cellsContent = WHITEPIECE;
				}

				if ((cellsContent != currentPiece && cellsContent != EMPTY)
						|| isScape(aux1) || currentPiece == BLACKPIECE
						&& isThrone(aux1)) {
					leftSide = true;

				}

			} else if (pos.y == 0) {
				if (atPosition(pos) == KINGPIECE && capturedByFour()) {
					leftSide = true;
				}
			}

			// Check right side
			if (pos.y < length() - 1) {
				aux1 = new Tuple<Integer, Integer>(pos.x, pos.y + 1);

				cellsContent = atPosition(aux1);
				if (cellsContent == KINGPIECE) {
					cellsContent = WHITEPIECE;
				}

				if ((cellsContent != currentPiece && cellsContent != EMPTY)
						|| isScape(aux1) || currentPiece == BLACKPIECE
						&& isThrone(aux1)) {
					rightSide = true;
				}
			} else if (pos.y == length() - 1) {
				if (atPosition(pos) == KINGPIECE && capturedByFour()) {
					rightSide = true;
				}
			}
		}

		if (leftSide && rightSide) {
			res = true;
		}

		return res;
	}

	/*
	 * Given a position of the board, checks if the piece inside is surrounded
	 * vertically.
	 */
	public boolean isSandwitchedVertical(Tuple<Integer, Integer> pos) {
		boolean res = false;

		Tuple<Integer, Integer> aux1;
		Integer cellsContent;

		boolean topSide = false;
		boolean downSide = false;

		int currentPiece = atPosition(pos);

		if (currentPiece == EMPTY) {
			// The cell is empty
			// Do nothing and return false
		} else {
			// Check top side
			if (pos.x > 0) {
				if (currentPiece == KINGPIECE) {
					// If the piece is the king, we assume that is a white piece
					// just because is easier to work with
					currentPiece = WHITEPIECE;
				}
				aux1 = new Tuple<Integer, Integer>(pos.x - 1, pos.y);

				cellsContent = atPosition(aux1);
				if (cellsContent == KINGPIECE) {
					cellsContent = WHITEPIECE;
				}

				if ((cellsContent != currentPiece && cellsContent != EMPTY)
						|| isScape(aux1) || currentPiece == BLACKPIECE
						&& isThrone(aux1)) {
					topSide = true;
				}
			} else if (pos.x == 0) {
				if (atPosition(pos) == KINGPIECE && capturedByFour()) {
					topSide = true;
				}
			}

			// Check down side
			if (pos.x < length() - 1) {
				aux1 = new Tuple<Integer, Integer>(pos.x + 1, pos.y);

				cellsContent = atPosition(aux1);
				if (cellsContent == KINGPIECE) {
					cellsContent = WHITEPIECE;
				}

				if ((cellsContent != currentPiece && cellsContent != EMPTY)
						|| isScape(aux1) || currentPiece == BLACKPIECE
						&& isThrone(aux1)) {
					downSide = true;
				}
			} else if (pos.x == length() - 1) {
				if (atPosition(pos) == KINGPIECE && capturedByFour()) {
					downSide = true;
				}
			}

			if (topSide && downSide) {
				res = true;
			}
		}

		return res;
	}

	// Calculates the value of the board
	@Override
	public int evaluate(int whoIsMaxing) {

		if (whoIsMaxing == BLACKPIECE) {
			if (!isKingAlive()) {
				return Integer.MAX_VALUE;
			} else if (isScape(getKingValue())) {
				return Integer.MIN_VALUE;
			} else {
				int differenceOfPieces = getNBlackPieces() - 2
						* getNWhitePieces();
				return differenceOfPieces + survabilityOfKing();
			}
		} else {
			if (isKingAlive() == false) {
				return Integer.MIN_VALUE;
			} else if (isScape(getKingValue())) {
				return Integer.MAX_VALUE;
			} else if (getNBlackPieces() == 0) {
				return Integer.MAX_VALUE;
			} else {
				int differenceOfPieces = getNWhitePieces() - getNBlackPieces();
				return differenceOfPieces - survabilityOfKing();
			}
		}
	}

	public int evaluateHard(int whoIsMaxing) {
		if (whoIsMaxing == BLACKPIECE) {
			if (!isKingAlive()) {
				return Integer.MAX_VALUE;
			} else if (isScape(getKingValue())) {
				return Integer.MIN_VALUE;
			} else {
				int differenceOfPieces = getNBlackPieces() - 2
						* getNWhitePieces();
				return differenceOfPieces + survabilityOfKing()
						- visibilityOfCastles();
			}
		} else {
			if (isKingAlive() == false) {
				return Integer.MIN_VALUE;
			} else if (isScape(getKingValue())) {
				return Integer.MAX_VALUE;
			} else if (getNBlackPieces() == 0) {
				return Integer.MAX_VALUE;
			} else {
				int differenceOfPieces = getNWhitePieces() - getNBlackPieces();
				return differenceOfPieces - survabilityOfKing()
						+ visibilityOfCastles();
			}
		}
	}

	public int visibilityOfCastles() {
		int res = 0;

		Tuple<Integer, Integer> origin = getKingValue();
		Tuple<Integer, Integer> destiny;

		// Check top positions
		for (int i2 = origin.x - 1; i2 >= 0; i2--) {
			destiny = new Tuple<Integer, Integer>(i2, origin.y);

			if (isScape(destiny)) {
				res += 50;
				break;
			} else if (atPosition(destiny) != Board.EMPTY) {
				break;
			}
		}

		// Check right positions
		for (int j2 = origin.y + 1; j2 < length(); j2++) {
			destiny = new Tuple<Integer, Integer>(origin.x, j2);

			if (isScape(destiny)) {
				res += 50;
				break;
			} else if (atPosition(destiny) != Board.EMPTY) {
				break;
			}
		}

		// Check bottom positions
		for (int i2 = origin.x + 1; i2 < length(); i2++) {
			destiny = new Tuple<Integer, Integer>(i2, origin.y);

			if (isScape(destiny)) {
				res += 50;
				break;
			} else if (atPosition(destiny) != Board.EMPTY) {
				break;
			}
		}

		// Check left positions
		for (int j2 = origin.y - 1; j2 >= 0; j2--) {
			destiny = new Tuple<Integer, Integer>(origin.x, j2);

			if (isScape(destiny)) {
				res += 50;
				break;
			} else if (atPosition(destiny) != Board.EMPTY) {
				break;
			}
		}

		return res;
	}

	// Gives a value to the survability of the king
	public int survabilityOfKing() {
		int res = 1;

		int increment = 3;

		if (isThrone(getKingValue())) {
			return 0;
		}

		if (getKingValue().x > 0) {
			if (atPosition(getKingValue().x - 1, getKingValue().y) == BLACKPIECE
					|| isScape(new Tuple<Integer, Integer>(
							getKingValue().x - 1, getKingValue().y))) {
				res *= increment;
			}
		}

		if (getKingValue().y > 0) {
			if (atPosition(getKingValue().x, getKingValue().y - 1) == BLACKPIECE
					|| isScape(new Tuple<Integer, Integer>(getKingValue().x,
							getKingValue().y - 1))) {
				res *= increment;
			}
		}

		if (getKingValue().x < length() - 1) {
			if (atPosition(getKingValue().x + 1, getKingValue().y) == BLACKPIECE
					|| isScape(new Tuple<Integer, Integer>(
							getKingValue().x + 1, getKingValue().y))) {
				res *= increment;
			}
		}

		if (getKingValue().y < length() - 1) {
			if (atPosition(getKingValue().x, getKingValue().y + 1) == BLACKPIECE
					|| isScape(new Tuple<Integer, Integer>(getKingValue().x,
							getKingValue().y + 1))) {
				res *= increment;
			}
		}

		return res;
	}

	// Is this board terminal?
	@Override
	public boolean isTerminal() {
		// King is dead
		if (!isKingAlive()) {
			return true;
		}

		// King scaped
		if (isScape(getKingValue())) {
			return true;
		}

		// Black pieces have been defeated
		if (getNBlackPieces() == 0) {
			return true;
		}

		return false;
	}

	public boolean isKingAlive() {
		boolean res = false;

		if (getKingValue() == null) {
			return res;
		}

		if (atPosition(getKingValue()) == KINGPIECE)
			res = true;

		return res;
	}

	public Board clone() {
		int[][] boardAux = new int[board.length][board.length];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				boardAux[i][j] = board[i][j];
			}
		}
		Board b = new Board(boardAux, CONS_UPLEFT_CORNER.clone(),
				CONS_UPRIGHT_CORNER.clone(), CONS_DOWNLEFT_CORNER.clone(),
				CONS_DOWNRIGHT_CORNER.clone(), CONS_THRONE.clone());
		return b;
	}

	// Update the number of white and black pieces on the board
	@Override
	public void numberOfWhitesAndBlackCoins() {
		int nWhite = 0;
		int nBlack = 0;

		for (int i = 0; i < length(); i++) {
			for (int j = 0; j < length(); j++) {
				if (board[i][j] == Board.BLACKPIECE) {
					nBlack++;
				} else if (board[i][j] == Board.WHITEPIECE) {
					nWhite++;
				}
			}
		}

		setnWhitePieces(nWhite);
		setnBlackPieces(nBlack);
	}

}
