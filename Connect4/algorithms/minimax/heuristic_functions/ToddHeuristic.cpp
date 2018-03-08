/*
 * ToddHeuristic.cpp
 *
 *  Created on: Feb 26, 2018
 *      Author: toddnguyen47
 */

#include "ToddHeuristic.h"

int ToddHeuristic::execute(vector<vector<int> >& board, int depth) {
	WinnerOrLoser t1(100);
	KillerMove1 t2(t1, 99);
	OneAway t3(t2, 98);
	return t3.execute(board, depth);
}
