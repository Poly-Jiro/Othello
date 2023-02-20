package game;

import java.awt.Point;
import java.util.Objects;

import board.Direction;
import board.Disk;
import board.OthelloBoard;

 /**
  * オセロルールに準じているか判定するクラス
  * @author fujita
  */
public class Referee {

	/**
	 * これから置く場所で指定した値がオセロ盤に置けるかどうか
	 * @param board オセロ盤
	 * @param point 置く場所
	 * @param disc 駒
	 * @return 置ける場合true, 置けない場合false
	 */

	public boolean isCorrectPoint(final OthelloBoard board,  final Point point, final Disk disk) {

		// NG判定
		boolean isError = true;

		if (Objects.isNull(point)) {
			// スキップ判定
			if (isCorrectSkip(board,  point, disk)) {
				System.out.println("配置可能な箇所がある場合のスキップはできません。指定してください。");
				isError = false;
				return isError;
			}
		} else {

			// 範囲内指定判定
			if (isOutOfRange(point)) {
				System.out.println("オセロボードの範囲内を指定してください。");
				isError = false;
				return isError;
			}

			// 重複判定
			if (isDuplicatedDisk(board,  point, disk)) {
				System.out.println("配置済駒があります。重複しない箇所を指定してください。");
				isError = false;
				return isError;
			}

			// 駒の連結判定
			if (isSeparatedPoint(board,  point, disk)) {
				System.out.println("配置済駒に連結するように指定してください。");
				isError = false;
				return isError;
			}

			// 裏返し判定
			if (CanFlipTheOpponentsDesk(board,  point, disk)) {
				System.out.println("指定箇所では裏返しができません。裏返し可能な箇所を指定してください。");
				isError = false;
				return isError;
			}
		}

		return isError;
	}

	/**
	 * 重複有無の判定をする。
	 * @param board
	 * @param point
	 * @param disk
	 * @return 重複有true, 重複無false
	 */
	public boolean isDuplicatedDisk(final OthelloBoard board,  final Point point, final Disk disk) {
		// 重複判定変数
		boolean isDuplicateError = false;

		Disk[][] boardWithDisk =  board.getBoard();
		if (boardWithDisk[point.x][point.y] != Disk.NONE) {
			isDuplicateError = true;
		}

		// TODO スキップの時

		return isDuplicateError;
	}

	/**
	 * 指定した場所で駒が裏返せるか判定する。
	 * @param board
	 * @param point
	 * @param disk
	 * @return 裏返せるtrue, 裏返せないfalse
	 */
	public boolean CanFlipTheOpponentsDesk (final OthelloBoard board,  final Point point, final Disk disk) {
		// 裏返し判定フラグ
		boolean canFlipFlg = false;

		// 裏返し判定
		canFlipFlg = ConfirmPossiblityOfFlippingTheOpponentsDesk(board, point, disk, Direction.NORTH);
		if (!canFlipFlg) {canFlipFlg = ConfirmPossiblityOfFlippingTheOpponentsDesk(board, point, disk, Direction.NORTH_EAST);}
		if (!canFlipFlg) {canFlipFlg = ConfirmPossiblityOfFlippingTheOpponentsDesk(board, point, disk, Direction.EAST);}
		if (!canFlipFlg) {canFlipFlg = ConfirmPossiblityOfFlippingTheOpponentsDesk(board, point, disk, Direction.SOUTH_EAST);}
		if (!canFlipFlg) {canFlipFlg = ConfirmPossiblityOfFlippingTheOpponentsDesk(board, point, disk, Direction.SOUTH);}
		if (!canFlipFlg) {canFlipFlg = ConfirmPossiblityOfFlippingTheOpponentsDesk(board, point, disk, Direction.SOUTH_WEST);}
		if (!canFlipFlg) {canFlipFlg = ConfirmPossiblityOfFlippingTheOpponentsDesk(board, point, disk, Direction.WEST);}
		if (!canFlipFlg) {canFlipFlg = ConfirmPossiblityOfFlippingTheOpponentsDesk(board, point, disk, Direction.NORTH_WEST);}

		return !canFlipFlg;
	}

