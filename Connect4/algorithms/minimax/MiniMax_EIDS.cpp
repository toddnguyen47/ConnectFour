/*
 * MiniMax_EIDS.cpp
 *
 *  Created on: Mar 3, 2018
 *      Author: JLepere2
 */

#include "MiniMax_EIDS.h"
#include <cassert>

vector<int> MiniMax_EIDS::getMove(vector<vector<int> >& board, int previousRow, int previousCol, int currentNumTiles) {
	cout << "AI is thinking...\n\n";
	// TODO: OPTIMIZE????
	initialBoard.clear();
	for (int r = 0; r < BOARD_SIZE; r ++) {
		vector<int> rv;
		for (int c = 0; c < BOARD_SIZE; c ++) {
			rv.push_back(board[r][c]);
		}
		initialBoard.push_back(rv);
	}
	currentTilesPlaced = currentNumTiles;
	startTime = time(0);
	maxDepth = 1;
	vector<int> bestMove;
	while (true) {
		visitedHash.clear();
		int alpha = NEG_INF;
		int beta = INF;
		int depth = 0;
		count = 0;
		vector<int> move = hMiniMax(board, depth, alpha, beta, previousRow, previousCol);
		//cout<<"ply: "<<maxDepth<<" in "<<difftime(time(0), startTime)<<"s - "<<count<<"\n";
		if (difftime(time(0), startTime) > maxTime) return {bestMove[0], bestMove[1]};
		if (maxDepth == 8) return {bestMove[0], bestMove[1]};
		bestMove = {move[1],move[2]};
		maxDepth ++;
	}
}

vector<int> MiniMax_EIDS::hMiniMax(vector<vector<int> >& board, int depth, int alpha, int beta, int previousRow, int previousCol) {
	//count ++;
	if (cutoffTest(board, depth, maxDepth)) return {heuristicFunction.execute(board, depth)};
	vector<vector<int> > successors = successorFunction.execute(board, previousRow, previousCol);
	successors = sortMove(board, successors, depth);
	if (depth % 2 == 0) {
		vector<int> v = {NEG_INF, -1, -1};
		for (int i = 0; i < successors.size(); i ++) {
			int row = successors[i][0];
			int col = successors[i][1];
			board[row][col] = MAX;
			int newDepth = depth + 1;
			//vector<int> res = hMiniMax(board, newDepth, alpha, beta, row, col);
			vector<int> res;
			int visistedValue = visitedHash.getValue(board);
			if (visistedValue != VisitedHashTable::NOT_VISITED_VALUE) {
				res = {visistedValue};
			} else {
				res = hMiniMax(board, newDepth, alpha, beta, row, col);
				visitedHash.addVisited(board, initialBoard, res[0]);
			}
			board[row][col] = 0;
			if (res[0] > v[0]) {v = {res[0], row, col};}
			if (v[0] >= beta) return v;
			alpha = max(v[0], alpha);
		}
		// Shows how deep IDS is going. Comment out.
		//if (depth == 0) std::cout<<v[0]<<" "<<v[1]<<" "<<v[2]<<"\n";
		return v;
	} else {
		vector<int> v = {INF, -1, -1};
		for (int i = 0; i < successors.size(); i ++) {
			int row = successors[i][0];
			int col = successors[i][1];
			board[row][col] = MIN;
			int newDepth = depth + 1;
			//vector<int> res = hMiniMax(board, newDepth, alpha, beta, row, col);
			vector<int> res;
			int visistedValue = visitedHash.getValue(board);
			if (visistedValue != VisitedHashTable::NOT_VISITED_VALUE) {
				//cout<<"checking\n";
				res = {visistedValue};
			} else {
				res = hMiniMax(board, newDepth, alpha, beta, row, col);
				visitedHash.addVisited(board, initialBoard, res[0]);
			}
			board[row][col] = 0;
			if (res[0] < v[0]) v = {res[0], row, col};
			if (v[0] <= alpha) return v;
			beta = min(v[0], beta);
		}
		return v;
	}
}

// Sort moves based on WinnerOrLoser, then KillerMove1, then OneAway
// @author: Tho Nguyen
vector<vector<int> > MiniMax_EIDS::sortMove(vector<vector<int> > &board,
vector<vector<int> > successors, int depth) {

	ToddHeuristic th(100); // WinnerOrLoser, then KillerMove1, then OneAway
	int player;
	if (depth % 2 == 0) {player = MAX;}
	else {player = MIN;}

	int zeroCounter = 0;

	int successorsSize = successors.size();
	int score[successorsSize];
	int topChildren = 7;
	topChildren = std::min(topChildren, successorsSize);

	unsigned seed1 = std::chrono::system_clock::now().time_since_epoch().count();
	std::default_random_engine generator(seed1);
	int randMax = 10;
	std::uniform_int_distribution<int> distribution2(0, randMax);

	for (int i = 0; i < successorsSize; i++) {
		// Make the move
		int row = successors[i][0];
		int col = successors[i][1];
		board[row][col] = player;

		// give weight to the first 2 tiles placed
		if (currentTilesPlaced < 2) {
			score[i] = 0;
			if (row >= 3 && row <= 4 && col >= 3 && col <= 4) {
				if (player == MAX) {score[i] += distribution2(generator);}
				else {score[i] -= distribution2(generator);}
			}
		}
		else {
			score[i] = th.execute(board, depth);
			if (score[i] == 0) {zeroCounter++;}
		}

		// Reset board
		board[row][col] = 0;
	}

	vector<vector<int> > orderedList;
	bool indexStorage[successorsSize]; // to store the indices of the best nodes
	for (int i = 0; i < successorsSize; i++) {
		indexStorage[i] = false;
	}

	// Sort the best moves
	for (int i = 0; i < topChildren; i++) {
		int maxScore = -16380, desiredIndex = 0;
		int minScore = 16380;
		for (int j = 0; j < successorsSize; j++) {
			// If max player
			if (player == MAX) {
				if (indexStorage[j] == false && score[j] > maxScore) {
					maxScore = score[j];
					desiredIndex = j;
				}
			}
			// If min player
			else {
				if (indexStorage[j] == false && score[j] < minScore) {
					minScore = score[j];
					desiredIndex = j;
				}
			}
		}

		orderedList.push_back(successors[desiredIndex]);
		indexStorage[desiredIndex] = true;
	}

	// Add the rest of successors back into orderedList
	for (int index = 0; index < successorsSize; index++) {
		if (indexStorage[index] == false) {orderedList.push_back(successors[index]);}
	}

	// Randomize some elements if there are more than 2 zeros
	int beginIndex = topChildren - 1; // leave the best 6 moves alone
	int numOfSwitches = 10;
	std::uniform_int_distribution<int> distribution(beginIndex, std::min(beginIndex + 10, successorsSize - 1));
	for (int i = beginIndex; i < numOfSwitches; i++) {
		int randomIndex = distribution(generator);
		vector<int> temp = orderedList[i];
		orderedList[i] = orderedList[randomIndex];
		orderedList[randomIndex] = temp;
	}

	return orderedList;
}
