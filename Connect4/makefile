CXX := g++
CXXFLAGS := -g -std=gnu++11
PROG := p3
SRC_DIR := .
OBJ_DIR := ./obj
OBJ_FILES := ./obj/algorithms/human/Human.o ./obj/algorithms/minimax/MiniMax.o ./obj/algorithms/minimax/MiniMax_DFS.o ./obj/algorithms/minimax/MiniMax_EIDS.o ./obj/algorithms/minimax/MiniMax_IDS.o ./obj/algorithms/minimax/MiniMax_MaxDepth.o ./obj/algorithms/minimax/hash_tables/VisitedHashTable.o ./obj/algorithms/minimax/hash_tables/ZobristHash.o ./obj/algorithms/minimax/heuristic_functions/AlwaysZero.o ./obj/algorithms/minimax/heuristic_functions/CanWin.o ./obj/algorithms/minimax/heuristic_functions/Disjoint.o ./obj/algorithms/minimax/heuristic_functions/Doubles.o ./obj/algorithms/minimax/heuristic_functions/DoublesAndTriples.o ./obj/algorithms/minimax/heuristic_functions/JakesHeuristic.o ./obj/algorithms/minimax/heuristic_functions/JakesHeuristic2.o ./obj/algorithms/minimax/heuristic_functions/KillerMove1.o ./obj/algorithms/minimax/heuristic_functions/KillerMove2.o ./obj/algorithms/minimax/heuristic_functions/OneAway.o ./obj/algorithms/minimax/heuristic_functions/Random.o ./obj/algorithms/minimax/heuristic_functions/ToddHeuristic.o ./obj/algorithms/minimax/heuristic_functions/TwoAway.o ./obj/algorithms/minimax/heuristic_functions/WinnerOrLoser.o ./obj/algorithms/minimax/successor_functions/Bloom.o ./obj/algorithms/minimax/successor_functions/RowByRow.o ./obj/algorithms/minimax/successor_functions/Spiral.o ./obj/game/Game.o ./obj/player/Player.o ./obj/src/Connect4.o ./obj/src/Utilities.o 

all: $(PROG)

$(PROG): $(OBJ_FILES)
	$(CXX) $(CXXFLAGS) -o $(PROG) $^

./obj/algorithms/human/%.o: ./algorithms/human/%.cpp
	mkdir -p ./obj/algorithms/human
	$(CXX) $(CXXFLAGS) -c -o $@ $^

./obj/player/%.o: ./player/%.cpp
	mkdir -p ./obj/player
	$(CXX) $(CXXFLAGS) -c -o $@ $^

./obj/algorithms/minimax/successor_functions/%.o: ./algorithms/minimax/successor_functions/%.cpp
	mkdir -p ./obj/algorithms/minimax/successor_functions
	$(CXX) $(CXXFLAGS) -c -o $@ $^

./obj/game/%.o: ./game/%.cpp
	mkdir -p ./obj/game
	$(CXX) $(CXXFLAGS) -c -o $@ $^

./obj/algorithms/minimax/heuristic_functions/%.o: ./algorithms/minimax/heuristic_functions/%.cpp
	mkdir -p ./obj/algorithms/minimax/heuristic_functions
	$(CXX) $(CXXFLAGS) -c -o $@ $^

./obj/algorithms/minimax/hash_tables/%.o: ./algorithms/minimax/hash_tables/%.cpp
	mkdir -p ./obj/algorithms/minimax/hash_tables
	$(CXX) $(CXXFLAGS) -c -o $@ $^

./obj/algorithms/minimax/%.o: ./algorithms/minimax/%.cpp
	mkdir -p ./obj/algorithms/minimax
	$(CXX) $(CXXFLAGS) -c -o $@ $^

./obj/src/%.o: ./src/%.cpp
	mkdir -p ./obj/src
	$(CXX) $(CXXFLAGS) -c -o $@ $^

clean:
	rm -rf *.o $(PROG) *.exe $(OBJ_DIR)/*