	/**
	 * 指定方向への裏返しを判定する
	 * @param board
	 * @param point
	 * @param disk
	 * @param direction
	 * @return 裏返せる true, 裏返せない false
	 */
	private Boolean ConfirmPossiblityOfFlippingTheOpponentsDesk( final OthelloBoard board,  final Point point, final Disk disk, Direction direction) {

		Boolean canFlipFlg = false;

		// 使用定数の定義
		Boolean hasMargin = false;
		Boolean hasTargetPlace = false;
		int newPointX = 0 ;
		int newPointY = 0;
		Boolean nextHasMargin = false;
		Boolean hasDiskForSandwichingByOwnDisk = false;
		Boolean hasDiskForSandwichingByMyDiskSomewhere = false;

		// 方角毎の値を設定する
		switch (direction) {
			case NORTH:
				hasMargin   = point.y >=2 && point.y < OthelloBoard.SIZE_Y;
				if (point.y -1 >= 0) {
					hasTargetPlace = board.getBoard()[point.x][point.y -1] != disk && board.getBoard()[point.x][point.y -1] != Disk.NONE;
				}
				newPointX = point.x;
				newPointY = point.y -1 -1;
				nextHasMargin = newPointY >=2;
				break;
			case NORTH_EAST:
				hasMargin = point.y >=2 && point.y < OthelloBoard.SIZE_Y && point.x < OthelloBoard.SIZE_X -2;
				if (point.x + 1 < OthelloBoard.SIZE_X && point.y -1 >= 0) {
					hasTargetPlace = board.getBoard()[point.x +1][point.y -1] != disk && board.getBoard()[point.x +1][point.y -1] != Disk.NONE;
				}
				newPointX = point.x +1 +1;
				newPointY = point.y -1 -1;
				nextHasMargin = newPointY >=2 && newPointX < OthelloBoard.SIZE_X -2;
				break;
			case EAST:
				hasMargin = point.x < OthelloBoard.SIZE_X -2;
				if (point.x + 1 < OthelloBoard.SIZE_X) {
					hasTargetPlace = board.getBoard()[point.x +1][point.y] != disk && board.getBoard()[point.x +1][point.y] != Disk.NONE;
				}
				newPointX = point.x +1 +1;
				newPointY = point.y;
				nextHasMargin = newPointX < OthelloBoard.SIZE_X -2;
				break;
			case SOUTH_EAST:
				hasMargin = point.y < OthelloBoard.SIZE_Y -2 && point.x < OthelloBoard.SIZE_X -2;
				if (point.x + 1 < OthelloBoard.SIZE_X && point.y + 1 < OthelloBoard.SIZE_Y) {
					hasTargetPlace = board.getBoard()[point.x + 1][point.y +1] != disk && board.getBoard()[point.x + 1][point.y + 1] != Disk.NONE;
				}
				newPointX = point.x +1 +1;
				newPointY = point.y +1 +1;
				nextHasMargin = newPointY < OthelloBoard.SIZE_Y -2 && newPointX < OthelloBoard.SIZE_X -2;
				break;
			case SOUTH:
				hasMargin   = point.y < OthelloBoard.SIZE_Y -2;
				if (point.y + 1 < OthelloBoard.SIZE_Y) {
					hasTargetPlace = board.getBoard()[point.x][point.y +1] != disk && board.getBoard()[point.x][point.y +1] != Disk.NONE;
				}
				newPointX = point.x;
				newPointY = point.y +1 +1;
				nextHasMargin = newPointY < OthelloBoard.SIZE_Y -2;
				break;
			case SOUTH_WEST:
				hasMargin   = point.y < OthelloBoard.SIZE_Y -2 && point.x >= 2 && point.x < OthelloBoard.SIZE_X;
				if (point.x -1 >= 0 && point.y < OthelloBoard.SIZE_Y) {
					hasTargetPlace = board.getBoard()[point.x -1][point.y +1] != disk && board.getBoard()[point.x -1][point.y +1] != Disk.NONE;
				}
				newPointX = point.x -1 -1;
				newPointY = point.y +1 +1;
				nextHasMargin = newPointY < OthelloBoard.SIZE_Y -2 && newPointX >= 2 && newPointX < OthelloBoard.SIZE_X;
				break;
			case WEST:
				hasMargin   = point.x >= 2 && point.x < OthelloBoard.SIZE_X;
				if (point.x -1 >= 0) {
					hasTargetPlace = board.getBoard()[point.x -1][point.y] != disk && board.getBoard()[point.x -1][point.y] != Disk.NONE;
				}
				newPointX = point.x -1 -1;
				newPointY = point.y;
				nextHasMargin = newPointX >= 2 && newPointX < OthelloBoard.SIZE_X;
				break;
			case NORTH_WEST:
				if (point.x -1 >= 0 && point.y -1 >= 0) {
					hasTargetPlace = board.getBoard()[point.x -1][point.y -1] != disk && board.getBoard()[point.x -1][point.y -1] != Disk.NONE;
				}
				hasMargin   = point.y >=2 && point.y < OthelloBoard.SIZE_Y && point.x >= 2 && point.x < OthelloBoard.SIZE_X;
				newPointX = point.x -1 -1;
				newPointY = point.y -1 -1;
				nextHasMargin = newPointY >=2 && newPointY < OthelloBoard.SIZE_Y && newPointX >= 2 && newPointX < OthelloBoard.SIZE_X;
				break;
		}

		// hasDisksの設定
		if (nextHasMargin) {
			hasDiskForSandwichingByOwnDisk = board.getBoard()[newPointX][newPointY] == disk;
			hasDiskForSandwichingByMyDiskSomewhere =board.getBoard()[newPointX][newPointY] != disk && board.getBoard()[newPointX][newPointY] != Disk.NONE;
		}

		if (hasMargin) {
			//指定マス上に裏返し対象が存在するかを判定
			if (hasTargetPlace) {
				// 駒を挟んでいるか判定
				while (nextHasMargin) {
					if (hasDiskForSandwichingByOwnDisk) {
						// 指定マス上の対になる所が自分の駒である場合は裏返し可能
						 canFlipFlg = true;
						break;
					} else if (hasDiskForSandwichingByMyDiskSomewhere) {
						// 指定マス上の対になる場所が相手の駒である場合はその次も調べる
						switch (direction) {
							case NORTH:
								newPointY--;
								nextHasMargin = newPointY >=2;
								break;
							case NORTH_EAST:
								newPointX++;
								newPointY--;
								nextHasMargin = newPointY >=2 && newPointX < OthelloBoard.SIZE_X -2;
								break;
							case  EAST:
								newPointX++;
								nextHasMargin = newPointX < OthelloBoard.SIZE_X -2;
								break;
							case SOUTH_EAST:
								newPointX++;
								newPointY++;
								nextHasMargin = newPointY < OthelloBoard.SIZE_Y -2 && newPointX < OthelloBoard.SIZE_X -2;
								break;
							case SOUTH:
								newPointY++;
								nextHasMargin = newPointY < OthelloBoard.SIZE_Y -2;
								break;
							case SOUTH_WEST:
								newPointX--;
								newPointY++;
								nextHasMargin = newPointY < OthelloBoard.SIZE_Y -2 && newPointX >= 2 && newPointX < OthelloBoard.SIZE_X;
								break;
							case WEST:
								newPointX++;
								nextHasMargin = newPointX >= 2 && newPointX < OthelloBoard.SIZE_X;
								break;
							case NORTH_WEST:
								newPointX++;
								newPointY--;
								nextHasMargin = newPointY >=2 && newPointY < OthelloBoard.SIZE_Y && newPointX >= 2 && newPointX < OthelloBoard.SIZE_X;
								break;
						}
						// hasDisksの設定
						if (nextHasMargin) {
							hasDiskForSandwichingByOwnDisk = board.getBoard()[newPointX][newPointY] == disk;
							hasDiskForSandwichingByMyDiskSomewhere =board.getBoard()[newPointX][newPointY] != disk && board.getBoard()[newPointX][newPointY] != Disk.NONE;
						}
					} else {
						// 指定マス上の上に駒がない場合は裏返し不可
						break;
					}
				}
			}
		}
		return canFlipFlg;
	}

