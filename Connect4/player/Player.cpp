/*
 * Player.cpp
 *
 *  Created on: Feb 20, 2018
 *      Author: JLepere2
 */

#include "Player.h"

vector<int> Player::play(int previousRow, int previousCol) {
	vector<int> move = algo.getMove(board, previousRow, previousCol, tilesPlaced);
	int row = move[0];
	int col = move[1];
	board[row][col] = 1;
	tilesPlaced += 1;
	return move;
}

void Player::opponentPlay(int row, int col) {
	board[row][col] = -1;
	tilesPlaced += 1;
}

vector<vector<int> >& Player::getBoard() {
	return board;
}

vector<vector<int> > Player::createInitialBoard() {
	vector<vector<int> > board;
	for (int i = 0; i < BOARD_SIZE; i ++) {
		vector<int> row;
		for (int j = 0; j < BOARD_SIZE; j ++) {
			row.push_back(0);
		}
		board.push_back(row);
	}
	return board;
}

void Player::createSpecificBoard(bool isP1) {
	this->board.clear();
	vector<int> rowA;
	vector<int> rowB;
	vector<int> rowC;
	vector<int> rowD;
	vector<int> rowE;
	vector<int> rowF;
	vector<int> rowG;
	vector<int> rowH;

	if (isP1) {
		rowA = {0,  0,  -1,  0,  0,  0,  0,  0};
		rowB = {0, 1,  0,  0,  -1, 1,  0,  0};
		rowC = {0, 0, 1, -1, 1, 1, 1, -1};
		rowD = {0, -1, -1, 1, 0, -1, 0, 0};
		rowE = {1,-1,1,0,0,0,0,0};
		rowF = {-1,1,0,0,0,-1,0,0};
		rowG = {0,0,0,0,0,0,0,0};
		rowH = {0,0,0,0,0,0,0,0};
	}
	// Reverse of the board on top
	else {
		rowA = {0,  0,  1,  0,  0,  0,  0,  0};
		rowB = {0, -1,  0,  0,  1, -1,  0,  0};
		rowC = {0, 0, -1, 1, -1, -1, -1, 1};
		rowD = {0, 1, 1, -1, 0, 1, 0, 0};
		rowE = {-1,1,-1,0,0,0,0,0};
		rowF = {1,-1,0,0,0,1,0,0};
		rowG = {0,0,0,0,0,0,0,0};
		rowH = {0,0,0,0,0,0,0,0};
	}

	this->board.push_back(rowA);
	this->board.push_back(rowB);
	this->board.push_back(rowC);
	this->board.push_back(rowD);
	this->board.push_back(rowE);
	this->board.push_back(rowF);
	this->board.push_back(rowG);
	this->board.push_back(rowH);
}
