/*
 * ToddHeuristic.h
 *
 *  Created on: Feb 26, 2018
 *      Author: toddnguyen47
 */

#ifndef TODDHEURISTIC_H_
#define TODDHEURISTIC_H_

#include "HeuristicFunction.h"
#include "WinnerOrLoser.h"
#include "OneAway.h"
#include "KillerMove1.h"
#include <iostream>

class ToddHeuristic: public HeuristicFunction
{
public:
	ToddHeuristic(int value):
		HeuristicFunction(value) {}
	~ToddHeuristic(){}
	int execute(vector<vector<int> >& board, int depth);
};

#endif /* TODDHEURISTIC_H_ */