	/**
	 * 範囲外を指定しているかどうか判定する。
	 * @param board
	 * @param point
	 * @param disk
	 * @return 範囲外指定true, 範囲内指定false
	 */
	public boolean isOutOfRange (final Point point) {
		// 範囲内指定判定変数
		boolean isSpecifiableError = false;

		if (!(point.x < OthelloBoard.SIZE_X && point.y < OthelloBoard.SIZE_Y && point.x >= 0 && point.y >= 0))  {
			isSpecifiableError = true;
		}
		return isSpecifiableError;
	}

	/**
	 * 指定した場所と駒が繋がっているかどうか判定する。
	 * @param board
	 * @param point
	 * @param disk
	 * @return 離れているtrue, 連結しているfalse
	 */
	public boolean isSeparatedPoint (final OthelloBoard board,  final Point point, final Disk disk) {
		// コネクト判定変数
		boolean isSeparatedError = true;

		// north
		if (point.y -1 >= 0) {
			if (board.getBoard()[point.x][point.y -1] != Disk.NONE) {
				isSeparatedError = false;
			}
		}

		// northEast
		if (point.x + 1 < OthelloBoard.SIZE_X && point.y -1 >= 0) {
			if (board.getBoard()[point.x +1][point.y -1] != Disk.NONE) {
				isSeparatedError = false;
			}
		}

		// east
		if (point.x + 1 < OthelloBoard.SIZE_X) {
			if (board.getBoard()[point.x + 1][point.y] != Disk.NONE) {
				isSeparatedError = false;
			}
		}

		// southEast
		if (point.x + 1 < OthelloBoard.SIZE_X && point.y + 1 < OthelloBoard.SIZE_Y) {
			if (board.getBoard()[point.x + 1][point.y + 1]!= Disk.NONE) {
				isSeparatedError = false;
			}
		}

		// south
		if (point.y + 1 < OthelloBoard.SIZE_Y) {
			if (board.getBoard()[point.x][point.y + 1] != Disk.NONE) {
				isSeparatedError = false;
			}
		}

		// southWest
		if (point.x - 1 >= 0 && point.y + 1 < OthelloBoard.SIZE_Y) {
			if (board.getBoard()[point.x - 1][point.y + 1] != Disk.NONE) {
				isSeparatedError = false;
			}
		}

		// west
		if (point.x - 1 >= 0) {
			if (board.getBoard()[point.x -1][point.y] != Disk.NONE) {
				isSeparatedError = false;
			}
		}

		// northWest
		if (point.x - 1 >= 0 && point.y - 1 >= 0) {
			if (board.getBoard()[point.x -1][point.y - 1] != Disk.NONE) {
				isSeparatedError = false;
			}
		}

		// 連結する駒がない場合はエラーとして返す
		return isSeparatedError;
	}


