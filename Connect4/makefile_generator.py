import os, sys
from pathlib import Path

cxxflags = "-g -std=gnu++11"

def init():
    output_file = get_command_line_args()
    write_to_file(output_file)

# Write to the makefile. Will not overwrite existing makefile
def write_to_file(projectName):
    tempList = getAllSrcFiles()
    listOfObjects = ""
    for x in tempList[1]:
        listOfObjects += x[0] + " "

    makefile_name = "makefile"
    output_file = Path(makefile_name);
    if output_file.is_file():
        user_input = input("Makefile currently exists. Overwrite? Y/N: ")
        if (user_input.lower() == "n"):
            print("Please delete the previous makefile before running this program again.\n")
            sys.exit()

    with open(makefile_name, 'w') as file1:
        file1.write(    "CXX := g++\n"
                        "CXXFLAGS := " + cxxflags + "\n"
                        "PROG := " + projectName + "\n"
                        "SRC_DIR := .\n"
                        "OBJ_DIR := ./obj\n" +
                        "OBJ_FILES := " + listOfObjects + "\n"
                        "\n"
                        "all: $(PROG)\n"
                        "\n"
                        "$(PROG): $(OBJ_FILES)\n"
                        "\t$(CXX) $(CXXFLAGS) -o $(PROG) $^\n"
                        "\n"
        )

        # per object file
        for dir_path1 in tempList[0]:

            file1.write("./obj" + dir_path1 + "/%.o: " + "." + dir_path1 + "/%.cpp\n"
                         "\tmkdir -p " + "./obj" + dir_path1 + "\n"
                         "\t$(CXX) $(CXXFLAGS) -c -o $@ $^\n"
                         "\n"
            )

        # Clean command
        file1.write("clean:\n"
                    "\trm -rf *.o $(PROG) *.exe $(OBJ_DIR)/*"
        )


# Get the output project name
def get_command_line_args():
    input_file = input("Enter input file: ")
    return input_file

# Get all source files
def getAllSrcFiles():
    cwd = os.getcwd()
    src_files = []
    dirList = []
    returnList = []
    for (dirpath, dirnames, filenames) in os.walk(cwd):
        for filename in filenames:
            if (".cpp" in filename):
                tempList = []
                dirList.append(dirpath.replace(cwd, "").replace("\\","/"))
                tempList.append("./obj" + (str(dirpath).replace(cwd,"") + "\\" + filename).replace("\\","/").replace(".cpp",".o"))
                src_files.append(tempList)
    dirList = set(dirList)
    returnList.append(dirList)
    returnList.append(src_files)
    return returnList

init()