	/**
	 * スキップ指定が正しいかどうか判定する
	 * @param board
	 * @param point
	 * @param disc
	 * @return 正しくないtrue, 正しいfalse
	 */
	public boolean isCorrectSkip(final OthelloBoard board,  final Point point, final Disk disk) {
		//スキップが適切かどうかの変数
		Boolean hasPossibleReversePoint = false;
			// 拡張ボードで判定（拡張場所はnull）
		Disk[][] ChooseablePoints = getChooseablePoint (board,  point, disk);
		for (int y = 0; y< OthelloBoard.SIZE_Y; y++) {
			for (int x = 0; x < OthelloBoard.SIZE_X; x++) {
				if (ChooseablePoints[x][y] == null) {
					// 裏返し判定
					if (CanFlipTheOpponentsDesk(board,  new Point(x, y), disk)) {
						// 拡張した位置でエラーとなる場合は何もしない
					} else {
						// エラーならない場合＝置ける場所がある時はfalse
						hasPossibleReversePoint = true;
						break;
					}
				}
			}
			// 多重ループ抜け
			if (hasPossibleReversePoint) {
				break;
			}
		}
		return hasPossibleReversePoint;
	}

	/**
	 * 置いてある駒を上下左右、斜め方向に1マスづつ拡張する
	 * @param board
	 * @param point
	 * @param disk
	 * @return
	 */
	private Disk[][] getChooseablePoint (final OthelloBoard board,  final Point point, final Disk disk) {
		// ボードの駒取得
		Disk[][]boardWithDisk = board.getBoard();

		// 拡張ボードの準備
		Disk[][]  expansionBoard =new Disk[OthelloBoard.SIZE_X][OthelloBoard.SIZE_Y];

		for (int y =0; y< OthelloBoard.SIZE_Y; y++) {
			for (int x =0; x < OthelloBoard.SIZE_X; x++) {
				expansionBoard[x][y] = boardWithDisk[x][y];
			}
		}

		// 拡張した場所の値
		Disk virtualImage = null;

		// 置ける場所調査
		// 拡張ボードを取得（拡張点はnullとする）
		for (int y = 0; y< OthelloBoard.SIZE_Y; y++) {
			for (int x = 0; x < OthelloBoard.SIZE_X; x++) {
				if (boardWithDisk[x][y] != Disk.NONE) {
					// north
					if (y-1 >= 0) {
						if (expansionBoard[x][y-1] == Disk.NONE) {
							expansionBoard[x][y-1] = virtualImage;
						}
					}
					// northEast
					if (y - 1 >= 0 && x + 1 < OthelloBoard.SIZE_X) {
						if (expansionBoard[x + 1][y - 1] == Disk.NONE) {
							expansionBoard[x + 1][y - 1] = virtualImage;
						}
					}
					// east
					if (x + 1 < OthelloBoard.SIZE_X) {
						if (expansionBoard[x + 1][y] == Disk.NONE) {
							expansionBoard[x + 1][y] = virtualImage;
						}
					}
					// southEast
					if (y + 1 < OthelloBoard.SIZE_Y && x + 1 < OthelloBoard.SIZE_X) {
						if (expansionBoard[x + 1][y + 1] == Disk.NONE) {
							expansionBoard[x + 1][y + 1] = virtualImage;
						}
					}
					// south
					if (y + 1 < OthelloBoard.SIZE_Y) {
						if (expansionBoard[x][y + 1] == Disk.NONE) {
							expansionBoard[x][y + 1] = virtualImage;
						}
					}
					// southWest
					if (y + 1 < OthelloBoard.SIZE_Y && x -1  >= 0) {
						if (expansionBoard[x - 1][y + 1] == Disk.NONE) {
							expansionBoard[x - 1][y + 1] = virtualImage;
						}
					}
					// west
					if (x -1  >= 0) {
						if (expansionBoard[x][y + 1] == Disk.NONE) {
							expansionBoard[x][y + 1] = virtualImage;
						}
					}
					// northWest
					if (y - 1 >= 0 && x -1  >= 0) {
						if (expansionBoard[x - 1][y - 1] == Disk.NONE) {
							expansionBoard[x - 1][y - 1] = virtualImage;
						}
					}
				}
			}
		}
		return expansionBoard;
	}
}
